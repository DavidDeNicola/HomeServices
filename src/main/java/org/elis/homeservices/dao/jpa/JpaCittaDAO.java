package org.elis.homeservices.dao.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import org.elis.homeservices.dao.definition.CittaDAO;
import org.elis.homeservices.exception.CittaGiaNelDatabaseException;
import org.elis.homeservices.exception.CittaNonTrovataException;
import org.elis.homeservices.model.Citta;

public class JpaCittaDAO implements CittaDAO {

    private EntityManagerFactory emf;

    public JpaCittaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Citta findCityByName(String name) throws CittaNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Citta> query = em.createQuery(
                "SELECT c FROM citta c WHERE c.nome = :name",
                Citta.class
            );

            query.setParameter("name", name);

            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                throw new CittaNonTrovataException("Città non trovata: " + name);
            }
        }
    }

    @Override
    public Long findIdByName(String name) throws CittaNonTrovataException {
        return findCityByName(name).getId();
    }

    @Override
    public boolean addCity(String name) throws CittaGiaNelDatabaseException {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Citta> query = em.createQuery(
                "SELECT c FROM citta c WHERE c.nome = :name",
                Citta.class
            );

            query.setParameter("name", name);

            if (!query.getResultList().isEmpty()) {
                throw new CittaGiaNelDatabaseException("Città già presente: " + name);
            }

            Citta citta = new Citta();
            citta.setNome(name);

            em.getTransaction().begin();
            em.persist(citta);
            em.getTransaction().commit();

            return true;
        }
    }

    @Override
    public boolean removeCityById(Long id) throws CittaNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {
        	
        	
            Citta citta = em.find(Citta.class, id);

            if (citta == null) {
                throw new CittaNonTrovataException("Città non trovata con id: " + id);
            }

            em.getTransaction().begin();
            em.remove(citta);
            em.getTransaction().commit();

            return true;
        }
    }

    @Override
    public List<Citta> getListCitta() {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Citta> query = em.createQuery(
                "SELECT c FROM citta c ORDER BY c.nome",
                Citta.class
            );

            return query.getResultList();
        }
    }

    @Override
    public Citta getCityById(Long id) throws CittaNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {

            Citta citta = em.find(Citta.class, id);

            if (citta == null) {
                throw new CittaNonTrovataException("Città non trovata con id: " + id);
            }

            return citta;
        }
    }

	@Override
	public boolean editCityNameById(Long id, String name) throws CittaNonTrovataException, CittaGiaNelDatabaseException{
	    
	    
	    
	    System.out.println("DEBUG: Metodo chiamato con ID: " + id);
	    
	    if (this.emf == null) {
	        System.err.println("ERRORE: emf è NULL! Controlla l'inizializzazione.");
	        return false;
	    }

	    EntityManager em = null;
	    
	    try {
	        em = emf.createEntityManager();
	        
	        EntityTransaction tx = em.getTransaction();
	        
	        
	        Citta citta = em.find(Citta.class, id);
	        
	        
	        
	        TypedQuery<Long> query = em.createQuery(
	                "SELECT COUNT(c) FROM citta c WHERE c.nome=:name and c.id != :id",
	                Long.class
	            );	
	        
	        query.setParameter("name", name);
	        query.setParameter("id", id);
	        
	        Long count = query.getSingleResult();
	        if (count > 0) {
	            System.out.println("DEBUG: Esiste già un'altra città chiamata " + name);
	            throw new CittaGiaNelDatabaseException("Impossibile rinominare: il nome '" + name + "' è già utilizzato.");
	        }
	        
	        tx.begin();
	        
	        if (citta == null) {
	            throw new CittaNonTrovataException("Città non trovata con id: " + id);
	        }
	        
	        citta.setNome(name);
	        
	        tx.commit(); 
	        return true;
	        
	    } catch (CittaGiaNelDatabaseException | CittaNonTrovataException e) {
	        if (em.getTransaction().isActive()) em.getTransaction().rollback();
	        throw e; 
	    } catch (Exception e) {
	        if (em.getTransaction() != null && em.getTransaction().isActive()) em.getTransaction().rollback();
	        e.printStackTrace(); 
	        throw e;
	    } finally {
	        if (em != null && em.isOpen()) {
	            em.close();
	        }
	    }
	}
}