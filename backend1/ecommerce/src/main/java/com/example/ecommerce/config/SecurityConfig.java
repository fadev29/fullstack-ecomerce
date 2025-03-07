package com.example.ecommerce.config;
import com.example.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final UserService customerService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserService customerService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.customerService = customerService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(session -> session
                        .requestMatchers(HttpMethod.POST, "/api/customer/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/customer/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/customer/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/customer/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/category/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/category/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/category/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/category/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/orders/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/orders/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/orders/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/orders/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/order_items/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/order_items/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/order_items/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/order_items/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/cart/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/cart/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/cart/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/cart/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/order_items/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/order_items/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/order_items/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/order_items/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/order_history/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedOrigin("http://localhost:3000");
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedMethod("*");
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setMaxAge(3600L);
            return corsConfiguration;
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customerDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public UserDetailsService customerDetailsService() {
        return customerService::loadUserByUsername;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
