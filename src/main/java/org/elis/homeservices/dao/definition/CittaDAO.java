package org.elis.homeservices.dao.definition;

import java.util.List;

import org.elis.homeservices.exception.CittaGiaNelDatabaseException;
import org.elis.homeservices.exception.CittaNonTrovataException;
import org.elis.homeservices.model.Citta;

public interface CittaDAO {
	Citta findCityByName(String name) throws CittaNonTrovataException;
	
	Long findIdByName(String name) throws CittaNonTrovataException;
	
	boolean addCity(String name) throws CittaGiaNelDatabaseException;
	
	boolean removeCityById(Long id) throws CittaNonTrovataException;
	
	boolean editCityNameById(Long id,String name) throws CittaNonTrovataException, CittaGiaNelDatabaseException;
	
	List<Citta> getListCitta();
	
	Citta getCityById(Long id) throws CittaNonTrovataException;
	
}
