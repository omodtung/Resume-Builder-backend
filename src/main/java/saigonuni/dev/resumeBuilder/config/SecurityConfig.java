package saigonuni.dev.resumeBuilder.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class SecurityConfig implements WebMvcConfigurer {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .addFilterBefore(
        jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class
      )
      .cors()
      .and()
      .csrf()
      .disable()
      .authorizeHttpRequests()
      .requestMatchers(
        // "/",
        "/register",
        "api/stripe-webhook",
        "/auth/authenticate/**",
        "/auth/refresh-token",
        "/auth/register",
        "/upload-file-cv",
        "/upload-file",
        "/admin/resumes",
        "/swagger-ui/**", // Allow Swagger UI
        "/swagger-ui/index.html", // Allow Swagger UI
        "/v3/api-docs/**", // Allow API docs
        "/swagger-resources/**",
        "/v3/api-docs/**", // Allow API docs
        "/api-docs/**",
        "/webjars/**",
        "/images/**",
        "/api/openai/generate-work-experience",
        "/file-open-send",
        "/file-cv-match-ai",
        "/api/agentAI/reviewCv"
      )
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session
      .and()
      .formLogin()
      .disable();

    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
      List.of("http://localhost:8080", "http://localhost:3000" ,"http://localhost:8081")
    );
    configuration.setAllowedMethods(
      List.of("GET", "POST", "PUT", "PATCH", "DELETE")
    );
    configuration.setAllowedHeaders(
      List.of("Authorization", "Content-Type", "idResume", "isResume" ,"userId","query")
    );

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration config
  ) throws Exception {
    return config.getAuthenticationManager();
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
      .addResourceHandler("/images/**")
      .addResourceLocations("classpath:/images/");
  }
}
