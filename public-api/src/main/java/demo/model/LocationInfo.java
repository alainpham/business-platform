package demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class LocationInfo implements Serializable{
    
    @JsonProperty("libelle")
    private int libelle;
    
    @JsonProperty("nom")
    private String nom;
    
    @JsonProperty("adresse")
    private String adresse;
    
    @JsonProperty("commune")
    private String commune;
    
    @JsonProperty("etat")
    private String etat;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("geo")
    private Geo geo;
    
    @JsonProperty("nbplacesdispo")
    private int nbPlacesDispo;
    
    @JsonProperty("nbvelosdispo")
    private int nbVelosDispo;
    
    @JsonProperty("etatconnexion")
    private String etatConnexion;
    
    @JsonProperty("localisation")
    private Localisation localisation;
    
    @JsonProperty("datemiseajour")
    private Date  dateMiseAJour;

    public int getLibelle() {
        return libelle;
    }

    public void setLibelle(int libelle) {
        this.libelle = libelle;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public int getNbPlacesDispo() {
        return nbPlacesDispo;
    }

    public void setNbPlacesDispo(int nbPlacesDispo) {
        this.nbPlacesDispo = nbPlacesDispo;
    }

    public int getNbVelosDispo() {
        return nbVelosDispo;
    }

    public void setNbVelosDispo(int nbVelosDispo) {
        this.nbVelosDispo = nbVelosDispo;
    }

    public String getEtatConnexion() {
        return etatConnexion;
    }

    public void setEtatConnexion(String etatConnexion) {
        this.etatConnexion = etatConnexion;
    }

    public Localisation getLocalisation() {
        return localisation;
    }

    public void setLocalisation(Localisation localisation) {
        this.localisation = localisation;
    }

    public Date getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(Date dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    // Inner classes for Geo and Localisation

    public static class Geo {
        @JsonProperty("lon")
        private double lon;
        
        @JsonProperty("lat")
        private double lat;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class Localisation {
        @JsonProperty("lon")
        private double lon;
        
        @JsonProperty("lat")
        private double lat;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }
}
