package org.elis.homeservices.dao.definition;

import org.elis.homeservices.model.Professione;

import java.util.List;

import org.elis.homeservices.exception.ProfessioneGiaNelDatabaseException;
import org.elis.homeservices.exception.ProfessioneNonTrovataException;


public interface ProfessioneDAO {
	boolean add(Professione p) throws ProfessioneGiaNelDatabaseException;
	
	boolean remove(Professione p) throws ProfessioneNonTrovataException;
	
	Professione findById(Long id) throws ProfessioneNonTrovataException;
	
	List<Professione> getListProfessioni();
}
