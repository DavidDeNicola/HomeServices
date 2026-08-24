package org.elis.homeservices.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.elis.homeservices.dao.definition.CittaDAO;
import org.elis.homeservices.exception.CittaGiaNelDatabaseException;
import org.elis.homeservices.exception.CittaNonTrovataException;
import org.elis.homeservices.model.Citta;

public class JdbcCittaDAO implements CittaDAO {

	private DataSource dataSource;

	public JdbcCittaDAO(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public Citta findCityByName(String name) throws CittaNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT * FROM citta WHERE nome = ?");
			select.setString(1, name);

			ResultSet result = select.executeQuery();

			while(result.next()) {
				if(result.getString("nome").equals(name)) {
					Citta c = new Citta(name);
					c.setId(result.getLong("id"));
					return c;
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}

		throw new CittaNonTrovataException("il nome in input non corrisponde ad alcuna città nel database");

	}


	@Override
	public Long findIdByName(String name) throws CittaNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT * FROM citta WHERE nome = ?");
			select.setString(1, name);

			ResultSet result = select.executeQuery();

			while(result.next()) {
				if(result.getString("nome").equals(name)) {
					return result.getLong("id");
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}

		throw new CittaNonTrovataException("il nome in input non corrisponde ad alcun id di una città nel database");
	}

	@Override
	public boolean addCity(String name) throws CittaGiaNelDatabaseException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM citta");
			ResultSet result = select.executeQuery();
			while(result.next()) {
				if(result.getString("nome").equals(name)) {
					throw new CittaGiaNelDatabaseException("una città con quel nome è già presente nel database");
				}
			}

			PreparedStatement insert = connection.prepareStatement("INSERT INTO citta(nome) VALUES (?)");
			insert.setString(1, name);
			insert.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean removeCityById(Long id) throws CittaNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM citta WHERE id = ?");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();
			
			while(result.next()) {
				if(((Long) result.getLong("id")).equals(id)) {
					PreparedStatement delete = connection.prepareStatement("DELETE FROM citta WHERE id = ?");
					delete.setLong(1, id);
					delete.executeUpdate();
					return true;
				}
			}
		
			throw new CittaNonTrovataException("stai cercando di rimuovere una citta il cui id non è presente nel database");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public List<Citta> getListCitta() {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM citta");
			ResultSet result = select.executeQuery();
			
			List<Citta> listaCitta = new ArrayList<>();
			
			while(result.next()) {
				Long id = (Long)result.getLong("id");
				String nome = result.getString("nome");
				
				Citta daAggiungere = new Citta(nome);
				daAggiungere.setId(id);
				
				listaCitta.add(daAggiungere);
			}
			
			return listaCitta;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public Citta getCityById(Long id) throws CittaNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT * FROM citta WHERE id = ?");
			select.setLong(1, id);

			ResultSet result = select.executeQuery();

			while(result.next()) {
				if(((Long)result.getLong("id")).equals(id)) {
					Citta cittaTrovata = new Citta(result.getString("nome"));
					cittaTrovata.setId(id);
					return cittaTrovata;
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}

		throw new CittaNonTrovataException("l'id in input non corrisponde a nessuna città nel database");
	}

	@Override
	public boolean editCityNameById(Long id, String name) throws CittaNonTrovataException {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
