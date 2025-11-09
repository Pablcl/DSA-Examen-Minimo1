// java
package edu.upc.dsa;

import edu.upc.dsa.exceptions.LibroNoEncontradoException;
import edu.upc.dsa.exceptions.LectorNoEncontradoException;
import edu.upc.dsa.exceptions.NoHayLibrosException;
import edu.upc.dsa.exceptions.SinEjemplaresException;
import edu.upc.dsa.models.Llibre;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Prestec;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LlibreManagerTest {
    LlibreManagerImpl lm;

    @Before
    public void setUp() throws Exception {
        lm = LlibreManagerImpl.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        lm.reset(); // Reiniciamos el estado del Singleton después de cada test
    }

    @Test
    public void testAfegirLectorNouIActualizar() {
        Lector lector = new Lector("lector1", "Pablo", "Casado", "63482641G", "12/08/2005", "Barcelona", "C/Viladomat 112");
        lm.afegirLector(lector);
        Assert.assertEquals(1, lm.sizeLectors());
        Lector found = lm.getLector("lector1");
        Assert.assertNotNull(found);
        Assert.assertEquals("Pablo", found.getNom());

        // Actualizar
        Lector updated = new Lector("lector1", "Pau", "Casado", "63482641G", "12/08/2005", "Barcelona", "C/Viladomat 112");
        lm.afegirLector(updated);
        Assert.assertEquals(1, lm.sizeLectors());
        Lector found2 = lm.getLector("lector1");
        Assert.assertEquals("Pau", found2.getNom());
    }

    @Test
    public void testEmmagatzemarICatalogar() {
        Llibre llibre = new Llibre("llibre1", "isbn-1234", "Titol", "Editorial", 2020, 1, "Autor", "Tematica");
        lm.emmagatzemarLlibre(llibre);
        lm.catalogarLlibre(); // catalogar primer ejemplar
        Assert.assertEquals(1, lm.sizeCatalogats());
        Llibre cat = lm.getLlibreByISBN("isbn-1234");
        Assert.assertNotNull(cat);
        Assert.assertEquals(1, cat.getQuantitat());

        // almacenar y catalogar otro del mismo ISBN -> incrementa quantitat
        Llibre llibre2 = new Llibre("llibre2", "isbn-1234", "Titol 2", "Editorial", 2021, 1, "Autor", "Tematica");
        lm.emmagatzemarLlibre(llibre2);
        lm.catalogarLlibre();
        Llibre cat2 = lm.getLlibreByISBN("isbn-1234");
        Assert.assertEquals(2, cat2.getQuantitat());
    }

    @Test(expected = NoHayLibrosException.class)
    public void testCatalogarSenseLlibres() {
        // Verificamos que si no hay libros para catalogar, se lanza la excepción correcta
        Assert.assertEquals(0, lm.sizeCatalogats());
        lm.catalogarLlibre();
    }

    @Test
    public void testPrestarLlibreExitós() {
        // preparar lector i llibre catalogat
        Lector lector = new Lector("lector1", "Pablo", "Casado", "63482641G", "12/08/2005", "Barcelona", "C/Viladomat 112");
        lm.afegirLector(lector);

        Llibre llibre = new Llibre("llibre1", "isbn-5678", "Titol", "Editorial", 2019, 1, "Autor", "Tematica");
        lm.emmagatzemarLlibre(llibre);
        lm.catalogarLlibre(); // now quantitat = 1

        Prestec prestec = new Prestec("prestec1", "lector1", "isbn-5678", "01/01/2025", "15/01/2025");
        lm.prestarLlibre(prestec);

        Assert.assertEquals(1, lm.sizePrestecs());
        Llibre cat = lm.getLlibreByISBN("isbn-5678");
        Assert.assertEquals(0, cat.getQuantitat());
        Assert.assertEquals("En tràmit", prestec.getEstat());
    }

    @Test(expected = LibroNoEncontradoException.class)
    public void testPrestarLibroNoEncontrado() {
        Lector lector = new Lector("lector1", "Pablo", "Casado", "63482641G", "12/08/2005", "Barcelona", "C/Viladomat 112");
        lm.afegirLector(lector);

        Prestec prestec = new Prestec("p1", "lector1", "isbn-no-existe", "01/01/2025", "15/01/2025");
        lm.prestarLlibre(prestec);
    }

    @Test(expected = LectorNoEncontradoException.class)
    public void testPrestarLectorNoEncontrado() {
        Llibre llibre = new Llibre("llibre1", "isbn-9999", "Titol", "Editorial", 2018, 1, "Autor", "Tematica");
        lm.emmagatzemarLlibre(llibre);
        lm.catalogarLlibre();

        Prestec prestec = new Prestec("p2", "lector-inexistent", "isbn-9999", "01/01/2025", "15/01/2025");
        lm.prestarLlibre(prestec);
    }

    @Test(expected = SinEjemplaresException.class)
    public void testPrestarSinExemplares() {
        Lector lector = new Lector("lector1", "Pablo", "Casado", "63482641G", "12/08/2005", "Barcelona", "C/Viladomat 112");
        lm.afegirLector(lector);

        Llibre llibre = new Llibre("llibre1", "isbn-0", "Titol", "Editorial", 2017, 1, "Autor", "Tematica");
        // catalogamos una vez y luego prestamos para dejar a 0
        lm.emmagatzemarLlibre(llibre);
        lm.catalogarLlibre();

        Prestec p1 = new Prestec("p1", "lector1", "isbn-0", "01/01/2025", "15/01/2025");
        lm.prestarLlibre(p1);

        // ahora intentar prestar de nuevo debe lanzar SinEjemplaresException
        Prestec p2 = new Prestec("p2", "lector1", "isbn-0", "02/01/2025", "16/01/2025");
        lm.prestarLlibre(p2);
    }

    @Test
    public void testConsultarPrestecs() {
        Lector lector = new Lector("lector1", "Pablo", "Casado", "63482641G", "12/08/2005", "Barcelona", "C/Viladomat 112");
        lm.afegirLector(lector);

        Llibre llibre = new Llibre("llibre1", "isbn-consult", "Titol", "Editorial", 2022, 1, "Autor", "Tematica");
        lm.emmagatzemarLlibre(llibre);
        lm.catalogarLlibre();

        Prestec p1 = new Prestec("p1", "lector1", "isbn-consult", "01/01/2025", "15/01/2025");
        lm.prestarLlibre(p1);

        Assert.assertEquals(1, lm.consultarPrestecs(lector).size());
    }
}
