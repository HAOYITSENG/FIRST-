package com.example.booking.config;

import com.example.booking.config.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer; // 確保有這個導入
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // AuthenticationManager 會自動從 AuthenticationConfiguration 獲取
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // config 這個參數是由 Spring Security 框架自動傳入的，不需要手動 autowire
        return config.getAuthenticationManager();
    }

    // SecurityFilterChain 會自動從 HttpSecurity 獲取
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // http 這個參數是由 Spring Security 框架自動傳入的，不需要手動 autowire
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/login", "/css/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .headers(headers -> headers
                        // 使用 Customizer.withDefaults() 來解決 frameOptions() 警告
                        // 這會設定 X-Frame-Options: DENY (預設行為)
                        // 如果您確定需要 SAMEORIGIN，應該寫成 .frameOptions(frameOptions -> frameOptions.sameOrigin())
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")); // ✅ 很重要！
        return http.build();
    }
}