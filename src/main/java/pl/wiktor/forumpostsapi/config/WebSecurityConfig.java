package pl.wiktor.forumpostsapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.wiktor.forumpostsapi.config.security.jwt.JwtRequestFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter requestFilter;

    List<String> publicUrls;

    @Autowired
    public WebSecurityConfig(JwtRequestFilter requestFilter) {
        this.requestFilter = requestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configurePublicUrls(http);
        http.csrf().disable()
                .headers().frameOptions().disable().and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/postsdb");
        web.ignoring().antMatchers("/postsdb/**");
    }

    private void configurePublicUrls(HttpSecurity http) {
        publicUrls.forEach(url -> configurePublicUrl(http, url));
    }

    private void configurePublicUrl(HttpSecurity http, String url) {
        try {
            http.authorizeRequests().antMatchers(url).permitAll();
        } catch (Exception e) {
            log.error("Error configuring public URLs", e);
        }
    }

    @Value("${authentication.publicUrls:@null}")
    public void setPublicUrls(String[] publicUrls) {
        if (publicUrls == null) {
            this.publicUrls = new ArrayList<>();
        } else {
            this.publicUrls = Arrays.asList(publicUrls);
        }
    }
}
