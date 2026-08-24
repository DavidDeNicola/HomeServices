package org.elis.homeservices.dao.definition;

import org.elis.homeservices.exception.RichiestaTimeOverlapException;
import org.elis.homeservices.exception.StatoNonAggiornabileException;

import java.util.List;

import org.elis.homeservices.exception.RichiestaNonTrovataException;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.Richiesta;

public interface RichiestaDAO {
	boolean add(Richiesta r) throws RichiestaTimeOverlapException;
	
	boolean remove(Long id) throws RichiestaNonTrovataException;
	
	List<Richiesta> findByUtenteRiceve(Utente u);
	
	List<Richiesta> findByUtenteRichiede(Utente u);

	void setIdRecensione(Long idRecensione, Long idRichiesta) throws RichiestaNonTrovataException;

	Long getIdRecensioneById(Long idRichiesta) throws RichiestaNonTrovataException;
	
	Richiesta getById(Long id) throws RichiestaNonTrovataException;
	
	void updateStato(Long id) throws RichiestaNonTrovataException, StatoNonAggiornabileException; 
	
	
}
