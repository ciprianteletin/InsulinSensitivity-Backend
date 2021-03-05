package com.insulin;

import org.apache.catalina.Context;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * The starting point of the entire application. On start, Spring Context will create all the components and will inject each object where it is needed.
 * It will scan for components(included services, repository, controllers) and will manage the lifecycle of each one.
 * It will find additional config files and will add that configuration to the application.
 */
@SpringBootApplication
@EnableCaching
public class LicentaBackendApplication {

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BasicTextEncryptor basicTextEncryptor() {
        return new BasicTextEncryptor();
    }

    /**
     * Add to every cookie sent do the client the property "sameSite = strict". This was necessary because
     * on Firefox, if the cookie had as sameSite value "Lax" or "None", a warning would appear.
     */
    @Bean
    public ServletWebServerFactory servletContainer() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                Rfc6265CookieProcessor rfc6265CookieProcessor = new Rfc6265CookieProcessor();
                rfc6265CookieProcessor.setSameSiteCookies("Strict");
                context.setCookieProcessor(rfc6265CookieProcessor);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(LicentaBackendApplication.class, args);
    }

}
