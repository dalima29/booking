package it.ariadne.booking.risorse;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import it.ariadne.booking.Prenotazione;
import it.ariadne.booking.Risorsa;

public class Macchina implements Risorsa {
	
	private List<Prenotazione> listaPren = new ArrayList<>();
	
	public Macchina (List<Prenotazione> listaPren) {
		this.listaPren = listaPren;
	}

	@Override
	public boolean getDisponibilità(DateTime inizio, DateTime fine) {
		return Risorsa.getDisponibilità(listaPren, inizio, fine);
	}
	
	public boolean addPrenotazione (String nomeP, DateTime inizio, DateTime fine) {
		boolean esito = this.getDisponibilità(inizio, fine);
		if (esito) {
			Interval intervallo = new Interval(inizio,fine);
			Prenotazione p = new Prenotazione (nomeP,intervallo);
			this.listaPren.add(p);
		}
		return esito;
	}
	
	public boolean removePrenotazione (String nomeP) {
		for (Prenotazione p : listaPren) {
			if (p.getNomeP().equals(nomeP)) {
				listaPren.remove(p);
				return true;
			}
		}
		return false;
	}

}
