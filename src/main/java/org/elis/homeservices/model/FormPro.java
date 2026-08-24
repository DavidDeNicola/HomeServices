package org.elis.homeservices.model;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity(name = "formpro")
public class FormPro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true) 
	private String cf;
	
	private BigDecimal tariffa;
	
	@ManyToOne
	private Citta citta;
	
	@ManyToOne
	private Professione professione;
	
	@OneToOne(mappedBy = "formPro")
	private Utente utente;
	
	
	public FormPro() {}
	
	public FormPro(String cf, BigDecimal tariffa, Citta citta, Professione professione, Utente utente) {
		this.cf = cf;
		this.tariffa = tariffa;
		this.citta = citta;
		this.professione = professione;
		this.utente = utente;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public BigDecimal getTariffa() {
		return tariffa;
	}

	public void setTariffa(BigDecimal tariffa) {
		this.tariffa = tariffa;
	}

	public Citta getCitta() {
		return citta;
	}

	public void setCitta(Citta citta) {
		this.citta = citta;
	}

	public Professione getProfessione() {
		return professione;
	}

	public void setProfessione(Professione professione) {
		this.professione = professione;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cf, citta, id, professione, tariffa, utente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormPro other = (FormPro) obj;
		return Objects.equals(cf, other.cf) && Objects.equals(citta, other.citta) && Objects.equals(id, other.id)
				&& Objects.equals(utente, other.utente);
	}

	@Override
	public String toString() {
		return "FormPro [id = " + id + ", cf = " + cf + ", tariffa = " + tariffa + ", citta = " + citta
				+ ", professione = " + professione + ", utente = " + utente + "]";
	}
		
}
