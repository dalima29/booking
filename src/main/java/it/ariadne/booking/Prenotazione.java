package it.ariadne.booking;

import org.joda.time.Interval;

import it.ariadne.booking.persone.Persona;

public class Prenotazione {

	private String nomeP;//nome prenotazione
	private Interval intervallo;//intervallo in cui si effettua la prenotazione
	private Persona p;//utente che effettua la prenotazione
	
	public Prenotazione (String nomeP, Interval intervallo, Persona p) {
		//aggiungere Persona
		this.nomeP = nomeP;
		this.intervallo = intervallo;
		this.p = p;
	}

	public Interval getIntervallo() {
		return intervallo;
	}

	public String getNomeP() {
		return nomeP;
	}
	
	public String toString() {//aggiungere persona dopo
		return "Prenotazione "+this.nomeP+" effettuata da "+this.p.getNome()
		+" "+this.p.getCognome()+" "+this.p.getUsername()+" "+intervallo.toString();
	}
	
	public Persona getPersona() {
		return this.p;
	}
}