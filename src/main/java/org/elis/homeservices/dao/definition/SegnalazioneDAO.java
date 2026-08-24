package org.elis.homeservices.dao.definition;

import java.util.List;

import org.elis.homeservices.exception.SegnalazioneNonTrovataException;
import org.elis.homeservices.model.Segnalazione;

public interface SegnalazioneDAO {
	boolean add(Segnalazione s);
	
	boolean remove(Segnalazione s) throws SegnalazioneNonTrovataException;
	
	boolean removeById(Long id) throws SegnalazioneNonTrovataException;
	
	List<Segnalazione> findByIdSegnalato(Long id);
	
	List<Segnalazione> getListAllSegnalazioni();

	List<Segnalazione> getListSegnalazioniSanzionate();
	
	void setSanzionatoTrue(Long id) throws SegnalazioneNonTrovataException;
}
