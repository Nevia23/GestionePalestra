package com.betacom.jpa.mapper;

import java.util.List;

import com.betacom.jpa.dto.output.AbbonamentoDTO;
import com.betacom.jpa.dto.output.UtenteDTO;
import com.betacom.jpa.models.Abbonamento;
import com.betacom.jpa.models.Utente;

public class UtenteMap {
	
	public static List<UtenteDTO> buildUtenteDTO(List<Utente> lU) {
		return lU.stream()
				.map(a -> buildUtenteDTO(a))
				.toList();
										
	}

	public static UtenteDTO buildUtenteDTO(Utente u) {
		return UtenteDTO.builder()
				.username(u.getUsername())
				.pwd(u.getPwd())
				.email(u.getEmail())
				.role(u.getRole().toString()) // per convertire enum in string basta un toString
				.build();
	}
}
