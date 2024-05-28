package demo.model;

import java.io.Serializable;
import java.util.Date;

public class EquipementInfo implements Serializable {
    
    private int id;
    private String name;
    private String address;
    private String status;
    private Double lon;
    private Double lat;
    private int numberOfSpotsAvailable;
    private int numberOfEquipmentsAvailable;
    private Date lastUpdate;
    private long freshnessSeconds;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Double getLon() {
        return lon;
    }
    public void setLon(Double lon) {
        this.lon = lon;
    }
    public Double getLat() {
        return lat;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }
    public int getNumberOfSpotsAvailable() {
        return numberOfSpotsAvailable;
    }
    public void setNumberOfSpotsAvailable(int numberOfSpotsAvailable) {
        this.numberOfSpotsAvailable = numberOfSpotsAvailable;
    }
    public int getNumberOfEquipmentsAvailable() {
        return numberOfEquipmentsAvailable;
    }
    public void setNumberOfEquipmentsAvailable(int numberOfEquipmentsAvailable) {
        this.numberOfEquipmentsAvailable = numberOfEquipmentsAvailable;
    }
    public Date getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public long getFreshnessSeconds() {
        return freshnessSeconds;
    }
    public void setFreshnessSeconds(long freshnessSeconds) {
        this.freshnessSeconds = freshnessSeconds;
    }

    
    
}
