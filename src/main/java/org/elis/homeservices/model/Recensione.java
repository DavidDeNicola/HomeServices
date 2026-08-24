package org.elis.homeservices.model;

import java.time.LocalDate;
import java.util.Objects;

import org.elis.homeservices.exception.RecensioneStessoUtenteException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity(name = "recensione")
public class Recensione {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Byte voto;
	
	private String descrizione;
	
	@Column(nullable = false)
	private LocalDate data;
	
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean segnalato = false;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Utente utenteScrive;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Utente utenteRiceve;
	
	@OneToOne(mappedBy = "recensione")
	private Richiesta richiesta;
	
	
	public Recensione() {}
	
	public Recensione(Byte voto, String descrizione, LocalDate data, Utente utenteScrive, Utente utenteRiceve) throws RecensioneStessoUtenteException {
		if(utenteScrive.equals(utenteRiceve)) {
			throw new RecensioneStessoUtenteException("Impossibile creare recensione tra due utenti uguali");
		}
		else {
			this.voto = voto;
			this.descrizione = descrizione;
			this.data = data;
			this.utenteScrive = utenteScrive;
			this.utenteRiceve = utenteRiceve;
		}
	}

	
	
	
	
	public Richiesta getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(Richiesta richiesta) {
		this.richiesta = richiesta;
	}

	public Utente getUtenteScrive() {
		return utenteScrive;
	}

	public void setUtenteScrive(Utente utenteScrive) {
		this.utenteScrive = utenteScrive;
	}

	public Utente getUtenteRiceve() {
		return utenteRiceve;
	}

	public void setUtenteRiceve(Utente utenteRiceve) {
		this.utenteRiceve = utenteRiceve;
	}

	public Boolean getSegnalato() {
		return segnalato;
	}

	public void setSegnalato(Boolean segnalato) {
		this.segnalato = segnalato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getVoto() {
		return voto;
	}

	public void setVoto(Byte voto) {
		this.voto = voto;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return "Recensione [id = " + id + ", voto = " + voto + ", descrizione = " + descrizione + ", data = " + data
				+ ", segnalato = " + segnalato + ", utenteScrive = " + utenteScrive + ", utenteRiceve = "
				+ utenteRiceve + "\n\trichiesta = " + richiesta + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(data, descrizione, id, utenteRiceve, utenteScrive, voto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recensione other = (Recensione) obj;
		return Objects.equals(id, other.id) ;
	}
	
}
