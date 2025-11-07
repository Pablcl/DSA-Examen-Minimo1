package edu.upc.dsa.models;

public class Llibre {

    private String id;
    private String ISBN;
    private String titol;
    private String editorial;
    private int any;
    private int numedicio;
    private String autor;
    private String tematica;
    private int quantitat;

    public Llibre() {} // Constructor vacío

    public Llibre(String id, String ISBN, String titol, String editorial, int any, int numedicio, String autor, String tematica) {
        this.id = id;
        this.ISBN = ISBN;
        this.titol = titol;
        this.editorial = editorial;
        this.any = any;
        this.numedicio = numedicio;
        this.autor = autor;
        this.tematica = tematica;
        this.quantitat = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAny() {
        return any;
    }

    public void setAny(int any) {
        this.any = any;
    }

    public int getNumedicio() {
        return numedicio;
    }

    public void setNumedicio(int numedicio) {
        this.numedicio = numedicio;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTematica() {
        return tematica;
    }

    public void setTematica(String tematica) {
        this.tematica = tematica;
    }

    public int getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(int quantitat) {
        this.quantitat = quantitat;
    }
}
