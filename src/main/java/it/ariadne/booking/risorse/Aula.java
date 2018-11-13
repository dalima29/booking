package it.ariadne.booking.risorse;

import it.ariadne.booking.Risorsa;

public class Aula implements Risorsa{
	
	private int capienza;
	private String tipo;
	
	public Aula(int capienza) {
		this.capienza = capienza;
		this.tipo = "Aula";
	}

	@Override
	public int getLimite() {
		return this.capienza;
	}

	@Override
	public String getTipo() {
		return this.tipo;
	}
}
