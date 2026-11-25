package com.betacom.jpa.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.PersoneReq;
import com.betacom.jpa.dto.output.PersoneDTO;
import com.betacom.jpa.exceptions.AcademyException;
import com.betacom.jpa.mapper.PersoneMap;
import com.betacom.jpa.models.Persone;
import com.betacom.jpa.repositories.IPersoneRepository;
import com.betacom.jpa.services.interfaces.IPersoneServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersoneImpl implements IPersoneServices {

	private final IPersoneRepository persR;

	@Transactional
	@Override
	public void create(PersoneReq req) throws Exception {
		log.debug("create {}", req);
		Persone p = new Persone();
		p.setCognome(req.getCognome());
		p.setNome(req.getNome());
		p.setEmail(req.getEmail());
		p.setColore(req.getColore());

		persR.save(p);
	}

	@Transactional
	@Override
	public void update(PersoneReq req) throws Exception {
		log.debug("update {}", req);
		Persone p = persR.findById(req.getId())
				.orElseThrow(() -> new AcademyException("persona non trovata"));
		if (req.getCognome() != null) p.setCognome(req.getCognome());
		if (req.getNome() != null) p.setNome(req.getNome());
		if (req.getEmail() != null) p.setEmail(req.getEmail());
		if (req.getColore() != null) p.setColore(req.getColore());
		
		persR.save(p);
	}

	@Transactional
	@Override
	public void delete(Integer id) throws Exception {
		log.debug("delete {}", id);
		Persone p = persR.findById(id)
				.orElseThrow(() -> new AcademyException("persona non trovata"));
		persR.delete(p);
	}

	@Override
	public List<PersoneDTO> list() throws Exception {
		log.debug("list");
		List<Persone> lP = persR.findAll();
		return PersoneMap.buildPersoneDTO(lP);
	}

	@Override
	public PersoneDTO getById(Integer id) throws Exception {
		log.debug("getById {}", id);
		Persone p = persR.findById(id)
				.orElseThrow(() -> new AcademyException("persona non trovata"));
		return PersoneMap.buildPersoneDTO(p);
	}

}
