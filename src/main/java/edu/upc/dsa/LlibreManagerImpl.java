package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Stack;

public class LlibreManagerImpl implements LlibreManager {

    private static LlibreManagerImpl instance;
    final static Logger logger = Logger.getLogger(LlibreManagerImpl.class);

    private Queue<Stack<Llibre>> llistamunts; 
    private List<Llibre> catalogats;
    private List<Lector> lectors;
    private List<Prestec> prestecs;

    private LlibreManagerImpl() {
        llistamunts = new LinkedList<>();
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

    @Override
    public void afegirLector(Lector lector){
        logger.info("afegirLector(id: " + lector.getId() + ", nom: " + lector.getNom() + ")");
        for (Lector l : lectors){
            if (l.getId().equals(lector.getId())){
                l.setNom(lector.getNom());
                l.setCognoms(lector.getCognoms());
                l.setDni(lector.getDni());
                l.setDatanaixement(lector.getDatanaixement());
                l.setLlocnaixement(lector.getLlocnaixement());
                l.setAdreça(lector.getAdreça());
                logger.info("Lector con id '" + lector.getId() + "' actualizado.");
                return;
            }
        }
        Lector nouLector = new Lector(lector.getId(), lector.getNom(), lector.getCognoms(), lector.getDni(), lector.getDatanaixement(), lector.getLlocnaixement(), lector.getAdreça());
        lectors.add(nouLector);
        logger.info("Nuevo lector añadido: " + nouLector.getNom() + ". Total lectores: " + lectors.size());
    }

    @Override
    public void emmagatzemarLlibre(Llibre llibre){
        logger.info("emmagatzemarLlibre(llibre: " + llibre.getTitol() + ")");
        Stack<Llibre> munt = ((LinkedList<Stack<Llibre>>) llistamunts).getLast();

        if(munt.size() >= 10){
            munt = new Stack<>();
            llistamunts.add(munt);
            logger.info("Se ha creado un nuevo munt. Total munts: " + llistamunts.size());

        }
        munt.push(llibre);
        logger.info("Libro '" + llibre.getTitol() + "' emmagatzemat. Tamaño del munt actual: " + munt.size());
    }

    @Override
    public void catalogarLlibre(){
        logger.info("catalogarLlibre() - Inicio");
        Stack<Llibre> munt = llistamunts.peek();

        if (munt.isEmpty()) {
            llistamunts.poll();
            logger.warn("El primer munt estaba vacío. Intentando con el siguiente.");
            catalogarLlibre();
            return;
        }

        Llibre llibre = munt.pop();

        if (munt.isEmpty()){
            llistamunts.poll();
        }
        for (Llibre l : catalogats){
            if (l.getISBN().equals(llibre.getISBN())){
                l.setQuantitat(l.getQuantitat() + 1);
                logger.info("ISBN '" + llibre.getISBN() + "' ya catalogado. Se ha incrementado el nombre d'exemplars a " + l.getQuantitat());
                logger.info("catalogarLlibre() - Fin");
                return; 
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
            if(l.getISBN().equals(prestec.getIdLlibre())){
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
                return; 
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

    public int sizeLectors() {
        return lectors.size();
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

    public Llibre getLlibre(String id) {
        return this.catalogats.stream().filter(l -> l.getId().equals(id)).findFirst().orElse(null);
    }

    public Lector getLector(String id) {
        return this.lectors.stream().filter(l -> l.getId().equals(id)).findFirst().orElse(null);
    }
}
