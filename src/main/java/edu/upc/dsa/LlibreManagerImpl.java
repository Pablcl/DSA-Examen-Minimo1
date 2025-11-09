package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.exceptions.*;
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
        lectors = new ArrayList<>();
        prestecs = new ArrayList<>();
    }

    public static LlibreManagerImpl getInstance() {
        if (instance == null) {
            instance = new LlibreManagerImpl();
        }
        return instance;
    }

    @Override
    public void afegirLector(Lector lector){
        logger.info("Iniciando la adición o actualización del lector con ID '" + lector.getId() + "'.");
        for (Lector l : lectors){
            if (l.getId().equals(lector.getId())){
                l.setNom(lector.getNom());
                l.setCognoms(lector.getCognoms());
                l.setDni(lector.getDni());
                l.setDatanaixement(lector.getDatanaixement());
                l.setLlocnaixement(lector.getLlocnaixement());
                l.setAdreça(lector.getAdreça());
                logger.info("Se han actualizado los datos del lector con ID '" + lector.getId() + "'.");
                return;
            }
        }
        Lector nouLector = new Lector(
                lector.getId(), lector.getNom(), lector.getCognoms(),
                lector.getDni(), lector.getDatanaixement(),
                lector.getLlocnaixement(), lector.getAdreça()
        );
        lectors.add(nouLector);
        logger.info("Se ha añadido un nuevo lector: " + nouLector.getNom() + ". El total de lectores ahora es " + lectors.size() + ".");
    }

    @Override
    public void emmagatzemarLlibre(Llibre llibre){
        logger.info("Iniciando el almacenamiento del libro: '" + llibre.getTitol() + "'.");
        if (llistamunts.isEmpty()) {
            llistamunts.add(new Stack<>());
            logger.warn("La lista de pilas estaba vacía. Se ha creado una nueva pila.");
        }
        Stack<Llibre> munt = ((LinkedList<Stack<Llibre>>) llistamunts).getLast();

        if(munt.size() >= 10){
            munt = new Stack<>();
            llistamunts.add(munt);
            logger.info("Se ha creado una nueva pila de libros. El total de pilas es " + llistamunts.size() + ".");
        }
        munt.push(llibre);
        logger.info("El libro '" + llibre.getTitol() + "' ha sido almacenado. La pila actual contiene " + munt.size() + " libros.");
    }

    @Override
    public void catalogarLlibre(){
        logger.info("Iniciando el proceso de catalogación de un libro.");


        while (!llistamunts.isEmpty() && llistamunts.peek().isEmpty()) {
            llistamunts.poll();
        }
        if (llistamunts.isEmpty()) {
            logger.warn("No se han encontrado libros pendientes para catalogar.");
            throw new NoHayLibrosException("No se han encontrado libros pendientes para catalogar.");
        }

        Stack<Llibre> munt = llistamunts.peek();
        Llibre llibre = munt.pop();

        if (munt.isEmpty()){
            llistamunts.poll();
        }

        for (Llibre l : catalogats){
            if (l.getISBN().equals(llibre.getISBN())){
                l.setQuantitat(l.getQuantitat() + 1);
                logger.info("El libro con ISBN '" + llibre.getISBN() + "' ya estaba catalogado. Se ha incrementado el número de ejemplares a " + l.getQuantitat() + ".");
                return;
            }
        }

        llibre.setQuantitat(1);
        catalogats.add(llibre);
        logger.info("Se ha catalogado un nuevo libro: '" + llibre.getTitol() + "'. El total de libros en el catálogo es " + catalogats.size() + ".");
    }

    @Override
    public void prestarLlibre(Prestec prestec){
        logger.info("Iniciando el proceso de préstamo para el lector '" + prestec.getIdLector() + "' y el libro con ISBN '" + prestec.getIsbnLlibre() + "'.");


        Llibre llibreTrobat = null;
        for (Llibre l : catalogats){
            if(l.getISBN().equals(prestec.getIsbnLlibre())){
                llibreTrobat = l;
                break;
            }
        }
        if (llibreTrobat == null) {
            logger.error("No se ha podido realizar el préstamo porque el libro con ISBN '" + prestec.getIsbnLlibre() + "' no se ha encontrado en el catálogo.");
            throw new LibroNoEncontradoException("No se ha podido realizar el préstamo porque el libro con ISBN '" + prestec.getIsbnLlibre() + "' no se ha encontrado en el catálogo.");
        }

        Lector lectorTrobat = null;
        for (Lector lector : lectors){
            if (lector.getId().equals(prestec.getIdLector())){
                lectorTrobat = lector;
                break;
            }
        }
        if (lectorTrobat == null) {
            logger.error("No se ha podido realizar el préstamo porque el lector con ID '" + prestec.getIdLector() + "' no se ha encontrado.");
            throw new LectorNoEncontradoException("No se ha podido realizar el préstamo porque el lector con ID '" + prestec.getIdLector() + "' no se ha encontrado.");
        }

        if (llibreTrobat.getQuantitat() <= 0) {
            logger.error("No se ha podido realizar el préstamo porque no quedan ejemplares disponibles del libro '" + llibreTrobat.getTitol() + "'.");
            throw new SinEjemplaresException("No se ha podido realizar el préstamo porque no quedan ejemplares disponibles del libro '" + llibreTrobat.getTitol() + "'.");
        }

        prestec.setEstat("En tràmit");
        llibreTrobat.setQuantitat(llibreTrobat.getQuantitat()-1);
        prestecs.add(prestec);

        logger.info("El préstamo con ID '" + prestec.getId() + "' se ha realizado con éxito. Libro: '" + llibreTrobat.getTitol() + "', Lector: '" + lectorTrobat.getNom() + "'. Quedan " + llibreTrobat.getQuantitat() + " ejemplares.");
    }

    @Override
    public List<Prestec> consultarPrestecs(Lector lector){
        logger.info("Iniciando la consulta de préstamos para el lector con ID '" + lector.getId() + "'.");
        List<Prestec> prestecsLector = new ArrayList<>();
        for(Prestec p: prestecs){
            if(p.getIdLector().equals(lector.getId())){
                prestecsLector.add(p);
            }
        }
        logger.info("La consulta ha finalizado. Se han encontrado " + prestecsLector.size() + " préstamos para el lector '" + lector.getNom() + "'.");
        return prestecsLector;
    }

    public int sizeLectors() {
        return lectors.size();
    }

    public int sizeCatalogats() {
        return catalogats.size();
    }

    public int sizePrestecs() {
        return prestecs.size();
    }

    public Llibre getLlibreCatalogat(String id) {
        for (Llibre l : catalogats){
            if (l.getId().equals(id)) return l;
        }
        return null;
    }

    public Llibre getLlibre(String id) {
        for (Llibre l : catalogats){
            if (l.getId().equals(id)) return l;
        }
        return null;
    }

    public Lector getLector(String id) {
        for (Lector l : lectors){
            if (l.getId().equals(id)) return l;
        }
        return null;
    }

    public Llibre getLlibreByISBN(String isbn) {
        for (Llibre l : catalogats){
            if (l.getISBN().equals(isbn)) return l;
        }
        return null;
    }

    public void reset() {
        this.lectors.clear();
        this.catalogats.clear();
        this.prestecs.clear();
        this.llistamunts.clear();
        this.llistamunts.add(new Stack<>());
    }
}
