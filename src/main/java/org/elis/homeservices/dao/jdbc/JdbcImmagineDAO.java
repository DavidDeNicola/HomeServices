package org.elis.homeservices.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.elis.homeservices.dao.definition.ImmagineDAO;
import org.elis.homeservices.exception.ImmagineGiaNelDatabaseException;
import org.elis.homeservices.exception.ImmagineNonTrovataException;
import org.elis.homeservices.model.Immagine;

public class JdbcImmagineDAO implements ImmagineDAO {
	
	private DataSource dataSource;

	public JdbcImmagineDAO(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	@Override
	public boolean add(Immagine i) throws ImmagineGiaNelDatabaseException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM immagine");
			ResultSet result = select.executeQuery();
			while(result.next()) {
				if(result.getString("percorso").equals(i.getPercorso())) {
					throw new ImmagineGiaNelDatabaseException("un'immagine con quel percorso è già presente nel database");
				}
			}

			PreparedStatement insert = connection.prepareStatement("INSERT INTO immagine(nome, percorso, isFotoProfilo, id_utente) VALUES (?, ?, ?, ?)");
			insert.setString(1, i.getNome());
			insert.setString(2, i.getPercorso());
			insert.setBoolean(3, i.getIsFotoProfilo());
//			insert.setLong(1, i.getIdUtente());
			insert.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean remove(Immagine i) throws ImmagineNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM immagine WHERE path = ?");
			select.setString(1, i.getPercorso());
			ResultSet result = select.executeQuery();
			
			if(result.next()) {
				PreparedStatement delete = connection.prepareStatement("DELETE FROM immagine WHERE path = ?");
				delete.setString(1, i.getPercorso());
				delete.executeUpdate();
				return true;
			}
		
			throw new ImmagineNonTrovataException("stai cercando di rimuovere un'immagine il cui path non è presente nel database");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}

