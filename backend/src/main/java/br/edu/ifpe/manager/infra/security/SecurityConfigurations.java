package br.edu.ifpe.manager.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
	@Autowired
    SecurityFilter securityFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return  httpSecurity
				.cors(cors -> cors.configure(httpSecurity))
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/usuarios/esqueceu-senha").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/usuarios/redefinir-senha").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/usuarios/redefinir-senha").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/recursos").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/recursos").permitAll()
						.requestMatchers(HttpMethod.PUT, "/api/recursos/{id}").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/recursos/{id}").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/reservas/status/{status}").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/reservas").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/reservas/{id}/cancelar").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/events").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/feedback").permitAll()
						.anyRequest().authenticated()
						)
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}