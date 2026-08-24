package org.elis.homeservices.dao.jdbc;

import javax.sql.DataSource;

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
import org.elis.homeservices.utility.DataSourceConfig;

public class JdbcDaoFactory extends DaoFactory {
	
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
	// gli altri DAO
	
	public JdbcDaoFactory() {
		DataSource dataSource = DataSourceConfig.getDataSource();
		
		this.utenteDao = new JdbcUtenteDAO(dataSource);
		this.richiestaDao = new JdbcRichiestaDAO(dataSource);
		this.professioneDao = new JdbcProfessioneDAO(dataSource);
		this.recensioneDao = new JdbcRecensioneDAO(dataSource);
		this.formProDao = new JdbcFormProDAO(dataSource);
		this.cittaDao = new JdbcCittaDAO(dataSource);
		this.disponibilitaDao = new JdbcDisponibilitaDAO(dataSource);
		this.immagineDao = new JdbcImmagineDAO(dataSource);
		this.segnalazioneDao = new JdbcSegnalazioneDAO(dataSource);
		this.veicoloDao = new JdbcVeicoloDAO(dataSource);
		// gli altri DAO
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
	public RecensioneDAO getRecensioneDAO() {
		return recensioneDao;
	}

	@Override
	public FormProDAO getFormProDAO() {
		return formProDao;
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
	public SegnalazioneDAO getSegnalazioneDAO() {
		return segnalazioneDao;
	}

	@Override
	public VeicoloDAO getVeicoloDAO() {
		return veicoloDao;
	}

}
