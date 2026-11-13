package com.betacom.jpa;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.betacom.jpa.abbonamento.AbbonamentoControllerTest;
import com.betacom.jpa.attivita.AttivitaControllerTest;
import com.betacom.jpa.certificato.CertificatoControllerTest;
import com.betacom.jpa.socio.SocioControllerTest;
import com.betacom.jpa.socio.SocioServiceTest;

@Suite
@SelectClasses({
	SocioServiceTest.class,
	SocioControllerTest.class,
	CertificatoControllerTest.class,
	AttivitaControllerTest.class,
	AbbonamentoControllerTest.class
})
public class SuiteClass {

}
