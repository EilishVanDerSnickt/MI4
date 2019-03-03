package com.example.eilishvds.fitmap;

public class Prestatie {

    private String naam;
    private int aantalKilometer;
    private int tijd;

    public Prestatie(){

    }

    public Prestatie(String naam, int aantalKilometer, int tijd){
        this.naam = naam;
        this.aantalKilometer = aantalKilometer;
        this.tijd = tijd;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public int getAantalKilometer() {
        return aantalKilometer;
    }

    public void setAantalKilometer(int aantalKilometer) {
        this.aantalKilometer = aantalKilometer;
    }

    public int getTijd() {
        return tijd;
    }

    public void setTijd(int tijd) {
        this.tijd = tijd;
    }
}
