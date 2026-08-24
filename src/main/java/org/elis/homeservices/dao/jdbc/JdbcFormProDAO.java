package org.elis.homeservices.dao.jdbc;

//import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.elis.homeservices.dao.definition.FormProDAO;
import org.elis.homeservices.exception.FormProNonTrovatoException;
import org.elis.homeservices.exception.UtenteGiaPresenteException;
import org.elis.homeservices.model.Citta;
import org.elis.homeservices.model.FormPro;
import org.elis.homeservices.model.Professione;
//import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.enums.Ruolo;

public class JdbcFormProDAO implements FormProDAO {

	private DataSource dataSource;

	public JdbcFormProDAO(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public boolean add(FormPro fp) throws UtenteGiaPresenteException {
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement select = connection.prepareStatement("SELECT * FROM formpro WHERE id_utente = ?");
			select.setLong(1, fp.getUtente().getId());
			ResultSet result = select.executeQuery();

			if(!result.next() && fp.getUtente().getRuolo().equals(Ruolo.USER)) {
				PreparedStatement insert = connection.prepareStatement("INSERT INTO formpro(codiceFiscale, tariffa, id_citta, id_professione, id_utente) VALUES(?, ?, ?, ?, ?)");
				insert.setString(1, fp.getCf());
				insert.setBigDecimal(2, fp.getTariffa());
				insert.setLong(3, fp.getCitta().getId());
				insert.setLong(4, fp.getProfessione().getId());
				insert.setLong(5, fp.getUtente().getId());

				insert.executeUpdate();
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		throw new UtenteGiaPresenteException("questo utente ha già creato un form per diventare professionista o lo è già diventato");
	}

	@Override
	public boolean remove(Long id) throws FormProNonTrovatoException {
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement select = connection.prepareStatement("SELECT * FROM formpro WHERE id = ?");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();

			if(result.next()) {
				PreparedStatement delete = connection.prepareStatement("DELETE FROM formpro WHERE id = ?");
				delete.setLong(1, id);
				delete.executeUpdate();
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		throw new FormProNonTrovatoException("un form con questo id non è stato trovato nel database");
	}

	@Override
	public List<FormPro> getForms() {
		try (Connection connection = dataSource.getConnection()) {

			PreparedStatement select = connection.prepareStatement("SELECT * FROM formpro");
			ResultSet result = select.executeQuery();

			List<FormPro> listaFormPro = new ArrayList<>();

			while(result.next()) {
				PreparedStatement selectCitta = connection.prepareStatement("SELECT * FROM citta WHERE id = ?");
				selectCitta.setLong(1, result.getLong("id_citta"));
				ResultSet resultCitta = selectCitta.executeQuery();
				
				PreparedStatement selectProfessione = connection.prepareStatement("SELECT * FROM professione WHERE id = ?");
				selectProfessione.setLong(1, result.getLong("id_professione"));
				ResultSet resultProfessione = selectProfessione.executeQuery();
				
				PreparedStatement selectUtente = connection.prepareStatement("SELECT * FROM utente WHERE id = ?");
				selectUtente.setLong(1, result.getLong("id_utente"));
				ResultSet resultUtente = selectUtente.executeQuery();
				
				if(resultCitta.next() && resultProfessione.next() && resultUtente.next()) {
//					Long id = (Long) result.getLong("id");
//					String cf = result.getString("codiceFiscale");
//					BigDecimal tariffa = result.getBigDecimal("tariffa");
					
					Citta citta = new Citta(resultCitta.getString("nome"));
					citta.setId(resultCitta.getLong("id"));
					
					Professione professione = new Professione(resultProfessione.getString("nome"));
					professione.setId(resultProfessione.getLong("id"));
					
//					Utente utente = new Utente(
//							resultUtente.getString("nome"), 
//							resultUtente.getString("cognome"), 
//							resultUtente.getString("email"),
//							resultUtente.getString("password"),
//							resultUtente.getDate("dataNascita").toLocalDate(),
//							resultUtente.getString("codiceFiscale"),
//							resultUtente.getLong("id_citta")
//							);
//					utente.setId(resultUtente.getLong("id"));
//					utente.setRuolo(Ruolo.values()[resultUtente.getInt("ruolo")]);
//					utente.setTariffa(result.getBigDecimal("tariffa"));
					
//					FormPro daAggiungere = new FormPro(cf, tariffa, citta, professione, utente);
//					daAggiungere.setId(id);
//
//					listaFormPro.add(daAggiungere);
				}
			}

			return listaFormPro;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public FormPro getById(Long id) throws FormProNonTrovatoException {
	    // Una singola query che tira su tutto grazie alle JOIN
	    String sql = "SELECT f.*, u.*, c.nome AS nome_citta, p.nome AS nome_professione " +
	                 "FROM formpro f " +
	                 "JOIN utente u ON f.id_utente = u.id " +
	                 "JOIN citta c ON f.id_citta = c.id " +
	                 "JOIN professione p ON f.id_professione = p.id " +
	                 "WHERE f.id = ?";

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setLong(1, id);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	               
	                Citta citta = new Citta(rs.getString("nome_citta"));
	                citta.setId(rs.getLong("id_citta"));

	             
	                Professione prof = new Professione(rs.getString("nome_professione"));
	                prof.setId(rs.getLong("id_professione"));

	                
//	                Utente utente = new Utente(
//	                    rs.getString("nome"), 
//	                    rs.getString("cognome"), 
//	                    rs.getString("email"),
//	                    rs.getString("password"),
//	                    rs.getDate("dataNascita").toLocalDate(),
//	                    rs.getString("codiceFiscale"),
//	                    rs.getLong("id_citta")
//	                );
//	                utente.setId(rs.getLong("id_utente"));
//	                utente.setRuolo(Ruolo.values()[rs.getInt("ruolo")]);
//
//	               
//	                FormPro fp = new FormPro(
//	                    rs.getString("codiceFiscale"), 
//	                    rs.getBigDecimal("tariffa"), 
//	                    citta, prof, utente
//	                );
//	                fp.setId(id);
//	                
//	                return fp;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    throw new FormProNonTrovatoException("Nessun form trovato con id: " + id);
	}

}
