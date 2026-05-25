package se.jensen.johanna.fakestoreinventoryservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.
        cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health").permitAll()
            .requestMatchers("/actuator/**").hasRole("ADMIN")
            .requestMatchers("/api/inventory/check-stock").permitAll()
            .requestMatchers("/api/reservations/**").permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth -> oauth
            .jwt(Customizer.withDefaults()));
    return http.build();

  }

}

