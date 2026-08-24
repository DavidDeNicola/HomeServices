package org.elis.homeservices.dao.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import org.elis.homeservices.dao.definition.ProfessioneDAO;
import org.elis.homeservices.exception.ProfessioneGiaNelDatabaseException;
import org.elis.homeservices.exception.ProfessioneNonTrovataException;
import org.elis.homeservices.model.Professione;

public class JpaProfessioneDAO implements ProfessioneDAO {

    private EntityManagerFactory emf;

    public JpaProfessioneDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean add(Professione p) throws ProfessioneGiaNelDatabaseException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();
           
            Query q = em.createQuery(
                "SELECT pr FROM professione pr WHERE pr.nome = :nome"
            );
            q.setParameter("nome", p.getNome());

            if (!q.getResultList().isEmpty()) {
                throw new ProfessioneGiaNelDatabaseException("Professione già presente");
            }

            et.begin();
            em.persist(p);
            et.commit();

            return true;

        } catch (PersistenceException e) {
            throw new ProfessioneGiaNelDatabaseException("Errore inserimento professione (vincoli DB)");
        }
    }

    @Override
    public boolean remove(Professione p) throws ProfessioneNonTrovataException {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Professione found = em.find(Professione.class, p.getId());

            if (found == null) {
                throw new ProfessioneNonTrovataException("Professione non trovata");
            }

            et.begin();

            em.createNativeQuery("DELETE FROM utente_professione WHERE id_professione = :id")
              .setParameter("id", found.getId())
              .executeUpdate();

            em.createNativeQuery("UPDATE richiesta SET professione_id = NULL WHERE professione_id = :id")
              .setParameter("id", found.getId())
              .executeUpdate();

            em.remove(found);
            
            et.commit();
            return true;
        }
    }

    @Override
    public Professione findById(Long id) throws ProfessioneNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {

            Professione p = em.find(Professione.class, id);

            if (p == null) {
                throw new ProfessioneNonTrovataException("Professione non trovata");
            }

            return p;
        }
    }

    @Override
    public List<Professione> getListProfessioni() {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Professione> q = em.createQuery("SELECT p FROM professione p ORDER BY p.nome", Professione.class);

            return q.getResultList();
        }
    }
}