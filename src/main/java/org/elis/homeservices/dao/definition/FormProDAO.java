package org.elis.homeservices.dao.definition;

import java.util.List; 

import org.elis.homeservices.exception.FormProNonTrovatoException;
import org.elis.homeservices.exception.UtenteGiaPresenteException;
import org.elis.homeservices.model.FormPro;

public interface FormProDAO {
	boolean add(FormPro fp) throws UtenteGiaPresenteException;
	
	boolean remove(Long id) throws FormProNonTrovatoException;
	
	List<FormPro> getForms();
	
	FormPro getById(Long id) throws FormProNonTrovatoException;
}
