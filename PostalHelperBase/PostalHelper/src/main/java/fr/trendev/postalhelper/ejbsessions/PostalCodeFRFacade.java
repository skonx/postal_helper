/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.ejbsessions;

import fr.trendev.postalhelper.entities.PostalCodeFR;
import fr.trendev.postalhelper.entities.PostalCodeFRPK;
import fr.trendev.postalhelper.entities.PostalCodeFR_;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jsie
 */
@Stateless
public class PostalCodeFRFacade {

    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    private static final Logger LOG = Logger.getLogger(PostalCodeFRFacade.class.
            getName());

    /**
     * Creates a PostalCodeFR
     *
     * @param code the postal code, must not be null
     * @param town the name of the town, must not be null
     * @param gpsCoords the GPS Coords, not used at the present time)
     * @return the created PostalCodeFR
     */
    public PostalCodeFR create(String code, String town,
            String gpsCoords) {
        PostalCodeFR pc = new PostalCodeFR();
        pc.setCode(code);
        pc.setTown(town);
        pc.setGpsCoords(gpsCoords);
        return pc;
    }

    /**
     * Persists the provided PostalCodeFR. PostalCodeFR should not be already
     * present in the DB, otherwise an exception is thrown and transaction is
     * roll-backed.
     *
     * @param pc a postal code
     */
    public void create(PostalCodeFR pc) {
        em.persist(pc);
        //em.flush();
    }

    public PostalCodeFR find(PostalCodeFR pc) {
        PostalCodeFRPK key = new PostalCodeFRPK();
        key.setCode(pc.getCode());
        key.setTown(pc.getTown());
        return em.find(PostalCodeFR.class, key);
    }

    public List<PostalCodeFR> findAll() {
        LOG.log(Level.INFO,
                "Providing all postal codes");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PostalCodeFR> cq = cb.createQuery(PostalCodeFR.class);
        Root<PostalCodeFR> root = cq.from(PostalCodeFR.class);
        cq.select(cq.from(PostalCodeFR.class));
        cq.orderBy(cb.asc(root.get(PostalCodeFR_.code)), cb.asc(root.get(
                PostalCodeFR_.town)));
        return em.createQuery(cq).getResultList();
    }

    public String count() {

        CriteriaQuery<Long> cq = em.
                getCriteriaBuilder().createQuery(Long.class);
        cq.select(em.getCriteriaBuilder().count(cq.from(PostalCodeFR.class)));
        TypedQuery<Long> q = em.createQuery(cq);

        Long count = q.getSingleResult();
        LOG.log(Level.INFO,
                "Providing the total count of postal codes : {0} postal codes in the DB",
                count);
        return count.toString();
    }

    public List<PostalCodeFR> findFromTown(String town) {
        LOG.log(Level.INFO,
                "Providing informations from town(s) containing the word [{0}]",
                town);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PostalCodeFR> cq = cb.createQuery(PostalCodeFR.class);
        Root<PostalCodeFR> root = cq.from(PostalCodeFR.class);
        cq.select(root).where(cb.like(root.get(PostalCodeFR_.town), "%" + town
                + "%"));
        cq.orderBy(cb.asc(root.get(PostalCodeFR_.town)), cb.asc(root.get(
                PostalCodeFR_.code)));
        TypedQuery<PostalCodeFR> q = em.createQuery(cq);
        return q.getResultList();
    }

    public List<PostalCodeFR> findFromCode(String code) {
        LOG.log(Level.INFO,
                "Providing informations from code [{0}]",
                code);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PostalCodeFR> cq = cb.createQuery(PostalCodeFR.class);
        Root<PostalCodeFR> root = cq.from(PostalCodeFR.class);
        cq.select(root).where(cb.equal(root.get(PostalCodeFR_.code), code));
        cq.orderBy(cb.asc(root.get(PostalCodeFR_.code)), cb.asc(root.get(
                PostalCodeFR_.town)));
        TypedQuery<PostalCodeFR> q = em.createQuery(cq);
        return q.getResultList();
    }

    public List<PostalCodeFR> findFromPartialCode(String code) {
        LOG.log(Level.INFO,
                "Providing informations from partial code [{0}]",
                code);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PostalCodeFR> cq = cb.createQuery(PostalCodeFR.class);
        Root<PostalCodeFR> root = cq.from(PostalCodeFR.class);
        cq.select(root).where(cb.like(root.get(PostalCodeFR_.code), code + "%"));
        cq.orderBy(cb.asc(root.get(PostalCodeFR_.code)), cb.asc(root.get(
                PostalCodeFR_.town)));
        TypedQuery<PostalCodeFR> q = em.createQuery(cq);
        return q.getResultList();
    }

    public void delete(PostalCodeFR pc) {
        LOG.log(Level.INFO,
                "Deleting postal code [{0};{1}]", new Object[]{pc.getCode(), pc.
                    getTown()}
        );
        em.remove(pc);
        //em.flush();
    }

    public int delete(String code) {
        LOG.log(Level.INFO,
                "Bulk Operation - Deleting postal code [{0}]", code);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<PostalCodeFR> cd = cb.createCriteriaDelete(
                PostalCodeFR.class);
        Root<PostalCodeFR> root = cd.from(PostalCodeFR.class);
        cd.where(cb.like(root.get(PostalCodeFR_.code), code + "%"));
        Query q = em.createQuery(cd);
        return q.executeUpdate();
    }

}
