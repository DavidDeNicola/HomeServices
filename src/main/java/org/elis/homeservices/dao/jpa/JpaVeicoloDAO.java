package org.elis.homeservices.dao.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import org.elis.homeservices.dao.definition.VeicoloDAO;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.exception.VeicoloGiaNelDatabaseException;
import org.elis.homeservices.exception.VeicoloNonTrovatoException;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.Veicolo;

public class JpaVeicoloDAO implements VeicoloDAO {

    private EntityManagerFactory emf;

    public JpaVeicoloDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean add(Veicolo v) throws VeicoloGiaNelDatabaseException {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Veicolo> q = em.createQuery(
                "SELECT v FROM veicolo v WHERE v.nome = :nome",
                Veicolo.class
            );
            q.setParameter("nome", v.getNome());

            if (!q.getResultList().isEmpty()) {
                throw new VeicoloGiaNelDatabaseException("Veicolo già presente");
            }

            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(v);
            et.commit();

            return true;
        }
    }

    @Override
    public boolean remove(Veicolo v) throws VeicoloNonTrovatoException {

        try (EntityManager em = emf.createEntityManager()) {

            Veicolo found = em.find(Veicolo.class, v.getId());

            if (found == null) {
                throw new VeicoloNonTrovatoException("Veicolo non trovato");
            }

            EntityTransaction et = em.getTransaction();
            et.begin();
            em.remove(found);
            et.commit();

            return true;
        }
    }

    @Override
    public List<Veicolo> getListaVeicoli() {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Veicolo> q = em.createQuery(
                "SELECT v FROM veicolo v ORDER BY v.nome",
                Veicolo.class
            );

            return q.getResultList();
        }
    }

    @Override
    public List<Veicolo> getListaVeicoliUtente(Long idUtente)
            throws UtenteNonTrovatoException {

        try (EntityManager em = emf.createEntityManager()) {

            Utente u = em.find(Utente.class, idUtente);

            if (u == null) {
                throw new UtenteNonTrovatoException("Utente non trovato");
            }

            TypedQuery<Veicolo> q = em.createQuery(
                "SELECT v FROM veicolo v JOIN v.utenti u WHERE u.id = :id", Veicolo.class
            );

            q.setParameter("id", idUtente);

            return q.getResultList();
        }
    }

    @Override
    public Veicolo findById(Long id) {

        try (EntityManager em = emf.createEntityManager()) {

            return em.find(Veicolo.class, id);
        }
    }
}