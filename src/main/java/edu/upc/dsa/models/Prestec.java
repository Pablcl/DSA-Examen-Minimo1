package edu.upc.dsa.models;

public class Prestec {

    private String id;
    private String idLector;
    private String isbnLlibre;
    private String dataPrestec;
    private String dataFinalDevolucio;
    private String estat; 

    public Prestec(String id, String idLector, String isbnLlibre, String dataPrestec, String dataFinalDevolucio) {
        this.id = id;
        this.idLector = idLector;
        this.isbnLlibre = isbnLlibre;
        this.dataPrestec = dataPrestec;
        this.dataFinalDevolucio = dataFinalDevolucio;
    }

    public Prestec(){}

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

    public String getIsbnLlibre() {
        return isbnLlibre;
    }

    public void setIsbnLlibre(String isbnLlibre) {
        this.isbnLlibre = isbnLlibre;
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
