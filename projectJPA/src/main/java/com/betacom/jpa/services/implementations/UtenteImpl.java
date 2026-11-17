package com.betacom.jpa.services.implementations;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.UtenteDTO;
import com.betacom.jpa.enums.Roles;
import com.betacom.jpa.exceptions.AcademyException;
import com.betacom.jpa.mapper.UtenteMap;
import com.betacom.jpa.models.Socio;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.repositories.IUtenteRepository;
import com.betacom.jpa.services.interfaces.IUtenteServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UtenteImpl implements IUtenteServices {

	private final IUtenteRepository utenteR;
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
	@Override
	public void create(UtenteReq req) throws Exception {
		log.debug("create {}", req);
		if (utenteR.existsById(req.getUsername()))
			throw new AcademyException("User esiste in db");
		Utente ut = new Utente();
		ut.setUsername(req.getUsername());
		ut.setPwd(passwordEncoder.encode(req.getPwd()));
		ut.setEmail(req.getEmail());
		ut.setRole(Roles.valueOf(req.getRole()));
		utenteR.save(ut);
	}

	@Override
	public void update(UtenteReq req) throws Exception {
		log.debug("update utente {}", req);
		
		// controllo se l'utente che voglio modificare esiste o no
		Utente ut = utenteR.findById(req.getUsername())
				.orElseThrow(() -> new AcademyException("Utente non trovato"));
		
		if (req.getEmail() != null) ut.setEmail(req.getEmail());
		if (req.getRole() != null) ut.setRole(Roles.valueOf(req.getRole()));
		
		utenteR.save(ut);
	}

	@Override
	public void delete(String username) throws Exception {
		log.debug("delete {}", username);
		Utente ut = utenteR.findById(username)
				.orElseThrow(() -> new AcademyException("Utente non trovato"));
		
		utenteR.delete(ut);
	}

	@Override
	public List<UtenteDTO> list() {
		log.debug("list");
		List<Utente> lU = utenteR.findAll();
		return UtenteMap.buildUtenteDTO(lU);
	}

	@Override
	public UtenteDTO getByUsername(String username) throws Exception {
		log.debug("getByUsername {}", username);
		Utente ut = utenteR.findById(username)
				.orElseThrow(() -> new AcademyException("Utente non trovato"));
		return UtenteMap.buildUtenteDTO(ut);
	}

}
