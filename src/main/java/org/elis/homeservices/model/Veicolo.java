package org.elis.homeservices.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity(name = "veicolo")
public class Veicolo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String nome;

	@OneToMany(mappedBy = "veicolo")
	private List<Immagine> immagini;
	
	@ManyToMany(mappedBy = "veicoli")
	private List<Utente> utenti; 
	
	
	public Veicolo() {}
	
	public Veicolo(String nome) {
		this.nome = nome;
	}

	
	
	
	public List<Utente> getUtenti() {
		return utenti;
	}

	public void setUtenti(List<Utente> utenti) {
		this.utenti = utenti;
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
		return "Veicolo \n\tid=" + id + "\n\tnome=" + nome + "\n\tutenti=" + utenti;
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
		Veicolo other = (Veicolo) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
	}

}
