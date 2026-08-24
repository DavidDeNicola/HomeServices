package org.elis.homeservices.dao.jpa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import org.elis.homeservices.model.enums.Ruolo;
import org.hibernate.Hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;

public class JpaUtenteDAO implements UtenteDAO{
	
	private EntityManagerFactory emf;
	
	public JpaUtenteDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	@Override
	public Utente findById(Long id) throws UtenteNonTrovatoException {
		try(EntityManager em = emf.createEntityManager()) {
			return em.find(Utente.class, id);
		}
	}

	@Override
	public Utente findByEmailPass(String email, String pass) throws UtenteNonTrovatoException {
		try(EntityManager em = emf.createEntityManager()){
			Query q = em.createQuery("SELECT u FROM utente u WHERE u.email = :email AND u.password = :password");
			q.setParameter("email", email);
			q.setParameter("password", pass);
			try {
			    return (Utente) q.getSingleResult();
			} catch (NoResultException e) {
			    throw new UtenteNonTrovatoException("Utente non trovato");
			}
		}
	}

	@Override
	public Utente findByEmail(String email) throws UtenteNonTrovatoException {
		try(EntityManager em = emf.createEntityManager()){
			Query q = em.createQuery("SELECT u FROM utente u WHERE u.email = :email");
			q.setParameter("email", email);
			try {
				return (Utente) q.getSingleResult();
			} catch (NoResultException e) {
				throw new UtenteNonTrovatoException("Nessun utente corrisponde all'id inserito.");
			}
		}
	}

	@Override
	public boolean add(Utente u) throws UtenteGiaPresenteException {
	    try(EntityManager em = emf.createEntityManager()) {
	    	EntityTransaction et = em.getTransaction();
	        et.begin();
	        em.persist(u);
	        et.commit();
	        return true;
	    } catch (PersistenceException e) {
	        throw new UtenteGiaPresenteException("Un utente con questa email o codice fiscale è già presente nel database");
	    }
	}

	@Override
	public boolean removeByEmail(String email) throws UtenteNonTrovatoException {
	    try (EntityManager em = emf.createEntityManager()) {
	        EntityTransaction et = em.getTransaction();
	        Query q = em.createQuery("DELETE FROM utente u WHERE u.email = :email");
	        q.setParameter("email", email);

	        et.begin();
	        int deleted = q.executeUpdate();
	        et.commit();

	        if (deleted == 0) {
	            throw new UtenteNonTrovatoException("Un utente con questa email non è presente nel database");
	        }

	        return true;
	    }
	}

	@Override
	public List<Utente> findByCity(Citta c) {
		try(EntityManager em = emf.createEntityManager()){
			Query q = em.createQuery("SELECT u FROM utente u WHERE u.citta = :citta AND u.ruolo = 1");
			q.setParameter("citta", c);
			@SuppressWarnings("unchecked")
			List<Utente> daRitornare = q.getResultList();
			return daRitornare;
		}
	}

	@Override
	public List<Utente> findByCity(Citta c, List<Utente> utenti) {
		List<Utente> listPro = new ArrayList<>();

		for(Utente u : utenti) {
			if(u.getCitta().equals(c)){
				listPro.add(u);
			}
		}

		return listPro;
	}

	@Override
	public List<Utente> findByMaxRate(BigDecimal maxTariffa) {
		try(EntityManager em = emf.createEntityManager()){
			Query q = em.createQuery("SELECT u FROM utente u WHERE u.tariffa = :tariffa AND u.ruolo = 1");
			q.setParameter("tariffa", maxTariffa);
			@SuppressWarnings("unchecked")
			List<Utente> daRitornare = q.getResultList();
			return daRitornare;
		}
	}

	@Override
	public List<Utente> findByMaxRate(BigDecimal maxTariffa, List<Utente> utenti) {
		List<Utente> listPro = new ArrayList<>();

		for(Utente u : utenti) {
			if(u.getTariffa().compareTo(maxTariffa) <= 0){
				listPro.add(u);
			}
		}

		return listPro;
	}

	@Override
	public boolean modifyRate(Long id, BigDecimal tariffa) {
		try(EntityManager em = emf.createEntityManager()){
			EntityTransaction et = em.getTransaction();
			Query q = em.createQuery("UPDATE utente u SET tariffa = :tariffa WHERE u.id = :id");
			q.setParameter("tariffa", tariffa);
			q.setParameter("id", id);
			
			et.begin();
			int updated = q.executeUpdate();
			et.commit();
			
			if(updated > 0) {
				return true;
			}
			return false;
		}
	}

