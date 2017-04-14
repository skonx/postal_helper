/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelperclient;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:PostalCodeService [fr]<br>
 * USAGE:
 * <pre>
 *        NewJerseyClient client = new NewJerseyClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author jsie
 */
public class NewJerseyClient {

    private javax.ws.rs.client.WebTarget webTarget;
    private javax.ws.rs.client.Client client;
    private static final String BASE_URI = "http://localhost:8080/PostalHelper/restapi";

    public NewJerseyClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("fr");
    }

    public Response add(Object requestEntity) throws
            javax.ws.rs.ClientErrorException {
        return webTarget.path("add").
                request(javax.ws.rs.core.MediaType.TEXT_PLAIN).
                post(javax.ws.rs.client.Entity.entity(requestEntity,
                        javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public <T> T findFromCode(Class<T> responseType, String code) throws
            javax.ws.rs.ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("code/{0}",
                new Object[]{code}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).
                get(responseType);
    }

    public String count() throws javax.ws.rs.ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).
                get(String.class);
    }

    public <T> T findFromTown(Class<T> responseType, String town) throws
            javax.ws.rs.ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("town/{0}",
                new Object[]{town}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).
                get(responseType);
    }

    public <T> T findAll(Class<T> responseType) throws
            javax.ws.rs.ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).
                get(responseType);
    }

    public void close() {
        client.close();
    }

}
