package it.ariadne.booking;

import org.joda.time.Interval;

public class Prenotazione {

	private String nomeP;
	private Interval intervallo;
	
	public Prenotazione (String nomeP, Interval intervallo) {
		//aggiungere Persona
		this.nomeP = nomeP;
		this.intervallo = intervallo;
	}

	public Interval getIntervallo() {
		return intervallo;
	}

	public String getNomeP() {
		return nomeP;
	}
	
	
	
	
	
}