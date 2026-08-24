package org.elis.homeservices.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.elis.homeservices.dao.definition.RichiestaDAO;
import org.elis.homeservices.exception.RichiestaNonTrovataException;
//import org.elis.homeservices.exception.RichiestaStessoUtenteException;
import org.elis.homeservices.exception.RichiestaTimeOverlapException;
import org.elis.homeservices.exception.StatoNonAggiornabileException;
import org.elis.homeservices.model.Richiesta;
import org.elis.homeservices.model.Utente;
//import org.elis.homeservices.model.enums.Stato;

public class JdbcRichiestaDAO implements RichiestaDAO {
	
	private DataSource dataSource;

	public JdbcRichiestaDAO(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	@Override
	public boolean add(Richiesta r) throws RichiestaTimeOverlapException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM richiesta");
			ResultSet result = select.executeQuery();
			while(result.next()) {
//				if (((Long) result.getLong("id_utente_riceve")).equals(r.getIdUtenteRiceve()) && result.getDate("data").toLocalDate().equals(r.getData())) {
//				    if (result.getTime("da").toLocalTime().isBefore(r.getA()) && result.getTime("a").toLocalTime().isAfter(r.getDa())) {
//				        throw new RichiestaTimeOverlapException("la richiesta si sovrappone temporalmente a un'altra esistente nel database");
//				    }
//				}
			}

			PreparedStatement insert = connection.prepareStatement("INSERT INTO richiesta(descrizione, data, da, a, indirizzo, stato, id_utente_richiede, id_utente_riceve, id_professione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			insert.setString(1, r.getDescrizione());
			insert.setDate(2, java.sql.Date.valueOf(r.getData()));
			insert.setTime(3, java.sql.Time.valueOf(r.getDa()));
			insert.setTime(4, java.sql.Time.valueOf(r.getA()));
			insert.setString(5, r.getIndirizzo());
			insert.setInt(6, r.getStato().ordinal());
//			insert.setLong(7, r.getIdUtenteRichiede());
//			insert.setLong(8, r.getIdUtenteRiceve());
//			insert.setLong(9, r.getIdProfessione());
//			insert.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean remove(Long id) throws RichiestaNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM richiesta WHERE id = ?");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();
			
			if(result.next()) {
				PreparedStatement delete = connection.prepareStatement("DELETE FROM richiesta WHERE id = ?");
				delete.setLong(1, id);
				delete.executeUpdate();
				return true;
			}
		
			throw new RichiestaNonTrovataException("stai cercando di rimuovere una richiesta che non è presente nel database");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public List<Richiesta> findByUtenteRiceve(Utente u) {
//		try (Connection connection = dataSource.getConnection()) {  
//			PreparedStatement select = connection.prepareStatement("SELECT * FROM richiesta WHERE id_utente_riceve = ?");
//			select.setLong(1, u.getId());
//			ResultSet result = select.executeQuery();
//			
//			List<Richiesta> richiesteUtente = new ArrayList<>();
//			while(result.next()) {
//				Richiesta nuovaRichiesta = new Richiesta(
//						result.getString("descrizione"), 
//						result.getDate("data").toLocalDate(), 
//						result.getTime("da").toLocalTime(),
//						result.getTime("a").toLocalTime(),
//						result.getString("indirizzo"),
//						(Long) result.getLong("id_utente_richiede"),
//						(Long) result.getLong("id_utente_riceve"),
//						(Long) result.getLong("id_professione"));
//				nuovaRichiesta.setId(result.getLong("id"));
//				nuovaRichiesta.setIdRecensione(result.getLong("id_recensione"));
//				nuovaRichiesta.setStato(Stato.values()[result.getInt("stato")]);
//			
//				richiesteUtente.add(nuovaRichiesta);
//			}
//			return richiesteUtente;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (RichiestaStessoUtenteException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public List<Richiesta> findByUtenteRichiede(Utente u) {
//		try (Connection connection = dataSource.getConnection()) {  
//			PreparedStatement select = connection.prepareStatement("SELECT * FROM richiesta WHERE id_utente_richiede = ?");
//			select.setLong(1, u.getId());
//			ResultSet result = select.executeQuery();
//			
//			List<Richiesta> richiesteUtente = new ArrayList<>();
//			while(result.next()) {
//				Richiesta nuovaRichiesta = new Richiesta(
//						result.getString("descrizione"), 
//						result.getDate("data").toLocalDate(), 
//						result.getTime("da").toLocalTime(),
//						result.getTime("a").toLocalTime(),
//						result.getString("indirizzo"),
//						(Long) result.getLong("id_utente_richiede"),
//						(Long) result.getLong("id_utente_riceve"),
//						(Long) result.getLong("id_professione"));
//				nuovaRichiesta.setId(result.getLong("id"));
//				nuovaRichiesta.setStato(Stato.values()[result.getInt("stato")]);
//			
//				richiesteUtente.add(nuovaRichiesta);
//			}
//			return richiesteUtente;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (RichiestaStessoUtenteException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public void setIdRecensione(Long idRecensione, Long idRichiesta) throws RichiestaNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT id FROM richiesta");
			ResultSet result = select.executeQuery();
			while(result.next()) {
				if(((Long)result.getLong("id")).equals(idRichiesta)) {
					PreparedStatement update = connection.prepareStatement("UPDATE richiesta SET id_recensione = ? WHERE id = ?");
					update.setLong(1, idRecensione);
					update.setLong(2,  idRichiesta);
					update.executeUpdate();
					return;
				}
			}
			
			throw new RichiestaNonTrovataException("non è presente una richiesta con quell'id nel database");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Long getIdRecensioneById(Long idRichiesta) throws RichiestaNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT id, id_recensione FROM richiesta WHERE id = ?");
			select.setLong(1, idRichiesta);
			ResultSet result = select.executeQuery();
			
			if(result.next()) {
				if(result.getLong("id_recensione") != 0) {
					return (Long) result.getLong("id_recensione");
				}
				
				return null;
			}
			
			throw new RichiestaNonTrovataException("non è presente una richiesta con quell'id nel database");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	@Override
	public Richiesta getById(Long id) throws RichiestaNonTrovataException {
//		try (Connection connection = dataSource.getConnection()) {
//			PreparedStatement select = connection.prepareStatement("SELECT * FROM richiesta WHERE id = ?");
//			select.setLong(1, id);
//			ResultSet result = select.executeQuery();
//			
//			if(result.next()) {
//				Richiesta richiestaTrovata = new Richiesta(
//						result.getString("descrizione"), 
//						result.getDate("data").toLocalDate(), 
//						result.getTime("da").toLocalTime(),
//						result.getTime("a").toLocalTime(),
//						result.getString("indirizzo"),
//						(Long) result.getLong("id_utente_richiede"),
//						(Long) result.getLong("id_utente_riceve"),
//						(Long) result.getLong("id_professione"));
//				richiestaTrovata.setId(result.getLong("id"));
//				richiestaTrovata.setStato(Stato.values()[result.getInt("stato")]);
//				return richiestaTrovata;
//			}
//			
//			throw new RichiestaNonTrovataException("non è presente una richiesta con quell'id nel database");
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (RichiestaStessoUtenteException e) {
//			e.printStackTrace();
//		}
		
		return null;
	}

	@Override
	public void updateStato(Long id) throws RichiestaNonTrovataException, StatoNonAggiornabileException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT * FROM richiesta WHERE id = ?");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();
			
			if(result.next()) {
				PreparedStatement update = connection.prepareStatement("UPDATE richiesta SET stato = ? WHERE id = ?");
				if(result.getInt("stato") == 0 || result.getInt("stato") == 1) {
					update.setInt(1, result.getInt("stato") + 1 );
					update.setLong(2, id);
					update.executeUpdate();
					return;
				}
				throw new StatoNonAggiornabileException ("Lo stato della richiesta e' già 'completato'.");
			}
			
			throw new RichiestaNonTrovataException("non è presente una richiesta con quell'id nel database");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
