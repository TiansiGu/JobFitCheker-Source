package com.JobFitChecker.JobFitCheckerApp.modules;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * This class exists temporarily for disabling spring security CRSF and pswd authentication, so that
 * developers can easily test controller APIs without authentication. To test spring security behavior,
 * comment out this class entirely and rebuild project again.
 * ToDo: Delete this class for production
 */
@Configuration
@EnableWebSecurity
public class SecurityModule {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().permitAll()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

}
