package org.elis.homeservices.model.enums;

public enum Stato {
	
	IN_ATTESA("In attesa"),
	IN_CORSO("In corso"),
	COMPLETATO("Completato");
	
	private String nome;
	
	private Stato(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
