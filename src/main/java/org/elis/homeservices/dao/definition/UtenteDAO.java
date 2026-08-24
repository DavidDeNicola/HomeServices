package org.elis.homeservices.dao.definition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.elis.homeservices.exception.ProfessioneGiaAssociataException;
import org.elis.homeservices.exception.ProfessioneNonAssociataException;
import org.elis.homeservices.exception.ProfessionistaNonTrovatoException;
import org.elis.homeservices.exception.UtenteGiaPresenteException;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.exception.VeicoloGiaAssociatoException;
import org.elis.homeservices.exception.VeicoloNonAssociatoException;
import org.elis.homeservices.model.Citta;
import org.elis.homeservices.model.Professione;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.Veicolo;

public interface UtenteDAO {
	Utente findById(Long id) throws UtenteNonTrovatoException;
	
	Utente findByEmailPass(String email, String pass) throws UtenteNonTrovatoException;
	Utente findByEmail(String email) throws UtenteNonTrovatoException;
	
	boolean add(Utente u) throws UtenteGiaPresenteException;
	
	boolean removeByEmail(String email) throws UtenteNonTrovatoException;
	
	List<Utente> findByCity(Citta c);
	List<Utente> findByCity(Citta c, List<Utente> utenti);
	
	List<Utente> findByMaxRate(BigDecimal maxTariffa);
	List<Utente> findByMaxRate(BigDecimal maxTariffa, List<Utente> utenti);
	boolean modifyRate(Long id,BigDecimal tariffa);
	
	List<Utente> findByMinRating(Double valutazione);
	List<Utente> findByMinRating(Double valutazione, List<Utente> utenti);

	List<Utente> findByPro(Professione p);
	List<Utente> findByPro(Professione p, List<Utente> utenti);
	
	List<Professione> showPro(Long id) throws UtenteNonTrovatoException;
	
	List<Utente> ricercaAvanzata(String nome, Long citta, Double valutazione, LocalDate data, LocalTime inizio, LocalTime fine , String ordine) throws ProfessionistaNonTrovatoException;
	
	boolean addPro(Utente u, Professione p) throws ProfessioneGiaAssociataException;
	
	
	boolean removePro(Utente u, Professione p) throws ProfessioneNonAssociataException;
	
	boolean addVehicle(Utente u, Veicolo v) throws VeicoloGiaAssociatoException;
	
	boolean removeVehicle(Utente u, Veicolo v) throws VeicoloNonAssociatoException;

	boolean toPro(Long id, String codiceFiscale) throws UtenteNonTrovatoException;

	List<Utente> findByNomeCognome(String query);
	
	List<Professione> getListaProfessioni(Long id) throws UtenteNonTrovatoException;
}
