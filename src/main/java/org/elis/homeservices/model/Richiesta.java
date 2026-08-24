package org.elis.homeservices.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import org.elis.homeservices.exception.RichiestaStessoUtenteException;
import org.elis.homeservices.model.enums.Stato;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity(name = "richiesta")
public class Richiesta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String descrizione;
	
	@Column(nullable = false)
	private LocalDate data;
	
	@Column(nullable = false)
	private LocalTime da;
	
	@Column(nullable = false)
	private LocalTime a;
	
	@Column(nullable = false)
	private String indirizzo;
	
	@Column(nullable = false) 
	private Stato stato;
	
	@OneToOne
	@JoinColumn(name = "id_recensione")
	private Recensione recensione;
	
	@ManyToOne
	private Utente utenteRichiede;
	
	@ManyToOne
	private Utente utenteRiceve;
	
	@ManyToOne
	private Professione professione;
	
	
	public Richiesta() {}
	
	public Richiesta(String descrizione, LocalDate data, LocalTime da, LocalTime a, String indirizzo, Utente utenteRichiede, Utente utenteRiceve, Professione professione) throws RichiestaStessoUtenteException {
		if(utenteRichiede.equals(utenteRiceve)) {
			throw new RichiestaStessoUtenteException("Impossibile creare richiesta tra due utenti uguali");
		}
		
		this.descrizione = descrizione;
		this.data = data;
		this.da = da;
		this.a = a;
		this.indirizzo = indirizzo;
		this.stato = Stato.IN_ATTESA;
		this.utenteRichiede = utenteRichiede;
		this.utenteRiceve = utenteRiceve;
		this.professione = professione;
	}
	


	public Recensione getRecensione() {
		return recensione;
	}

	public void setRecensione(Recensione recensione) {
		this.recensione = recensione;
	}

	public Utente getUtenteRichiede() {
		return utenteRichiede;
	}

	public void setUtenteRichiede(Utente utenteRichiede) {
		this.utenteRichiede = utenteRichiede;
	}

	public Utente getUtenteRiceve() {
		return utenteRiceve;
	}

	public void setUtenteRiceve(Utente utenteRiceve) {
		this.utenteRiceve = utenteRiceve;
	}

	public Professione getProfessione() {
		return professione;
	}

	public void setProfessione(Professione professione) {
		this.professione = professione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalTime getDa() {
		return da;
	}

	public void setDa(LocalTime da) {
		this.da = da;
	}

	public LocalTime getA() {
		return a;
	}

	public void setA(LocalTime a) {
		this.a = a;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Stato getStato() {
		return stato;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}

	@Override
	public String toString() {
		return "Richiesta\n\tid=" + id + "\n\tdescrizione=" + descrizione + "\n\tdata=" + data + "\n\tda=" + da + "\n\ta=" + a 
				+ "\n\tindirizzo=" + indirizzo + "\n\tstato=" + stato 
				+ "\n\tutenteRiceve=" + utenteRiceve + "\n\tutenteRichiede=" + utenteRichiede + "\n\tprofessione=" + professione;
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
		Richiesta other = (Richiesta) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
	

}
