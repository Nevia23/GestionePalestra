package com.betacom.jpa.mapper;

import java.util.List;

import com.betacom.jpa.dto.output.AttivitaDTO;
import com.betacom.jpa.models.Attivita;

public class AttivitaMap {

	public static AttivitaDTO buildAttivitaDTO(Attivita a) {
		return AttivitaDTO.builder()
				.id(a.getId())
				.descrizione(a.getDescrizione())
				.prezzo(a.getPrezzo())
				.build();
	}
	
	public static List<AttivitaDTO> buildAttivitaDTO(List<Attivita> lA) {
		return lA.stream()
				.map(s -> buildAttivitaDTO(s))
				.toList();
	}
}
