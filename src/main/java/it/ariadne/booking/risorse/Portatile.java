package it.ariadne.booking.risorse;

import it.ariadne.booking.Risorsa;

public class Portatile implements Risorsa {

	private int ram;
	private String tipo;
	
	public Portatile(int ram) {
		this.ram = ram;
		this.tipo = "Portatile";
	}

	public int getLimite() {
		return this.ram;
	}

	public String getTipo() {
		return this.tipo;
	}
}