/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.web.restapi;

import fr.trendev.postalhelper.ejbsessions.PostalCodeFRFacade;
import fr.trendev.postalhelper.entities.PostalCodeFR;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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

    private Response sendNotFoundResponse(String path, String value) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder entity = factory.createObjectBuilder()
                .add(path, value);
        return Response.status(Status.NOT_FOUND)
                .entity(entity.build())
                .build();
    }

    @GET
    @Path("code/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findFromCode(
            @PathParam("code") String code) {

        if (code.length() > 5) {
            LOG.log(Level.WARNING, "The code is too long...");
            return sendNotFoundResponse("code", code);
        }

        List<PostalCodeFR> list;

        if (code.length() == POSTALCODE_LENGTH) {
            list = facade.findFromCode(code);
        } else {
            list = facade.findFromPartialCode(code);
        }

        if (list.isEmpty()) {
            return sendNotFoundResponse("code", code);
        }

        GenericEntity<List<PostalCodeFR>> entity = new GenericEntity<List<PostalCodeFR>>(
                list) {
        };
        return Response.status(Status.OK)
                .entity(entity)
                .build();
    }

    @GET
    @Path("town/{town}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findFromTown(
            @PathParam("town") String town) {

        List<PostalCodeFR> list = facade.findFromTown(town);
        if (list.isEmpty()) {
            return sendNotFoundResponse("town", town);
        }

        GenericEntity<List<PostalCodeFR>> entity = new GenericEntity<List<PostalCodeFR>>(
                list) {
        };
        return Response.status(Status.OK)
                .entity(entity)
                .build();
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

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public void add(PostalCodeFR entity) {
        facade.persist(entity);
    }
}
