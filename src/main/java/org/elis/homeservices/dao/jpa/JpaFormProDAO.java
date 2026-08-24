package org.elis.homeservices.dao.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import org.elis.homeservices.dao.definition.FormProDAO;
import org.elis.homeservices.exception.FormProNonTrovatoException;
import org.elis.homeservices.exception.UtenteGiaPresenteException;
import org.elis.homeservices.model.FormPro;
import org.elis.homeservices.model.Utente;

public class JpaFormProDAO implements FormProDAO {

    private EntityManagerFactory emf;

    public JpaFormProDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean add(FormPro fp) throws UtenteGiaPresenteException {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Query q1 = em.createQuery("SELECT f FROM formpro f WHERE f.cf = :cf");
            q1.setParameter("cf", fp.getCf());

            Query q2 = em.createQuery("SELECT f FROM formpro f WHERE f.utente.id = :uid");
            q2.setParameter("uid", fp.getUtente().getId());

            if (!q1.getResultList().isEmpty()) {
                throw new UtenteGiaPresenteException("CF già presente");
            }

            if (!q2.getResultList().isEmpty()) {
                throw new UtenteGiaPresenteException("Utente già associato a un FormPro");
            }

            et.begin();
            em.persist(fp);
            Utente utente = em.find(Utente.class, fp.getUtente().getId());
            utente.setFormPro(fp);
            et.commit();

            return true;

        } catch (PersistenceException e) {
            throw new UtenteGiaPresenteException("Errore inserimento FormPro (vincoli violati)");
        }
    }

    @Override
    public boolean remove(Long id) throws FormProNonTrovatoException {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            FormPro fp = em.find(FormPro.class, id);

            if (fp == null) {
                throw new FormProNonTrovatoException("FormPro non trovato");
            }

            et.begin();

           
            if (fp.getUtente() != null) {
                Utente utente = em.find(Utente.class, fp.getUtente().getId());
                if (utente != null) {
                    utente.setFormPro(null);
                    em.flush();
                }
            }

            em.remove(fp);
            et.commit();

            return true;
        }
    }

    @Override
    public List<FormPro> getForms() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<FormPro> q = em.createQuery("SELECT f FROM formpro f", FormPro.class);
            return q.getResultList();
        }
    }

    @Override
    public FormPro getById(Long id) throws FormProNonTrovatoException {
        try (EntityManager em = emf.createEntityManager()) {
            FormPro fp = em.find(FormPro.class, id);

            if (fp == null) {
                throw new FormProNonTrovatoException("FormPro non trovato");
            }

            return fp;
        }
    }
}