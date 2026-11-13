package com.betacom.jpa.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.jpa.dto.input.AbbonamentoReq;
import com.betacom.jpa.dto.output.AbbonamentoDTO;
import com.betacom.jpa.exceptions.AcademyException;
import com.betacom.jpa.mapper.AbbonamentoMap;
import com.betacom.jpa.models.Abbonamento;
import com.betacom.jpa.models.Attivita;
import com.betacom.jpa.models.Socio;
import com.betacom.jpa.repositories.IAbbonamentoRepository;
import com.betacom.jpa.repositories.IAttivitaRepository;
import com.betacom.jpa.repositories.ISocioRepository;
import com.betacom.jpa.services.interfaces.IAbbonamentoServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class AbbonamentoImpl implements IAbbonamentoServices {

	private final IAbbonamentoRepository abbR;
	private final ISocioRepository socR;
	private final IAttivitaRepository attR;
	
	@Transactional
    @Override
    public void create(AbbonamentoReq req) throws Exception {
        log.debug("create {}", req);
        Socio soc = socR.findById(req.getSocioID())
                .orElseThrow(() -> new AcademyException("socio-ntfnd"));
        Abbonamento abb = new Abbonamento();
        abb.setDataAbbonamento(req.getDataAbbonamento());
        abb.setDurataAbbonamento(req.getDurataAbbonamento() == null ? 6 : req.getDurataAbbonamento());

        abb.setSocio(soc);
        abbR.save(abb);

    }

	@Transactional
	@Override
	public void update(AbbonamentoReq req) throws Exception {
		log.debug("update {}", req);
		Socio soc = socR.findById(req.getSocioID())
				.orElseThrow(() -> new AcademyException("socio-ntfnd"));
		
		// controllo che l'abbonamento sia un abbonamento del socio
		Abbonamento abb = soc.getAbbonamento().stream()
				.filter(a -> a.getId().equals(req.getId()))
				.findFirst()
				.orElseThrow(() -> new AcademyException("abb-socio-ntfnd"));
		
		if (req.getDataAbbonamento() != null) abb.setDataAbbonamento(req.getDataAbbonamento());
		if (req.getDurataAbbonamento() != null) abb.setDurataAbbonamento(req.getDurataAbbonamento());
		
		abbR.save(abb);
	}

	@Override
	public void delete(Integer abbonamentoID, Integer socioID) throws Exception {
		log.debug("delete {} / {}", abbonamentoID, socioID);
		Socio soc = socR.findById(socioID)
				.orElseThrow(() -> new AcademyException("socio-ntfnd"));
		
		// controllo che l'abbonamento sia un abbonamento del socio
		Abbonamento abb = soc.getAbbonamento().stream()
				.filter(a -> a.getId().equals(abbonamentoID))
				.findFirst()
				.orElseThrow(() -> new AcademyException("abb-socio-ntfnd"));
				
		abbR.delete(abb);
	}

	@Override
	public List<AbbonamentoDTO> listBySocio(Integer socioID) throws Exception {
		log.debug("listBySocio : {}", socioID);
		
		// cerco il socio con l'ID dato in input
		Socio soc = socR.findById(socioID)
				.orElseThrow(() -> new AcademyException("socio-ntfnd"));
		
		// prendo la lista di abbonamenti del socio specifico
		List<Abbonamento> lA = soc.getAbbonamento();
		
		// converto la lista di abbonamenti in lista di abbonamentoDTO e la restituisco
		return AbbonamentoMap.buildAbbonamentoDTO(lA);
	}

	@Override
	public void createAbbonamentoAttivita(AbbonamentoReq req) {
		log.debug("createAbbonamentoAttivita : {}", req);
		
		// controllo se l'abbonamento esiste
		Abbonamento abb = abbR.findById(req.getId())
				.orElseThrow(() -> new AcademyException("abb-ntfnd"));
		
		// controllo se l'attività esiste
		Attivita att = attR.findById(req.getAttivitaID())
				.orElseThrow(() -> new AcademyException("att-ntfnd"));
		
		// non voglio mettere due volte la stessa attività nell'abbonamento
		if (abb.getAttivita().contains(att))
			throw new AcademyException("abb-att-ntfnd");
		
		// aggiungo l'attività alla lista di attività dell'abbonamento
		abb.getAttivita().add(att);
		abbR.save(abb);
		
	}

	@Override
	public void deleteAbbonamentoAttivita(Integer abbonamentoID, Integer attivitaID) throws Exception {
		log.debug("deleteAbbonamentoAttivita : {} / {}", abbonamentoID, attivitaID);
		
		// controllo se l'abbonamento esiste
		Abbonamento abb = abbR.findById(abbonamentoID)
				.orElseThrow(() -> new AcademyException("abb-ntfnd"));
		
		abb.getAttivita().stream()
			.filter(a -> a.getId().equals(attivitaID))
			.findFirst()
			.ifPresent(abb.getAttivita() :: remove); // oppure abb.getAttivita().remove(a)
		abbR.save(abb);
	}

}
