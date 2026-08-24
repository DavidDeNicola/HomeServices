package org.elis.homeservices.dao.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import org.elis.homeservices.dao.definition.SegnalazioneDAO;
import org.elis.homeservices.exception.SegnalazioneNonTrovataException;
import org.elis.homeservices.model.Segnalazione;

public class JpaSegnalazioneDAO implements SegnalazioneDAO {

    private EntityManagerFactory emf;

    public JpaSegnalazioneDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean add(Segnalazione s) {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            et.begin();
            em.persist(s);
            et.commit();

            return true;
        }
    }

    @Override
    public boolean remove(Segnalazione s) throws SegnalazioneNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Segnalazione found = em.find(Segnalazione.class, s.getId());

            if (found == null) {
                throw new SegnalazioneNonTrovataException("Segnalazione non trovata");
            }

            et.begin();
            em.remove(found);
            et.commit();

            return true;
        }
    }

    @Override
    public boolean removeById(Long id) throws SegnalazioneNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Segnalazione found = em.find(Segnalazione.class, id);

            if (found == null) {
                throw new SegnalazioneNonTrovataException("Segnalazione non trovata");
            }

            et.begin();
            em.remove(found);
            et.commit();

            return true;
        }
    }

    @Override
    public List<Segnalazione> findByIdSegnalato(Long id) {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Segnalazione> q = em.createQuery(
                "SELECT s FROM segnalazione s WHERE s.utenteSegnalato.id = :id", Segnalazione.class
            );

            q.setParameter("id", id);

            return q.getResultList();
        }
    }

    @Override
    public List<Segnalazione> getListAllSegnalazioni() {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Segnalazione> q = em.createQuery(
                "SELECT s FROM segnalazione s WHERE s.sanzionato = false ORDER BY s.id DESC", Segnalazione.class
            );

            return q.getResultList();
        }
    }

    @Override
    public List<Segnalazione> getListSegnalazioniSanzionate() {

        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Segnalazione> q = em.createQuery(
                "SELECT s FROM segnalazione s WHERE s.sanzionato = true ORDER BY s.id DESC", Segnalazione.class
            );

            return q.getResultList();
        }
    }

    @Override
    public void setSanzionatoTrue(Long id) throws SegnalazioneNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Segnalazione s = em.find(Segnalazione.class, id);

            if (s == null) {
                throw new SegnalazioneNonTrovataException("Segnalazione non trovata");
            }

            et.begin();
            s.setSanzionato(true);
            et.commit();
        }
    }
}