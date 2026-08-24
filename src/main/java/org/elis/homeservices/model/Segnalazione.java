package org.elis.homeservices.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity(name = "segnalazione")
public class Segnalazione {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String motivazione;
	
	@Column(nullable = false)
	private Boolean sanzionato = false;
	
	@ManyToOne
	private Utente utenteSegnalato;
	
	@ManyToOne
	private Utente utenteSegnalante;
	
	
	public Segnalazione() {}
	
	public Segnalazione(String motivazione, Utente utenteSegnalato, Utente utenteSegnalante) {
		super();
		this.motivazione = motivazione;
		this.utenteSegnalato = utenteSegnalato;
		this.utenteSegnalante = utenteSegnalante;
	}
	
	
	public Boolean getSanzionato() {
		return sanzionato;
	}

	public void setSanzionato(Boolean sanzionato) {
		this.sanzionato = sanzionato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}

	public Utente getUtenteSegnalato() {
		return utenteSegnalato;
	}

	public void setUtenteSegnalato(Utente utenteSegnalato) {
		this.utenteSegnalato = utenteSegnalato;
	}

	public Utente getUtenteSegnalante() {
		return utenteSegnalante;
	}

	public void setUtenteSegnalante(Utente utenteSegnalante) {
		this.utenteSegnalante = utenteSegnalante;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Segnalazione other = (Segnalazione) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Segnalazione " +
				"\n\tid = " + id + "\n\tmotivazione = " + motivazione +
				"\n\tsanzionato = " + sanzionato +
				"\n\tutenteSegnalato = " + utenteSegnalato + 
				"\n\tutenteSegnalante = " + utenteSegnalante;
	}
	
}

