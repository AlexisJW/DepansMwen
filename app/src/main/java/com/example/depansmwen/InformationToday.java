package com.example.depansmwen;

public class InformationToday {
    String montant;
    String categorie;
    String devise;
    String note;
    String compte;
    Integer id;

    public InformationToday(String categorie, String montant, String devise, String note, String compte, Integer id) {
        this.montant = montant;
        this.categorie = categorie;
        this.devise = devise;
        this.note = note;
        this.compte = compte;
        this.id = id;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompte() {
        return compte;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }
}