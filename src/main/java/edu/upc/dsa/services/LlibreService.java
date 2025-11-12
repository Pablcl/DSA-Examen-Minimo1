package edu.upc.dsa.services;

import edu.upc.dsa.LlibreManager;
import edu.upc.dsa.LlibreManagerImpl;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Llibre;
import edu.upc.dsa.models.Prestec;
import edu.upc.dsa.exceptions.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/biblioteca", description = "Endpoint para el servicio de libros")
@Path("/biblioteca")
public class LlibreService {

    private final LlibreManager lm;

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
    @ApiOperation(value = "Añadir un nuevo lector", notes = "Añade un nuevo lector al sistema")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Lector añadido correctamente", response = Lector.class),
            @ApiResponse(code = 400, message = "Faltan datos del lector")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response afegirLector(Lector lector) {
        if (lector == null || lector.getId() == null || lector.getNom() == null) {
            return Response.status(400).entity("Faltan datos del lector").build();
        }
        this.lm.afegirLector(lector);
        return Response.status(201).entity(lector).build();
    }

    @POST
    @Path("/almacen/libros")
    @ApiOperation(value = "Almacenar un nuevo libro", notes = "Añade un libro al almacén para catalogar")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Libro almacenado correctamente"),
            @ApiResponse(code = 400, message = "Faltan datos del libro")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response emmagatzemarLlibre(Llibre llibre) {
        if (llibre == null || llibre.getId() == null || llibre.getTitol() == null) {
            return Response.status(400).entity("Faltan datos del libro").build();
        }
        this.lm.emmagatzemarLlibre(llibre);
        return Response.status(201).build();
    }

    @POST
    @Path("/catalogar")
    @ApiOperation(value = "Catalogar un libro", notes = "Cataloga el siguiente libro del almacén")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operación de catalogación realizada"),
            @ApiResponse(code = 404, message = "No hay libros para catalogar")
    })
    public Response catalogarLlibre() {
        try {
            this.lm.catalogarLlibre();
            return Response.status(200).entity("Operación de catalogación realizada.").build();
        } catch (NoHayLibrosException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/prestecs")
    @ApiOperation(value = "Realizar un préstamo", notes = "Presta un libro a un lector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Préstamo realizado correctamente", response = Prestec.class),
            @ApiResponse(code = 400, message = "Faltan datos del préstamo"),
            @ApiResponse(code = 404, message = "Libro o lector no encontrado"),
            @ApiResponse(code = 409, message = "No hay ejemplares disponibles")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response prestarLlibre(Prestec prestec) {
        if (prestec == null || prestec.getIdLector() == null || prestec.getIsbnLlibre() == null) {
            return Response.status(400).entity("Faltan datos del préstamo").build();
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
    @ApiOperation(value = "Consultar préstamos de un lector", notes = "Obtiene la lista de préstamos de un lector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Préstamos encontrados", response = Prestec.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Lector no encontrado")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarPrestecs(@PathParam("idLector") String idLector) {
        Lector lector = new Lector();
        lector.setId(idLector);

        List<Prestec> prestecs = this.lm.consultarPrestecs(lector);
        GenericEntity<List<Prestec>> entity = new GenericEntity<List<Prestec>>(prestecs) {};
        return Response.status(200).entity(entity).build();
    }
}
