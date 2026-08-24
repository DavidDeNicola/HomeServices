package org.elis.homeservices.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity(name = "professione")
public class Professione {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	@OneToMany(mappedBy = "professione")
	private List<FormPro> forms;
	
	@OneToMany(mappedBy = "professione")
	private List<Richiesta> richieste;
	
	@ManyToMany(mappedBy = "professioni")
	private List<Utente> professionisti;
	

	public Professione() {}
	
	public Professione(String nome) {
		this.nome = nome;
	}
	
	
	public List<Utente> getProfessionisti() {
		return professionisti;
	}

	public void setProfessionisti(List<Utente> professionisti) {
		this.professionisti = professionisti;
	}
	
	
	public List<Richiesta> getRichieste() {
		return richieste;
	}

	public void setRichieste(List<Richiesta> richieste) {
		this.richieste = richieste;
	}

	public List<FormPro> getForms() {
		return forms;
	}

	public void setForms(List<FormPro> forms) {
		this.forms = forms;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public String toString() {
		return "Professione\n\tid=" + id + "\n\tnome=" + nome + "\n\tforms=" + forms + "\n\trichieste=" + richieste+"\n\tprofessionisti="+professionisti;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, nome);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Professione other = (Professione) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
	}

	

}
