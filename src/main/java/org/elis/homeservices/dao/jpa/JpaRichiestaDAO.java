package org.elis.homeservices.dao.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import org.elis.homeservices.dao.definition.RichiestaDAO;
import org.elis.homeservices.exception.RichiestaNonTrovataException;
import org.elis.homeservices.exception.RichiestaTimeOverlapException;
import org.elis.homeservices.exception.StatoNonAggiornabileException;
import org.elis.homeservices.model.Richiesta;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.enums.Stato;

public class JpaRichiestaDAO implements RichiestaDAO {

    private EntityManagerFactory emf;

    public JpaRichiestaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean add(Richiesta r) throws RichiestaTimeOverlapException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Query q = em.createQuery(
                "SELECT r FROM richiesta r " +
                "WHERE r.utenteRiceve.id = :uid " +
                "AND r.data = :data " +
                "AND r.da < :a " +
                "AND r.a > :da"
            );

            q.setParameter("uid", r.getUtenteRiceve().getId());
            q.setParameter("data", r.getData());
            q.setParameter("da", r.getDa());
            q.setParameter("a", r.getA());

            if (!q.getResultList().isEmpty()) {
                throw new RichiestaTimeOverlapException("Richiesta sovrapposta nel tempo");
            }

            et.begin();
            em.persist(r);
            et.commit();

            return true;
        }
    }

    @Override
    public boolean remove(Long id) throws RichiestaNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Richiesta r = em.find(Richiesta.class, id);

            if (r == null) {
                throw new RichiestaNonTrovataException("Richiesta non trovata");
            }

            et.begin();
            em.remove(r);
            et.commit();

            return true;
        }
    }

    @Override
    public List<Richiesta> findByUtenteRiceve(Utente u) {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Richiesta> q = em.createQuery(
                "SELECT r FROM richiesta r WHERE r.utenteRiceve.id = :uid ORDER BY r.data DESC", Richiesta.class
            );

            q.setParameter("uid", u.getId());

            return q.getResultList();
        }
    }

    @Override
    public List<Richiesta> findByUtenteRichiede(Utente u) {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Richiesta> q = em.createQuery(
                "SELECT r FROM richiesta r WHERE r.utenteRichiede.id = :uid ORDER BY r.data DESC", Richiesta.class
            );

            q.setParameter("uid", u.getId());

            return q.getResultList();
        }
    }

    @Override
    public void setIdRecensione(Long idRecensione, Long idRichiesta)
            throws RichiestaNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Richiesta r = em.find(Richiesta.class, idRichiesta);

            if (r == null) {
                throw new RichiestaNonTrovataException("Richiesta non trovata");
            }

            et.begin();

            Query q = em.createQuery(
                "UPDATE richiesta r SET r.recensione.id = :rid WHERE r.id = :id"
            );

            q.setParameter("rid", idRecensione);
            q.setParameter("id", idRichiesta);

            q.executeUpdate();

            et.commit();
        }
    }

    @Override
    public Long getIdRecensioneById(Long idRichiesta)
            throws RichiestaNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Long> q = em.createQuery(
                "SELECT r.recensione.id FROM richiesta r WHERE r.id = :id", Long.class
            );

            q.setParameter("id", idRichiesta);

            List<Long> res = q.getResultList();

            if (res.isEmpty()) {
                throw new RichiestaNonTrovataException("Richiesta non trovata");
            }

            return res.get(0);
        }
    }

    @Override
    public Richiesta getById(Long id) throws RichiestaNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {

            Richiesta r = em.find(Richiesta.class, id);

            if (r == null) {
                throw new RichiestaNonTrovataException("Richiesta non trovata");
            }

            return r;
        }
    }

    @Override
    public void updateStato(Long id)
            throws RichiestaNonTrovataException, StatoNonAggiornabileException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Richiesta r = em.find(Richiesta.class, id);

            if (r == null) {
                throw new RichiestaNonTrovataException("Richiesta non trovata");
            }

            Stato attuale = r.getStato();

            Stato nuovo;

            switch (attuale) {

                case IN_ATTESA:
                    nuovo = Stato.IN_CORSO;
                    break;

                case IN_CORSO:
                    nuovo = Stato.COMPLETATO;
                    break;

                case COMPLETATO:
                default:
                    throw new StatoNonAggiornabileException(
                        "La richiesta è già COMPLETATA e non può essere modificata"
                    );
            }

            et.begin();
            r.setStato(nuovo);
            et.commit();
        }
    }
}