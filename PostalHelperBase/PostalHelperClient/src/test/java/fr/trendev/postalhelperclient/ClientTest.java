/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelperclient;

import fr.trendev.postalhelper.entities.PostalCodeFR;
import java.util.List;
import javax.ws.rs.core.GenericType;
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
public class ClientTest {

    private final static int EXPECTED_COUNT = 39200;

    private final static String TOWN_CODE = "99999";

    public ClientTest() {
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
     * Test of add method, of class Client.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Client client = new Client();

        for (int i = 0; i < 5; i++) {

            PostalCodeFR pc = new PostalCodeFR();
            pc.setCode(TOWN_CODE);
            pc.setTown("TOWN_" + i + "_" + System.currentTimeMillis());
            pc.setGpsCoords("Latitude, Longitude");

            Response response = client.add(pc);
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

    @Test
    public void testConstraintsAdd() {
        System.out.println("add with Constraint Violation");
        Client client = new Client();

        PostalCodeFR pc = new PostalCodeFR(null, "BIKINI BOTTOM");
        pc.setGpsCoords("Latitude, Longitude");

        Response response = client.add(pc);
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

        assert !Status.fromStatusCode(response.
                getStatus()).
                equals(Status.CREATED);
        assert !Status.fromStatusCode(response.getStatus()).
                equals(Status.CONFLICT);
        assert Status.fromStatusCode(response.
                getStatus()).
                equals(Status.EXPECTATION_FAILED);

        System.out.println("==> HTTP Code = " + response.getStatus());
        client.close();
    }

    @Test
    public void testFindFromCode() {
        System.out.println("findFromCode");
        Client client = new Client();

        testFindFromCodeRoutine(client, "77600", Status.OK, 7);
        testFindFromCodeRoutine(client, "12345", Status.NOT_FOUND, 0);
        testFindFromCodeRoutine(client, "12", Status.OK, 327);

        client.close();
    }

    private void testFindFromCodeRoutine(Client client, String code,
            Status expectedStatus,
            int expectedCount) {

        Response response = client.findFromCode(Response.class, code);

        assert Status.fromStatusCode(response.getStatus()).equals(
                expectedStatus);

        List<PostalCodeFR> list = response.readEntity(
                new GenericType<List<PostalCodeFR>>() {
        });
        assert list.size() == expectedCount;
        System.out.println("We've got " + list.size()
                + " postal codes from findFromCode service (searching " + code
                + ")");
    }

    /**
     * Test of count method, of class Client.
     */
    @Test
    public void testCount() {
        System.out.println("count");
        Client client = new Client();

        Response response = client.count(Response.class);
        assert Status.fromStatusCode(response.getStatus()).equals(Status.OK);

        String count = response.readEntity(String.class);

        int val = Integer.parseInt(count);
        assert EXPECTED_COUNT <= val;
        System.out.println("Total Count : " + val);
        client.close();
    }

    /**
     * Test of findFromTown method, of class Client.
     */
    @Test
    public void testFindFromTown() {
        System.out.println("findFromTown");
        Client client = new Client();

        testFindFromTownRoutine(client, "Paris", Status.OK, 31);
        testFindFromTownRoutine(client, "Julien", Status.OK, 107);
        testFindFromTownRoutine(client, "city", Status.NOT_FOUND, 0);
        testFindFromTownRoutine(client, "Bussy", Status.OK, 19);

        client.close();
    }

    private void testFindFromTownRoutine(Client client, String town,
            Status expectedStatus,
            int expectedCount) {

        Response response = client.findFromTown(Response.class, town);

        assert Status.fromStatusCode(response.getStatus()).equals(
                expectedStatus);

        List<PostalCodeFR> list = response.readEntity(
                new GenericType<List<PostalCodeFR>>() {
        });
        assert list.size() == expectedCount;
        System.out.println("We've got " + list.size()
                + " postal codes from findFromTown service (searching word \""
                + town
                + "\")");
    }

    /**
     * Test of findAll method, of class Client.
     */
    @Test
    public void testFindAll() {
        System.out.println("findAll");
        Client client = new Client();

        Response response = client.findAll(Response.class);
        assert Status.fromStatusCode(response.getStatus()).equals(Status.OK);

        List<PostalCodeFR> list = response.readEntity(
                new GenericType<List<PostalCodeFR>>() {
        });
        assert list.size() >= EXPECTED_COUNT;
        System.out.println("We've got " + list.size()
                + " postal codes from findAll service");
        client.close();
    }

    /**
     * Test of close method, of class Client.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        Client client = new Client();
        client.close();
    }

    @Test
    public void crossCheckCount() {
        System.out.println("crossCheckCount");
        Client client = new Client();

        Response response = client.findAll(Response.class);
        assert Status.fromStatusCode(response.getStatus()).equals(Status.OK);

        List<PostalCodeFR> list = response.readEntity(
                new GenericType<List<PostalCodeFR>>() {
        });

        response = client.count(Response.class);
        assert Status.fromStatusCode(response.getStatus()).equals(Status.OK);

        String count = response.readEntity(String.class);

        int val = Integer.parseInt(count);

        assert val == list.size();

        System.out.println("TOTAL Count expected is correct : " + val);

        client.close();
    }

    @Test
    public void testDelete() {
        String code = TOWN_CODE;
        Client client = new Client();

        Response response = client.findFromCode(Response.class, code);

        assert Status.fromStatusCode(response.getStatus()).equals(Status.OK);

        List<PostalCodeFR> list = response.readEntity(
                new GenericType<List<PostalCodeFR>>() {
        });

        System.out.println(list.size() + " Postal Codes to delete");

        response = client.delete(Response.class, code);

        assert Status.fromStatusCode(response.getStatus()).equals(Status.OK);

        List<PostalCodeFR> deletedList = response.readEntity(
                new GenericType<List<PostalCodeFR>>() {
        });

        System.out.println(deletedList.size() + " Postal Codes deleted");

        assert !deletedList.isEmpty();
        assert deletedList.size() == list.size();

        response = client.findFromCode(Response.class, code);

        assert Status.fromStatusCode(response.getStatus()).equals(
                Status.NOT_FOUND);

        list = response.readEntity(
                new GenericType<List<PostalCodeFR>>() {
        });

        assert list.isEmpty();

        System.out.println("Deletion is successful");

        client.close();
    }

}
