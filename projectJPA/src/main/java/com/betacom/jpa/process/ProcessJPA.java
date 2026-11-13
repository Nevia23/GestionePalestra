package com.betacom.jpa.process;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.betacom.jpa.dto.input.CertificatoReq;
import com.betacom.jpa.dto.input.SocioReq;
import com.betacom.jpa.dto.output.SocioDTO;
import com.betacom.jpa.services.interfaces.ICertificatoServices;
import com.betacom.jpa.services.interfaces.ISocioServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class ProcessJPA {
//	
//	private final ISocioServices socioS;
//	private final ICertificatoServices cerS;
//	
//	public void createSocio() {
//		log.debug("Create socio ...");
//		try {
//			socioS.create(SocioReq.builder()
//					.nome("Paolo")
//					.cognome("Brilli")
//					.codiceFiscale("12934761ji")
//					.email("tr@al.it")
//					.build());
//			
//			socioS.create(SocioReq.builder()
//					.nome("Anna")
//					.cognome("Bianca")
//					.codiceFiscale("22222222222")
//					.email("tr@oq.it")
//					.build());
//			
//			socioS.create(SocioReq.builder()
//					.nome("Sara")
//					.cognome("Strilli")
//					.codiceFiscale("0926491adawg")
//					.email("paw@al.it")
//					.build());
//			
//		} catch (Exception e) {
//			log.error(e.getMessage());
//		}
//	}
//	
//	public void listSocio() {
//		try {
//			List<SocioDTO> lS = socioS.list();
//			lS.forEach(s -> log.debug(s.toString()));
//		} catch (Exception e) {
//			log.error(e.getMessage());
//		}
//	}
//	
//	public void setCertificato() {
//		try {
//			cerS.updateCertificato(CertificatoReq.builder()
//					.dataCertificato(LocalDate.parse("2026-09-01"))
//					.socioID(3)
//					.build());
//		} catch (Exception e) {
//			log.error("Errore in aggiornamento certificato {}", e.getMessage());
//		}
//		
//	}
//	
//	public void updateSocio() {
//		try {
//			socioS.update(SocioReq.builder()
//					.id(3)
//					.nome("Alice")
//					.codiceFiscale("52222225525252")
//					.build());
//		} catch (Exception e) {
//			log.error("Errore in aggiornamento socio {}", e.getMessage());
//		}
//	}
//	
//	public void deleteSocio() {
//		try {
//			socioS.delete(3);;
//		} catch (Exception e) {
//			log.error("Errore in aggiornamento socio {}", e.getMessage());
//		}
//	}
}
