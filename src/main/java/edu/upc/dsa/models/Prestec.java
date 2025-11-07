package edu.upc.dsa.models;

public class Prestec {

    private String id;
    private String idLector;
    private String idLlibre;
    private String dataPrestec;
    private String dataFinalDevolucio;
    private String estat; // Estado (si está en trámite o si no), no es mala la de que sea una variable tipo boolean (estat false/true)

    public Prestec() {}

    public Prestec(String id, String idLector, String idLlibre, String dataPrestec, String dataFinalDevolucio) {
        this.id = id;
        this.idLector = idLector;
        this.idLlibre = idLlibre;
        this.dataPrestec = dataPrestec;
        this.dataFinalDevolucio = dataFinalDevolucio;
        this.estat = "Nada por el momento"; // Estado por defecto
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdLector() {
        return idLector;
    }

    public void setIdLector(String idLector) {
        this.idLector = idLector;
    }

    public String getIdLlibre() {
        return idLlibre;
    }

    public void setIdLlibre(String idLlibre) {
        this.idLlibre = idLlibre;
    }

    public String getDataPrestec() {
        return dataPrestec;
    }

    public void setDataPrestec(String dataPrestec) {
        this.dataPrestec = dataPrestec;
    }

    public String getDataFinalDevolucio() {
        return dataFinalDevolucio;
    }

    public void setDataFinalDevolucio(String dataFinalDevolucio) {
        this.dataFinalDevolucio = dataFinalDevolucio;
    }

    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }
}
