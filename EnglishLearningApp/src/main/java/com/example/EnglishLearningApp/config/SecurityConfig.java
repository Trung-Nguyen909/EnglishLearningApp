package com.example.EnglishLearningApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    @Value("${jwt.secretKey}")
    private String secretKey;
    private final String[] PUBLIC_ENDPOINT = {"/user/**","/kynang/**", "/cauhoi/**","/KhoaHoc/**","/baihoc/**", "/img_user/user_avatar/**", "/baitap/**"};
    private final String[] PRIVATE_ENDPOINT = {"/user/admin/*", "/user/summary"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
                request.requestMatchers(PUBLIC_ENDPOINT).permitAll()
                        .requestMatchers(PRIVATE_ENDPOINT).hasAnyAuthority("SCOPE_ADMIN")
                        .anyRequest().authenticated());
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(jwtDecoder()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler())
        );
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS256");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = System.getProperty("user.dir") + "/img_user/user_avatar/";
        registry.addResourceHandler("/img_user/user_avatar/**")
                .addResourceLocations("file:" + uploadDir);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // áp dụng cho TẤT CẢ API
                .allowedOrigins(
                        "http://localhost:5500",   // Live Server VSCode
                        "http://127.0.0.1:5500",
                        "http://localhost:3000"    // nếu sau này dùng React
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
