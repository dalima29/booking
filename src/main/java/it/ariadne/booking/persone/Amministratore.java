package it.ariadne.booking.persone;

import it.ariadne.booking.GestionePrenotazioni;
import it.ariadne.booking.Risorsa;

public class Amministratore extends Persona{

	public Amministratore(String nome, String cognome, String email, String username, String password) {
		super(nome, cognome, email, username, password);
	}

	public boolean aggiungiRisorsa(GestionePrenotazioni gp, Risorsa risorsa) {
		return gp.aggiungiRisorsa(risorsa);
	}

}
