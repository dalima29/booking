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

	public DateTime primaDataDisponibile(String tipoRis,Period periodo,DateTime inizioD,DateTime fineD) {
		boolean dataDisp = false;
		boolean almeno1risorsa = false;
		do {
			for(Map.Entry<Risorsa, List<Prenotazione>> entry: mappa.entrySet()) {
				if(entry.getKey().getTipo().equals(tipoRis)) {
					almeno1risorsa=true;
					dataDisp = getDisponibilità(inizioD, inizioD.plus(periodo), entry.getKey());
					if(dataDisp) {
						DateTime dt = new DateTime(inizioD);
						return dt;
					}
				}
			}
			inizioD = inizioD.plusHours(1);
		} while(inizioD.plus(periodo).isBefore(fineD) && almeno1risorsa);
		return null;
	}
	
	public DateTime primaDataDisponibileLimite(String tipoRis, Period periodo, DateTime inizioD, int limite) {
		boolean dataDisp = false;
		boolean almeno1risorsa = false;//verifico se ho almeno una determinata risorsa che rispetti limite
		do {			
			for (Map.Entry<Risorsa, List<Prenotazione>> entry : mappa.entrySet()) {
				if(entry.getKey().getTipo().equals(tipoRis) && (entry.getKey().getLimite()>= limite)) {
					almeno1risorsa=true;
					dataDisp = getDisponibilità(inizioD, inizioD.plus(periodo), entry.getKey());
					if(dataDisp) {						
						return inizioD;
					}
				}
			}
			inizioD = inizioD.plusHours(1);
		} while(almeno1risorsa);
		return null;
	}

	public String leggiRisorsa(String nomeRis) {
		String s = "";
		for(Map.Entry<Risorsa, List<Prenotazione>> entry : mappa.entrySet()) {
			if(entry.getKey().getNome().equals(nomeRis)) {
				s = entry.getKey().getTipo()+" "+entry.getKey().getNome()+
						" ha le seguenti prenotazioni: "+"\n";
				List<Prenotazione> lista = entry.getValue();
				for (Prenotazione p : lista) {
					s+="\t"+p.toString()+"\n";
				}
			}
		}
		
		return s;
	}
	public String riepilogoPrisorsa () {
		String s="";
		for(Map.Entry<Risorsa, List<Prenotazione>> entry : mappa.entrySet()) {
			s = entry.getKey().getTipo()+" "+entry.getKey().getNome()+
					" ha le seguenti prenotazioni: "+"\n";
			List<Prenotazione> lista = entry.getValue();
			for (Prenotazione p: lista) {
				s+="\t"+p.toString()+"\n";
			}
		}
		return s;
	}

}