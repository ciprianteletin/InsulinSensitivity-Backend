package com.insulin.configs;

import com.insulin.filters.JwtAccessDeniedHandler;
import com.insulin.filters.JwtAuthenticationEntryPoint;
import com.insulin.filters.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.insulin.shared.constants.SecurityConstants.*;
import static java.util.Arrays.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Class with the scope of providing configuration regarding the security of the application.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public SecurityConfiguration(JwtAuthorizationFilter authFilter, JwtAccessDeniedHandler handler,
                                 JwtAuthenticationEntryPoint entryPoint,
                                 @Qualifier("userService") UserDetailsService userDetailsService,
                                 BCryptPasswordEncoder encoder) {
        this.jwtAuthorizationFilter = authFilter;
        this.jwtAccessDeniedHandler = handler;
        this.jwtAuthenticationEntryPoint = entryPoint;
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    /**
     * Specifying the userService that we created and the custom password encoder that we are going to use
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(encoder);
    }

    /**
     * Method where we specify what url's are permitted and what are not, custom exception handler and
     * several configuration (Stateless session, csrf disabled etc)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(this::createCorsConfig).and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS) //Since we are using REST + JWT, we don't need to keep a state of who is logged.
                .and().authorizeRequests().antMatchers(PUBLIC_URLS).permitAll() //URLS accessible by anyone (login, register, recover password etc).
                .anyRequest().authenticated() //TODO Secure in the future
                .and()
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Method to create and configure CorsConfiguration, to allow the communication between
     * front-end and back-end.
     */
    private CorsConfiguration createCorsConfig(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        config.setExposedHeaders(List.of(JWT_TOKEN_HEADER, CAPTCHA_HEADER));
        config.setAllowedMethods(asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.addAllowedOrigin("http://localhost:4200");
        config.setAllowCredentials(true);
        return config;
    }
}

