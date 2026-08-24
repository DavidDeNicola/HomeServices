package org.elis.homeservices.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "disponibilita")
public class Disponibilita {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private LocalDate data;
	
	@Column(nullable = false)
	private LocalTime da;
	
	@Column(nullable = false)
	private LocalTime a;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Utente utente;
	
	
	
	public Disponibilita() {}
	
	
	public Disponibilita(LocalDate data, LocalTime da, LocalTime a, Utente utente) {
		this.data = data;
		this.da = da;
		this.a = a;
		this.utente = utente;
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	
	public Utente getUtente() {
		return utente;
	}


	public void setUtente(Utente utente) {
		this.utente = utente;
	}


	@Override
	public String toString() {
	    return "Disponibilita [id=" + id + ", data=" + data + ", utente_id=" + (utente != null ? utente.getId() : "null") + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, da, data, id, utente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Disponibilita other = (Disponibilita) obj;
		return Objects.equals(a, other.a) && Objects.equals(da, other.da) && Objects.equals(data, other.data)
				&& Objects.equals(id, other.id) && Objects.equals(utente, other.utente);
	}
	
	
	
	

}
