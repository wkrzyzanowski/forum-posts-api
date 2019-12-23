package pl.wiktor.forumpostsapi.config.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.wiktor.forumpostsapi.config.security.jwt.model.UserSecurity;
import pl.wiktor.forumpostsapi.config.security.jwt.service.UserAuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    UserAuthService userAuthService;
    JwtUtil jwtUtil;

    public JwtRequestFilter(UserAuthService userAuthService, JwtUtil jwtUtil) {
        this.userAuthService = userAuthService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUserName(jwt);

        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserSecurity userDetails = this.userAuthService.extractFromToken(jwt);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
