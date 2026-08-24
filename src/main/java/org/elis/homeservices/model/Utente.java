package org.elis.homeservices.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.elis.homeservices.model.enums.Ruolo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity(name = "utente")
public class Utente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String cognome;
	
	@Column(unique = true, nullable = false) 
	private String email;
	
    @Column(nullable = false)
    private String password;
	
	@Column(nullable = false)
	private LocalDate dataNascita;
	
	@Column(unique = true) 
	private String cf;
	
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Ruolo ruolo = Ruolo.USER;
	
    @Column(precision = 10, scale = 2)
    private BigDecimal tariffa;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Citta citta;
	
	@OneToMany(mappedBy = "utente", fetch = FetchType.EAGER)
	private List<Disponibilita> disponibilita;
	
	@OneToMany(mappedBy = "utente", fetch = FetchType.EAGER)
	private List<Immagine> immagini;
	
	@OneToMany(mappedBy = "utenteScrive", fetch = FetchType.EAGER)
	private List<Recensione> recensioniScritte;
	
	@OneToMany(mappedBy = "utenteRiceve", fetch = FetchType.EAGER)
	private List<Recensione> recensioniRicevute;
	
	@OneToMany(mappedBy = "utenteRichiede", fetch = FetchType.EAGER)
	private List<Richiesta> richiesteInviate;
	
	@OneToMany(mappedBy = "utenteRiceve", fetch = FetchType.EAGER)
	private List<Richiesta> richiesteRicevute;
	
	@OneToMany(mappedBy = "utenteSegnalato", fetch = FetchType.EAGER)
	private List<Segnalazione> segnalazioniRicevute;
	
	@OneToMany(mappedBy = "utenteSegnalante", fetch = FetchType.EAGER)
	private List<Segnalazione> segnalazioniInviate;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "utente_veicolo",
	    joinColumns = @JoinColumn(name = "id_utente"),
	    inverseJoinColumns = @JoinColumn(name = "id_veicolo")
	)
	private List<Veicolo> veicoli;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "utente_professione",
	    joinColumns = @JoinColumn(name = "id_utente"),
	    inverseJoinColumns = @JoinColumn(name = "id_professione")
	)
	private List<Professione> professioni;
	
	@Column(nullable = false)
	private Double rating = 0.0;
	
	@OneToOne
	@JoinColumn(name="id_form_pro")
	private FormPro formPro;
	
	
	public Utente() {}
	
	public List<Professione> getProfessioni() {
		return professioni;
	}

	public void setProfessioni(List<Professione> professioni) {
		this.professioni = professioni;
	}

	public List<Veicolo> getVeicoli() {
		return veicoli;
	}

	public void setVeicoli(List<Veicolo> veicoli) {
		this.veicoli = veicoli;
	}

	public List<Segnalazione> getSegnalazioniRicevute() {
		return segnalazioniRicevute;
	}

	public void setSegnalazioniRicevute(List<Segnalazione> segnalazioniRicevute) {
		this.segnalazioniRicevute = segnalazioniRicevute;
	}

	public List<Segnalazione> getSegnalazioniInviate() {
		return segnalazioniInviate;
	}

	public void setSegnalazioniInviate(List<Segnalazione> segnalazioniInviate) {
		this.segnalazioniInviate = segnalazioniInviate;
	}

	public List<Richiesta> getRichiesteInviate() {
		return richiesteInviate;
	}

	public void setRichiesteInviate(List<Richiesta> richiesteInviate) {
		this.richiesteInviate = richiesteInviate;
	}

	public List<Richiesta> getRichiesteRicevute() {
		return richiesteRicevute;
	}

	public void setRichiesteRicevute(List<Richiesta> richiesteRicevute) {
		this.richiesteRicevute = richiesteRicevute;
	}

	public List<Recensione> getRecensioniScritte() {
		return recensioniScritte;
	}

	public void setRecensioniScritte(List<Recensione> recensioniScritte) {
		this.recensioniScritte = recensioniScritte;
	}

	public List<Recensione> getRecensioniRicevute() {
		return recensioniRicevute;
	}

	public void setRecensioniRicevute(List<Recensione> recensioniRicevute) {
		this.recensioniRicevute = recensioniRicevute;
	}

	public List<Immagine> getImmagini() {
		return immagini;
	}

	public void setImmagini(List<Immagine> immagini) {
		this.immagini = immagini;
	}

	public List<Disponibilita> getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(List<Disponibilita> disponibilita) {
		this.disponibilita = disponibilita;
	}

	public FormPro getFormPro() {
		return formPro;
	}

	public void setFormPro(FormPro formPro) {
		this.formPro = formPro;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
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

	@Override
	public String toString() {
		return "\nUtente \n\tid="+id+"\n\tnome="+nome+"\n\tcognome="+cognome+"\n\temail="+email+"\n\tpassword="+password+
				"\n\tdataNascita="+dataNascita+"\n\tcf="+cf+"\n\truolo="+ruolo+"\n\ttariffa="+tariffa+"\n\trating="+rating+
				"\n\tcitta="+citta+"\n\tdisponibilita="+disponibilita+"\n\tformPro="+formPro+"\n\timmagini="+immagini+
				"\n\trecensioniScritte="+recensioniScritte+"\n\trecensioniRicevute="+recensioniRicevute+
				"\n\tsegnalazioniRicevute="+segnalazioniRicevute+"\n\tsegnalazioniInviate="+segnalazioniInviate+
				"\n\tveicoli="+veicoli+"\n\tprofessioni="+professioni;
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
		Utente other = (Utente) obj;
		return Objects.equals(id, other.id);
	}
	
	
	

}
