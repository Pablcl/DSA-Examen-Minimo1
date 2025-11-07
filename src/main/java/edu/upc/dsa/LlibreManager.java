package edu.upc.dsa;

import edu.upc.dsa.models.Llibre;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Prestec;
import java.util.List;

public interface LlibreManager {

    public void afegirLector(String id, String nom, String cognoms, String dni, String datanaixement, String llocnaixement, String adreça);
    public void emmagatzemarLlibre(Llibre llibre);
    public void catalogarLlibre();
    public void prestarLlibre(Prestec prestec);
    public List<Prestec> consultarPrestecs(Lector lector);
}
