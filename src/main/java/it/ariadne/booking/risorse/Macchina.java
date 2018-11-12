package it.ariadne.booking.risorse;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import it.ariadne.booking.Prenotazione;
import it.ariadne.booking.Risorsa;

public class Macchina implements Risorsa {
	
	private int numeroPosti;
	private String tipo;
	
	public Macchina (int numeroPosti) {
		this.numeroPosti = numeroPosti;
		this.tipo = "Macchina";
	}
	
	public int getLimite () {
		return this.numeroPosti;
	}
	
	public String getTipo () {
		return this.tipo;
	}

}
