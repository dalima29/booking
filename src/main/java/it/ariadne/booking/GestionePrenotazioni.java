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
	
	/**
	 * Metodo che permette all'amministratore di aggiungere una risorsa nella mappa
	 * @param ris, viene passata la risorsa da aggiungere
	 * @return true se la risorsa è stata aggiunta, false se la risorsa è già presente
	 */
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
	
	/**
	 * Il metodo (privato) verifica se una risorsa è disponibile o meno in una certa data
	 * @param inizio, data/ora inizio prenotazione per una data risorsa
	 * @param fine, data/ora fine prenotazione per una data risorsa
	 * @param ris, risorsa richiesta
	 * @return true se la risorsa è disponibile, false se non lo è
	 */
	private boolean getDisponibilità(DateTime inizio, DateTime fine, Risorsa ris) {
		Interval intervallo = new Interval (inizio,fine);
		List<Prenotazione> lista = this.mappa.get(ris);
		for (Prenotazione p : lista) {
			if(p.getIntervallo().overlaps(intervallo)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Il metodo permette di prenotare una risorsa per una certa data
	 * @param nomeP, nome della prenotazione
	 * @param inizio, data/ora inizio prenotazione
	 * @param fine, data/ora fine prenotazione
	 * @param ris, risorsa alla quale aggiungere la prenotazione
	 * @param persona, persona che effettua la prentazione
	 * @return true se la risorsa è disponibili per quella data, false altrimenti
	 */
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
	
	/**
	 * Il metodo rimuove una prenotazione effettuata su una risorsa
	 * @param nomeP, nome prenotazione
	 * @param nomeR, nome risorsa
	 * @return true se la prenotazione è stata rimossa, false se non c'era
	 */
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
	
	/**
	 * Il metodo ritorna all'utente la prima data disponibile per una determinata risorsa
	 * @param tipoRis, tipo risorsa che l'utente vuole
	 * @param nomeRis, nome della risorsa che vuole
	 * @param periodo, periodo di tempo che si vuole prenotare la risorsa
	 * @param inizioRicerca, data/ora da cui iniziare la ricerca della disponibilità
	 * @return la prima data/ora disponibile, o null se la risorsa cercata non esiste
	 */
	public DateTime primaData(String tipoRis, String nomeRis, Period periodo, DateTime inizioRicerca) {
		boolean dataDisp = false;
		boolean esisteRisorsa = false;
		do {
			for(Map.Entry<Risorsa, List<Prenotazione>> entry: this.mappa.entrySet()) {
				if(entry.getKey().getTipo().equals(tipoRis) && entry.getKey().getNome().equals(nomeRis)) {
					esisteRisorsa=true;
					dataDisp = getDisponibilità(inizioRicerca, inizioRicerca.plus(periodo), entry.getKey());
					if(dataDisp) {
						DateTime dt = new DateTime(inizioRicerca);
						return dt;
					}
				}
			}
			inizioRicerca = inizioRicerca.plusHours(1);
		} while(esisteRisorsa);
		return null;
	}

	/**
	 * Il metodo ritorna, all'utente, 
	 * la prima data disponibile per una determinata risorsa
	 * @param tipoRis, tipo risorsa da cercare
	 * @param periodo, periodo in cui si vuole prenotare la risorsa
	 * @param inizioD, data/ora da cui iniziare la ricerca
	 * @param fineD, data/ora in cui terminare la ricerca della disponibilità
	 * @return la data/ora se è stata trovata, null se la risorsa non esiste o
	 * 			non è disponibile in quell'arco di tempo
	 */
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
	
	/**
	 * Il metodo ritorna, all'utente, la prima data disponibile per prenotare
	 * una risorsa che rispetti un certo limite
	 * @param tipoRis, tipo di risorsa da prenotare
	 * @param periodo, periodo in cui si vuole prenotarla
	 * @param inizioD, data/ora inizio ricerca
	 * @param limite, limite (intero) della risorsa che l'utente vuole che si rispetti
	 * @return la data/ora se la risorsa è disponibile, null se la risorsa
	 * 			non esiste o non è disponibile
	 */
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

	/**
	 * Il metodo restituisce, all'amministratore, le prenotazioni di una determinata risorsa
	 * @param nomeRis, risorsa che si sta cercando
	 * @return una stringa con le prenotazioni o una stringa vuota se la risorsa
	 * 			non esiste o non ha prenotazioni
	 */
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
	
	/**
	 * Il metodo ritorna le prenotazioni di tutte le risorse
	 * @return una stringa con tutte le prenotazioni
	 */
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
	
	/**
	 * Il metodo ritorna le prenotazioni per persona
	 * @param lista di persone registrate al sito
	 * @return una stringa con le prenotazioni ordinate per persona
	 */
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
	
	/**
	 * Il metodo ritorna, all'utente, le prenotazioni correnti
	 * @param username, username dell'utente che sta ricercando le sue prenotazioni
	 * @return una stringa con le prenotazioni effettuate
	 */
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
	
	/**
	 * Il metodo prende tutte le prenotazioni (passate e non) di un utente
	 * @param username, username dell'utente che vuole sapere tutte le sue prenotazioni
	 * @return una stringa con tutte le prenotazioni
	 */
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

	/**
	 * Il metodo permette, all'amministratore, di eliminare una risorsa
	 * @param nomeR, nome della risorsa da eliminare
	 * @return true se è stata rimossa, false altrimenti
	 */
	public boolean eliminaRisorsa(String nomeR) {
		for(Map.Entry<Risorsa, List<Prenotazione>> entry : mappa.entrySet()) {
			if(entry.getKey().getNome().equals(nomeR)) {
				mappa.remove(entry.getKey());
				return true;
			}
		}
		return false;
	}

	/**
	 * Il metodo permette, all'amministratore, di aggiornare il limite di una risorsa
	 * @param nomeR, nome della risorsa da aggiornare
	 * @param limiteDaModificare, limite da modificare
	 * @return true se è stata aggiornata, false altrimenti
	 */
	public boolean aggiornaRisorsa(String nomeR, int limiteDaModificare) {
		for(Map.Entry<Risorsa, List<Prenotazione>> entry : this.mappa.entrySet()) {
			if(entry.getKey().getNome().equals(nomeR)) {
				entry.getKey().setLimite(limiteDaModificare);
				return true;
			}
		}
		return false;
	}

}