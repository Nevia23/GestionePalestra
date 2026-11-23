package com.betacom.fe.controllers;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.betacom.fe.dto.input.AttivitaReq;
import com.betacom.fe.dto.input.UtenteReq;
import com.betacom.fe.dto.output.AttivitaDTO;
import com.betacom.fe.dto.output.ResponseDTO;
import com.betacom.fe.dto.output.UtenteDTO;
import com.betacom.fe.security.UtenteServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

	private final WebClient webclient;
	private final UtenteServices utenteS;
	
	@GetMapping("listAttivita")
	public ModelAndView listAttivita() {
		ModelAndView mav = new ModelAndView("admin/listAttivita");

		List<AttivitaDTO> att = webclient.get()
				.uri(uriBuilder -> uriBuilder
						.path("attivita/list")
						.build())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {
				}).block();

		mav.addObject("listAttivita", att);
		return mav;
	}

	@GetMapping("createAttivita")
	public ModelAndView createAttivita(Model model) {
		ModelAndView mav = new ModelAndView("admin/createAttivita");

		if (!model.containsAttribute("attivita")) {
			mav.addObject("attivita", new AttivitaReq());
		}

		model.addAttribute("titolo", "Creazione nuova attività");
		return mav;
	}

	@GetMapping("updateAttivita")
	public ModelAndView updateAttivita(@RequestParam Integer id, Model model) {
		ModelAndView mav = new ModelAndView("admin/createAttivita");

		if (!model.containsAttribute("attivita")) {
			List<AttivitaDTO> att = webclient.get()
					.uri(uriBuilder -> uriBuilder.path("attivita/list")
							.build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {
					}).block();

			AttivitaDTO attivita = att.stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow();

			model.addAttribute("attivita", AttivitaReq.builder()
					.id(attivita.getId())
					.descrizione(attivita.getDescrizione())
					.prezzo(attivita.getPrezzo())
					.build());

		}
		model.addAttribute("titolo", "Aggiornamento attività");
		return mav;
	}
	
	@PostMapping("saveAttivita")
	public String saveAttivita(@ModelAttribute("attivita") AttivitaReq req, RedirectAttributes ra) {
		
		String operation = req.getId() == null ? "create" : "update";
		String errorLink = req.getId() == null ? "createAttivita" : "updateAttivita?id=" + req.getId();
		String url = "attivita/" + operation;
		
		HttpMethod metodo = req.getId() == null ? HttpMethod.POST : HttpMethod.PATCH;
		
		ResponseEntity<ResponseDTO> response = webclient.method(metodo)
				.uri(url)
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class))
				.block();
		
		if (!response.getStatusCode().is2xxSuccessful()) {
			log.debug("error found in create");
			log.debug("msg: {}", response.getBody().getMsg());
			ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
			ra.addFlashAttribute("attivita", req);
			
			return "redirect:/admin/" + errorLink;
		}
		
		return "redirect:/admin/listAttivita";	
	}
	
	@GetMapping("deleteAttivita")
	public String deleteAttivita(@RequestParam Integer attID, RedirectAttributes ra) {

		ResponseEntity<ResponseDTO> response = webclient.delete()
				.uri("attivita/delete/{attID}", attID)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class))
				.block();

		log.debug("response deleteAttivita {}", response.getBody().getMsg());
		
		if (!response.getStatusCode().is2xxSuccessful()) {
	        ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
	    }
		
		return "redirect:/admin/listAttivita";
	}
	
	@GetMapping("listUtente")
	public ModelAndView listUtente() {
		ModelAndView mav = new ModelAndView("admin/listUtente");

		List<UtenteDTO> att = webclient.get()
				.uri(uriBuilder -> uriBuilder
						.path("utente/list")
						.build())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<UtenteDTO>>() {
				}).block();

		mav.addObject("listUtente", att);
		return mav;
	}
	
	@GetMapping("updateUtente")
	public ModelAndView updateUtente(@RequestParam String username, Model model) {
	    ModelAndView mav = new ModelAndView("admin/updateUtente");

	    if (!model.containsAttribute("utente")) {
	        UtenteDTO ut = webclient.get()
	                .uri(uriBuilder -> uriBuilder
	                        .path("utente/getByUsername")
	                        .queryParam("username", username)
	                        .build())
	                .retrieve()
	                .bodyToMono(UtenteDTO.class)
	                .block();

	        model.addAttribute("utente", UtenteReq.builder()
	                .username(ut.getUsername())
	                .email(ut.getEmail())
	                .role(ut.getRole())
	                .build());
	    }
	    model.addAttribute("titolo", "Modifica utente " + username);
	    return mav;
	}

	@GetMapping("createUtente")
	public ModelAndView createUtente(Model model) {
		ModelAndView mav = new ModelAndView("admin/registrazioneAdmin");

		if (!model.containsAttribute("utente")) {
			mav.addObject("utente", new UtenteReq());
		}

		model.addAttribute("titolo", "Creazione nuovo utente");
		return mav;
	}

	@PostMapping("createUtente")
	public String createUtente(@ModelAttribute("utente") UtenteReq req, RedirectAttributes ra) {

		if (!"USER".equals(req.getRole()) && !"ADMIN".equals(req.getRole())) {
			ra.addFlashAttribute("errorMsg", "Ruolo non valido");
			ra.addFlashAttribute("utente", req);
			return "redirect:/admin/createUtente";
		}

		ResponseEntity<ResponseDTO> response = webclient.post().uri("utente/create").bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class)).block();

		if (response == null || !response.getStatusCode().is2xxSuccessful()) {
			String msg = response != null && response.getBody() != null ? response.getBody().getMsg()
					: "Errore durante la creazione dell'utente";

			ra.addFlashAttribute("errorMsg", msg);
			ra.addFlashAttribute("utente", req);
			return "redirect:/admin/createUtente";
		}

		// Sincronizzo Spring Security
		utenteS.updateUtente(req);

		return "redirect:/admin/listUtente";
	}
	
	@PostMapping("saveUtente")
	public String saveUtente(@ModelAttribute("utente") UtenteReq req, RedirectAttributes ra) {

		if (!"USER".equals(req.getRole()) && !"ADMIN".equals(req.getRole())) {
			ra.addFlashAttribute("errorMsg", "Ruolo non valido");
			ra.addFlashAttribute("utente", req);
			return "redirect:/admin/updateUtente?username=" + req.getUsername();
		}

		ResponseEntity<ResponseDTO> response = webclient.patch().uri("utente/update").bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class)).block();

		if (response == null || !response.getStatusCode().is2xxSuccessful()) {
			String msg = response != null && response.getBody() != null ? response.getBody().getMsg()
					: "Errore durante la modifica dell'utente";

			ra.addFlashAttribute("errorMsg", msg);
			ra.addFlashAttribute("utente", req);
			return "redirect:/admin/updateUtente?username=" + req.getUsername();
		}

		// Aggiorno il ruolo nella memoria di Spring Security
		utenteS.updateRuolo(req.getUsername(), req.getRole());

		return "redirect:/admin/listUtente";
	}

	@GetMapping("deleteUtente")
	public String deleteUtente(@RequestParam String username, RedirectAttributes ra) {

		ResponseEntity<ResponseDTO> response = webclient.delete()
				.uri("utente/delete/{username}", username)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class)).block();

		log.debug("response user delete {}", response.getBody().getMsg());

		if (!response.getStatusCode().is2xxSuccessful()) {
			ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
			return "redirect:/admin/listUtente";
		}

		// Elimino l'utente anche dalla memoria di Spring Security
		utenteS.deleteUtente(username);

		log.debug("Utente {} eliminato anche dalla memoria frontend", username);

		return "redirect:/admin/listUtente";
	}

}
