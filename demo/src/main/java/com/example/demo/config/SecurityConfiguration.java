package com.example.demo.config;

import com.example.demo.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final LogoutHandler logoutHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(cr->cr.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/auth/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .maximumSessions(1)
                                )
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(lg->lg.logoutUrl("/auth/my/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) ->
                                SecurityContextHolder.clearContext()
                        )
                        .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                            httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        })
                        .logoutSuccessUrl("http://localhost:4200/auth/login")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200"); // Replace with the actual origin of your frontend
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
