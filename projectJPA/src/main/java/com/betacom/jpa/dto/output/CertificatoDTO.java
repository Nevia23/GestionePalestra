package com.betacom.jpa.dto.output;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CertificatoDTO {
	
	private Integer id;
	private String tipo;
	private LocalDate dataCertificato;
	private Integer validitaCertificato;

    private Integer socioID;
    private String socioNome;
    private String stato;
}
