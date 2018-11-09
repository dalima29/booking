package it.ariadne.booking;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public interface Risorsa {
	
static boolean getDisponibilità (List<Prenotazione> listaPren, DateTime inizio, DateTime fine) {
	Interval intervallo = new Interval(inizio,fine);
		for (Prenotazione p : listaPren) {
			if (p.getIntervallo().overlaps(intervallo)) {
				return false;
			}
		}
		return true;
	}

	boolean getDisponibilità(DateTime inizio, DateTime fine);
	boolean addPrenotazione(String nomePrenotazione, DateTime inizio, DateTime fine);

	boolean removePrenotazione(String nomeP);
	

}
