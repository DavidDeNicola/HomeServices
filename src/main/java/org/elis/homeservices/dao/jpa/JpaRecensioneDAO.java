package org.elis.homeservices.dao.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import org.elis.homeservices.dao.definition.RecensioneDAO;
import org.elis.homeservices.exception.RecensioneNonTrovataException;
import org.elis.homeservices.model.Recensione;
import org.elis.homeservices.model.Utente;

public class JpaRecensioneDAO implements RecensioneDAO {

    private EntityManagerFactory emf;

    public JpaRecensioneDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean add(Recensione r) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();
            et.begin();
      
            em.persist(r);
            
        
            em.createQuery(
                "UPDATE utente u SET u.rating = " +
                "(SELECT AVG(CAST(rec.voto AS double)) FROM recensione rec WHERE rec.utenteRiceve.id = :uid) " +
                "WHERE u.id = :uid"
            ).setParameter("uid", r.getUtenteRiceve().getId())
             .executeUpdate();
            
            et.commit();
            return true;
        }
    }

    @Override
    public boolean remove(Recensione r) throws RecensioneNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Recensione found = em.find(Recensione.class, r.getId());

            if (found == null) {
                throw new RecensioneNonTrovataException("Recensione non trovata");
            }

            et.begin();
            em.remove(found);
            et.commit();

            return true;
        }
    }

    @Override
    public List<Recensione> findByUtenteRiceve(Utente u) {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Recensione> q = em.createQuery(
                "SELECT r FROM recensione r WHERE r.utenteRiceve.id = :uid ORDER BY r.data DESC", Recensione.class
            );

            q.setParameter("uid", u.getId());

            return q.getResultList();
        }
    }

    @Override
    public Long findIdUltimaRecensione() {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Long> q = em.createQuery(
                "SELECT r.id FROM recensione r ORDER BY r.id DESC", Long.class
            );

            q.setMaxResults(1);

            List<Long> result = q.getResultList();

            if (result.isEmpty()) {
                return null;
            }

            return result.get(0);
        }
    }

    @Override
    public Recensione findById(Long id) throws RecensioneNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {

            Recensione r = em.find(Recensione.class, id);

            if (r == null) {
                throw new RecensioneNonTrovataException("Recensione non trovata");
            }

            return r;
        }
    }

    @Override
    public Long getIdUtenteScriveById(Long id) throws RecensioneNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {

            Query q = em.createQuery(
                "SELECT r.utenteScrive.id FROM recensione r WHERE r.id = :id"
            );

            q.setParameter("id", id);

            try {
                return (Long) q.getSingleResult();
            } catch (NoResultException e) {
                throw new RecensioneNonTrovataException("Recensione non trovata");
            }
        }
    }
}