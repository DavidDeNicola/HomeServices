package org.elis.homeservices.dao.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import org.elis.homeservices.dao.definition.DisponibilitaDAO;
import org.elis.homeservices.exception.DisponibilitaNonTrovataException;
import org.elis.homeservices.exception.DisponibilitaTimeOverlapException;
import org.elis.homeservices.model.Disponibilita;

public class JpaDisponibilitaDAO implements DisponibilitaDAO {

    private EntityManagerFactory emf;

    public JpaDisponibilitaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean add(Disponibilita d) throws DisponibilitaTimeOverlapException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Query q = em.createQuery(
                "SELECT disp FROM disponibilita disp " +
                "WHERE disp.utente.id = :uid " +
                "AND disp.data = :data " +
                "AND disp.da < :a " +
                "AND disp.a > :da"
            );

            q.setParameter("uid", d.getUtente().getId());
            q.setParameter("data", d.getData());
            q.setParameter("da", d.getDa());
            q.setParameter("a", d.getA());

            if (!q.getResultList().isEmpty()) {
                throw new DisponibilitaTimeOverlapException("Overlap rilevato");
            }

            et.begin();
            em.persist(d);
            et.commit();

            return true;
        }
    }

    @Override
    public boolean remove(Disponibilita d) throws DisponibilitaNonTrovataException {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction et = em.getTransaction();

            Disponibilita found = em.find(Disponibilita.class, d.getId());

            if (found == null) {
                throw new DisponibilitaNonTrovataException("Disponibilità non trovata");
            }

            et.begin();
            em.remove(found);
            et.commit();

            return true;
        }
    }

    @Override
    public List<Disponibilita> findByIdPro(Long id) {

        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Disponibilita> q = em.createQuery(
                "SELECT d FROM disponibilita d WHERE d.utente.id = :id ORDER BY d.data, d.da", Disponibilita.class
            );

            q.setParameter("id", id);

            return q.getResultList();
        }
    }
}