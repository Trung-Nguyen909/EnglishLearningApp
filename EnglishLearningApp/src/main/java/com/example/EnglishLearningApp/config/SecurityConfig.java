package com.example.EnglishLearningApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Value("${jwt.secretKey}")
    private String secretKey;

    //  Chỉ public đúng các API cần public
    private static final String[] PUBLIC_ENDPOINT = {
            "/user/login",
            "/user/register",
            "/img_user/user_avatar/**",

            // Các API app của bạn nếu muốn public
            "/kynang/**",
            "/cauhoi/**",
            "/KhoaHoc/**",
            "/baihoc/**",
            "/baitap/**",
            "/user/register",
            "/tuvung/baihoc/**"
    };

    //  Chỉ admin mới được vào
    private static final String[] ADMIN_ENDPOINT = {
            "/user/admin/**",

    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //  CORS chạy ở Security layer (quan trọng nhất)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5500",
                "http://127.0.0.1:5500",
                "http://localhost:3000",
                "http://localhost:5174",
                "http://localhost:5173"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        // Nếu FE cần đọc header nào thì add vào exposedHeaders
        // config.setExposedHeaders(List.of("Authorization"));

        // JWT Bearer không cần credentials, nhưng để true vẫn được nếu origin cụ thể (không phải "*")
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(req -> req
                        //  Cho preflight đi qua
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        //  Public endpoints
                        .requestMatchers(PUBLIC_ENDPOINT).permitAll()

                        //  Admin endpoints
                        .requestMatchers(ADMIN_ENDPOINT).hasAuthority("SCOPE_ADMIN")

                        //  còn lại phải login
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAccessDeniedHandler())
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS256");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    //  Static file (avatar)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = System.getProperty("user.dir") + "/img_user/user_avatar/";
        registry.addResourceHandler("/img_user/user_avatar/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
