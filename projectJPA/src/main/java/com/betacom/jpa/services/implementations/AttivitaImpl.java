package com.betacom.jpa.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.jpa.dto.input.AttivitaReq;
import com.betacom.jpa.dto.output.AttivitaDTO;
import com.betacom.jpa.exceptions.AcademyException;
import com.betacom.jpa.mapper.AttivitaMap;
import com.betacom.jpa.models.Attivita;
import com.betacom.jpa.repositories.IAttivitaRepository;
import com.betacom.jpa.services.interfaces.IAttivitaServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class AttivitaImpl implements IAttivitaServices {

	private final IAttivitaRepository attR;
	
	@Transactional
	@Override
	public void create(AttivitaReq req) throws Exception {
		log.debug("create {}", req);

		if(attR.existsByDescrizione(req.getDescrizione().trim().toUpperCase()))
			throw new AcademyException("att-fnd");
		Attivita att = new Attivita();
		
		att.setDescrizione(Optional.ofNullable(req.getDescrizione().trim().toUpperCase())
				.orElseThrow(() -> new AcademyException("att-no-desc")));
		att.setPrezzo(Optional.ofNullable(req.getPrezzo())
				.orElseThrow(() -> new AcademyException("att-no-prezzo")));
		
		attR.save(att);
	}

	@Transactional
	@Override
	public void update(AttivitaReq req) throws Exception {
		log.debug("update {}", req);
		Attivita att = attR.findById(req.getId())
				.orElseThrow(() -> new AcademyException("att-ntfnd"));
		
		Optional.ofNullable(req.getDescrizione())
			.map(d -> d.trim().toUpperCase())
			.filter(d -> !d.equals(att.getDescrizione()))
			.ifPresent(d -> {
				if (attR.existsByDescrizione(d)) {
					throw new AcademyException("att-fnd");
				}
				att.setDescrizione(d);
			});
		
		Optional.ofNullable(req.getPrezzo()).ifPresent(att::setPrezzo);
		
		attR.save(att);		
	}

	@Override
	public void delete(Integer attivitaID) throws Exception {
		log.debug("update {}", attivitaID);
		Attivita att = attR.findById(attivitaID)
				.orElseThrow(() -> new AcademyException("att-ntfnd"));
		if (!att.getAbbonamento().isEmpty())
			throw new AcademyException("att-with-abb");
		attR.delete(att);
		
		log.debug("Attività cancellata");		
	}

	@Override
	public List<AttivitaDTO> list() throws Exception {
		List<Attivita> lA = attR.findAll();
		return AttivitaMap.buildAttivitaDTO(lA);
	}

}
