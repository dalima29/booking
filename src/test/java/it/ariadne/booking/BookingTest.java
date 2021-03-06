package it.ariadne.booking;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
	/**
	 * Il test verifica i metodi per ottenere email e password di un utente
	 */
	@Test
	public void prendiEmailEpassword () {
		Persona p = new Utente("Davide", "Limardi", "@gmail.com", "pippo", "123456");
		String email = ((Utente)p).getEmail();
		assertEquals("email è","@gmail.com",email);
		String password = ((Utente)p).getPassword();
		assertEquals("password è","123456",password);
	}
	
	/**
	 * Il test verifica il metodo per rimuovere una prenotazione, in questo caso si testa
	 * l'esito false, ovvero la prenotazione non esiste
	 */
	@Test
	public void rimuoviPrenotazione () {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Persona p = new Utente("Davide","Limardi" , "@gmail.com","pippo", "123456");
		Risorsa ris = new Macchina("Toyota Yaris",5);
		boolean esito = ((Utente)p).removePrenotazione(gp, "pluto", ris.getNome());
		assertEquals("La prenotazione non è stata rimossa, esito false", false,esito);
	}
	
	/**
	 * Il test verifica il metodo per ottenere il limite di una risorsa
	 */
	@Test
	public void ottieniLimite () {
		Risorsa risorsa = new Aula("A5", 5);
		int limite = risorsa.getLimite();
		assertEquals("il limite è",5,limite);
		Risorsa portatile = new Portatile("Lenovo", 4);
		int limite2 = portatile.getLimite();
		assertEquals("il limite è",4,limite2);
	}
	
	/**
	 * Il test verifica il metodo per cambiare il limite di una risorsa
	 */
	@Test
	public void cambiaLimite () {
		Risorsa macchina = new Macchina("Opel",5);
		macchina.setLimite(4);
		assertEquals("il limite è",4,macchina.getLimite());
		Risorsa portatile = new Portatile("Lenovo", 4);
		portatile.setLimite(8);
		assertEquals("il limte è",8,portatile.getLimite());
	}
	
	/*
	 * Il test verifica il metodo per cercare la prima data
	 * in cui una risorsa è disponibile
	 */
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
		DateTime dataDisp = gp.primaData(ris.getTipo(),ris.getNome(), periodo, inizioRicerca);
		DateTime dataDaVer = new DateTime(2018,12,25,10,0);
		assertEquals("Prima data",dataDaVer,dataDisp);
	}
	
	/**
	 * Il test verifica il metodo primaDataDisponibile che ritorna la prima data
	 * in cui una risorsa è disponibile, il secondo test verifica invece il caso
	 * in cui non ci sia una data che vada bene
	 */
	@Test
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
	
	/**
	 * Il test verifica la disponibilità di una risorsa che rispetta un certo
	 * limite imposto dall'utente. Si sono testati i vari casi in cui ci può trovare
	 */
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
	
	/**
	 * Il test verifica il corretto funzionamento del metodo che ritorna
	 * le prenotazioni per risorsa
	 */
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
	/**
	 * Il test verifica il corretto funzionamento del metodo che ritorna le prenotazioni
	 * correnti di un Utente
	 */
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

	/**
	 * Il test verifica il corretto funzionamento del metodo che ritorna le prenotazioni
	 * per una determinata risorsa
	 */
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
	
	/**
	 * Il test verifica il metodo che permette di trovare la prima data disponibile
	 * per una risorsa, nel primo caso viene ritornata una data, nel secondo caso
	 * viene ritornato null poichè la risorsa non è stata aggiunta
	 */
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
		DateTime dataDisp = ((Utente)p).primaData(gp,ris.getTipo(),ris.getNome(), periodo, inizioRicerca);
		DateTime dataDaVer = new DateTime(2018,12,25,10,0);
		//DateTime primaDataDisp = new DateTime
		assertEquals("Prima data",dataDaVer,dataDisp);
		//risorsa che non è stata aggiunta;
		Risorsa ris2 = new Aula("A5",100);
		DateTime dataDisp2 = ((Utente)p).primaData(gp,ris2.getTipo(),ris2.getNome(), periodo, inizioRicerca);
		assertEquals("Nessuna data",null,dataDisp2);
		
	}
	
	/**
	 * Il test verifica la prima data disponibile per una certa risorsa. Nel primo
	 * caso la data esiste, nel secondo no poichè supera la data che l'utente
	 * ha inserito come fine ricerca
	 */
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
	
	/**
	 * Il test verifica la prima data disponibile per una risorsa che rispetti
	 * il limite imposto dall'utente. Nei primi due casi si testa la condizione in cui
	 * la risorsa esiste, nel secondo caso no
	 */
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
	
	/**
	 * Il test verifica il metodo che permette all'amministratore di aggiungere
	 * una risorsa, nel primo caso la risorsa viene aggiunta, nel secondo no
	 * perchè esiste già
	 */
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
	
	/**
	 * Il test verifica il metodo che torna, all'amministratore, le prenotazioni di una
	 * determinata risorsa
	 */
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
	
	/**
	 * Test che verifica il corretto funzionamento del metodo che ritorna una stringa
	 * con le prenotazioni per risorsa
	 */
	@Test
	public void testAmministratoreRiepilogoPrenotazioniPerRisorsa() {
		Persona p = new Utente("Davide", "Rossi", "das@gmail.com","pluto","ciao");
		Persona p2 = new Amministratore("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		GestionePrenotazioni gp = new GestionePrenotazioni();
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		Risorsa aula = new Aula("A5", 200);
		((Amministratore)p2).aggiungiRisorsa(gp,aula);
		((Utente)p).addPrenotazione(gp,"pippo", inizio, fine, aula);
		String riepilogo = ((Amministratore)p2).riepilogoPrisorsa(gp);
		String verifica = "Aula A5 ha le seguenti prenotazioni: "+"\n"+"\t"+
		"Prenotazione pippo effettuata da Davide Rossi pluto 2018-12-25T07:00:00.000+01:00/2018-12-25T09:59:00.000+01:00"+
				"\n";
		assertEquals("Il riepilogo prenotazioni aula a5 è",verifica,riepilogo);
	}
	
	/**
	 * Test che verifica il funzionamento del metodo che permette all'amministratore
	 * di eliminare una risorsa. Prima viene testato il caso in cui la risorsa viene
	 * eliminata positivamente, dopo vien testato il caso in cui la risorsa che si vuole
	 * eliminare non c'è
	 */
	@Test
	public void eliminaRisorsaAmministratore () {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Persona p2 = new Amministratore("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		Risorsa risorsa = new Macchina("Toyota Yaris",5);
		((Amministratore)p2).aggiungiRisorsa(gp,risorsa);
		boolean esito = ((Amministratore)p2).eliminaRisorsa(gp,risorsa.getNome());
		assertEquals("Risorsa eliminata",true,esito);
		boolean esito2 = ((Amministratore)p2).eliminaRisorsa(gp,risorsa.getNome());
		//esito false perchè la risorsa è già stata eliminata
		assertEquals("Risorsa eliminata",false,esito2);
	}
	
	/**
	 * Test che verifica il corretto funzionamento del metodo che ritorna, 
	 * all'amministratore, tutte le prenotazioni effettuate ordinate per persona
	 */
	@Test
	public void testAmministratoreRiepilogoPrenotazioniPerPersona() {
		Persona p = new Utente("Davide", "Rossi", "das@gmail.com","pluto","ciao");
		Persona p2 = new Amministratore("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		List<Persona> lista = new ArrayList<>();
		lista.add(p);
		GestionePrenotazioni gp = new GestionePrenotazioni();
		DateTime inizio = new DateTime(2018, 12, 25,7, 0);
		DateTime fine = new DateTime(2018, 12, 25, 9, 59);
		Risorsa aula = new Aula("A5", 200);
		((Amministratore)p2).aggiungiRisorsa(gp,aula);
		((Utente)p).addPrenotazione(gp,"ppp", inizio, fine, aula);
		String riepilogo = ((Amministratore)p2).riepilogoPpersona(gp,lista);
		String verifica = "Davide Rossi pluto"+"\n"+"\t"+
		"Aula A5 Prenotazione ppp "+"2018-12-25T07:00:00.000+01:00/2018-12-25T09:59:00.000+01:00"+
				"\n";
		assertEquals("Il riepilogo prenotazioni aula a5 è",verifica,riepilogo);
	}
	
	/**
	 * Il test verifica il corretto funzionamento del metodo che permette, 
	 * all'amministatore, di aggiornare il limite di una risorsa. Nel primo
	 * caso l'aggiornamento va a buon fine, nel secondo caso no perchè
	 * si sta tentando di aggiornare una risorsa che non c'è
	 */
	@Test
	public void aggiornaRisorsa() {
		GestionePrenotazioni gp = new GestionePrenotazioni();
		Persona p2 = new Amministratore("Davide", "Limardi", "das@gmail.com","pippo","ciao");
		int limiteDaModificare = 200;
		Risorsa aula = new Aula("A5", 100);
		((Amministratore)p2).aggiungiRisorsa(gp, aula);
		String nomeR = aula.getNome();
		boolean verifica = true;
		boolean esito = ((Amministratore)p2).aggiornaRisorsa(gp,nomeR,limiteDaModificare);
		assertEquals("risorsa aggiornata positivamente",verifica,esito);
		//risorsa non aggiunta
		Risorsa risorsa = new Macchina("Toyota Yaris",5);
		boolean esito2 = ((Amministratore)p2).aggiornaRisorsa(gp,risorsa.getNome(),limiteDaModificare);
		assertEquals("risorsa non aggiornata(esito false",false,esito2);
	}
}