	@Override
	public List<Utente> findByMinRating(Double valutazione) {
		return null;
	}

	@Override
	public List<Utente> findByMinRating(Double valutazione, List<Utente> utenti) {
		return null;
	}

	@Override
	public List<Utente> findByPro(Professione p) {
		try(EntityManager em = emf.createEntityManager()){
			Query q = em.createQuery("SELECT u FROM utente u WHERE :professione MEMBER OF u.professioni AND u.ruolo = 1");
			q.setParameter("professione", p);
			@SuppressWarnings("unchecked")
			List<Utente> daRitornare = q.getResultList();
			return daRitornare;
		}
	}

	@Override
	public List<Utente> findByPro(Professione p, List<Utente> utenti) {
		List<Utente> listPro = new ArrayList<>();

		for(Utente u : utenti) {
			if(u.getProfessioni().contains(p)){
				listPro.add(u);
			}
		}

		return listPro;
	}

	@Override
	public List<Professione> showPro(Long id) throws UtenteNonTrovatoException {
		try(EntityManager em = emf.createEntityManager()) {
			Utente utente = em.find(Utente.class, id);
			if(utente == null) throw new UtenteNonTrovatoException("utente non trovato per id: "+id);
			return utente.getProfessioni();
		}
	}

	@Override
	public List<Utente> ricercaAvanzata(String nome, Long idCitta, Double valutazione, LocalDate data, LocalTime inizio, LocalTime fine, String ordine) throws ProfessionistaNonTrovatoException {
	    try (EntityManager em = emf.createEntityManager()) {
	        StringBuilder jpql = new StringBuilder(
	            "SELECT DISTINCT u FROM utente u " +
	            "LEFT JOIN u.citta c " +
	            "LEFT JOIN u.professioni p " + 
	            "WHERE u.ruolo = :ruolo "
	        );

	        if (nome != null && !nome.trim().isEmpty()) {
	            jpql.append("AND (LOWER(u.nome) LIKE LOWER(:nome) OR LOWER(u.cognome) LIKE LOWER(:nome) OR LOWER(p.nome) LIKE LOWER(:nome)) ");
	        }
	        if (idCitta != null) {
	            jpql.append("AND c.id = :idCitta ");
	        }
	        if (data != null) {
	            jpql.append("AND EXISTS (SELECT d FROM disponibilita d WHERE d.utente = u AND d.data = :data) ");
	        }
	        if (inizio != null) {
	            jpql.append("AND EXISTS (SELECT d FROM disponibilita d WHERE d.utente = u AND d.da <= :inizio) ");
	        }
	        if (fine != null) {
	            jpql.append("AND EXISTS (SELECT d FROM disponibilita d WHERE d.utente = u AND d.a >= :fine) ");
	        }
	        if ("prezzo".equals(ordine)) {
	            jpql.append("ORDER BY u.tariffa ASC ");
	        }

	        Query query = em.createQuery(jpql.toString());
	        query.setParameter("ruolo", Ruolo.PRO);

	        if (nome != null && !nome.trim().isEmpty()) {
	            query.setParameter("nome", "%" + nome.trim() + "%");
	        }
	        if (idCitta != null) {
	            query.setParameter("idCitta", idCitta);
	        }
	        if (data != null) query.setParameter("data", data);
	        if (inizio != null) query.setParameter("inizio", inizio);
	        if (fine != null) query.setParameter("fine", fine);
	        

	        @SuppressWarnings("unchecked")
	        List<Utente> lista = query.getResultList();

	        for (Utente u : lista) {
	            Hibernate.initialize(u.getProfessioni());
	            Hibernate.initialize(u.getVeicoli());
	        }

	        if (valutazione != null && valutazione > 0) {
	            lista = lista.stream()
	                .filter(u -> u.getRating() >= valutazione)
	                .collect(Collectors.toList());
	        }
	        
	        if ("voto".equals(ordine)) {
	            lista.sort((u1, u2) -> Double.compare(u2.getRating(), u1.getRating()));
	        }

	        if (lista.isEmpty()) throw new ProfessionistaNonTrovatoException("Nessun risultato");
	        
	        return lista;
	    }
	}

