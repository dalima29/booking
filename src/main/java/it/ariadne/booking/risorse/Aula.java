package it.ariadne.booking.risorse;

import it.ariadne.booking.Risorsa;

public class Aula implements Risorsa{
	
	private int capienza;//numero posti Aula
	private String nomeAula;
	
	public Aula(String nomeAula,int capienza) {
		this.nomeAula = nomeAula;
		this.capienza = capienza;
	}

	@Override
	public int getLimite() {
		return this.capienza;
	}
	
	public void setLimite(int limite) {
		this.capienza = limite;
	}

	@Override
	public String getTipo() {
		return "Aula";
	}
	
	public String getNome() {
		return nomeAula;
	}
}
