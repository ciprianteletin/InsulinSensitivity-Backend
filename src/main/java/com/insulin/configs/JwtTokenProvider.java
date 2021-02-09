package com.insulin.configs;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.insulin.model.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.insulin.shared.SecurityConstants.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;

/**
 * Utility class with several useful methods for security configs.
 * It generates the token, provide the token secret, and uses several methods to get relevant information.
 */
@Component
public class JwtTokenProvider {
    //Secret code used for encoding the token. It is stored inside property file
    @Value("${jwt.secret}")
    private String secret;

    /**
     * After the user is authenticated with success, we need a way to generate the JWT Token which will be used by the client.
     * We pass the list of authorities, issuer, expire date, the algorithm strategy to generate that token
     */
    public String generateJwtToken(UserPrincipal userPrincipal) {
        String[] permissions = getAuthoritiesFromUser(userPrincipal);
        return JWT.create().withIssuer(ISSUER) //
                .withAudience(AUDIENCE) //
                .withIssuedAt(new Date()) //
                .withSubject(userPrincipal.getUsername()) //
                .withArrayClaim(AUTHORITIES, permissions)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))//
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken userPasswordToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        userPasswordToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPasswordToken;
    }

    /**
      * Method to check if the token is valid, meaning not empty and not expired.
     */
    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
    }

    /**
     * Get the subject from the token which is in fact the username provided in generateToken.
     */
    public String getSubject(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    /**
     * Returns a collection of permissions for the current user. For extracting the permissions from the token, we use JWTVerifier.
     */
    public List<GrantedAuthority> getAuthorities(String token) {
        String[] permissions = getAuthoritiesFromToken(token);
        return stream(permissions).map(SimpleGrantedAuthority::new).collect(toList());
    }

    private String[] getAuthoritiesFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    /**
     * Generate the JWTVerifier object in order to manipulate the token and extract desired information from it.
     */
    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret.getBytes());
            verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }

    /**
     * Retrieve the list of authorities from the current user.
     */
    private String[] getAuthoritiesFromUser(UserPrincipal userPrincipal) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority auth : userPrincipal.getAuthorities()) {
            authorities.add(auth.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }
}
