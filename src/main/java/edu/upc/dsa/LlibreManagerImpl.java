package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class LlibreManagerImpl implements LlibreManager {

    private static LlibreManagerImpl instance;

    final static Logger logger = Logger.getLogger(LlibreManagerImpl.class);


    private List<Stack<Llibre>> llistamunts; //lista de munts de 10
    private List<Llibre> catalogats;                    //es como la lista de libros (pero un munt)
    private List<Lector> lectors;
    private List<Prestec> prestecs;


    private LlibreManagerImpl() {
        llistamunts = new ArrayList<>();
        llistamunts.add(new Stack<>());
        catalogats = new ArrayList<>();
        lectors = new ArrayList<Lector>();
        prestecs = new ArrayList<Prestec>();
    }

    public static LlibreManagerImpl getInstance() {
        if (instance == null) {
            instance = new LlibreManagerImpl();
        }
        return instance;
    }

    // Mètode per reiniciar la instància (només per a proves)
    public void reset() {
        this.catalogats.clear();
        this.lectors.clear();
        this.prestecs.clear();
        this.llistamunts.clear();
        this.llistamunts.add(new Stack<>());
    }

    @Override
    public void afegirLector(String id, String nom, String cognoms, String dni, String datanaixement, String llocnaixement, String adreça){
        logger.info("afegirLector(id: " + id + ", nom: " + nom + ", cognoms: " + cognoms + ")");
        for (Lector l : lectors){
            if (l.getId().equals(id)){
                l.setId(id);
                l.setNom(nom);
                l.setCognoms(cognoms);
                l.setDni(dni);
                l.setDatanaixement(datanaixement);
                l.setLlocnaixement(llocnaixement);
                l.setAdreça(adreça);
                logger.info("Lector con id '" + id + "' actualizado.");
                return;
            }
        }
        Lector lector = new Lector(id, nom, cognoms, dni, datanaixement, llocnaixement, adreça);
        lectors.add(lector);
        logger.info("Nuevo lector añadido: " + lector.getNom() + ". Total lectores: " + lectors.size());
    }
    @Override
    public void emmagatzemarLlibre(Llibre llibre){
        logger.info("emmagatzemarLlibre(llibre: " + llibre.getTitol() + ")");
        Stack<Llibre> munt = llistamunts.get(llistamunts.size()-1); // ultimo munt

        if(munt.size() >= 10){
            munt = new Stack<>();
            llistamunts.add(munt);
            logger.info("Se ha creado un nuevo munt. Total munts: " + llistamunts.size());

        }
        munt.push(llibre);
        logger.info("Libro '" + llibre.getTitol() + "' emmagatzemat. Tamaño del munt actual: " + munt.size());
    }

    @Override
    public void catalogarLlibre(){ // Cambiado a void
        logger.info("catalogarLlibre() - Inicio");
        Stack<Llibre> munt = llistamunts.get(0); //primer munt

        if (munt.isEmpty()) { // Comprobamos antes de intentar pop
            logger.warn("No hay libros en el primer munt para catalogar. Operación de catalogación omitida.");
            return; // Salida temprana, no se devuelve código de estado
        }
        Llibre llibre = munt.pop(); // Ahora es seguro hacer pop

        if (munt.isEmpty()){
            if(llistamunts.size() > 1){
                llistamunts.remove(0); //se elimina munt actual
            }
        }
        for (Llibre l : catalogats){ // El bucle for debe estar fuera del if (llibre == null)
            if (l.getISBN().equals(llibre.getISBN())){
                l.setQuantitat(l.getQuantitat() + 1);
                logger.info("ISBN '" + llibre.getISBN() + "' ya catalogado. Se ha incrementado el nombre d'exemplars a " + l.getQuantitat());
                logger.info("catalogarLlibre() - Fin");
                return; // Salida temprana
            }
        }
        llibre.setQuantitat(1);
        catalogats.add(llibre);
        logger.info("Nuevo libro catalogado: '" + llibre.getTitol() + "'. Total libros catalogados: " + catalogats.size());
        logger.info("catalogarLlibre() - Fin");
    }

    @Override
    public void prestarLlibre(Prestec prestec){
        logger.info("prestarLlibre(prestecID: " + prestec.getId() + ", llibreID: " + prestec.getIdLlibre() + ", lectorID: " + prestec.getIdLector() + ")");
        for (Llibre l : catalogats){
            if(l.getId().equals(prestec.getIdLlibre())){
                if (l.getQuantitat() <= 0) {
                    logger.error("Error en prestarLlibre: No existen ejemplares suficientes del libro con ID '" + l.getId() + "'.");
                    return;
                }
                for (Lector lector : lectors){
                    if (lector.getId().equals(prestec.getIdLector())){
                        prestec.setEstat("En tràmit");
                        l.setQuantitat(l.getQuantitat()-1);
                        prestecs.add(prestec);
                        logger.info("Préstamo '" + prestec.getId() + "' realizado con éxito. Libro: '" + l.getTitol() + "', Lector: '" + lector.getNom() + "'. Ejemplares restantes: " + l.getQuantitat());
                        return;
                    }
                }
                logger.error("Error en prestarLlibre: Lector con ID '" + prestec.getIdLector() + "' no encontrado.");
                return; // Lector no encontrado
            }
        }
        logger.error("Error en prestarLlibre: Libro con ID '" + prestec.getIdLlibre() + "' no encontrado en el catálogo.");
    }


    @Override
    public List<Prestec> consultarPrestecs(Lector lector){
        logger.info("consultarPrestecs(lectorID: " + lector.getId() + ") - Inicio");
        List<Prestec> prestecsLector = new ArrayList<>();
        for(Prestec p: prestecs){
            if(p.getIdLector().equals(lector.getId())){
                prestecsLector.add(p);
            }
        }
        logger.info("consultarPrestecs - Fin. Se encontraron " + prestecsLector.size() + " préstamos para el lector '" + lector.getNom() + "'.");
        return prestecsLector;

    }

    @Override
    public int sizeLectors() {
        return this.lectors.size();
    }

    public int sizeCatalogats() {
        return this.catalogats.size();
    }

    public int sizePrestecs() {
        return this.prestecs.size();
    }

    public Llibre getLlibreCatalogat(String id) {
        return this.catalogats.stream().filter(l -> l.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Llibre getLlibre(String id) {
        return this.catalogats.stream().filter(l -> l.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Lector getLector(String id) {
        return this.lectors.stream().filter(l -> l.getId().equals(id)).findFirst().orElse(null);
    }
}
