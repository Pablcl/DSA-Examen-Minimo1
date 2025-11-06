package edu.upc.dsa;

import edu.upc.dsa.models.Avion;
import edu.upc.dsa.models.Equipaje;
import edu.upc.dsa.models.Vuelo;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.*;
import java.Comparator;


public class VuelosManagerImpl implements VuelosManager {

    private static VuelosManagerImpl instance;

    final static Logger logger = Logger.getLogger(VuelosManagerImpl.class);

    private List<Vuelo> vuelos;
    private List<Avion> aviones;


    private VuelosManagerImpl() {
        vuelos = new ArrayList<Vuelo>();
        aviones = new ArrayList<Avion>();
    }

    public static VuelosManagerImpl getInstance() {
        if (instance == null) {
            instance = new VuelosManagerImpl();
        }
        return instance;
    }

    @Override
    public void addAvion(String id, String empresa, String modelo) {
    }


    @Override
    public void addVuelo(Vuelo vuelo) {
        logger.info("Añadiendo vuelo con id: " + vuelo.getIdVuelo());
        vuelos.add(vuelo);
    }

    public int sizeVuelos() {
        return vuelos.size();
    }

    public List<Vuelo> getVuelos() {
        logger.info("Obteniendo todos las vuelos");
        return this.vuelos;
    }


}
