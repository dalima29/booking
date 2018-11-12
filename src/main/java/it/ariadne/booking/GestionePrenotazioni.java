package it.ariadne.booking;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

public class GestionePrenotazioni {
	
	private Map <Risorsa,List<Prenotazione>> mappa;
	
	public GestionePrenotazioni () {
		mappa = new LinkedHashMap<>();
	}
	
	public void aggiungiRisorsa(Risorsa ris) {
		List<Prenotazione> lis = new ArrayList<>();
		mappa.put(ris, lis);
	}
	
	public boolean getDisponibilità(DateTime inizio, DateTime fine, Risorsa ris) {
		Interval intervallo = new Interval (inizio,fine);
		List<Prenotazione> lista = this.mappa.get(ris);
		for (Prenotazione p : lista) {
			if(p.getIntervallo().overlaps(intervallo)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean addPrenotazione (String nomeP, DateTime inizio, DateTime fine, Risorsa ris) {
		List<Prenotazione> lis = this.mappa.get(ris);
		boolean esito = getDisponibilità(inizio, fine, ris);
		if (esito) {
			Interval intervallo = new Interval(inizio,fine);
			Prenotazione p = new Prenotazione(nomeP,intervallo);
			lis.add(p);
		}
		return esito;
	}
	
	public boolean removePrenotazione(String nomeP, Risorsa ris) {
		List<Prenotazione> lisP = this.mappa.get(ris);
		for (Prenotazione p : lisP) {
			if (p.getNomeP().equals(nomeP)) {
				lisP.remove(p);
				return true;
			}
		}
		return false;
	}
	
	public DateTime primaDataDisponibile(Risorsa ris, Period periodo, DateTime inizioD, DateTime fineD) {
		boolean dataDisp = false;
		while (inizioD.plus(periodo).isBefore(fineD) && dataDisp == false) {//ciclo finchè sono nel periodo di interesse
			dataDisp = getDisponibilità(inizioD, inizioD.plusHours(3), ris);
			if (dataDisp) {
				DateTime dt = new DateTime(inizioD);
				return dt;
			} else {
				inizioD = inizioD.plusHours(1);
			}
		}
		return null;
	}

}