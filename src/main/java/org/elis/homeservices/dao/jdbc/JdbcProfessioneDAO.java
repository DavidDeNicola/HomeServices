package org.elis.homeservices.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.elis.homeservices.dao.definition.ProfessioneDAO;
import org.elis.homeservices.exception.ProfessioneGiaNelDatabaseException;
import org.elis.homeservices.exception.ProfessioneNonTrovataException;
import org.elis.homeservices.model.Professione;

public class JdbcProfessioneDAO implements ProfessioneDAO {
	
	private DataSource dataSource;

	public JdbcProfessioneDAO(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	@Override
	public boolean add(Professione p) throws ProfessioneGiaNelDatabaseException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM professione");
			ResultSet result = select.executeQuery();
			while(result.next()) {
				if(result.getString("nome").equals(p.getNome())) {
					throw new ProfessioneGiaNelDatabaseException("una professione con quel nome è già presente nel database");
				}
			}

			PreparedStatement insert = connection.prepareStatement("INSERT INTO professione(nome) VALUES (?)");
			insert.setString(1, p.getNome());
			insert.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean remove(Professione p) throws ProfessioneNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM professione WHERE nome = ?");
			select.setString(1, p.getNome());
			ResultSet result = select.executeQuery();
			
			PreparedStatement deleteUP = connection.prepareStatement("DELETE FROM utente_professione WHERE id_professione = ?");
			deleteUP.setLong(1, p.getId());
			deleteUP.executeUpdate();
			
			if(result.next()) {
				PreparedStatement delete = connection.prepareStatement("DELETE FROM professione WHERE nome = ?");
				delete.setString(1, p.getNome());
				delete.executeUpdate();
				return true;
			}
		
			throw new ProfessioneNonTrovataException("stai cercando di rimuovere una professione che non è presente nel database");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Professione findById(Long id) throws ProfessioneNonTrovataException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM professione WHERE id = ?");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();
			
			if(result.next()) {
				if( ((Long) result.getLong("id")).equals(id)) {
					Professione professioneTrovata = new Professione(result.getString("nome"));
					professioneTrovata.setId(id);
					return professioneTrovata;
				}
			}
		
			throw new ProfessioneNonTrovataException("stai cercando di rimuovere una professione che non è presente nel database");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Professione> getListProfessioni() {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM professione");
			ResultSet result = select.executeQuery();
			
			List<Professione> listaProfessioni = new ArrayList<>();
			
			while(result.next()) {
				Long id = (Long)result.getLong("id");
				String nome = result.getString("nome");
				
				Professione daAggiungere = new Professione(nome);
				daAggiungere.setId(id);
				
				listaProfessioni.add(daAggiungere);
			}
			
			return listaProfessioni;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
