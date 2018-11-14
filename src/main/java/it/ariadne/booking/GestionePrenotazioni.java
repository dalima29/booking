package it.ariadne.booking;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import it.ariadne.booking.persone.Persona;

public class GestionePrenotazioni {
	
	private Map <Risorsa,List<Prenotazione>> mappa;
	
	public GestionePrenotazioni () {
		mappa = new LinkedHashMap<>();
	}
	
	public boolean aggiungiRisorsa(Risorsa ris) {
		boolean risorsaEsistente = this.mappa.containsKey(ris);
		if(!risorsaEsistente) {
			List<Prenotazione> lis = new ArrayList<>();
			this.mappa.put(ris, lis);
			return true;
		} else {
			System.out.println("risorsa già esistente");
			return false;
		}
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
	
	public boolean addPrenotazione (String nomeP, DateTime inizio, DateTime fine, Risorsa ris, Persona persona) {
		List<Prenotazione> lis = this.mappa.get(ris);
		boolean esito = getDisponibilità(inizio, fine, ris);
		if (esito) {
			Interval intervallo = new Interval(inizio,fine);
			Prenotazione p = new Prenotazione(nomeP,intervallo,persona);
			lis.add(p);
		}
		return esito;
	}
	
	public boolean removePrenotazione(String nomeP, String nomeR) {
		for(Map.Entry<Risorsa, List<Prenotazione>> entry: mappa.entrySet()) {
			if(entry.getKey().getNome().equals(nomeR)) {
				List<Prenotazione> lisP = entry.getValue();
				for(Prenotazione p : lisP) {
					if(p.getNomeP().equals(nomeP)) {
						lisP.remove(p);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public DateTime primaData(Risorsa ris, Period periodo, DateTime inizioRicerca) {
		boolean dataDisp = false;
		while(dataDisp == false) {
			dataDisp = getDisponibilità(inizioRicerca, inizioRicerca.plus(periodo), ris);
			if (dataDisp) {
				DateTime dt = new DateTime(inizioRicerca);
				return dt;
			}
			inizioRicerca = inizioRicerca.plusHours(1);
		}
		return null;
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
				for (Prenotazione p : entry.getValue()) {
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
			for (Prenotazione p: entry.getValue()) {
				s+="\t"+p.toString()+"\n";
			}
		}
		return s;
	}
	
	public String riepilogoPpersona(List<Persona> lista) {
		String s = "";
		for (Persona p : lista) {
			s+=p.getNome()+" "+p.getCognome()+" "+p.getUsername()+"\n";
			for(Map.Entry<Risorsa, List<Prenotazione>> entry : mappa.entrySet()) {
				for(Prenotazione pre :entry.getValue()) {
					if(pre.getPersona().getUsername().equals(p.getUsername())) {
						s+="\t"+entry.getKey().getTipo()+" "+
					entry.getKey().getNome()+" "+"Prenotazione "+pre.getNomeP()+
					" "+pre.getIntervallo().toString()+"\n";
					}
				}
			}
		}
		System.out.println(s);
		return s;
	}
	
	public String getPrenotazioniUtenteNonPassate(String username) {
		String s = "Le mie prenotazioni sono ";
		for(Map.Entry<Risorsa, List<Prenotazione>> entry : mappa.entrySet()) {
			s+="\n";
			for(Prenotazione p : entry.getValue()) {
				if (p.getPersona().getUsername().equals(username)
						&& p.getIntervallo().isAfterNow()) {
					s+="\t"+entry.getKey().getTipo()+" "+entry.getKey().getNome()+ " "
							+p.toString()+"\n";
				}
			}
		}
		return s;
	}
	
	public String getCronologiaPrenotazioniUtente(String username) {
		String s = "Le mie prenotazioni sono ";
		for(Map.Entry<Risorsa, List<Prenotazione>> entry : mappa.entrySet()) {
			s+="\n";
			for(Prenotazione p : entry.getValue()) {
				if (p.getPersona().getUsername().equals(username)) {
					s+="\t"+entry.getKey().getTipo()+" "+entry.getKey().getNome()+ " "
							+p.toString()+"\n";
				}
			}
		}
		return s;
	}

	public boolean eliminaRisorsa(String nomeR) {
		for(Map.Entry<Risorsa, List<Prenotazione>> entry : mappa.entrySet()) {
			if(entry.getKey().getNome().equals(nomeR)) {
				mappa.remove(entry.getKey());
				return true;
			}
		}
		return false;
	}

}