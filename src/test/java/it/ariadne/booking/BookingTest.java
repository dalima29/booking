package it.ariadne.booking;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;
import org.joda.time.Period;

import it.ariadne.booking.persone.Amministratore;
import it.ariadne.booking.persone.Persona;
import it.ariadne.booking.persone.Utente;
import it.ariadne.booking.risorse.Aula;
import it.ariadne.booking.risorse.Macchina;
import it.ariadne.booking.risorse.Portatile;



public class BookingTest {
	
	@Test
	public void testRichiede() {
		GestionePrenotazioni gesP = new GestionePrenotazioni();
		
		Risorsa risorsa = new Macchina("Toyota Yaris",5);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 0);
		DateTime inizio2 = new DateTime(2018,12,25,6,0);
		DateTime fine2 = new DateTime(2018,12,25,10,0);
		DateTime inizio3 = new DateTime(2018,12,25,10,0);
		DateTime fine3 = new DateTime(2018,12,25,12,0);
		
		gesP.aggiungiRisorsa(risorsa);
		boolean prenotazioneEffettuata = gesP.addPrenotazione("pippo",inizio,fine,risorsa,null);
		
		boolean disponibilità = gesP.getDisponibilità(inizio,fine,risorsa);
		boolean disponibilità2 = gesP.getDisponibilità(inizio2, fine2,risorsa);
		boolean disponibilità3 = gesP.getDisponibilità(inizio3, fine3,risorsa);
		//ASSERT FIRST
		assertEquals("La risorsa è disponibile in quella data", false, disponibilità);
		//TRIANGULATE
		assertEquals("La risorsa è disponibile in quella data", false, disponibilità2);
		assertEquals("La risorsa è disponibile in quella data", true, disponibilità3);
		
		boolean prenotazioneEffettuata2 = gesP.addPrenotazione("pippo2",inizio2, fine2,risorsa,null);
		//ASSERT FIRST
		assertEquals("La prenotazione è stata aggiunta", true, prenotazioneEffettuata);
		//TRIANGULATE
		assertEquals("La prenotazione non è stata aggiunta, esito false", false, prenotazioneEffettuata2);
		
		boolean prenotazioneRimossa = gesP.removePrenotazione("pippo",risorsa.getNome());
		boolean prenotazioneRimossa2 = gesP.removePrenotazione("pluto",risorsa.getNome());
		
