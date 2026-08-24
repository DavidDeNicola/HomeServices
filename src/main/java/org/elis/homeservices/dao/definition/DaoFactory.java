package org.elis.homeservices.dao.definition;

import org.elis.homeservices.dao.jdbc.JdbcDaoFactory;
import org.elis.homeservices.dao.jpa.JpaDaoFactory;

public abstract class DaoFactory {
	
	protected DaoFactory() {	
	}
	
	private static final DaoFactory INSTANCE;
	
	static {
		String FACTORY_IMPLEMENTATION = System.getenv("FACTORY_IMPLEMENTATION");

		if (FACTORY_IMPLEMENTATION == null) {
			FACTORY_IMPLEMENTATION = "JPA";
		}

		INSTANCE = switch(FACTORY_IMPLEMENTATION) {
			case "JDBC" -> new JdbcDaoFactory();
			case "JPA" -> new JpaDaoFactory();
			default -> throw new IllegalStateException("Factory non valida: " + FACTORY_IMPLEMENTATION);
		};
	}
	
	public static DaoFactory getInstance() {
		return INSTANCE;
	}
	
	public abstract CittaDAO getCittaDAO();
	
	public abstract DisponibilitaDAO getDisponibilitaDAO();
	
	public abstract ImmagineDAO getImmagineDAO();
	
	public abstract RecensioneDAO getRecensioneDAO();
	
	public abstract SegnalazioneDAO getSegnalazioneDAO();
	
	public abstract VeicoloDAO getVeicoloDAO();
	
	public abstract UtenteDAO getUtenteDAO();

	public abstract RichiestaDAO getRichiestaDAO();

	public abstract ProfessioneDAO getProfessioneDAO();

	public abstract FormProDAO getFormProDAO();
}
