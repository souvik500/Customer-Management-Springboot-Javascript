//package com.example.Customer_Details_Demo.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends Web {
//
//	@Autowired
//	private JwtRequestFilter jwtRequestFilter;
//
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable()) // Disable CSRF
//				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/auth/login").permitAll() // Permit
//																												// the
//																												// login
//																												// endpoint
//						.anyRequest().authenticated() // All other requests require authentication
//				).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use
//																												// stateless
//																												// session
//				);
//
//		// Add the JWT filter
//		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//		return http.build();
//	}
//
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
//			throws Exception {
//		return authenticationConfiguration.getAuthenticationManager();
//	}
//}
