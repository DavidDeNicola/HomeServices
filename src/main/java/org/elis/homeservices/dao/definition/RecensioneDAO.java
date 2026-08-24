package org.elis.homeservices.dao.definition;

import java.util.List;

import org.elis.homeservices.exception.RecensioneNonTrovataException;
import org.elis.homeservices.model.Recensione;
import org.elis.homeservices.model.Utente;

public interface RecensioneDAO {
	boolean add(Recensione r);
	
	boolean remove(Recensione r) throws RecensioneNonTrovataException;
	
	List<Recensione> findByUtenteRiceve(Utente u);
	
	Long findIdUltimaRecensione();
	
	Recensione findById(Long id) throws RecensioneNonTrovataException;
	
	Long getIdUtenteScriveById(Long id) throws RecensioneNonTrovataException;
}
