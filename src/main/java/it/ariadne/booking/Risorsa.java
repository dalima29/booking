package it.ariadne.booking;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public interface Risorsa {
	
static boolean getDisponibilità (List<Interval> listaPren, DateTime inizio, DateTime fine) {
		Interval intervallo = new Interval(inizio,fine);
		for (Interval inter : listaPren) {
			if (inter.overlaps(intervallo)) {
				return false;
			}
		}
		return true;
	}

	boolean getDisponibilità(DateTime inizio, DateTime fine);
	boolean addPrenotazione(DateTime inizio, DateTime fine);

}
