package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.jpa.dto.input.AttivitaReq;
import com.betacom.jpa.dto.input.ValidationGroup;
import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.services.interfaces.IAttivitaServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/attivita")
public class AttivitaController {

	private final IAttivitaServices attS;

    @GetMapping("/list")
    public ResponseEntity<Object> list() throws Exception {        
        return ResponseEntity.ok(attS.list());
    }
    
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO> create(
			@Validated(ValidationGroup.Create.class)
			@RequestBody AttivitaReq req) throws Exception {
		attS.create(req);			
		return ResponseEntity.ok(ResponseDTO.builder()
        		.msg("create activity successful")
        		.build()
        		);
	}
	
    @PatchMapping("/update")
    public ResponseEntity<ResponseDTO> update(
			@Validated(ValidationGroup.Update.class)
    		@RequestBody AttivitaReq req) throws Exception {
        attS.update(req);            
        return ResponseEntity.ok(ResponseDTO.builder()
        		.msg("update activity successful")
        		.build()
        		);
    }
    
    @DeleteMapping("delete/{attID}")
    public ResponseEntity<ResponseDTO> delete(
    		@PathVariable Integer attID) throws Exception {
        attS.delete(attID);        
        return ResponseEntity.ok(ResponseDTO.builder()
        		.msg("delete activity successful")
        		.build()
        		);
    }

}
