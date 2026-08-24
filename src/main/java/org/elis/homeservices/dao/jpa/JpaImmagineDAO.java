package org.elis.homeservices.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;

import org.elis.homeservices.dao.definition.ImmagineDAO;
import org.elis.homeservices.exception.ImmagineGiaNelDatabaseException;
import org.elis.homeservices.exception.ImmagineNonTrovataException;
import org.elis.homeservices.model.Immagine;

public class JpaImmagineDAO implements ImmagineDAO {

    private EntityManagerFactory emf;

    public JpaImmagineDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean add(Immagine i) throws ImmagineGiaNelDatabaseException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();
            Query q = em.createQuery(
                "SELECT img FROM immagine img WHERE img.percorso = :percorso"
            );
            q.setParameter("percorso", i.getPercorso());

            if (!q.getResultList().isEmpty()) {
                throw new ImmagineGiaNelDatabaseException("Immagine con questo percorso già presente");
            }

            et.begin();
            em.persist(i);
            et.commit();

            return true;

        } catch (PersistenceException e) {
            throw new ImmagineGiaNelDatabaseException("Errore inserimento immagine (vincoli DB)");
        }
    }

    @Override
    public boolean remove(Immagine i) throws ImmagineNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Immagine found = em.find(Immagine.class, i.getId());

            if (found == null) {
                throw new ImmagineNonTrovataException("Immagine non trovata");
            }

            et.begin();
            em.remove(found);
            et.commit();

            return true;
        }
    }
}