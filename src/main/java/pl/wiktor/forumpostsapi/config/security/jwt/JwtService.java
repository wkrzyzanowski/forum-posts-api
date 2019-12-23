package pl.wiktor.forumpostsapi.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pl.wiktor.forumpostsapi.config.security.jwt.model.UserSecurity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String ROLES_CLAIM = "authorities";
    private static final String EMAIL_CLAIM = "email";
    private static final String GUID_CLAIM = "guid";
    public static final String ROLE_PREFIX = "ROLE_";

    @Value("${jwt.config.expirationTime}")
    String expirationTime;

    @Value("${jwt.config.secret}")
    String secret;


    void addAuthentication(HttpServletResponse res, Authentication authentication) {
        UserSecurity user = (UserSecurity) authentication.getPrincipal();
        String jwt = createJwt(user);
        res.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwt);
        res.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            res.getWriter().write(objectMapper.writeValueAsString(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Authentication getAuthentication(HttpServletRequest request) {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(jwt)) {
            log.error("Missing authentication token");
            return null;
        }
        String jwtToken = retrieveJwt(jwt);
        Jwt<JwsHeader, Claims> jwtObject;
        try {
            jwtObject = parseJwt(jwtToken);
        } catch (AuthorizationServiceException e) {
            return null;
        }
        UserSecurity user = getUser(jwtObject);
        List<SimpleGrantedAuthority> authorities = getRoles(jwtObject);
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    private UserSecurity getUser(Jwt<JwsHeader, Claims> jwtObject) {
        UserSecurity user = new UserSecurity();
        user.setEmail((String) jwtObject.getBody().get(EMAIL_CLAIM));
        user.setUserId((String) jwtObject.getBody().get(GUID_CLAIM));
        return user;
    }

    private List<SimpleGrantedAuthority> getRoles(Jwt<JwsHeader, Claims> jwtObject) {
        List<String> roles = (List<String>) jwtObject.getBody().get(ROLES_CLAIM);
        return roles.stream()
                .filter(StringUtils::isNotBlank)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Jwt<JwsHeader, Claims> parseJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException e) {
            String message = "Invalid token";
            log.error(message, e);
            throw new AuthorizationServiceException("Invalid token");
        }
    }

    private String retrieveJwt(String jwt) {
        return jwt.replace(TOKEN_PREFIX, StringUtils.EMPTY);
    }

    private String createJwt(UserSecurity user) {
        long currentTime = System.currentTimeMillis();
        long jwtExpirationTime = currentTime + Long.parseLong(expirationTime);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim(ROLES_CLAIM, user.getRoles())
                .claim(EMAIL_CLAIM, user.getEmail())
                .claim(GUID_CLAIM, user.getUserId())
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(jwtExpirationTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
