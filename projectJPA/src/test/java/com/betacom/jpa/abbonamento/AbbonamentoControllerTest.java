package com.betacom.jpa.abbonamento;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.betacom.jpa.dto.input.AbbonamentoReq;
import com.betacom.jpa.dto.input.AttivitaReq;
import com.betacom.jpa.dto.input.SocioReq;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AbbonamentoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	// ID letti dal database: non scritti a mano, così il test non dipende dagli altri
	private static Integer socioID;
	private static Integer attivitaID;
	private static Integer abbonamentoID;

	@Test
	@Order(1)
	public void createSocioRest() throws Exception {
		log.debug("createSocioRest");

		SocioReq req = new SocioReq();
		req.setNome("Luca");
		req.setCognome("Abbonati");
		req.setCodiceFiscale("AB7777777");
		req.setEmail("l.abbonati@mail.com");

		mockMvc.perform(post("/rest/socio/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))
				).andExpect(status().isOk());

		MvcResult result = mockMvc.perform(get("/rest/socio/list"))
				.andExpect(status().isOk())
				.andReturn();
		List<Integer> ids = JsonPath.read(
				result.getResponse().getContentAsString(),
				"$[?(@.codiceFiscale=='AB7777777')].id");
		socioID = ids.get(0);
	}

	@Test
	@Order(2)
	public void createAttivitaRest() throws Exception {
		log.debug("createAttivitaRest");

		AttivitaReq req = new AttivitaReq();
		req.setDescrizione("Pilates");
		req.setPrezzo(50L);

		mockMvc.perform(post("/rest/attivita/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))
				).andExpect(status().isOk());

		MvcResult result = mockMvc.perform(get("/rest/attivita/list"))
				.andExpect(status().isOk())
				.andReturn();
		List<Integer> ids = JsonPath.read(
				result.getResponse().getContentAsString(),
				"$[?(@.descrizione=='PILATES')].id");
		attivitaID = ids.get(0);
	}

	@Test
	@Order(3)
	public void createAbbonamentoRest() throws Exception {
		log.debug("createAbbonamentoRest");

		AbbonamentoReq req = new AbbonamentoReq();
		req.setSocioID(socioID);
		req.setDataAbbonamento(LocalDate.now());
		req.setDurataAbbonamento(6);

		mockMvc.perform(post("/rest/abbonamento/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))
				).andExpect(status().isOk());

		MvcResult result = mockMvc.perform(get("/rest/abbonamento/listBySocio/{socioID}", socioID))
				.andExpect(status().isOk())
				.andReturn();
		abbonamentoID = JsonPath.read(
				result.getResponse().getContentAsString(),
				"$[0].id");
	}

	@Test
	@Order(4)
	public void createAbbonamentoErrorRest() throws Exception {
		log.debug("createAbbonamentoErrorRest");

		AbbonamentoReq req = new AbbonamentoReq();
		req.setSocioID(99999);
		req.setDataAbbonamento(LocalDate.now());

		mockMvc.perform(post("/rest/abbonamento/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))
				)
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(5)
	public void updateAbbonamentoRest() throws Exception {
		log.debug("updateAbbonamentoRest");

		AbbonamentoReq req = new AbbonamentoReq();
		req.setId(abbonamentoID);
		req.setSocioID(socioID);
		req.setDurataAbbonamento(12);

		mockMvc.perform(patch("/rest/abbonamento/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))
				).andExpect(status().isOk());
	}

	@Test
	@Order(6)
	public void listBySocioRest() throws Exception {
		log.debug("listBySocioRest");

		mockMvc.perform(get("/rest/abbonamento/listBySocio/{socioID}", socioID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].durataAbbonamento").value(12));
	}

	@Test
	@Order(7)
	public void createAbbonamentoAttivitaRest() throws Exception {
		log.debug("createAbbonamentoAttivitaRest");

		AbbonamentoReq req = new AbbonamentoReq();
		req.setId(abbonamentoID);
		req.setSocioID(socioID);
		req.setAttivitaID(attivitaID);

		mockMvc.perform(post("/rest/abbonamento/createAbbonamentoAttivita")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))
				).andExpect(status().isOk());

		mockMvc.perform(get("/rest/abbonamento/listBySocio/{socioID}", socioID))
				.andExpect(jsonPath("$[0].attivita[0].descrizione").value("PILATES"));
	}

	@Test
	@Order(8)
	public void deleteAbbonamentoAttivitaRest() throws Exception {
		log.debug("deleteAbbonamentoAttivitaRest");

		mockMvc.perform(delete(
				"/rest/abbonamento/deleteAbbonamentoAttivita/{abboID}/{attivitaID}",
				abbonamentoID, attivitaID))
				.andExpect(status().isOk());
	}

	@Test
	@Order(9)
	public void deleteAbbonamentoRest() throws Exception {
		log.debug("deleteAbbonamentoRest");

		mockMvc.perform(delete(
				"/rest/abbonamento/delete/{abboID}/{socioID}",
				abbonamentoID, socioID))
				.andExpect(status().isOk());
	}
}