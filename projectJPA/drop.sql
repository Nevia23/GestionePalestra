
    alter table abbonamento_attivita 
       drop 
       foreign key FKma8fmps7cdmvas2fhd06m2tlr;

    alter table abbonamento_attivita 
       drop 
       foreign key FK7slwjgyb7wchv5gnm07g9m6v1;

    alter table abbonamento_socio 
       drop 
       foreign key FK22uaw7830fw71duo88vg10i3m;

    alter table socio_palestra 
       drop 
       foreign key FKx1r6i4l4gr6iyvj9lla4fe4y;

    drop table if exists abbonamento_attivita;

    drop table if exists abbonamento_socio;

    drop table if exists attivita_palestra;

    drop table if exists certificato_medico;

    drop table if exists messaggi_systema;

    drop table if exists socio_palestra;

    drop table if exists utente;