	@Override
	public boolean addPro(Utente u, Professione p) throws ProfessioneGiaAssociataException {
		try(EntityManager em = emf.createEntityManager()) {
			EntityTransaction et = em.getTransaction();
			et.begin();
			Utente trovato = em.find(Utente.class, u.getId());
			List<Professione> professioni = trovato.getProfessioni();
			if(professioni.contains(p)) {
				throw new ProfessioneGiaAssociataException("Questo utente è già associato a questa professione");
			}
			professioni.add(p);
			trovato.setProfessioni(professioni);
			et.commit();
			return true;
		}
	}

	@Override
	public boolean removePro(Utente u, Professione p) throws ProfessioneNonAssociataException {
		try(EntityManager em = emf.createEntityManager()) {
			EntityTransaction et = em.getTransaction();
			et.begin();
			Utente trovato = em.find(Utente.class, u.getId());
			List<Professione> professioni = trovato.getProfessioni();
			if(!professioni.contains(p)) {
				throw new ProfessioneNonAssociataException("Questo utente non è associato a questa professione");
			}
			professioni.remove(p);
			trovato.setProfessioni(professioni);
			et.commit();
			return true;
		}
	}

	@Override
	public boolean addVehicle(Utente u, Veicolo v) throws VeicoloGiaAssociatoException {
		try(EntityManager em = emf.createEntityManager()) {
			EntityTransaction et = em.getTransaction();
			et.begin();
			Utente trovato = em.find(Utente.class, u.getId());
			List<Veicolo> veicoli = trovato.getVeicoli();
			if(veicoli.contains(v)) {
				throw new VeicoloGiaAssociatoException("Questo utente è già associato a questo veicolo");
			}
			veicoli.add(v);
			trovato.setVeicoli(veicoli);
			et.commit();
			return true;
		}
	}

	@Override
	public boolean removeVehicle(Utente u, Veicolo v) throws VeicoloNonAssociatoException {
		try(EntityManager em = emf.createEntityManager()) {
			EntityTransaction et = em.getTransaction();
			et.begin();
			Utente trovato = em.find(Utente.class, u.getId());
			List<Veicolo> veicoli = trovato.getVeicoli();
			if(!veicoli.contains(v)) {
				throw new VeicoloNonAssociatoException("Questo utente non è associato a questo veicolo");
			}
			veicoli.remove(v);
			trovato.setVeicoli(veicoli);
			et.commit();
			return true;
		}
	}

	@Override
	public boolean toPro(Long id, String codiceFiscale) throws UtenteNonTrovatoException {
		try(EntityManager em = emf.createEntityManager()) {
			EntityTransaction et = em.getTransaction();
			et.begin();
			Utente trovato = em.find(Utente.class, id);
			if(trovato == null) {
				throw new UtenteNonTrovatoException("Un utente con questo id non è stato trovato nel database");
			}
			trovato.setRuolo(Ruolo.PRO);
			trovato.setCf(codiceFiscale);
			et.commit();
			return true;
		}
	}

	@Override
	public List<Utente> findByNomeCognome(String query) {
	    try (EntityManager em = emf.createEntityManager()) {
	        String pattern = "%" + query.trim().toLowerCase() + "%";
	        
	        Query q = em.createQuery(
	            "SELECT u FROM utente u WHERE u.ruolo = 1 AND (" +
	            "LOWER(u.nome) LIKE :pattern OR " +
	            "LOWER(u.cognome) LIKE :pattern OR " +
	            "LOWER(CONCAT(u.nome, ' ', u.cognome)) LIKE :pattern)"
	        );
	        q.setParameter("pattern", pattern);
	        @SuppressWarnings("unchecked")
			List<Utente> daRitornare = q.getResultList();
	        return daRitornare;
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public List<Professione> getListaProfessioni(Long id) throws UtenteNonTrovatoException {
		try(EntityManager em = emf.createEntityManager()) {
			Utente utente = em.find(Utente.class, id);
			if(utente == null) throw new UtenteNonTrovatoException("utente non trovato per id: "+id);
			
			Hibernate.initialize(utente.getProfessioni());
			return new ArrayList<>(utente.getProfessioni());
		}
	}

}
