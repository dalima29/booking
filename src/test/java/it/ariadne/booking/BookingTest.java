package it.ariadne.booking;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import it.ariadne.booking.risorse.Macchina;



public class BookingTest {
	
	@Test
	public void testRichiede() {
		List<Prenotazione> listaPren = new ArrayList<>();
		GestionePrenotazioni gesP = new GestionePrenotazioni();
		
		Risorsa risorsa = new Macchina();
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 0);
		DateTime inizio2 = new DateTime(2018,12,25,6,0);
		DateTime fine2 = new DateTime(2018,12,25,10,0);
		DateTime inizio3 = new DateTime(2018,12,25,10,0);
		DateTime fine3 = new DateTime(2018,12,25,12,0);
		
		gesP.aggiungiRisorsa(risorsa);
		boolean prenotazioneEffettuata = gesP.addPrenotazione("pippo",inizio,fine,risorsa);
		
		boolean disponibilità = gesP.getDisponibilità(inizio,fine,risorsa);
		boolean disponibilità2 = gesP.getDisponibilità(inizio2, fine2,risorsa);
		boolean disponibilità3 = gesP.getDisponibilità(inizio3, fine3,risorsa);
		//ASSERT FIRST
		assertEquals("La risorsa è disponibile in quella data", false, disponibilità);
		//TRIANGULATE
		assertEquals("La risorsa è disponibile in quella data", false, disponibilità2);
		assertEquals("La risorsa è disponibile in quella data", true, disponibilità3);
		
		boolean prenotazioneEffettuata2 = gesP.addPrenotazione("pippo2",inizio2, fine2,risorsa);
		//ASSERT FIRST
		assertEquals("La prenotazione è stata aggiunta", true, prenotazioneEffettuata);
		//TRIANGULATE
		assertEquals("La prenotazione non è stata aggiunta, esito false", false, prenotazioneEffettuata2);
		
		boolean prenotazioneRimossa = gesP.removePrenotazione("pippo",risorsa);
		boolean prenotazioneRimossa2 = gesP.removePrenotazione("pluto",risorsa);
		
		//ASSERT FIRST
		assertEquals("La prenotazione è stata rimossa", true, prenotazioneRimossa);
		//TRIANGULATE
		assertEquals("La prenotazione non è stata rimossa, esito false", false, prenotazioneRimossa2);
		
	}
	
	@Test
	//Prima data disponibile
	public void TestDisponibile () {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Risorsa ris = new Macchina();
		gp.aggiungiRisorsa(ris);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 8, 59);
		gp.addPrenotazione("pippo", inizio, fine, ris);
		Period periodo = new Period(3, 0, 0, 0);
		DateTime inizioDisp = new DateTime(2018,12,25,7,0);
		DateTime fineDisp = new DateTime(2018,12,26,7,0);
		DateTime primaData = gp.primaDataDisponibile(ris,periodo,inizioDisp,fineDisp);
		
		//ASSERT FIRST
		assertEquals("La prima data disponibile è ora",new DateTime(2018,12,25,9,0),primaData);
		
		DateTime inizioDisp2 = new DateTime(2018,12,25,7,0);
		DateTime fineDisp2 = new DateTime(2018,12,25,9,59);
		DateTime secondaData = gp.primaDataDisponibile(ris, periodo, inizioDisp2, fineDisp2);
		//TRIANGULATE
		assertEquals("La prima data disponibile è null", null, secondaData);
	}

}
