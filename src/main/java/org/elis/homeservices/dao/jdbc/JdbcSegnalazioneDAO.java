package org.elis.homeservices.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.elis.homeservices.dao.definition.SegnalazioneDAO;
import org.elis.homeservices.exception.SegnalazioneNonTrovataException;
import org.elis.homeservices.model.Segnalazione;

public class JdbcSegnalazioneDAO implements SegnalazioneDAO {

	private DataSource dataSource;

	public JdbcSegnalazioneDAO(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public boolean add(Segnalazione s) {
//		try (Connection connection = dataSource.getConnection()) {
//			PreparedStatement insert = connection.prepareStatement(
//					"INSERT INTO segnalazione(motivazione, id_utente_segnalato, id_utente_segnalante) VALUES (?, ?, ?)");
//			insert.setString(1, s.getMotivazione());
//			insert.setLong(2, s.getId_utente_segnalato());
//			insert.setLong(3, s.getId_utente_segnalante());
//			insert.executeUpdate();
//			return true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return false;
	}

	@Override
	public boolean remove(Segnalazione s) throws SegnalazioneNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT * FROM segnalazione WHERE id = ?");
			select.setLong(1, s.getId());
			ResultSet result = select.executeQuery();
			if (result.next()) {
				PreparedStatement delete = connection.prepareStatement("DELETE FROM segnalazione WHERE id = ?");
				delete.setLong(1, s.getId());
				delete.executeUpdate();
				return true;
			}
			throw new SegnalazioneNonTrovataException("la segnalazione non è presente nel database");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Segnalazione> findByIdSegnalato(Long id) {
//		try (Connection connection = dataSource.getConnection()) {
//			PreparedStatement select = connection.prepareStatement(
//					"SELECT * FROM segnalazione WHERE id_utente_segnalato = ?");
//			select.setLong(1, id);
//			ResultSet result = select.executeQuery();
//			List<Segnalazione> segnalazioniUtente = new ArrayList<>();
//			while (result.next()) {
//				Segnalazione nuovaSegnalazione = new Segnalazione(
//						result.getString("motivazione"),
//						result.getLong("id_utente_segnalato"),
//						result.getLong("id_utente_segnalante"));
//				nuovaSegnalazione.setId(result.getLong("id"));
//				nuovaSegnalazione.setSanzionato(result.getBoolean("sanzionato")); // ← fix
//				segnalazioniUtente.add(nuovaSegnalazione);
//			}
//			return segnalazioniUtente;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public List<Segnalazione> getListAllSegnalazioni() {
//		try (Connection connection = dataSource.getConnection()) {
//			PreparedStatement select = connection.prepareStatement(
//					"SELECT * FROM segnalazione WHERE sanzionato = false");
//			ResultSet result = select.executeQuery();
//			List<Segnalazione> segnalazioni = new ArrayList<>();
//			while (result.next()) {
//				Segnalazione nuovaSegnalazione = new Segnalazione(
//						result.getString("motivazione"),
//						result.getLong("id_utente_segnalato"),
//						result.getLong("id_utente_segnalante"));
//				nuovaSegnalazione.setId(result.getLong("id"));
//				segnalazioni.add(nuovaSegnalazione);
//			}
//			return segnalazioni;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public List<Segnalazione> getListSegnalazioniSanzionate() {
//		try (Connection connection = dataSource.getConnection()) {
//			PreparedStatement select = connection.prepareStatement(
//					"SELECT * FROM segnalazione WHERE sanzionato = true");
//			ResultSet result = select.executeQuery();
//			List<Segnalazione> segnalazioni = new ArrayList<>();
//			while (result.next()) {
//				Segnalazione nuovaSegnalazione = new Segnalazione(
//						result.getString("motivazione"),
//						result.getLong("id_utente_segnalato"),
//						result.getLong("id_utente_segnalante"));
//				nuovaSegnalazione.setId(result.getLong("id"));
//				nuovaSegnalazione.setSanzionato(true);
//				segnalazioni.add(nuovaSegnalazione);
//			}
//			return segnalazioni;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public boolean removeById(Long id) throws SegnalazioneNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT * FROM segnalazione WHERE id = ?");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();
			if (result.next()) {
				PreparedStatement delete = connection.prepareStatement("DELETE FROM segnalazione WHERE id = ?");
				delete.setLong(1, id);
				delete.executeUpdate();
				return true;
			}
			throw new SegnalazioneNonTrovataException("la segnalazione non è presente nel database");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void setSanzionatoTrue(Long id) throws SegnalazioneNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement(
					"SELECT * FROM segnalazione WHERE id = ? AND sanzionato = false");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();
			if (result.next()) {
				PreparedStatement update = connection.prepareStatement(
						"UPDATE segnalazione SET sanzionato = true WHERE id = ?");
				update.setLong(1, id);
				update.executeUpdate();
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SegnalazioneNonTrovataException(
				"l'id non corrisponde ad alcuna segnalazione con sanzionato 'false' nel database");
	}
}