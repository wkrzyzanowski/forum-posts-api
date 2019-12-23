package pl.wiktor.forumpostsapi.config.security.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pl.wiktor.forumpostsapi.config.security.jwt.JwtUtil;
import pl.wiktor.forumpostsapi.config.security.jwt.model.UserSecurity;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAuthService {

    @Autowired
    JwtUtil jwtUtil;

    public UserSecurity extractFromToken(String jwt) {

        String username = jwtUtil.extractUserName(jwt);

        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        jwtUtil.extractRoles(jwt).forEach(role -> {
            roles.add(new SimpleGrantedAuthority(role));
        });

        return new UserSecurity(username, "secret_password",roles);
    }
}
