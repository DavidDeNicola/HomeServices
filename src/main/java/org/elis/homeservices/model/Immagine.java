package org.elis.homeservices.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.elis.homeservices.model.enums.TipoImmagine;

@Entity(name = "immagine")
public class Immagine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String percorso;

    @Column(nullable = false)
    private Boolean isFotoProfilo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoImmagine tipo;

    @ManyToOne
    @JoinColumn(name = "id_veicolo", nullable = true)
    private Veicolo veicolo;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Utente utente;


    public Immagine() {}

    // Costruttore per PROFILO o LAVORO (senza veicolo)
    public Immagine(String nome, String percorso, Boolean isFotoProfilo,
                    TipoImmagine tipo, Utente utente) {
        this.nome         = nome;
        this.percorso     = percorso;
        this.isFotoProfilo = isFotoProfilo;
        this.tipo         = tipo;
        this.utente       = utente;
    }

    // Costruttore per VEICOLO (con riferimento al veicolo)
    public Immagine(String nome, String percorso, Boolean isFotoProfilo,
                    TipoImmagine tipo, Veicolo veicolo, Utente utente) {
        this.nome         = nome;
        this.percorso     = percorso;
        this.isFotoProfilo = isFotoProfilo;
        this.tipo         = tipo;
        this.veicolo      = veicolo;
        this.utente       = utente;
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

    public String getPercorso() { 
    	return percorso; 
    }
    
    public void setPercorso(String p) { 
    	this.percorso = p; 
    }

    public Boolean getIsFotoProfilo() { 
    	return isFotoProfilo; 
    }
    
    public void setIsFotoProfilo(Boolean v) { 
    	this.isFotoProfilo = v; 
    }

    public TipoImmagine getTipo() { 
    	return tipo; 
    }
    
    public void setTipo(TipoImmagine tipo) { 
    	this.tipo = tipo; 
    }

    public Veicolo getVeicolo() { 
    	return veicolo; 
    }
    
    public void setVeicolo(Veicolo veicolo) { 
    	this.veicolo = veicolo; 
    }

    public Utente getUtente() { 
    	return utente; 
    }
    
    public void setUtente(Utente utente) { 
    	this.utente = utente; 
    }


    @Override
    public String toString() {
        return "Immagine\n\tid=" + id + "\n\tnome=" + nome + "\n\tpercorso=" + percorso
                + "\n\tisFotoProfilo=" + isFotoProfilo + "\n\ttipo=" + tipo
                + "\n\tveicolo=" + (veicolo != null ? veicolo.getId() : "null")
                + "\n\tutente=" + utente;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, utente, isFotoProfilo, nome, percorso, tipo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)  return true;
        if (obj == null)  return false;
        if (getClass() != obj.getClass()) return false;
        Immagine other = (Immagine) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(utente, other.utente)
                && Objects.equals(isFotoProfilo, other.isFotoProfilo)
                && Objects.equals(nome, other.nome)
                && Objects.equals(percorso, other.percorso)
                && Objects.equals(tipo, other.tipo);
    }
}

