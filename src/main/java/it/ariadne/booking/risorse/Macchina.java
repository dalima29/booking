package it.ariadne.booking.risorse;

import it.ariadne.booking.Risorsa;

public class Macchina implements Risorsa {
	
	private int numeroPosti;
	private String nomeMacchina;
	
	public Macchina (String nomeMacchina, int numeroPosti) {
		this.numeroPosti = numeroPosti;
		this.nomeMacchina = nomeMacchina;
	}
	
	public int getLimite () {
		return this.numeroPosti;
	}
	
	public String getTipo () {
		return "Macchina";
	}
	
	public String getNome() {
		return nomeMacchina;
	}

}
