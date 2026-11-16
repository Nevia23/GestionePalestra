package com.betacom.jpa.services.implementations;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.jpa.dto.input.CertificatoReq;
import com.betacom.jpa.dto.output.CertificatoDTO;
import com.betacom.jpa.exceptions.AcademyException;
import com.betacom.jpa.mapper.CertificatoMap;
import com.betacom.jpa.models.Certificato;
import com.betacom.jpa.models.Socio;
import com.betacom.jpa.repositories.ICertificatoRepository;
import com.betacom.jpa.repositories.ISocioRepository;
import com.betacom.jpa.services.interfaces.ICertificatoServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CertificatoImpl implements ICertificatoServices {

	private final ICertificatoRepository cerR;
	private final ISocioRepository socR;
	
	@Transactional
	@Override
	public void updateCertificato(CertificatoReq req) throws Exception {
		log.debug("Begin updateCertificato {}", req);
		
		Socio soc = socR.findById(req.getSocioID())
				.orElseThrow(() -> new AcademyException("socio-ntfnd"));
		
		Certificato cer = null;
		if (soc.getCertificato() == null) {
			log.debug("new certificato per {}", soc.getId());
			cer = new Certificato();
		} else
			cer = soc.getCertificato();
		
		cer.setDataCertificato(req.getDataCertificato());
		cer.setTipo(req.getTipo() == null ? false : req.getTipo());
		cer.setValiditaCertificato(req.getValiditaCertificato());
		
		soc.setCertificato(cerR.save(cer));		
		socR.save(soc);
	}

//	@Override
//	public List<CertificatoDTO> listCertificato() throws Exception {
//		log.debug("list");
//		List<Certificato> lC = cerR.findAll();
//		return CertificatoMap.buildCertificatoDTO(lC);		
//	}

	@Override
    public List<CertificatoDTO> listCertificato() throws Exception {

        log.debug("list");

        return socR.findAll().stream()

                .filter(soc -> soc.getCertificato() != null)

                .map(soc -> {

                    Certificato cer = soc.getCertificato();

                    String stato = "Non definita";

                    if (cer.getDataCertificato() != null
                            && cer.getValiditaCertificato() != null) {

                        LocalDate scadenza = cer.getDataCertificato()
                                .plusMonths(cer.getValiditaCertificato());

                        stato = scadenza.isBefore(LocalDate.now())
                                ? "Scaduto"
                                : "Valido";
                    }

                    return CertificatoMap.buildCertificatoDTO(
                            cer,
                            soc.getId(),
                            soc.getCognome() + " " + soc.getNome(),
                            stato);
                })

                .toList();
    }
}
