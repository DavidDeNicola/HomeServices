package org.elis.homeservices.dao.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.exception.ProfessioneGiaAssociataException;
import org.elis.homeservices.exception.ProfessioneNonAssociataException;
import org.elis.homeservices.exception.ProfessionistaNonTrovatoException;
import org.elis.homeservices.exception.UtenteGiaPresenteException;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.exception.VeicoloGiaAssociatoException;
import org.elis.homeservices.exception.VeicoloNonAssociatoException;
import org.elis.homeservices.model.Citta;
import org.elis.homeservices.model.Professione;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.Veicolo;
//import org.elis.homeservices.model.enums.Ruolo;

public class JdbcUtenteDAO implements UtenteDAO{

	private DataSource dataSource;

	public JdbcUtenteDAO(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public Utente findByEmailPass(String email, String pass) throws UtenteNonTrovatoException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT * FROM utente WHERE email = ? AND password = ?");
			select.setString(1, email);
			select.setString(2, pass);

//			ResultSet result = select.executeQuery();

//			while(result.next()) {
//				if(result.getString("email").equals(email) && result.getString("password").equals(pass)) {
//					Utente utenteTrovato = new Utente(
//							result.getString("nome"), 
//							result.getString("cognome"), 
//							result.getString("email"),
//							result.getString("password"),
//							result.getDate("dataNascita").toLocalDate(),
//							result.getString("codiceFiscale"),
//							result.getLong("id_citta")
//							);
//					utenteTrovato.setId(result.getLong("id"));
//					utenteTrovato.setRuolo(Ruolo.values()[result.getInt("ruolo")]);
//					utenteTrovato.setTariffa(result.getBigDecimal("tariffa"));
//
//					return utenteTrovato;
//				}
//			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}

		throw new UtenteNonTrovatoException("l'email e la password non corrispondono a nessun utente");
	}

