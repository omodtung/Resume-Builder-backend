package saigonuni.dev.resumeBuilder.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  //   @Bean
  //   public SecurityFilterChain securityFilterChain(HttpSecurity http)
  //     throws Exception {
  //     //     // http
  //     //     //   .csrf()
  //     //     //   .disable()
  //     //     //   .authorizeHttpRequests()
  //     //     //   .requestMatchers(
  //     //     //
  //     //     // "/api/auth/**", // Authentication endpoi// nts
  //     //     // "/api/swagger-ui/**", // Swagger//  UI
  //     //     // "/api/v3/api-docs/**", // OpenAPI d// ocs
  //     //     // "/api/swagger-resources/**", // Swagger resour// ces
  //     //     // "/api/webjars/**" // Webjars used by Swag// ger
  //     //     //   )
  //     //     //   .permitAll() // Allow access to Swagger and authentication endpoi// nts
  //     //     //   .anyReques// t()
  //     //     //   .authenticate// d()
  //     //     //   .an// d()
  //     //     //   .httpBasic(); // Use basic authentication for simplic// ity
  //     //     // return http.build();

  //     http
  //       .cors()
  //       .and()
  //       .csrf()
  //       .disable()
  //       .authorizeHttpRequests()
  //       .requestMatchers("/api/auth/**")
  //       .permitAll() // Đảm bảo API này mở
  //       .anyRequest()
  //       .authenticated()
  //       .and()
  //       .sessionManagement()
  //       .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Không dùng session
  //       .and()
  //       .formLogin()
  //       .disable();

  //     return http.build();
  //   }

  //   @Bean
  //   CorsConfigurationSource corsConfigurationSource() {
  //     CorsConfiguration configuration = new CorsConfiguration();

  //     configuration.setAllowedOrigins(List.of("http://localhost:8080"));
  //     configuration.setAllowedMethods(List.of("GET", "POST"));
  //     configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

  //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

  //     source.registerCorsConfiguration("/**", configuration);

  //     return source;
  //   }

  //   @Bean
  //   public PasswordEncoder passwordEncoder() {
  //     return new BCryptPasswordEncoder();
  //   }

  //   @Bean
  //   public AuthenticationManager authenticationManager(
  //     AuthenticationConfiguration config
  //   ) throws Exception {
  //     return config.getAuthenticationManager();
  //   }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .cors()
      .and()
      .csrf()
      .disable()
      .authorizeHttpRequests()
      .requestMatchers(
        "/**",
        "/swagger-ui/**", // Allow Swagger UI
        "/swagger-ui/index.html", // Allow Swagger UI
        "/v3/api-docs/**", // Allow API docs
        "/swagger-resources/**",
        "/api-docs/**",
        "/webjars/**"
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
    configuration.setAllowedOrigins(List.of("http://localhost:8080"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

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
}
