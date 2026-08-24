package org.elis.homeservices.dao.jpa;

import java.util.HashMap;
import java.util.Map;

import org.elis.homeservices.dao.definition.CittaDAO;
import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.DisponibilitaDAO;
import org.elis.homeservices.dao.definition.FormProDAO;
import org.elis.homeservices.dao.definition.ImmagineDAO;
import org.elis.homeservices.dao.definition.ProfessioneDAO;
import org.elis.homeservices.dao.definition.RecensioneDAO;
import org.elis.homeservices.dao.definition.RichiestaDAO;
import org.elis.homeservices.dao.definition.SegnalazioneDAO;
import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.dao.definition.VeicoloDAO;
//import org.elis.homeservices.dao.jdbc.JdbcCittaDAO;
//import org.elis.homeservices.dao.jdbc.JdbcDisponibilitaDAO;
//import org.elis.homeservices.dao.jdbc.JdbcFormProDAO;
//import org.elis.homeservices.dao.jdbc.JdbcImmagineDAO;
//import org.elis.homeservices.dao.jdbc.JdbcProfessioneDAO;
//import org.elis.homeservices.dao.jdbc.JdbcRecensioneDAO;
//import org.elis.homeservices.dao.jdbc.JdbcRichiestaDAO;
//import org.elis.homeservices.dao.jdbc.JdbcSegnalazioneDAO;
//import org.elis.homeservices.dao.jdbc.JdbcUtenteDAO;
//import org.elis.homeservices.dao.jdbc.JdbcVeicoloDAO;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence; 

public class JpaDaoFactory extends DaoFactory{
	
	private UtenteDAO utenteDao;
	private RichiestaDAO richiestaDao;
	private ProfessioneDAO professioneDao;
	private RecensioneDAO recensioneDao;
	private FormProDAO formProDao;
	private CittaDAO cittaDao;
	private DisponibilitaDAO disponibilitaDao;
	private ImmagineDAO immagineDao;
	private SegnalazioneDAO segnalazioneDao;
	private VeicoloDAO veicoloDao;
	
	public JpaDaoFactory() {
		Map<String,String> properties = new HashMap<String, String>();
		properties.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
		properties.put("jakarta.persistence.jdbc.url", "jdbc:mysql://localhost:3306/home_services_db");
		properties.put("jakarta.persistence.jdbc.user", "root");
		if(System.getenv("DB_PASS") != null) {
			properties.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASS"));
		}
		else {
			properties.put("jakarta.persistence.jdbc.password", "root");
		}
		properties.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.format_sql", "true");
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("HomeServices", properties);
		
		this.utenteDao = new JpaUtenteDAO(emf);
		this.richiestaDao = new JpaRichiestaDAO(emf);
		this.professioneDao = new JpaProfessioneDAO(emf);
		this.recensioneDao = new JpaRecensioneDAO(emf);
		this.formProDao = new JpaFormProDAO(emf);
		this.cittaDao = new JpaCittaDAO(emf);
		this.disponibilitaDao = new JpaDisponibilitaDAO(emf);
		this.immagineDao = new JpaImmagineDAO(emf);
		this.segnalazioneDao = new JpaSegnalazioneDAO(emf);
		this.veicoloDao = new JpaVeicoloDAO(emf);
	}
	
	
	@Override
	public CittaDAO getCittaDAO() {
		return cittaDao;
	}

	@Override
	public DisponibilitaDAO getDisponibilitaDAO() {
		return disponibilitaDao;
	}

	@Override
	public ImmagineDAO getImmagineDAO() {
		return immagineDao;
	}

	@Override
	public RecensioneDAO getRecensioneDAO() {
		return recensioneDao;
	}

	@Override
	public SegnalazioneDAO getSegnalazioneDAO() {
		return segnalazioneDao;
	}

	@Override
	public VeicoloDAO getVeicoloDAO() {
		return veicoloDao;
	}

	@Override
	public UtenteDAO getUtenteDAO() {
		return utenteDao;
	}

	@Override
	public RichiestaDAO getRichiestaDAO() {
		return richiestaDao;
	}

	@Override
	public ProfessioneDAO getProfessioneDAO() {
		return professioneDao;
	}

	@Override
	public FormProDAO getFormProDAO() {
		return formProDao;
	}
	
}
