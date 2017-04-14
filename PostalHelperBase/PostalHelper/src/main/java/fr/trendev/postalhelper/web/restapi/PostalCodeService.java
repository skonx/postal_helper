/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.web.restapi;

import fr.trendev.postalhelper.ejbsessions.PostalCodeFRFacade;
import fr.trendev.postalhelper.entities.PostalCodeFR;
import fr.trendev.postalhelper.exceptions.utils.ExceptionHelper;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

    private Response sendNotFoundResponse() {

        return Response.status(Status.NOT_FOUND)
                .entity(new GenericEntity<List<PostalCodeFR>>(
                        Collections.emptyList()) {
                })
                .build();
    }

    @GET
    @Path("code/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findFromCode(
            @PathParam("code") String code) {

        if (code.length() > 5) {
            LOG.log(Level.WARNING, "The code is too long...");
            return sendNotFoundResponse();
        }
        try {
            List<PostalCodeFR> list;

            if (code.length() == POSTALCODE_LENGTH) {
                list = facade.findFromCode(code);
            } else {
                list = facade.findFromPartialCode(code);
            }

            if (list.isEmpty()) {
                LOG.log(Level.WARNING, "The code {0} is unknown", code);
                return sendNotFoundResponse();
            }

            return Response.status(Status.OK)
                    .entity(new GenericEntity<List<PostalCodeFR>>(
                            list) {
                    })
                    .build();
        } catch (Exception ex) {
            LOG.
                    log(Level.WARNING,
                            "Exception occurs searching postal codes with code {0}...",
                            code);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
                    ExceptionHelper.
                            findRootCauseException(ex).getClass().
                            toString()).
                    build();
        }
    }

    @GET
    @Path("town/{town}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findFromTown(
            @PathParam("town") String town) {

        try {
            List<PostalCodeFR> list = facade.findFromTown(town);
            if (list.isEmpty()) {
                return sendNotFoundResponse();
            }

            return Response.status(Status.OK)
                    .entity(new GenericEntity<List<PostalCodeFR>>(
                            list) {
                    })
                    .build();
        } catch (Exception ex) {
            LOG.
                    log(Level.WARNING,
                            "Exception occurs searching postal codes for town {0}...",
                            town);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
                    ExceptionHelper.
                            findRootCauseException(ex).getClass().
                            toString()).
                    build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {

        try {
            List<PostalCodeFR> list = facade.findAll();

            return Response.status(Status.OK).entity(
                    new GenericEntity<List<PostalCodeFR>>(
                            list) {
            }).build();
        } catch (Exception ex) {
            LOG.
                    log(Level.WARNING,
                            "Exception occurs providing the entire postal code list...");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
                    ExceptionHelper.
                            findRootCauseException(ex).getClass().
                            toString()).
                    build();
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        try {
            return Response.ok(String.valueOf(facade.count())).build();
        } catch (Exception ex) {
            LOG.
                    log(Level.WARNING,
                            "Exception occurs counting the entire postal code list...");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
                    ExceptionHelper.
                            findRootCauseException(ex).getClass().
                            toString()).
                    build();
        }
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response add(PostalCodeFR entity) {
        LOG.log(Level.INFO, "Adding a postal code : {0} / {1}", new Object[]{
            entity.getCode(), entity.getTown()});
        try {
            if (facade.find(entity) == null) {
                facade.persist(entity);
                return Response.status(Status.CREATED).entity(entity.getCode()
                        + ";"
                        + entity.getTown()).build();
            }
        } catch (Exception ex) {
            LOG.
                    log(Level.WARNING,
                            "Exception occurs adding a postal code : {0} / {1}",
                            new Object[]{
                                entity.getCode(), entity.getTown()});
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
                    ExceptionHelper.
                            findRootCauseException(ex).getClass().
                            toString()).
                    build();
        }
        LOG.log(Level.WARNING, "Postal code {0} / {1} already present",
                new Object[]{
                    entity.getCode(), entity.getTown()});
        return Response.status(Status.CONFLICT).
                build();
    }

    @DELETE
    @Path("del/{code}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("code") String code) {
        LOG.log(Level.INFO, "Deleting Postal Codes from code {0}", code);
        try {
            List<PostalCodeFR> list;

            if (code.length() == POSTALCODE_LENGTH) {
                list = facade.findFromCode(code);
            } else {
                list = facade.findFromPartialCode(code);
            }

            if (list.isEmpty()) {
                LOG.log(Level.WARNING, "The code {0} is unknown", code);
                return Response.status(Status.NOT_FOUND).entity(
                        new GenericEntity<List<PostalCodeFR>>(Collections.
                                emptyList()) {
                }).build();

                /*new GenericEntity<Boolean>(false) {}
                "false"
                 */
            }
            LOG.log(Level.INFO, "{0} Postal Codes to delete", list.size());
            list.stream().forEach(pc -> facade.delete(pc));
            return Response.status(Status.OK)
                    .entity(new GenericEntity<List<PostalCodeFR>>(list) {
                    }).build();
        } catch (Exception ex) {
            LOG.
                    log(Level.WARNING,
                            "Exception occurs deleting postal codes with code {0}...",
                            code);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
                    new GenericEntity<List<PostalCodeFR>>(Collections.
                            emptyList()) {
            }).
                    build();
        }
    }
}
