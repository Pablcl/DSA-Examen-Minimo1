package edu.upc.dsa;

import edu.upc.dsa.models.Llibre;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Prestec;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LlibreManagerTest {
    LlibreManagerImpl lm;

    @Before
    public void setUp() {
        lm = LlibreManagerImpl.getInstance();
        lm.afegirLector("lector1", "Pablo", "Casado", "12345678A", "01/01/2005", "Barcelona", "C/Viladomat 123");
        lm.emmagatzemarLlibre(new Llibre("llibre1", "978-84-08-06782-1", "El Codi Da Vinci", "Planeta", 2003, 1, "Dan Brown", "Misteri"));
        lm.emmagatzemarLlibre(new Llibre("llibre2", "978-84-9809-207-5", "La noia del tren", "La Campana", 2015, 1, "Paula Hawkins", "Thriller"));
    }

    @After
    public void tearDown() {
        this.lm.reset();
    }


    //un test per cada metode
    @Test
    public void testAfegirLector() {
        Assert.assertEquals(1, lm.sizeLectors());
        lm.afegirLector("lector2", "Maria", "Soler", "87654321B", "02/02/1992", "Girona", "Plaça Major 1");
        Assert.assertEquals(2, lm.sizeLectors());
    }

    @Test
    public void testEmmagatzemarLlibre() {
        // El setUp ja emmagatzema 2 llibres
        lm.emmagatzemarLlibre(new Llibre("llibre3", "978-0307474278", "The Road", "Vintage", 2006, 1, "Cormac McCarthy", "Post-apocalíptic"));
        // No podem verificar directament la pila, però podem veure l'efecte al catalogar
        lm.catalogarLlibre();
        lm.catalogarLlibre();
        lm.catalogarLlibre();
        Assert.assertEquals(3, lm.sizeCatalogats());
    }

    @Test
    public void testCatalogarLlibre() {
        Assert.assertEquals(0, lm.sizeCatalogats());
        lm.catalogarLlibre(); // Cataloga "La noia del tren"
        Assert.assertEquals(1, lm.sizeCatalogats());
        Llibre llibreCatalogat = lm.getLlibreCatalogat("llibre2");
        Assert.assertEquals(1, llibreCatalogat.getQuantitat());

        // Afegim un altre llibre amb el mateix ISBN per provar l'increment de quantitat
        lm.emmagatzemarLlibre(new Llibre("llibre2-copia", "978-84-9809-207-5", "La noia del tren", "La Campana", 2015, 1, "Paula Hawkins", "Thriller"));
        lm.catalogarLlibre(); // Cataloga "El Codi Da Vinci"
        lm.catalogarLlibre(); // Cataloga la còpia de "La noia del tren"
        Assert.assertEquals(2, lm.sizeCatalogats()); // No augmenta el nº de llibres catalogats, sinó la quantitat
        Assert.assertEquals(2, llibreCatalogat.getQuantitat());
    }

    @Test
    public void testPrestarLlibre() {
        lm.catalogarLlibre(); // Cataloga "La noia del tren" (ID: llibre2)
        Llibre llibre = lm.getLlibreCatalogat("llibre2");
        Assert.assertEquals(1, llibre.getQuantitat());
        Assert.assertEquals(0, lm.sizePrestecs());

        Prestec prestec = new Prestec("prestec1", "lector1", "llibre2", "2023-11-21", "2023-12-21");
        lm.prestarLlibre(prestec);

        Assert.assertEquals(0, llibre.getQuantitat());
        Assert.assertEquals(1, lm.sizePrestecs());
    }

    @Test
    public void testConsultarPrestecsLector() {
        lm.catalogarLlibre(); // Cataloga "La noia del tren"
        lm.catalogarLlibre(); // Cataloga "El Codi Da Vinci"

        Prestec p1 = new Prestec("p1", "lector1", "llibre1", "2023-10-01", "2023-10-21");
        Prestec p2 = new Prestec("p2", "lector1", "llibre2", "2023-11-01", "2023-11-21");
        lm.prestarLlibre(p1);
        lm.prestarLlibre(p2);

        Lector lector = new Lector();
        lector.setId("lector1");
        List<Prestec> prestecs = lm.consultarPrestecs(lector);

        Assert.assertEquals(2, prestecs.size());
        Assert.assertEquals("p1", prestecs.get(0).getId());
        Assert.assertEquals("p2", prestecs.get(1).getId());
    }
}
