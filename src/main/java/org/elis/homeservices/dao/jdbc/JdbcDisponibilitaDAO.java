package org.elis.homeservices.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.elis.homeservices.dao.definition.DisponibilitaDAO;
import org.elis.homeservices.exception.DisponibilitaNonTrovataException;
import org.elis.homeservices.exception.DisponibilitaTimeOverlapException;
import org.elis.homeservices.model.Disponibilita;

public class JdbcDisponibilitaDAO implements DisponibilitaDAO {
	
	private DataSource dataSource;

	public JdbcDisponibilitaDAO(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	@Override
	public boolean add(Disponibilita d) throws DisponibilitaTimeOverlapException {
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement select = connection.prepareStatement("SELECT * FROM disponibilita id_utente = ? AND data = ?");
//			select.setLong(1, d.getIdUtente());
			select.setDate(2, java.sql.Date.valueOf(d.getData()));
			ResultSet result = select.executeQuery();
			
			while(result.next()) {
				if(!result.getTime("da").toLocalTime().isAfter(d.getA()) && !result.getTime("a").toLocalTime().isBefore(d.getDa())) {
					throw new DisponibilitaTimeOverlapException("l'orario della disponibilita in argomento è già occupato da una disponibilita dell'utente");
				}
			}
			
			PreparedStatement insert = connection.prepareStatement("INSERT INTO disponibilita(data, da, a, id_utente) VALUES (?, ?, ?, ?)");
			insert.setDate(1, java.sql.Date.valueOf(d.getData()));
			insert.setTime(2, java.sql.Time.valueOf(d.getDa()));
			insert.setTime(3, java.sql.Time.valueOf(d.getA()));
//			insert.setLong(3, d.getIdUtente());
			insert.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public boolean remove(Disponibilita d) throws DisponibilitaNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {  
			PreparedStatement select = connection.prepareStatement("SELECT * FROM disponibilita WHERE id = ?");
			select.setLong(1, d.getId());
			ResultSet result = select.executeQuery();
			
			if(result.next()) {
				PreparedStatement delete = connection.prepareStatement("DELETE FROM disponibilita WHERE id = ?");
				delete.setLong(1, d.getId());
				delete.executeUpdate();
				return true;
			}
			
			throw new DisponibilitaNonTrovataException("la disponibilita non è presente nel database");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Disponibilita> findByIdPro(Long id) {
		try (Connection connection = dataSource.getConnection()) {  
			PreparedStatement select = connection.prepareStatement("SELECT * FROM disponibilita WHERE id_utente = ?");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();
			
			List<Disponibilita> disponibilitaPro = new ArrayList<>();
			while(result.next()) {
//				Disponibilita nuovaDisponibilita = new Disponibilita(
//						result.getDate("data").toLocalDate(), 
//						result.getTime("da").toLocalTime(), 
//						result.getTime("a").toLocalTime(),
//						result.getLong("id_utente"));
//				nuovaDisponibilita.setId(result.getLong("id"));
//				
//				disponibilitaPro.add(nuovaDisponibilita);
			}
			return disponibilitaPro;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
