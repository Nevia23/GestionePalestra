package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.CertificatoReq;
import com.betacom.jpa.dto.output.CertificatoDTO;

public interface ICertificatoServices {
	
	void updateCertificato(CertificatoReq req) throws Exception;
	
	List<CertificatoDTO> listCertificato() throws Exception;

}
