package com.betacom.fe.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import com.betacom.fe.dto.input.UtenteReq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UtenteServices {
	
	private final PasswordEncoder getPasswordEncoder;
	private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
	
	public void updateUtente(UtenteReq req) {
		if (inMemoryUserDetailsManager.userExists(req.getUsername())) {
			inMemoryUserDetailsManager.deleteUser(req.getUsername());
			log.debug("utente {} deleted", req.getUsername());
		}
		
		inMemoryUserDetailsManager.createUser(User
				.withUsername(req.getUsername())
				.password(getPasswordEncoder.encode(req.getPwd().toString()))
				.roles(req.getRole())
				.build()
				);
		log.debug("utente {} has been created", req.getUsername());
	}
	
	public void updateRuolo(String username, String role) {
	    if (!inMemoryUserDetailsManager.userExists(username)) return;
	    inMemoryUserDetailsManager.updateUser(User
	            .withUserDetails(inMemoryUserDetailsManager.loadUserByUsername(username))
	            .roles(role)
	            .build());
	}

	public void deleteUtente(String username) {
	    inMemoryUserDetailsManager.deleteUser(username);
	}
}
