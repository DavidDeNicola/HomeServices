package org.elis.homeservices.dao.definition;

import java.util.List;

import org.elis.homeservices.exception.DisponibilitaNonTrovataException;
import org.elis.homeservices.exception.DisponibilitaTimeOverlapException;
import org.elis.homeservices.model.Disponibilita;

public interface DisponibilitaDAO {
	boolean add(Disponibilita d) throws DisponibilitaTimeOverlapException;
	
	boolean remove(Disponibilita d) throws DisponibilitaNonTrovataException;
	
	List<Disponibilita> findByIdPro(Long id);
}
