package com.betacom.fe.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttivitaDTO {

	private Integer id;
	private String descrizione;
	private Long prezzo;
	
}
