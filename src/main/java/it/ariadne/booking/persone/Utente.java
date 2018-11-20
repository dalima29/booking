package it.ariadne.booking.persone;

import org.joda.time.DateTime;
import org.joda.time.Period;

import it.ariadne.booking.GestionePrenotazioni;
import it.ariadne.booking.Risorsa;

public class Utente extends Persona{

	public Utente(String nome, String cognome, String email,String username,String password) {
		super(nome, cognome, email,username,password);
	}

	public boolean addPrenotazione(GestionePrenotazioni gp, String nomeP, DateTime inizio, DateTime fine,
			Risorsa risorsa) {
		return gp.addPrenotazione(nomeP, inizio, fine, risorsa,this);
	}

	public boolean getDisponibilità(GestionePrenotazioni gp, DateTime inizio, DateTime fine, Risorsa risorsa) {
		return gp.getDisponibilità(inizio, fine, risorsa);
	}

	public boolean removePrenotazione(GestionePrenotazioni gp,String nomeP, String nome) {
		return gp.removePrenotazione(nomeP, nome);
	}

	public DateTime primaData(GestionePrenotazioni gp, String tipoR, String nomeR, Period periodo, DateTime inizioRicerca) {
		return gp.primaData(tipoR,nomeR, periodo, inizioRicerca);
	}

	public DateTime primaDataDisponibile(GestionePrenotazioni gp, String tipo, Period periodo, DateTime inizioDisp,
			DateTime fineDisp) {
		return gp.primaDataDisponibile(tipo, periodo, inizioDisp, fineDisp);
	}

	public DateTime primaDataDisponibileLimite(GestionePrenotazioni gp, String tipo, Period periodo,
			DateTime inizioRicerca, int limite) {
		return gp.primaDataDisponibileLimite(tipo, periodo, inizioRicerca, limite);
	}

	public String getPrenotazioniUtenteNonPassate(GestionePrenotazioni gp) {
		return gp.getPrenotazioniUtenteNonPassate(this.getUsername());
	}

	public String getCronologiaPrenotazioniUtente(GestionePrenotazioni gp) {
		return gp.getCronologiaPrenotazioniUtente(this.getUsername());
	}

}
