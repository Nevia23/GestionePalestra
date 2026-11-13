package com.betacom.fe.dto.input;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificatoReq {

	private Boolean tipo;  // false normale, true agonistico
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  // per dire al frontend che la data è tipo ISO
	private LocalDate dataCertificato;
	private Integer socioID;
}
