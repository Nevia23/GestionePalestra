package com.betacom.fe.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
				// per andare sugli url di questo tipo si deve avere il tuolo admin
				.requestMatchers("/admin", "/admin/**").hasRole("ADMIN")  // ** vuol dire "sottocomponente"
				.requestMatchers("/login", "/registry", "/saveNuovoUtente").permitAll()
				.anyRequest().authenticated()
				)
				.formLogin((form) -> form
						.loginPage("/login")
						.permitAll()
						)
				.logout((logout) -> logout.permitAll());
		return http.build();
	}
	
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	UserDetailsService userDetailsServices() {
//		
//		List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
//
//		userDetailsList.add(User.withUsername("user")
//			.password(getPasswordEncoder().encode("user").toString())
//			.roles("USER")
//			.build());
//		
//		userDetailsList.add(User.withUsername("admin")
//				.password(getPasswordEncoder().encode("admin").toString())
//				.roles("ADMIN")
//				.build());
//		
//		return new InMemoryUserDetailsManager(userDetailsList);
//	}
}
