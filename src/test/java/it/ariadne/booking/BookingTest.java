package it.ariadne.booking;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import it.ariadne.booking.risorse.Macchina;



public class BookingTest {
	
	@Test
	public void testRichiede() {
		List<Interval> listaPren = new ArrayList<>();
		Risorsa risorsa = new Macchina(listaPren);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 0);
		DateTime inizio2 = new DateTime(2018,12,25,6,0);
		DateTime fine2 = new DateTime(2018,12,25,10,0);
		DateTime inizio3 = new DateTime(2018,12,25,10,0);
		DateTime fine3 = new DateTime(2018,12,25,12,0);
		
		boolean prenotazioneEffettuata = risorsa.addPrenotazione(inizio,fine);
		
		boolean disponibilità = risorsa.getDisponibilità(inizio,fine);
		boolean disponibilità2 = risorsa.getDisponibilità(inizio2, fine2);
		boolean disponibilità3 = risorsa.getDisponibilità(inizio3, fine3);
		//ASSERT FIRST
		assertEquals("La risorsa è disponibile in quella data", false, disponibilità);
		//TRIANGULATE
		assertEquals("La risorsa è disponibile in quella data", false, disponibilità2);
		assertEquals("La risorsa è disponibile in quella data", true, disponibilità3);
		
		boolean prenotazioneEffettuata2 = risorsa.addPrenotazione(inizio2, fine2);
		//ASSERT FIRST
		assertEquals("La risorsa è stata aggiunta", true, prenotazioneEffettuata);
		//TRIANGULATE
		assertEquals("La risorsa non è stata aggiunta, esito false", false, prenotazioneEffettuata2);
		
	}

}
