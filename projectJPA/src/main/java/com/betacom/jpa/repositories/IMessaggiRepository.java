package com.betacom.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.jpa.models.Messaggi;
import com.betacom.jpa.models.MessaggiID;

// a sx la classe della tabella, a dx il tipo della primary key
public interface IMessaggiRepository extends JpaRepository<Messaggi, MessaggiID> {

}
