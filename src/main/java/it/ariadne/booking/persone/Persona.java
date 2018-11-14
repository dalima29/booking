package it.ariadne.booking.persone;

public abstract class Persona {

	private String nome;
	private String cognome;
	private String email;
	private String username;
	private String password;
	
	public Persona(String nome, String cognome, String email,String username,String password) {
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.username = username;
		this.password = password;
	}
	public String getNome() {
		return this.nome;
	}
	public String getCognome() {
		return this.cognome;
	}
	
	public String getEmail() {
		return this.email;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}

}
