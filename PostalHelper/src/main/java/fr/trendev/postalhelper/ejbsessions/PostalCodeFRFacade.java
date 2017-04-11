/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.ejbsessions;

import fr.trendev.postalhelper.entities.PostalCodeFR;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    public PostalCodeFR create(String code, String town,
            String gpsCoords) {
        PostalCodeFR pc = new PostalCodeFR();
        pc.setCode(code);
        pc.setTown(town);
        pc.setGpsCoords(gpsCoords);
        return pc;
    }

    public void persist(PostalCodeFR pc) {
        em.persist(pc);
    }
}
