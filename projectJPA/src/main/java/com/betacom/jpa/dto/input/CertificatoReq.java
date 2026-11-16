package com.betacom.jpa.dto.input;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificatoReq {

	private Boolean tipo;  // false normale, true agonistico
	
	@NotNull(groups = {ValidationGroup.Create.class, ValidationGroup.Update.class}, message = "certif-no-data")
	private LocalDate dataCertificato;
	
	@NotNull(groups = {ValidationGroup.Create.class, ValidationGroup.Update.class}, message = "socio-no-id")
	private Integer socioID;
	
	private Integer validitaCertificato;
}
