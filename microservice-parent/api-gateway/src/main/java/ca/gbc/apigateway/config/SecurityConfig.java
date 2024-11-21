package ca.gbc.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final String[] noauthResourceUris = {
            "/swagger-ui",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api-docs/**",
            "/aggregate/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{


        log.info("Initializing Security Filter Chain....");

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)               // Disable CSRF protection
               /* .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())  */              // All requests temporarily permitted

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(noauthResourceUris)
                        .permitAll()
                        .anyRequest().authenticated())             // All requests require authentication
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()))
                .build();

    }





}
