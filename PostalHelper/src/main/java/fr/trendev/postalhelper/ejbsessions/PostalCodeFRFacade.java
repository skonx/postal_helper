/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.ejbsessions;

import fr.trendev.postalhelper.entities.PostalCodeFR;
import fr.trendev.postalhelper.entities.PostalCodeFR_;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
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
     * present in the DB.
     *
     * @param pc a postal code
     */
    public void persist(PostalCodeFR pc) {
        em.persist(pc);
    }

    public List<PostalCodeFR> findAll() {
        LOG.log(Level.INFO,
                "Providing all postal codes");
        CriteriaQuery cq = em.
                getCriteriaBuilder().createQuery();
        cq.select(cq.from(PostalCodeFR.class));
        return em.createQuery(cq).getResultList();
    }

    public String count() {

        CriteriaQuery cq = em.
                getCriteriaBuilder().createQuery();
        cq.select(em.getCriteriaBuilder().count(cq.from(PostalCodeFR.class)));
        Query q = em.createQuery(cq);

        Long count = ((Long) q.getSingleResult());
        LOG.log(Level.INFO,
                "Providing the total count of postal codes : {0} postal codes in the DB",
                count);
        return count.toString();
    }

    public List<PostalCodeFR> findFromTown(String town) {
        LOG.log(Level.INFO,
                "Providing informations from town(s) containing the word [{0}]",
                town);
        return null;
    }

    public List<PostalCodeFR> findFromCode(String code) {
        LOG.log(Level.INFO,
                "Providing informations from code [{0}]",
                code);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PostalCodeFR> cq = cb.createQuery(PostalCodeFR.class);
        Root<PostalCodeFR> root = cq.from(PostalCodeFR.class);
        cq.select(root).where(cb.equal(root.get(PostalCodeFR_.code), code));
        Query q = em.createQuery(cq);
        return q.getResultList();
    }

    public List<PostalCodeFR> findFromPartialCode(String code) {
        LOG.log(Level.INFO,
                "Providing informations from partial code [{0}]",
                code);
        return null;
    }

}
