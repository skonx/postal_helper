/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelperclient;

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
    // @Test
    public void testAdd() {
        System.out.println("add");

    }

    /**
     * Test of findFromCode method, of class NewJerseyClient.
     */
    //@Test
    public void testFindFromCode() {
        System.out.println("findFromCode");

    }

    /**
     * Test of count method, of class NewJerseyClient.
     */
    @Test
    public void testCount() {
        System.out.println("count");
        NewJerseyClient client = new NewJerseyClient();
        client.close();
    }

    /**
     * Test of findFromTown method, of class NewJerseyClient.
     */
    //@Test
    public void testFindFromTown() {
        System.out.println("findFromTown");

    }

    /**
     * Test of findAll method, of class NewJerseyClient.
     */
    //@Test
    public void testFindAll() {
        System.out.println("findAll");

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