		//ASSERT FIRST
		assertEquals("La prenotazione è stata rimossa", true, prenotazioneRimossa);
		//TRIANGULATE
		assertEquals("La prenotazione non è stata rimossa, esito false", false, prenotazioneRimossa2);
		
	}
	
	@Test
	public void testPrimaData () {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Risorsa ris = new Macchina("Toyota Yaris",5);
		gp.aggiungiRisorsa(ris);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		gp.addPrenotazione("pippo", inizio, fine, ris,null);
		Period periodo = new Period(3,0,0,0);
		DateTime inizioRicerca = new DateTime(2018,12,25,9,0);
		DateTime dataDisp = gp.primaData(ris, periodo, inizioRicerca);
		DateTime dataDaVer = new DateTime(2018,12,25,10,0);
		//DateTime primaDataDisp = new DateTime
		assertEquals("Prima data",dataDaVer,dataDisp);
	}
	
	@Test
	//Prima data disponibile
	public void TestDisponibile () {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Risorsa ris = new Macchina("Toyota Yaris",5);
		gp.aggiungiRisorsa(ris);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 8, 59);
		gp.addPrenotazione("pippo", inizio, fine, ris, null);
		Period periodo = new Period(3, 0, 0, 0);
		DateTime inizioDisp = new DateTime(2018,12,25,7,0);
		DateTime fineDisp = new DateTime(2018,12,26,7,0);
		DateTime primaData = gp.primaDataDisponibile(ris.getTipo(),periodo,inizioDisp,fineDisp);
		
		//ASSERT FIRST
		assertEquals("La prima data disponibile è ora",new DateTime(2018,12,25,9,0),primaData);
		
		DateTime inizioDisp2 = new DateTime(2018,12,25,7,0);
		DateTime fineDisp2 = new DateTime(2018,12,25,9,59);
		DateTime secondaData = gp.primaDataDisponibile(ris.getTipo(), periodo, inizioDisp2, fineDisp2);
		//TRIANGULATE
		assertEquals("La prima data disponibile è null", null, secondaData);
	}
	@Test
	public void TestDisponibileLimite () {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Risorsa ris = new Macchina("Toyota Yaris",5);
		Risorsa ris2 = new Macchina("Opel Corsa",6);
		Risorsa ris3 = new Aula("A5",45);
		Risorsa ris4 = new Portatile("Asus 845",4);
		gp.aggiungiRisorsa(ris4);
		gp.aggiungiRisorsa(ris3);
		gp.aggiungiRisorsa(ris);
		gp.aggiungiRisorsa(ris2);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		gp.addPrenotazione("pippo", inizio, fine, ris, null);
		gp.addPrenotazione("pluto", inizio, fine.minusHours(1), ris2, null);
		gp.addPrenotazione("paperino", inizio, fine, ris4, null);
		Period periodo = new Period(3,0,0,0);
		int numPosti = 4;
		DateTime inizioRicerca = new DateTime(2018,12,25,9,0);
		DateTime dataDispLimite = gp.primaDataDisponibileLimite(ris.getTipo(), periodo, inizioRicerca,numPosti);
		//la prima data che va bene è inizioRicerca con la seconda Risorsa;
		assertEquals("La prima data disponibile che rispetta il limite è",inizioRicerca,dataDispLimite);
		gp.addPrenotazione("ciao", new DateTime(2018,12,25,9,0), new DateTime(2018,12,25,10,59),ris2,null);
		//la prima data che va bene è (2018,12,25,10,0)
		DateTime dataDispLimite2 = gp.primaDataDisponibileLimite(ris.getTipo(), periodo, inizioRicerca, numPosti);
		assertEquals("La prima data disponibile che rispetta il limite è", new DateTime(2018,12,25,10,0),dataDispLimite2);
		//non ho una risorsa che rispetti il limite
		DateTime dataDispLimite3 = gp.primaDataDisponibileLimite(ris.getTipo(), periodo, inizioRicerca, 7);
		assertEquals("Non esiste una risorsa che rispetti il limite, data null", null,dataDispLimite3);
	}
	@Test
	public void riepilogoPrenotazioniRisorse () {
		Persona p = new Utente("Davide", "Limardi", "das@gmail.com","pluto","ciao");
		GestionePrenotazioni gp = new GestionePrenotazioni();
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		Risorsa aula = new Aula("A5", 200);
		gp.aggiungiRisorsa(aula);
		((Utente)p).addPrenotazione(gp,"pippo", inizio, fine, aula);
		String riepilogo = gp.riepilogoPrisorsa();
		String verifica = "Aula A5 ha le seguenti prenotazioni: "+"\n"+"\t"+
		"Prenotazione pippo effettuata da Davide Limardi pluto 2018-12-25T07:00:00.000+01:00/2018-12-25T09:59:00.000+01:00"+
				"\n";
		assertEquals("Il riepilogo prenotazioni aula a5 è",verifica,riepilogo);
	}
	@Test
	public void riepilogoPrenotazioniUtenteNonPassate () {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Persona p = new Utente("Davide", "Limardi", "das@gmail.com","pluto","ciao");
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		Risorsa ris = new Macchina("Toyota yaris",5);
		gp.aggiungiRisorsa(ris);
		((Utente)p).addPrenotazione(gp,"pippo", inizio, fine, ris);
		String stringaDaVerificare ="Le mie prenotazioni sono "+"\n"+"\t"+
		"Macchina Toyota yaris Prenotazione pippo effettuata da Davide Limardi pluto "+"2018-12-25T07:00:00.000+01:00/2018-12-25T09:59:00.000+01:00"+
		"\n";
		String prenotazioni = ((Utente)p).getPrenotazioniUtenteNonPassate(gp);
		assertEquals("Le prenotazione di Davide sono",stringaDaVerificare,prenotazioni);
		//data passata
		DateTime inizio2 = new DateTime(2018, 11, 12, 7, 0);
		DateTime fine2 = new DateTime(2018, 11, 12, 9, 59);
		((Utente)p).addPrenotazione(gp,"pippo", inizio2, fine2, ris);
		String stringaDaVerificare2=stringaDaVerificare;
		String prenotazioni2 = ((Utente)p).getPrenotazioniUtenteNonPassate(gp);
		assertEquals("Le mie prenotazioni sono ",stringaDaVerificare2,prenotazioni2);
		//cronologia prenotazioni
		String stringaDaVerificare3 =stringaDaVerificare+"\t"+
				"Macchina Toyota yaris Prenotazione pippo effettuata da Davide Limardi pluto "+"2018-11-12T07:00:00.000+01:00/2018-11-12T09:59:00.000+01:00"+
				"\n";
		String prenotazioni3 = ((Utente)p).getCronologiaPrenotazioniUtente(gp);
		assertEquals("Le mie prenotazioni sono ",stringaDaVerificare3,prenotazioni3);
	}

	
	@Test
	public void leggiRisorsa () {
		Persona p = new Utente("Davide", "Limardi", "das@gmail.com","pluto","ciao");
		Risorsa risorsa = new Portatile("Lenovo G230",4);
		String nomeR = risorsa.getNome();
		GestionePrenotazioni gp = new GestionePrenotazioni();
		gp.aggiungiRisorsa(risorsa);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		((Utente)p).addPrenotazione(gp,"pippo", inizio, fine, risorsa);
		String riepilogo = gp.leggiRisorsa(nomeR);
		String verifica = "Portatile Lenovo G230 ha le seguenti prenotazioni: "+"\n"+"\t"+
		"Prenotazione pippo effettuata da Davide Limardi pluto 2018-12-25T07:00:00.000+01:00/2018-12-25T09:59:00.000+01:00"+
				"\n";
		assertEquals("Informazioni sulla risorsa",verifica,riepilogo);
	}
	
	@Test
	public void eliminaRisorsa () {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Risorsa risorsa = new Macchina("Toyota Yaris",5);
		gp.aggiungiRisorsa(risorsa);
		boolean esito = gp.eliminaRisorsa(risorsa.getNome());
		assertEquals("Risorsa eliminata",true,esito);
		boolean esito2 = gp.eliminaRisorsa(risorsa.getNome());
		//esito false perchè la risorsa è già stata eliminata
		assertEquals("Risorsa eliminata",false,esito2);
	}
	@Test
	public void testUtenteAggiungiPrenotazione() {
		Persona p = new Utente("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		GestionePrenotazioni gp = new GestionePrenotazioni();
		String nomeP = "pluto";
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		Risorsa risorsa = new Portatile("Lenovo G230",5);
		gp.aggiungiRisorsa(risorsa);
		boolean esito = ((Utente) p).addPrenotazione(gp,nomeP,inizio,fine,risorsa);
		//aggiungi prenotazione
		assertEquals("Prenotazione aggiunta",true,esito);
		//rimuovi prenotazione
		boolean esito2 = ((Utente)p).removePrenotazione(gp,nomeP,risorsa.getNome());
		assertEquals("Prenotazione rimossa",true,esito2);
	}
	
	@Test
	public void testUtentePrendiDisponibilità() {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		DateTime inizio = new DateTime(2018,12,25,7,0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		Risorsa risorsa = new Aula("A5",100);
		Persona p = new Utente("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		gp.aggiungiRisorsa(risorsa);
		boolean esito = ((Utente)p).getDisponibilità(gp,inizio,fine,risorsa);
		assertEquals("C'è la disponibilità",true,esito);
	}
	@Test
	public void testUtentePrimaData () {
		Persona p = new Utente("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Risorsa ris = new Macchina("Toyota Yaris",5);
		gp.aggiungiRisorsa(ris);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		((Utente)p).addPrenotazione(gp,"pippo", inizio, fine, ris);
		Period periodo = new Period(3,0,0,0);
		DateTime inizioRicerca = new DateTime(2018,12,25,9,0);
		DateTime dataDisp = ((Utente)p).primaData(gp,ris, periodo, inizioRicerca);
		DateTime dataDaVer = new DateTime(2018,12,25,10,0);
		//DateTime primaDataDisp = new DateTime
		assertEquals("Prima data",dataDaVer,dataDisp);
		
	}
	@Test
	public void testUtentePrimaDataDisponibile() {
		Persona p = new Utente("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Risorsa ris = new Macchina("Toyota Yaris",5);
		gp.aggiungiRisorsa(ris);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 8, 59);
		((Utente)p).addPrenotazione(gp,"pippo", inizio, fine, ris);
		Period periodo = new Period(3, 0, 0, 0);
		DateTime inizioDisp = new DateTime(2018,12,25,7,0);
		DateTime fineDisp = new DateTime(2018,12,26,7,0);
		DateTime primaData = ((Utente)p).primaDataDisponibile(gp,ris.getTipo(),periodo,inizioDisp,fineDisp);
		
		//ASSERT FIRST
		assertEquals("La prima data disponibile è ora",new DateTime(2018,12,25,9,0),primaData);
		
		DateTime inizioDisp2 = new DateTime(2018,12,25,7,0);
		DateTime fineDisp2 = new DateTime(2018,12,25,9,59);
		DateTime secondaData = ((Utente)p).primaDataDisponibile(gp,ris.getTipo(), periodo, inizioDisp2, fineDisp2);
		//TRIANGULATE
		assertEquals("La prima data disponibile è null", null, secondaData);
	}
	@Test
	public void testUtentePrimaDataDisponibileLimite() {
		Persona p = new Utente("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Risorsa ris = new Macchina("Toyota Yaris",5);
		Risorsa ris2 = new Macchina("Opel Corsa",6);
		Risorsa ris3 = new Aula("A5",45);
		Risorsa ris4 = new Portatile("Asus 845",4);
		gp.aggiungiRisorsa(ris4);
		gp.aggiungiRisorsa(ris3);
		gp.aggiungiRisorsa(ris);
		gp.aggiungiRisorsa(ris2);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		((Utente)p).addPrenotazione(gp,"pippo", inizio, fine, ris);
		((Utente)p).addPrenotazione(gp,"pluto", inizio, fine.minusHours(1), ris2);
		((Utente)p).addPrenotazione(gp,"paperino", inizio, fine, ris4);
		Period periodo = new Period(3,0,0,0);
		int numPosti = 4;
		DateTime inizioRicerca = new DateTime(2018,12,25,9,0);
		DateTime dataDispLimite = ((Utente)p).primaDataDisponibileLimite(gp,ris.getTipo(), periodo, inizioRicerca,numPosti);
		//la prima data che va bene è inizioRicerca con la seconda Risorsa;
		assertEquals("La prima data disponibile che rispetta il limite è",inizioRicerca,dataDispLimite);
		((Utente)p).addPrenotazione(gp,"ciao", new DateTime(2018,12,25,9,0), new DateTime(2018,12,25,10,59),ris2);
		//la prima data che va bene è (2018,12,25,10,0)
		DateTime dataDispLimite2 = ((Utente)p).primaDataDisponibileLimite(gp,ris.getTipo(), periodo, inizioRicerca, numPosti);
		assertEquals("La prima data disponibile che rispetta il limite è", new DateTime(2018,12,25,10,0),dataDispLimite2);
		//non ho una risorsa che rispetti il limite
		DateTime dataDispLimite3 = ((Utente)p).primaDataDisponibileLimite(gp,ris.getTipo(), periodo, inizioRicerca, 7);
		assertEquals("Non esiste una risorsa che rispetti il limite, data null", null,dataDispLimite3);
	}
	@Test
	public void testAggiungiRisorsaAmministratore() {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Risorsa risorsa = new Macchina("Toyota Yaris", 5);
		Persona p = new Amministratore("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		boolean esito = ((Amministratore)p).aggiungiRisorsa(gp,risorsa);
		assertEquals("La risorsa è stata aggiunta",true,esito);
		//la risorsa c'è già esito false
		boolean esito2 = ((Amministratore)p).aggiungiRisorsa(gp,risorsa);
		assertEquals("La risorsa non è stata aggiunta",false,esito2);
	}
	@Test
	public void testLeggiRisorsaAmministratore() {
		Persona p = new Utente("Davide", "Rossi", "das@gmail.com","pluto","ciao");
		Persona p2 = new Amministratore("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		Risorsa risorsa = new Portatile("Lenovo G230",4);
		String nomeR = risorsa.getNome();
		GestionePrenotazioni gp = new GestionePrenotazioni();
		((Amministratore)p2).aggiungiRisorsa(gp,risorsa);
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		((Utente)p).addPrenotazione(gp,"pippo", inizio, fine, risorsa);
		String riepilogo = ((Amministratore)p2).leggiRisorsa(gp,nomeR);
		String verifica = "Portatile Lenovo G230 ha le seguenti prenotazioni: "+"\n"+"\t"+
		"Prenotazione pippo effettuata da Davide Rossi pluto 2018-12-25T07:00:00.000+01:00/2018-12-25T09:59:00.000+01:00"+
				"\n";
		assertEquals("Informazioni sulla risorsa",verifica,riepilogo);
	}

}
