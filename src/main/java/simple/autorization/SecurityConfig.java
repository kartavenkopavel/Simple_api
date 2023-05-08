package simple.autorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Objects;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig {

    @Value("${app.http.auth-token-header-name}")
    private String principalRequestHeader;

    @Value("${app.http.auth-token}")
    private String principalRequestValue;

    private static final String[] PUBLIC_URL = {
            "/h2-console/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ApiKeyAuthFilter filter = new ApiKeyAuthFilter(principalRequestHeader);
        filter.setAuthenticationManager(
                authentication -> {
                    String principal = (String) authentication.getPrincipal();
                    if (!Objects.equals(principalRequestValue,principal)) {
                        throw new BadCredentialsException(
                                "The API Key was not found or not the expected value.");
                    }
                    authentication.setAuthenticated(true);
                    return authentication;
                });
        http.antMatcher("/api/**")
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter)
                .authorizeRequests()
                .antMatchers(PUBLIC_URL)
                .permitAll()
                .anyRequest()
                .authenticated();

        http.headers()
                .frameOptions()
                .disable();

        return http.build();
    }
}