	@Override
	public boolean add(Utente u) throws UtenteGiaPresenteException {
		try (Connection connection = dataSource.getConnection()) { 
//			PreparedStatement select = connection.prepareStatement("SELECT email FROM utente WHERE email = ?");
//			select.setString(1, u.getEmail());
//			ResultSet result = select.executeQuery();
//
//			if(!result.next()) {
//				PreparedStatement insert = connection.prepareStatement("INSERT INTO utente(nome, cognome, email, password, dataNascita, codiceFiscale, ruolo, tariffa, id_citta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
//				insert.setString(1, u.getNome());
//				insert.setString(2, u.getCognome());
//				insert.setString(3, u.getEmail());
//				insert.setString(4, u.getPassword());
//				insert.setDate(5, java.sql.Date.valueOf(u.getDataNascita()));
//				insert.setString(6, u.getCf());
//				insert.setInt(7, 0);
//				insert.setBigDecimal(8, null);
//				insert.setLong(9, u.getIdCitta());
//
//				insert.executeUpdate();
//				return true;
//			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		throw new UtenteGiaPresenteException("un utente con questa email o codice fiscale è già presente nel database");
	}

	@Override
	public boolean removeByEmail(String email) throws UtenteNonTrovatoException {
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement select = connection.prepareStatement("SELECT * FROM utente WHERE email = ?");
			select.setString(1, email);
			ResultSet result = select.executeQuery();

			if(result.next()) {
				PreparedStatement delete = connection.prepareStatement("DELETE FROM utente WHERE email = ?");
				delete.setString(1, email);
				delete.executeUpdate();
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		throw new UtenteNonTrovatoException("un utente con questa email non è stato trovato nel database");

	}

	@Override
	public List<Utente> findByCity(Citta c){
		try (Connection connection = dataSource.getConnection()) { 
//			PreparedStatement select = connection.prepareStatement("SELECT * FROM utente WHERE id_citta = ? AND ruolo = 1");
//			select.setLong(1, c.getId());
//			ResultSet result = select.executeQuery();
//			List<Utente> listPro = new ArrayList<>();
//
//			while(result.next()) {
//				Utente nuovoUtente = new Utente(
//						result.getString("nome"), 
//						result.getString("cognome"), 
//						result.getString("email"), 
//						result.getString("password"), 
//						result.getDate("dataNascita").toLocalDate(),
//						result.getString("codiceFiscale"), 
//						c.getId());
//
//				nuovoUtente.setId(result.getLong("id"));
//				nuovoUtente.setRuolo(Ruolo.PRO);
//				nuovoUtente.setTariffa(result.getBigDecimal("tariffa"));
//
//				listPro.add(nuovoUtente);
//			}

//			return listPro;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public List<Utente> findByCity(Citta c, List<Utente> utenti){

		List<Utente> listPro = new ArrayList<>();

		for(Utente u : utenti) {
//			if(u.getIdCitta().equals(c.getId())){
				listPro.add(u);
//			}
		}

		return listPro;
	}


	@Override
	public List<Utente> findByMaxRate(BigDecimal maxTariffa){
//		try (Connection connection = dataSource.getConnection()) { 
//			PreparedStatement select = connection.prepareStatement("SELECT * FROM utente WHERE ruolo = 1 AND tariffa <= ?");
//			select.setBigDecimal(1, maxTariffa);
//			ResultSet result = select.executeQuery();
//			List<Utente> listPro = new ArrayList<>();
//
//
//			while(result.next()) {
//				Utente nuovoUtente = new Utente(
//						result.getString("nome"), 
//						result.getString("cognome"), 
//						result.getString("email"), 
//						result.getString("password"), 
//						result.getDate("dataNascita").toLocalDate(),
//						result.getString("codiceFiscale"), 
//						result.getLong("id_citta"));
//
//				nuovoUtente.setId(result.getLong("id"));
//				nuovoUtente.setRuolo(Ruolo.PRO);
//				nuovoUtente.setTariffa(result.getBigDecimal("tariffa"));
//
//				listPro.add(nuovoUtente);
//			}
//
//			return listPro;
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;

	}

	@Override
	public List<Utente> findByMaxRate(BigDecimal maxTariffa, List<Utente> utenti){

		List<Utente> listPro = new ArrayList<>();

		for(Utente u : utenti) {
			if(u.getTariffa().compareTo(maxTariffa) <= 0){
				listPro.add(u);
			}
		}

		return listPro;
	}


	@Override
	public List<Utente> findByMinRating(Double valutazione){
//		try (Connection connection = dataSource.getConnection()) { 
//			PreparedStatement select = connection.prepareStatement(
//					"SELECT * FROM utente u "
//							+ "JOIN (SELECT id_utente_riceve, AVG(voto) as media_voti FROM recensione "
//							+ "WHERE id_utente_riceve IN (SELECT id FROM utente WHERE ruolo = 1) "
//							+ "GROUP BY id_utente_riceve) AS mv "
//							+ "ON mv.id_utente_riceve = u.id WHERE media_voti >= ?;");
//
//			select.setDouble(1, valutazione);
//			ResultSet result = select.executeQuery();
//			List<Utente> listPro = new ArrayList<>();
//
//
//			while(result.next()) {
//				Utente nuovoUtente = new Utente(
//						result.getString("nome"), 
//						result.getString("cognome"), 
//						result.getString("email"), 
//						result.getString("password"), 
//						result.getDate("dataNascita").toLocalDate(),
//						result.getString("codiceFiscale"), 
//						result.getLong("id_citta"));
//
//				nuovoUtente.setId(result.getLong("id"));
//				nuovoUtente.setRuolo(Ruolo.PRO);
//				nuovoUtente.setTariffa(result.getBigDecimal("tariffa"));
//				nuovoUtente.setRating(result.getDouble("media_voti"));
//
//				listPro.add(nuovoUtente);
//			}
//
//			return listPro;
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;
	}


	@Override
	public List<Utente> findByMinRating(Double valutazione, List<Utente> utenti){	
//		try (Connection connection = dataSource.getConnection()) { 
//			List<Long> listIdUtenti = new ArrayList<>();
//			for(Utente u : utenti) {
//				listIdUtenti.add(u.getId());
//			}
//
//			PreparedStatement select = connection.prepareStatement(
//					"SELECT * FROM utente u "
//							+ "JOIN (SELECT id_utente_riceve, AVG(voto) as media_voti FROM recensione "
//							+ "WHERE id_utente_riceve = ? "
//							+ "GROUP BY id_utente_riceve) AS mv "
//							+ "ON mv.id_utente_riceve = u.id WHERE media_voti >= ?;");
//			select.setDouble(2, valutazione);
//
//			List<Utente> listPro = new ArrayList<>();
//
//			for(Long id : listIdUtenti) {
//				select.setLong(1, id);
//				try (ResultSet result = select.executeQuery();) { 
//					result.next();
//					Utente nuovoUtente = new Utente(
//							result.getString("nome"), 
//							result.getString("cognome"), 
//							result.getString("email"), 
//							result.getString("password"), 
//							result.getDate("dataNascita").toLocalDate(),
//							result.getString("codiceFiscale"), 
//							result.getLong("id_citta"));
//					nuovoUtente.setId(result.getLong("id"));
//					nuovoUtente.setRuolo(Ruolo.PRO);
//					nuovoUtente.setTariffa(result.getBigDecimal("tariffa"));
//					nuovoUtente.setRating(result.getDouble("media_voti"));
//
//					listPro.add(nuovoUtente);
//				}
//			}
//
//			return listPro;
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;

	}


	@Override
	public List<Utente> findByPro(Professione p){
//		try (Connection connection = dataSource.getConnection()) { 
//			PreparedStatement select = connection.prepareStatement("SELECT * FROM utente u JOIN utente_professione up ON up.id_utente = u.id WHERE up.id_professione = ?;");
//
//			select.setLong(1, p.getId());
//			ResultSet result = select.executeQuery();
//			List<Utente> listPro = new ArrayList<>();
//
//
//			while(result.next()) {
//				Utente nuovoUtente = new Utente(
//						result.getString("nome"), 
//						result.getString("cognome"), 
//						result.getString("email"), 
//						result.getString("password"), 
//						result.getDate("dataNascita").toLocalDate(),
//						result.getString("codiceFiscale"), 
//						result.getLong("id_citta"));
//
//				nuovoUtente.setId(result.getLong("id"));
//				nuovoUtente.setRuolo(Ruolo.PRO);
//				nuovoUtente.setTariffa(result.getBigDecimal("tariffa"));
//
//				listPro.add(nuovoUtente);
//			}
//
//			return listPro;
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public List<Utente> findByPro(Professione p, List<Utente> utenti){
		List<Utente> listPro = new ArrayList<>();


		for(Utente u : utenti) {
			try (Connection connection = dataSource.getConnection()) { 
				PreparedStatement select = connection.prepareStatement("SELECT id_professione FROM utente_professione WHERE id_utente = ?");
				select.setLong(1, u.getId());
				ResultSet result = select.executeQuery();
				while(result.next()) {
					if(((Long)result.getLong("id_professione")).equals(p.getId())) {
						listPro.add(u);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		return listPro;
	
	}

	@Override
	public boolean addPro(Utente u, Professione p) throws ProfessioneGiaAssociataException{
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement selectU = connection.prepareStatement("SELECT * FROM utente_professione");
			ResultSet resultU = selectU.executeQuery();
			
			while(resultU.next()) {
				if(u.getId().equals(resultU.getLong("id_utente")) && p.getId().equals(resultU.getLong("id_professione"))) {
					throw new ProfessioneGiaAssociataException("una relazione tra questo utente e questa professione è già presente nel database");
				}
			}
			
			PreparedStatement insert = connection.prepareStatement("INSERT INTO utente_professione(id_utente, id_professione) VALUES (?, ?);");
			insert.setLong(1, u.getId());
			insert.setLong(2, p.getId());
			insert.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}

	@Override
	public boolean removePro(Utente u, Professione p) throws ProfessioneNonAssociataException{
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement selectU = connection.prepareStatement("SELECT * FROM utente_professione");
			ResultSet resultU = selectU.executeQuery();
			
			while(resultU.next()) {
				if(u.getId().equals(resultU.getLong("id_utente")) && p.getId().equals(resultU.getLong("id_professione"))) {
					PreparedStatement delete = connection.prepareStatement("DELETE FROM utente_professione WHERE id_utente = ? AND id_professione = ?;");
					delete.setLong(1, u.getId());
					delete.setLong(2, p.getId());
					delete.executeUpdate();
					
					return true;
				}
			}
			
			
			throw new ProfessioneNonAssociataException("una relazione tra questo utente e questa professione non è presente nel database");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addVehicle(Utente u, Veicolo v) throws VeicoloGiaAssociatoException {
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement selectU = connection.prepareStatement("SELECT * FROM utente_veicolo");
			ResultSet resultU = selectU.executeQuery();
			
			while(resultU.next()) {
				if(u.getId().equals(resultU.getLong("id_utente")) && v.getId().equals(resultU.getLong("id_veicolo"))) {
					throw new VeicoloGiaAssociatoException("una relazione tra questo utente e questo veicolo è già presente nel database");
				}
			}
			
			PreparedStatement insert = connection.prepareStatement("INSERT INTO utente_veicolo(id_utente, id_veicolo) VALUES (?, ?);");
			insert.setLong(1, u.getId());
			insert.setLong(2, v.getId());
			insert.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeVehicle(Utente u, Veicolo v) throws VeicoloNonAssociatoException {
		try (Connection connection = dataSource.getConnection()) { 
			PreparedStatement selectU = connection.prepareStatement("SELECT * FROM utente_veicolo");
			ResultSet resultU = selectU.executeQuery();
			
			while(resultU.next()) {
				if(u.getId().equals(resultU.getLong("id_utente")) && v.getId().equals(resultU.getLong("id_veicolo"))) {
					PreparedStatement delete = connection.prepareStatement("DELETE FROM utente_veicolo WHERE id_utente = ? AND id_veicolo = ?;");
					delete.setLong(1, u.getId());
					delete.setLong(2, v.getId());
					delete.executeUpdate();
					
					return true;
				}
			}
			
			
			throw new VeicoloNonAssociatoException("una relazione tra questo utente e questo veicolo non è presente nel database");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Utente findById(Long id) throws UtenteNonTrovatoException {
//		try (Connection connection = dataSource.getConnection()) {
//			PreparedStatement select = connection.prepareStatement("SELECT * FROM utente WHERE id = ?");
//			select.setLong(1, id);
//
//			ResultSet result = select.executeQuery();
//
//			while(result.next()) {
//				if(((Long)result.getLong("id")).equals(id)) {
//					Utente utenteTrovato = new Utente(
//							result.getString("nome"), 
//							result.getString("cognome"), 
//							result.getString("email"),
//							result.getString("password"),
//							result.getDate("dataNascita").toLocalDate(),
//							result.getString("codiceFiscale"),
//							result.getLong("id_citta")
//							);
//					utenteTrovato.setId(result.getLong("id"));
//					utenteTrovato.setRuolo(Ruolo.values()[result.getInt("ruolo")]);
//
//					return utenteTrovato;
//				}
//			} 
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

		throw new UtenteNonTrovatoException("l'id non corrisponde a nessun utente");
	}

	@Override
	public boolean modifyRate(Long id, BigDecimal tariffa) {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT tariffa FROM utente WHERE id = ?");
			select.setLong(1, id);

			ResultSet result = select.executeQuery();

			if(result.next()) {
				PreparedStatement update = connection.prepareStatement("UPDATE utente set tariffa = ? where id= ?");
				update.setBigDecimal(1, tariffa);
				update.setLong(2, id);
				update.executeUpdate();
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean toPro(Long id, String codiceFiscale) throws UtenteNonTrovatoException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT * FROM utente WHERE id = ?");
			select.setLong(1, id);
			ResultSet result = select.executeQuery();

			if(result.next()) {
				PreparedStatement update = connection.prepareStatement("UPDATE utente SET ruolo = 1 where id = ?");
				update.setLong(1, id);
				update.executeUpdate();
				
				PreparedStatement update2 = connection.prepareStatement("UPDATE utente SET codiceFiscale = ? where id = ?");
				update2.setString(1, codiceFiscale);
				update2.setLong(2, id);
				update2.executeUpdate();
				return true;
			}
			
			throw new UtenteNonTrovatoException("l'id non corrisponde a nessun utente nel database");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Professione> showPro(Long id) {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement select = connection.prepareStatement("SELECT * FROM utente_professione JOIN professione ON utente_professione.id_professione = professione.id WHERE id_utente = ?");
			select.setLong(1, id);

			ResultSet result = select.executeQuery();
			
			List<Professione> listaProfessioni=new ArrayList<>();

			while(result.next()) {
				Professione daAggiungere=new Professione(result.getString("nome"));
				daAggiungere.setId(result.getLong("id_professione"));
				listaProfessioni.add(daAggiungere);
				System.out.println(daAggiungere);
			}
			return listaProfessioni;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Utente findByEmail(String email) throws UtenteNonTrovatoException {
//		try (Connection connection = dataSource.getConnection()) {
//			PreparedStatement select = connection.prepareStatement("SELECT * FROM utente WHERE email = ?");
//			select.setString(1, email);
//
//			ResultSet result = select.executeQuery();
//
//			while(result.next()) {
//				if(result.getString("email").equals(email)) {
//					Utente utenteTrovato = new Utente(
//							result.getString("nome"), 
//							result.getString("cognome"), 
//							result.getString("email"),
//							result.getString("password"),
//							result.getDate("dataNascita").toLocalDate(),
//							result.getString("codiceFiscale"),
//							result.getLong("id_citta")
//							);
//					utenteTrovato.setId(result.getLong("id"));
//					utenteTrovato.setRuolo(Ruolo.values()[result.getInt("ruolo")]);
//					utenteTrovato.setTariffa(result.getBigDecimal("tariffa"));
//
//					return utenteTrovato;
//				}
//			} 
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

		throw new UtenteNonTrovatoException("l'email non corrisponde a nessun utente");
	}

	@Override

	public List<Utente> ricercaAvanzata(String nome, Long idCitta, Double valutazione, LocalDate data, LocalTime inizio,
			LocalTime fine, String ordine) throws ProfessionistaNonTrovatoException{
		List<Utente> listaProfessionisti= new ArrayList<>();

//		// FIX 3: LEFT JOIN su disponibilita (con JOIN normale i professionisti senza disponibilità scomparivano)
//		// FIX 2: aggiunto JOIN professione per cercare anche per nome professione
//		StringBuilder builder=new StringBuilder("SELECT DISTINCT u.*,COALESCE(AVG(r.voto),0) AS media_voto,c.nome AS nome_citta "
//				+ " FROM utente u "
//				+ " JOIN citta c on c.id=u.id_citta "
//				+ " LEFT JOIN recensione r on u.id= r.id_utente_riceve "
//				+ " JOIN utente_professione up on u.id=up.id_utente "
//				+ " JOIN professione prof on prof.id=up.id_professione "
//				+ " WHERE u.ruolo= 1 ");
//		
//		List<Object> parametri=new ArrayList<>();
//
//		// FIX 2: cerca su nome, cognome e nome della professione
//		if(nome!=null && !nome.isEmpty()) {
//			builder.append(" AND (u.nome LIKE ? OR u.cognome LIKE ? OR prof.nome LIKE ?) ");
//			parametri.add("%" + nome + "%");
//			parametri.add("%" + nome + "%");
//			parametri.add("%" + nome + "%");
//		}
//		if(idCitta!=null && idCitta>0) {
//			builder.append(" AND u.id_citta = ? ");
//			parametri.add(idCitta);
//		}
//		if(data!=null) {
//			builder.append(" AND EXISTS (SELECT 1 FROM disponibilita d WHERE d.id_utente = u.id AND d.data = ?) ");
//			parametri.add(Date.valueOf(data));
//		}
//		if(inizio!=null) {
//			builder.append(" AND EXISTS (SELECT 1 FROM disponibilita d WHERE d.id_utente = u.id AND d.da <= ?) ");
//			parametri.add(Time.valueOf(inizio));
//		}
//		if(fine!=null) {
//			builder.append(" AND EXISTS (SELECT 1 FROM disponibilita d WHERE d.id_utente = u.id AND d.a >= ?) ");
//			parametri.add(Time.valueOf(fine));
//		}
//		
//		builder.append(" GROUP BY u.id ");
//		
//		// FIX 1: valutazione aggiunta a parametri PRIMA di costruire il PreparedStatement
//		if(valutazione!=null && valutazione>=0) {
//			builder.append(" HAVING media_voto >= ? ");
//			parametri.add(valutazione);
//		}
//		
//		try (Connection connection = dataSource.getConnection()){
//			PreparedStatement select=connection.prepareStatement(builder.toString());
//			
//			for(int i=0;i<parametri.size();i++) {
//				select.setObject(i + 1, parametri.get(i));
//			}
//			
//			try (ResultSet result= select.executeQuery()){
//				while(result.next()) {
//					Long idUtente=result.getLong("id");
//					String nomeUtente=result.getString("nome");
//					String cognomeUtente=result.getString("cognome");
//					String emailUtente=result.getString("email");
//					String passwordUtente=result.getString("password");
//					Date sqldata=result.getDate("dataNascita");
//					LocalDate ddnUtente= (sqldata!=null)? sqldata.toLocalDate():null;
//					String cfUtente=result.getString("codiceFiscale");
//					Integer ruoloUtente=result.getInt("ruolo");
//					BigDecimal tariffaUtente=result.getBigDecimal("tariffa");
//					Double mediaVotiUtente=result.getDouble("media_voto");
//					Long idCittaUtente=result.getLong("id_citta");
//					
//					
//					String nomeCitta=result.getString("nome_citta");
//					Citta citta=new Citta(nomeCitta);
//					citta.setId(idCittaUtente);
//					
//					Utente utente=new Utente(nomeUtente,cognomeUtente,emailUtente,passwordUtente,ddnUtente,cfUtente,idCittaUtente);
//					utente.setId(idUtente);
//					utente.setRating(mediaVotiUtente);
//					System.out.println(mediaVotiUtente);
//					System.out.println(idUtente);
//					utente.setTariffa(tariffaUtente);
//					utente.setRuolo(Ruolo.values()[ruoloUtente]);
//					
//					
//					listaProfessionisti.add(utente);
//				}
//			}
//			
//			if(listaProfessionisti.isEmpty()) {
//				throw new ProfessionistaNonTrovatoException("nessun Professionista trovato");
//			}
//			
//			
//		}catch(SQLException e) {
//			e.printStackTrace();
//		}
				
		return listaProfessionisti;
	}
	
	
	
	
	public List<Utente> findByNomeCognome(String query) {
//		try (Connection connection = dataSource.getConnection()) {
//			// Cerca per nome, cognome o nome+cognome (case insensitive, match parziale)
//			String pattern = "%" + query.trim().toLowerCase() + "%";
//			PreparedStatement select = connection.prepareStatement(
//				"SELECT * FROM utente WHERE ruolo = 1 AND (LOWER(nome) LIKE ? OR LOWER(cognome) LIKE ? OR LOWER(CONCAT(nome, ' ', cognome)) LIKE ?)");    
//			select.setString(1, pattern);
//			select.setString(2, pattern);
//			select.setString(3, pattern);
//			ResultSet result = select.executeQuery();
//			List<Utente> lista = new ArrayList<>();
//			while (result.next()) {
//				Utente u = new Utente(
//						result.getString("nome"),
//						result.getString("cognome"),
//						result.getString("email"),
//						result.getString("password"),
//						result.getDate("dataNascita").toLocalDate(),
//						result.getString("codiceFiscale"),
//						result.getLong("id_citta"));
//				u.setId(result.getLong("id"));
//				u.setRuolo(Ruolo.PRO);
//				u.setTariffa(result.getBigDecimal("tariffa"));
//				lista.add(u);
//			}
//			return lista;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public List<Professione> getListaProfessioni(Long id) throws UtenteNonTrovatoException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement selectUtente = connection.prepareStatement("SELECT * FROM utente WHERE id = ?");
			selectUtente.setLong(1, id);
			ResultSet resultUtente = selectUtente.executeQuery();
			
			if(resultUtente.next()) {
				PreparedStatement select = connection.prepareStatement("SELECT p.* FROM professione p JOIN utente_professione up ON up.id_professione = p.id WHERE up.id_utente = ?");
				select.setLong(1, id);
				ResultSet result = select.executeQuery();
				List<Professione> listaProfessioni = new ArrayList<>();
				
				while(result.next()) {
					Professione daAggiungere = (new Professione( 
										result.getString("nome")
							));
					daAggiungere.setId(result.getLong("id"));
					listaProfessioni.add(daAggiungere);	
				}
				return listaProfessioni;
			}
			
			throw new UtenteNonTrovatoException("L'id non corrisponde a nessun utente del database.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}



}
