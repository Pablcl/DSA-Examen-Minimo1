package edu.upc.dsa.services;

import edu.upc.dsa.LlibreManager;
import edu.upc.dsa.LlibreManagerImpl;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Llibre;
import edu.upc.dsa.models.Prestec;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/biblioteca")
public class LlibreService {

    private LlibreManagerImpl lm;

    public LlibreService() {
        this.lm = LlibreManagerImpl.getInstance();

        if (lm.sizeLectors() == 0) {
            lm.afegirLector(new Lector("lector1", "Pablo", "Casado", "63482641G", "12/08/2005", "Barcelona", "C/Viladomat 112"));
            lm.emmagatzemarLlibre(new Llibre("llibre1", "978-84-08-06782-1", "El Codi Da Vinci", "Planeta", 2003, 1, "Dan Brown", "Misteri"));
            lm.emmagatzemarLlibre(new Llibre("llibre2", "978-84-9809-207-5", "La noia del tren", "La Campana", 2015, 1, "Paula Hawkins", "Thriller"));

            lm.catalogarLlibre();
            lm.catalogarLlibre();
        }
    }

    @POST
    @Path("/lectores")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response afegirLector(Lector lector) {
        if (lector == null || lector.getId() == null || lector.getNom() == null) {
            return Response.status(400).entity("Falten dades del lector").build();
        }
        this.lm.afegirLector(lector);
        return Response.status(201).entity(lector).build();
    }

    @POST
    @Path("/almacen/libros")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response emmagatzemarLlibre(Llibre llibre) {
        if (llibre == null || llibre.getId() == null || llibre.getTitol() == null) {
            return Response.status(400).entity("Falten dades del llibre").build();
        }
        this.lm.emmagatzemarLlibre(llibre);
        return Response.status(201).build();
    }

    @POST
    @Path("/catalogar")
    public Response catalogarLlibre() {
        try {
            this.lm.catalogarLlibre();
            return Response.status(200).entity("Operació de catalogació realitzada.").build();
        } catch (NoHayLibrosException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }
    
    @POST
    @Path("/prestecs")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response prestarLlibre(Prestec prestec) {
        if (prestec == null || prestec.getIsbnLlibre() == null || prestec.getIdLector() == null) {
            return Response.status(400).entity("Falten dades del préstec").build();
        }
        try {
            this.lm.prestarLlibre(prestec);
            return Response.status(200).entity(prestec).build();
        } catch (LibroNoEncontradoException | LectorNoEncontradoException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (SinEjemplaresException e) {
            return Response.status(409).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/lectores/{idLector}/prestecs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarPrestecs(@PathParam("idLector") String idLector) {
        Lector lector = new Lector();
        lector.setId(idLector);
        
        List<Prestec> prestecs = this.lm.consultarPrestecs(lector);
        
        GenericEntity<List<Prestec>> entity = new GenericEntity<List<Prestec>>(prestecs) {};
        return Response.status(200).entity(entity).build();
    }
}
