package it.ariadne.booking;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GestionePrenotazioni {
	
	private Map <Risorsa,List<Prenotazione>> mappa;
	
	public GestionePrenotazioni () {
		mappa = new LinkedHashMap<>();
	}

}