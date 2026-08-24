package org.elis.homeservices.dao.definition;

import org.elis.homeservices.exception.ImmagineGiaNelDatabaseException;
import org.elis.homeservices.exception.ImmagineNonTrovataException;
import org.elis.homeservices.model.Immagine;

public interface ImmagineDAO {
	boolean add(Immagine i) throws ImmagineGiaNelDatabaseException;
	
	boolean remove(Immagine i) throws ImmagineNonTrovataException;
}
