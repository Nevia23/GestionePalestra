package com.betacom.jpa.attivita;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.betacom.jpa.dto.input.AttivitaReq;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AttivitaControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	@Order(1)
	public void createAttivitaRest() throws Exception {
		log.debug("createAttivitaRest");
		
		AttivitaReq req = new AttivitaReq();
		req.setDescrizione("Yoga");
		req.setPrezzo(40L);
		
		mockMvc.perform(post("/rest/attivita/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))		
				).andExpect(status().isOk());
	}
	
	@Test
	@Order(2)
	public void updateAttivitaRest() throws Exception {
		log.debug("updateAttivitaRest");

		AttivitaReq req = new AttivitaReq();
		req.setId(1);
		req.setDescrizione("Zumba");
		
		mockMvc.perform(patch("/rest/attivita/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))
				).andExpect(status().isOk());
	}
	
	@Test
	@Order(3)
	public void listAttivitaRest() throws Exception {
	    log.debug("listRest");

	    mockMvc.perform(get("/rest/attivita/list"))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("$").isArray())
	        .andExpect(jsonPath("$[0].descrizione").value("ZUMBA"));
	}
	
	@Test
	@Order(4)
	public void deleteAttivitaRest() throws Exception {
		log.debug("deleteAttivitaRest");
		
		mockMvc.perform(delete("/rest/attivita/delete/{attID}", 1))
			.andExpect(status().isOk());
	}
	
}
