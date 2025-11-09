package edu.upc.dsa.models;

public class Lector {

    private String id;
    private String nom;
    private String cognoms;
    private String dni;
    private String datanaixement;
    private String llocnaixement;
    private String adreça;

    public Lector(String id, String nom, String cognoms, String dni, String datanaixement, String llocnaixement, String adreça) {
        this.id = id;
        this.nom = nom;
        this.cognoms = cognoms;
        this.dni = dni;
        this.datanaixement = datanaixement;
        this.llocnaixement = llocnaixement;
        this.adreça = adreça;
    }
    public Lector(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognoms() {
        return cognoms;
    }

    public void setCognoms(String cognoms) {
        this.cognoms = cognoms;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDatanaixement() {
        return datanaixement;
    }

    public void setDatanaixement(String datanaixement) {
        this.datanaixement = datanaixement;
    }

    public String getLlocnaixement() {
        return llocnaixement;
    }

    public void setLlocnaixement(String llocnaixement) {
        this.llocnaixement = llocnaixement;
    }

    public String getAdreça() {
        return adreça;
    }

    public void setAdreça(String adreça) {
        this.adreça = adreça;
    }
}
