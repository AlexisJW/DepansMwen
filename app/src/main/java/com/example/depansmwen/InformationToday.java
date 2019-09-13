package com.example.depansmwen;

public class InformationToday {
    String montant;
    String categorie;
    String devise;
    String note;

    public InformationToday(String categorie, String montant, String devise, String note) {
        this.montant = montant;
        this.categorie = categorie;
        this.devise = devise;
        this.note = note;
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
}