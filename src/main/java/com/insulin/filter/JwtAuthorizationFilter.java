package com.insulin.filter;

import com.insulin.configs.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.insulin.shared.SecurityConstants.OPTIONS_HTTP_METHOD;
import static com.insulin.shared.SecurityConstants.TOKEN_PREFIX;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Fire every new request, by implementing OncePerRequestFilter. We implement doFilterInternal to check the validity of
 * the token.
 */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JwtTokenProvider provider;

    @Autowired //not mandatory, as it is done automatically when there is only one constructor
    public JwtAuthorizationFilter(JwtTokenProvider provider) {
        this.provider = provider;
    }

    /**
     * Every request needs to be verified and filtered out by this method.
     * Before every request is sent, a request of type OPTIONS is emitted to collect
     * data about the server. We permit those request because we need to inform the
     * client with desired information(if it accept a kind of header etc.)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals(OPTIONS_HTTP_METHOD)) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); //taking the auth header from the request
        if (isNotEmpty(authHeader) && authHeader.startsWith(TOKEN_PREFIX)) {
            String token = authHeader.substring(TOKEN_PREFIX.length()); //remove the bearer word from the real token
            String username = provider.getSubject(token); //extract the username from the provider
            if (provider.isTokenValid(username, token)) {
                List<GrantedAuthority> userAuthorities = provider.getAuthorities(token); //permisiunile userului
                Authentication authentication = provider.getAuthentication(username, userAuthorities, request); //get authentication
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response); //passing to the next filter method, if there is any.
    }
}
