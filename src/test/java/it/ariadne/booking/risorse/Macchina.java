package it.ariadne.booking.risorse;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import it.ariadne.booking.Risorsa;

public class Macchina implements Risorsa {
	
	private List<Interval> listaPren = new ArrayList<>();
	
	public Macchina (List<Interval> listaPren) {
		this.listaPren = listaPren;
	}

	@Override
	public boolean getDisponibilità(DateTime inizio, DateTime fine) {
		return Risorsa.getDisponibilità(listaPren, inizio, fine);
	}
	
	public boolean addPrenotazione (DateTime inizio, DateTime fine) {
		boolean esito = this.getDisponibilità(inizio, fine);
		if (esito) {
			Interval intervallo = new Interval(inizio,fine);
			this.listaPren.add(intervallo);
		}
		return esito;
	}

}
