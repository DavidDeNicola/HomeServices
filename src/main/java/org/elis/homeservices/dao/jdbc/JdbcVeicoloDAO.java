package org.elis.homeservices.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.elis.homeservices.dao.definition.VeicoloDAO;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.exception.VeicoloGiaNelDatabaseException;
import org.elis.homeservices.exception.VeicoloNonTrovatoException;
import org.elis.homeservices.model.Veicolo;

public class JdbcVeicoloDAO implements VeicoloDAO {
	
	private DataSource dataSource;

	public JdbcVeicoloDAO(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	
	@Override
	public boolean add(Veicolo v) throws VeicoloGiaNelDatabaseException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM veicolo");
			ResultSet result = select.executeQuery();
			while(result.next()) {
				if(result.getString("nome").equals(v.getNome())) {
					throw new VeicoloGiaNelDatabaseException("un veicolo con quel nome è già presente nel database");
				}
			}

			PreparedStatement insert = connection.prepareStatement("INSERT INTO veicolo(nome) VALUES (?)");
			insert.setString(1, v.getNome());
			insert.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean remove(Veicolo v) throws VeicoloNonTrovatoException {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM veicolo WHERE nome = ?");
			select.setString(1, v.getNome());
			ResultSet result = select.executeQuery();
			
			if(result.next()) {
				PreparedStatement delete = connection.prepareStatement("DELETE FROM veicolo WHERE nome = ?");
				delete.setString(1, v.getNome());
				delete.executeUpdate();
				return true;
			}
		
			throw new VeicoloNonTrovatoException("stai cercando di rimuovere un veicolo che non è presente nel database");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}


	@Override
	public List<Veicolo> getListaVeicoli() {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM veicolo");
			ResultSet result = select.executeQuery();
			
			List<Veicolo> listaVeicoli = new ArrayList<>();
			
			while(result.next()) {
				Long id = (Long)result.getLong("id");
				String nome = result.getString("nome");
				
				Veicolo daAggiungere = new Veicolo(nome);
				daAggiungere.setId(id);
				
				listaVeicoli.add(daAggiungere);
			}
			
			return listaVeicoli;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<Veicolo> getListaVeicoliUtente(Long idUtente) throws UtenteNonTrovatoException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement selectUtente = connection.prepareStatement("SELECT * FROM utente WHERE id = ?");
			selectUtente.setLong(1, idUtente);
			
			ResultSet resultUtente = selectUtente.executeQuery();
			
			if(!resultUtente.next()) {
				throw new UtenteNonTrovatoException("L'id non e' associato a nessun utente nel databse.");
			}
			
			PreparedStatement select = connection.prepareStatement("SELECT v.* FROM veicolo v JOIN utente_veicolo uv ON v.id = uv.id_veicolo WHERE id_utente = ? ");
			select.setLong(1, idUtente);
			ResultSet result = select.executeQuery();
			
			List<Veicolo> listaVeicoli = new ArrayList<>();
			
			while(result.next()) {
				Long id = (Long)result.getLong("id");
				String nome = result.getString("nome");
				
				Veicolo daAggiungere = new Veicolo(nome);
				daAggiungere.setId(id);
				
				listaVeicoli.add(daAggiungere);
			}
			
			return listaVeicoli;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Veicolo findById(Long id) {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM veicolo where id=?");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();
			
			if(result.next()) {
				Veicolo daRestituire= new Veicolo(result.getString("nome"));
				daRestituire.setId(result.getLong("id"));
				return daRestituire;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}
