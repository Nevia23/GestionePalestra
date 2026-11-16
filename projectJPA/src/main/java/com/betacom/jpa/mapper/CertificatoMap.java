package com.betacom.jpa.mapper;

import java.util.List;

import com.betacom.jpa.dto.output.CertificatoDTO;
import com.betacom.jpa.models.Certificato;

public class CertificatoMap {
	
	public static CertificatoDTO buildCertificatoDTO(Certificato c) {
		return CertificatoDTO.builder()
				.id(c.getId())
				.tipo(c.getTipo() ? "agonistico" : "normale")
				.dataCertificato(c.getDataCertificato())
				.validitaCertificato(c.getValiditaCertificato())
				.build();				
	}
	
	public static CertificatoDTO buildCertificatoDTO(
            Certificato c,
            Integer socioID,
            String socioNome,
            String stato) {

        return CertificatoDTO.builder()
                .id(c.getId())
                .tipo(c.getTipo() == null || !c.getTipo()
                        ? "normale" : "agonistico")
                .dataCertificato(c.getDataCertificato())
                .validitaCertificato(c.getValiditaCertificato())
                .socioID(socioID)
                .socioNome(socioNome)
                .stato(stato)
                .build();
    }

	
	public static List<CertificatoDTO> buildCertificatoDTO(List<Certificato> lC) {
		return lC.stream()
				.map(c -> buildCertificatoDTO(c))
				.toList();
	}
}
