package org.elis.homeservices.dao.definition;

import java.util.List;

import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.exception.VeicoloGiaNelDatabaseException;
import org.elis.homeservices.exception.VeicoloNonTrovatoException;
import org.elis.homeservices.model.Veicolo;

public interface VeicoloDAO {
	boolean add(Veicolo v) throws VeicoloGiaNelDatabaseException;
	
	boolean remove(Veicolo v) throws VeicoloNonTrovatoException;
	
	List<Veicolo> getListaVeicoli();
	
	List<Veicolo> getListaVeicoliUtente(Long idUtente) throws UtenteNonTrovatoException;
	
	Veicolo findById(Long id);
}
