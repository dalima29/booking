package it.ariadne.booking.persone;

import java.util.List;

import it.ariadne.booking.GestionePrenotazioni;
import it.ariadne.booking.Risorsa;

public class Amministratore extends Persona{

	public Amministratore(String nome, String cognome, String email, String username, String password) {
		super(nome, cognome, email, username, password);
	}

	public boolean aggiungiRisorsa(GestionePrenotazioni gp, Risorsa risorsa) {
		return gp.aggiungiRisorsa(risorsa);
	}

	public String leggiRisorsa(GestionePrenotazioni gp, String nomeR) {
		return gp.leggiRisorsa(nomeR);
	}

	public String riepilogoPrisorsa(GestionePrenotazioni gp) {
		return gp.riepilogoPrisorsa();
	}

	public boolean eliminaRisorsa(GestionePrenotazioni gp, String nome) {
		return gp.eliminaRisorsa(nome);
	}

	public String riepilogoPpersona(GestionePrenotazioni gp, List<Persona> lista) {
		// TODO Auto-generated method stub
		return gp.riepilogoPpersona(lista);
	}

	public boolean aggiornaRisorsa(GestionePrenotazioni gp, String nomeR, int limiteDaModificare) {
		return gp.aggiornaRisorsa(nomeR,limiteDaModificare);
	}



}
