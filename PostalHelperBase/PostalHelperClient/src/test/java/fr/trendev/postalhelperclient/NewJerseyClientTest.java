/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelperclient;

import fr.trendev.postalhelper.entities.PostalCodeFR;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class NewJerseyClientTest {

    private final static int EXPECTED_COUNT = 39200;

    public NewJerseyClientTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class NewJerseyClient.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        NewJerseyClient client = new NewJerseyClient();

        for (int i = 0; i < 5; i++) {

            PostalCodeFR pc = new PostalCodeFR();
            pc.setCode("99999");
            pc.setTown("TOWN_" + i + "_" + System.currentTimeMillis());
            pc.setGpsCoords("Latitude, Longitude");

            JsonBuilderFactory factory = Json.createBuilderFactory(null);
            JsonObjectBuilder pcJson = factory.createObjectBuilder()
                    .add("code", pc.getCode())
                    .add("town", pc.getTown())
                    .add("gpsCoords", pc.getGpsCoords());

            Response response = client.add(
                    pcJson.build().toString());
            StringBuilder sb = new StringBuilder();
            sb.append("Status Code = ").append(response.getStatus()).append(
                    " / ");

            if (!response.hasEntity()) {
                sb.append("NO ENTITY");
            } else {
                String msg = response.readEntity(String.class);
                sb.append(msg);
            }

            System.out.println("Response from add service : " + sb.toString());
            assert !Status.fromStatusCode(response.getStatus()).
                    equals(Status.CONFLICT);
        }
        client.close();
    }

    /**
     * Test of findFromCode method, of class NewJerseyClient.
     */
    @Test
    public void testFindFromPartialCode() {
        System.out.println("findFromCode");
        NewJerseyClient client = new NewJerseyClient();
        InputStream input = client.findFromCode(InputStream.class, "12");

        JsonReader reader = Json.createReader(input);

        JsonArray array = reader.readArray();

        assert array.size() == 327;
        System.out.println("We've got " + 327
                + " postal codes from findFromCode service (searching 12XXX)");

        client.close();
    }

    @Test
    public void testFindFromCode() {
        System.out.println("findFromCode");
        NewJerseyClient client = new NewJerseyClient();
        InputStream input = client.findFromCode(InputStream.class, "77600");

        JsonReader reader = Json.createReader(input);

        JsonArray array = reader.readArray();

        assert array.size() == 7;
        System.out.println("We've got " + 7
                + " postal codes from findFromCode service (searching 77600)");

        client.close();
    }

    /**
     * Test of count method, of class NewJerseyClient.
     */
    @Test
    public void testCount() {
        System.out.println("count");
        NewJerseyClient client = new NewJerseyClient();
        int count = Integer.parseInt(client.count());
        assert EXPECTED_COUNT <= count;
        System.out.println("Total Count : " + count);
        client.close();
    }

    /**
     * Test of findFromTown method, of class NewJerseyClient.
     */
    @Test
    public void testFindFromTown() {
        System.out.println("findFromTown");
        NewJerseyClient client = new NewJerseyClient();
        InputStream input = client.findFromTown(InputStream.class, "PARIS");

        JsonReader reader = Json.createReader(input);

        JsonArray array = reader.readArray();

        assert array.size() == 31;
        System.out.println("We've got " + 31
                + " postal codes from findFromTown service (searching PARIS)");

        client.close();
    }

    /**
     * Test of findAll method, of class NewJerseyClient.
     */
    @Test
    public void testFindAll() {
        System.out.println("findAll");
        NewJerseyClient client = new NewJerseyClient();
        InputStream input = client.findAll(InputStream.class);

        JsonReader reader = Json.createReader(input);

        JsonArray array = reader.readArray();

        assert array.size() >= EXPECTED_COUNT;
        System.out.println("We've got " + array.size()
                + " postal codes from findAll service");

        client.close();
    }

    /**
     * Test of close method, of class NewJerseyClient.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        NewJerseyClient client = new NewJerseyClient();
        client.close();
    }

}
