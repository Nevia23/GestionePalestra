package com.betacom.jpa.mapper;

import java.util.List;

import com.betacom.jpa.dto.output.PersoneDTO;
import com.betacom.jpa.models.Persone;

public class PersoneMap {

	public static List<PersoneDTO> buildPersoneDTO(List<Persone> lP) {
		return lP.stream()
				.map(a -> buildPersoneDTO(a))
				.toList();
										
	}

	public static PersoneDTO buildPersoneDTO(Persone a) {
		return PersoneDTO.builder()
				.id(a.getId())
				.cognome(a.getCognome())
				.nome(a.getNome())
				.email(a.getEmail())
				.colore(a.getColore())
				.build();
	}
}
