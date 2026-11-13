package com.betacom.jpa.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.jpa.dto.input.SocioReq;
import com.betacom.jpa.dto.output.SocioDTO;
import com.betacom.jpa.exceptions.AcademyException;
import com.betacom.jpa.mapper.SocioMap;
import com.betacom.jpa.models.Socio;
import com.betacom.jpa.repositories.ISocioRepository;
import com.betacom.jpa.services.interfaces.IMessaggioServices;
import com.betacom.jpa.services.interfaces.ISocioServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocioImpl implements ISocioServices {

	private final ISocioRepository socioR;
	private final IMessaggioServices msgS;

	@Transactional
	@Override
	public void create(SocioReq req) throws Exception {
		log.debug("create {}", req);
		if (socioR.existsByCodiceFiscale(req.getCodiceFiscale()))
				throw new AcademyException("cfisc-fnd");
		
		Socio soc = new Socio();
		soc.setCodiceFiscale(req.getCodiceFiscale());
		soc.setCognome(req.getCognome());
		soc.setEmail(req.getEmail());
		soc.setNome(req.getNome());
		socioR.save(soc);
	}

	@Transactional
	@Override
	public void update(SocioReq req) throws Exception {
		log.debug("update {}", req);
		
		/* metodo vecchio rispetto al seguente
		Optional<Socio> soc = socioR.findById(req.getId());
		if (soc.isEmpty()) throw new AcademyException("Socio non trovato");
		*/
		
		// il socio che vogliamo modificare esiste o no?
		Socio soc = socioR.findById(req.getId())
				.orElseThrow(() -> new AcademyException(msgS.get("socio-ntfnd")));
		
		// Se il codice fiscale non è nullo ed è diverso da quello attuale,
		// verifico che non sia già presente nel database		
		if (req.getCodiceFiscale() != null && !req.getCodiceFiscale().equalsIgnoreCase(soc.getCodiceFiscale())) {
			if (socioR.existsByCodiceFiscale(req.getCodiceFiscale()))
				throw new AcademyException("cfisc-fnd");
			soc.setCodiceFiscale(req.getCodiceFiscale());
		}
		
		if (req.getCognome() != null) soc.setCognome(req.getCognome());
		if (req.getNome() != null) soc.setNome(req.getNome());
		if (req.getEmail() != null) soc.setEmail(req.getEmail());
		socioR.save(soc);

		
	}

	@Override
	public void delete(Integer id) throws Exception {
		log.debug("delete {}", id);
		Socio soc = socioR.findById(id)
				.orElseThrow(() -> new AcademyException("socio-ntfnd"));
		
		socioR.delete(soc);
		
	}

	@Override
	public List<SocioDTO> list() throws Exception {
		log.debug("list");
		List<Socio> lS = socioR.findAll();
		return SocioMap.buildSocioDTO(lS);
	}

	@Override
	public List<SocioDTO> listByCognome(String cognome) throws Exception {
		log.debug("listByCognome {}", cognome);
//		List<Socio> lS = socioR.searchByCognome(cognome);
		List<Socio> lS = socioR.findByCognomeStartingWith(cognome);

		return SocioMap.buildSocioDTO(lS);
	}

	@Override
	public List<SocioDTO> listByAttivita(Integer attivitaID) throws Exception {
		log.debug("listByAttivita {}", attivitaID);
		List<Socio> lS = socioR.selectByAttivita(attivitaID);
		return SocioMap.buildSocioDTO(lS);
	}

	@Override
	public List<SocioDTO> listByOptionalFilters(Integer socioID, String nome, String cognome, String attivita) throws Exception {
		log.debug("listByOptionalFilters {} / {} / {} / {}", socioID, nome, cognome, attivita);
		List<Socio> lS = socioR.filterByOptionalParameters(socioID, nome, cognome, attivita);
		return SocioMap.buildSocioDTO(lS);
	}

	@Override
	public SocioDTO getById(Integer id) throws Exception {
		log.debug("getById {}", id);
		Socio soc = socioR.findById(id)
				.orElseThrow(() -> new AcademyException(msgS.get("socio-ntfnd")));
		return SocioMap.buildSocioDTO(soc);
	}

}
