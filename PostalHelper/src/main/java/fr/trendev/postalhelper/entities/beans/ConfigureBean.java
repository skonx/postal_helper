/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.entities.beans;

import fr.trendev.postalhelper.entities.PostalCodeFR;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
public class ConfigureBean {

    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        PostalCodeFR pc1 = new PostalCodeFR();
        pc1.setCode("77860");
        pc1.setTown("QUINCY VOISINS");
        pc1.setGpsCoords("Latitude; Longitude");
        em.persist(pc1);
        System.out.println("pc1 has been persisted");

        PostalCodeFR pc2 = new PostalCodeFR();
        pc2.setCode("77600");
        pc2.setTown("CHANTELOUP EN BRIE");
        em.persist(pc2);
        System.out.println("pc2 has been persisted");
    }

}
