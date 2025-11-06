package edu.upc.dsa;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import edu.upc.dsa.models.Equipaje;
import edu.upc.dsa.models.Vuelo;

import java.util.List;

public class VuelosManagerTest {
    VuelosManagerImpl manager;

    @Before
    public void setUp() {
        manager = VuelosManagerImpl.getInstance();

        manager.addAvion("AAA1", "Vueling", "Boeing 737");
        manager.add();

        manager.addVuelo("VUELO1", "10:00", "12:00",manager.getAvion("AAA1"), "Barcelona", "Madrid");
        manager.add();

    }

    @After
    public void tearDown() {
        this.manager = null;
    }

    @Test
    public void testAddAvion() {

    }

    @Test
    public void addVuelo() {
    }

}
