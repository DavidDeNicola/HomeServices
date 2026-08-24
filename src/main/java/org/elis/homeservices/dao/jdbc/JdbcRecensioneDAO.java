package org.elis.homeservices.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.elis.homeservices.dao.definition.RecensioneDAO;
import org.elis.homeservices.exception.RecensioneNonTrovataException;
//import org.elis.homeservices.exception.RecensioneStessoUtenteException;
import org.elis.homeservices.model.Recensione;
import org.elis.homeservices.model.Utente;

public class JdbcRecensioneDAO implements RecensioneDAO {
	
	private DataSource dataSource;

	public JdbcRecensioneDAO(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	@Override
	public boolean add(Recensione r){
		try (Connection connection = dataSource.getConnection()) { 
//			PreparedStatement insert = connection.prepareStatement("INSERT INTO recensione(voto, descrizione, data, id_utente_scrive, id_utente_riceve) VALUES (?, ?, ?, ?, ?)");
//			insert.setInt(1, r.getVoto());
//			insert.setString(2, r.getDescrizione());
//			insert.setDate(3, java.sql.Date.valueOf(r.getData()));
//			insert.setLong(4, r.getIdUtenteScrive());
//			insert.setLong(5, r.getIdUtenteRiceve());
//			insert.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean remove(Recensione r) throws RecensioneNonTrovataException {
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement select = connection.prepareStatement("SELECT * FROM recensione WHERE id = ?");
			select.setLong(1, r.getId());
			ResultSet result = select.executeQuery();

			if(result.next()) {
				PreparedStatement delete = connection.prepareStatement("DELETE FROM recensione WHERE id = ?");
				delete.setLong(1, r.getId());
				delete.executeUpdate();
				return true;
			}
			
			throw new RecensioneNonTrovataException("una recensione con questo id non è stata trovata nel database");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}

	@Override
	public List<Recensione> findByUtenteRiceve(Utente u) {
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement select = connection.prepareStatement("SELECT * FROM recensione WHERE id_utente_riceve = ?");
			select.setLong(1, u.getId());
			ResultSet result = select.executeQuery();
			
			List<Recensione> recensioniUtente = new ArrayList<>();
			while(result.next()) {
//				try {
//					Recensione nuovaRecensione = new Recensione(result.getByte("voto"), result.getString("descrizione"), result.getDate("data").toLocalDate(), (Long) result.getLong("id_utente_scrive"), (Long) result.getLong("id_utente_riceve"));
//					recensioniUtente.add(nuovaRecensione);
//				} catch(RecensioneStessoUtenteException e) {
//					e.printStackTrace();
//				}
			}
			
			return recensioniUtente;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Long findIdUltimaRecensione() {
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement select = connection.prepareStatement("SELECT id FROM recensione ORDER BY id DESC LIMIT 1");
			ResultSet result = select.executeQuery();
			
			while(result.next()) {
				return result.getLong("id");
			}					
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Recensione findById(Long id) throws RecensioneNonTrovataException {
//		try (Connection connection = dataSource.getConnection()) { 
//			PreparedStatement select = connection.prepareStatement("SELECT * FROM recensione WHERE id = ?");
//			select.setLong(1, id);
//			ResultSet result = select.executeQuery();
//			
//			if(result.next()) {
//				return new Recensione(result.getByte("voto"), result.getString("descrizione"), result.getDate("data").toLocalDate(), (Long) result.getLong("id_utente_scrive"), (Long) result.getLong("id_utente_riceve"));
//			}	
//			
//			throw new RecensioneNonTrovataException("l'id non corrisponde a nessuna recensione nel database");
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (RecensioneStessoUtenteException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public Long getIdUtenteScriveById(Long id) throws RecensioneNonTrovataException {
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement select = connection.prepareStatement("SELECT id_utente_scrive FROM recensione WHERE id = ?");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();
			
			if(result.next()) {
				Long idUtenteScrive = result.getLong("id_utente_scrive");
				return idUtenteScrive;
			}	
			
			throw new RecensioneNonTrovataException("l'id non corrisponde a nessuna recensione nel database");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RecensioneNonTrovataException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}
