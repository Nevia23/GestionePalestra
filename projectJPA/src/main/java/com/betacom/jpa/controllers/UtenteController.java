package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.jpa.dto.input.SocioReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.input.ValidationGroup;
import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.services.interfaces.IUtenteServices;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/utente")
public class UtenteController {

	private final IUtenteServices utenteS;

	@PostMapping("/create")
	public ResponseEntity<ResponseDTO> create(@Validated(ValidationGroup.Create.class) @RequestBody UtenteReq req)
			throws Exception {
		utenteS.create(req);
		return ResponseEntity.ok(ResponseDTO.builder().msg("create sucessfull").build());
	}
	
	@GetMapping("/list")
	public ResponseEntity<Object> list() throws Exception {
		return ResponseEntity.ok(utenteS.list());
	}
	
	@PatchMapping("/update")
    public ResponseEntity<ResponseDTO> update(
    		@Validated(ValidationGroup.Update.class)
    		@RequestBody UtenteReq req) throws Exception {
        utenteS.update(req);
        return ResponseEntity.ok(ResponseDTO.builder()
        		.msg("user update successful")
        		.build()
        		);
    }
	
	@GetMapping("/getByUsername")
	public ResponseEntity<Object> getByUsername(
    		@RequestParam String username) throws Exception {
        return ResponseEntity.ok(utenteS.getByUsername(username));
    }
	
	
}
