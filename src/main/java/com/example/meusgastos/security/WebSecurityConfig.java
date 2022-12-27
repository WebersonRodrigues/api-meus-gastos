package com.example.meusgastos.security;
import org.modelmapper.ModelMapper;
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
import org.springframework.security.web.SecurityFilterChain;

import com.example.meusgastos.domain.service.UsuarioService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationConfiguration authConfig;

	@Autowired
	private UserDetailsSecurityServer userDetailsSecurityServer;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				.headers().frameOptions().disable().and()
				.cors().and()
				.csrf().disable()
				.authorizeHttpRequests((auth) -> auth
						.requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
						.anyRequest().authenticated())
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilter(new JwtAuthenticationFilter(authenticationManager(authConfig), jwtUtil));
		http.addFilter(new JwtAuthorizationFilter(authenticationManager(authConfig), jwtUtil, userDetailsSecurityServer));

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
