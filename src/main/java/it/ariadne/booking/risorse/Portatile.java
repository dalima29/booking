package it.ariadne.booking.risorse;

import it.ariadne.booking.Risorsa;

public class Portatile implements Risorsa {

	private int ram;
	private String nomePortatile;
	
	public Portatile(String nomePortatile,int ram) {
		this.ram = ram;
		this.nomePortatile = nomePortatile;
	}

	public int getLimite() {
		return this.ram;
	}
	
	public void setLimite(int limite) {
		this.ram = limite;
	}

	public String getTipo() {
		return "Portatile";
	}
	
	public String getNome() {
		return this.nomePortatile;
	}
}