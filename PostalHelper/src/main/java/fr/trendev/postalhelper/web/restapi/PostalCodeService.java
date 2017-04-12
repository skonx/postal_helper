/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.web.restapi;

import fr.trendev.postalhelper.ejbsessions.PostalCodeFRFacade;
import fr.trendev.postalhelper.entities.PostalCodeFR;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jsie
 */
@Stateless
@Path("fr")
public class PostalCodeService {

    @EJB
    private PostalCodeFRFacade facade;

    private final static int POSTALCODE_LENGTH = 5;

    private static final Logger LOG = Logger.getLogger(PostalCodeService.class.
            getName());

    @GET
    @Path("c/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PostalCodeFR> findFromCode(
            @PathParam("code") String code) {

        if (code.length() == POSTALCODE_LENGTH) {
            return facade.findFromCode(code);
        } else {
            return facade.findFromPartialCode(code);
        }
    }

    @GET
    @Path("t/{town}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PostalCodeFR> findFromTown(
            @PathParam("town") String town) {
        return facade.findFromTown(town);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PostalCodeFR> findAll() {
        return facade.findAll();
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(facade.count());
    }
}
