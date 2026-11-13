package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class AttivitaReq {

	@NotNull(groups = ValidationGroup.Update.class, message = "att-no-id")
	private Integer id;
	
	@NotNull(groups = ValidationGroup.Create.class, message = "att-no-desc")
    @NotBlank(groups = ValidationGroup.Create.class, message = "att-no-desc")
	private String descrizione;
	
	@NotNull(groups = ValidationGroup.Create.class, message = "att-no-prezzo")
	@PositiveOrZero(groups = ValidationGroup.Create.class, message = "att-pos-prezzo")
	private Long prezzo;
}
