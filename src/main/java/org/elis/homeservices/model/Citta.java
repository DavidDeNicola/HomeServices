package org.elis.homeservices.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "citta")
public class Citta {
	
	@Column(unique = true, nullable = false) 
	private String nome;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@OneToMany(mappedBy = "citta")
	private List<Utente> residenti;
	
	@OneToMany(mappedBy = "citta")
	private List<FormPro> forms;
	
	
	public Citta() {}
	
	
	public Citta(String nome) {
		this.nome = nome;
	}
	
	
	
	

	public List<FormPro> getForms() {
		return forms;
	}

	public void setForms(List<FormPro> forms) {
		this.forms = forms;
	}

	public List<Utente> getResidenti() {
		return residenti;
	}

	public void setResidenti(List<Utente> residenti) {
		this.residenti = residenti;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Citta \n\tid = " + id + "\n\tnome = " + nome + "\n\tresidenti = " + residenti;
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
		Citta other = (Citta) obj;
		return Objects.equals(id, other.id);
	}

}
