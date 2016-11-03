package co.cristiangarcia.hdtransmileniopro.objects;

import java.io.Serializable;

/**
 * Created by cristiangarcia on 24/10/16.
 */

public class StationView  implements Serializable {
    private String color;
    private String description;
    private double distancia;
    private boolean duplicated;
    private int id;
    private double latitud;
    private double longitud;
    private String name;
    private int show;
    private int troncal;
    private String troncalName;
    private String troncalDescription;

    public String getTroncalDescription() {
        return troncalDescription;
    }

    public void setTroncalDescription(String troncalDescription) {
        this.troncalDescription = troncalDescription;
    }

    public StationView() {
        this.duplicated = false;
    }

    public StationView(int id, String name) {
        this.id = id;
        this.name=name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTroncal() {
        return this.troncal;
    }

    public void setTroncal(int troncal) {
        this.troncal = troncal;
    }

    public String getTroncalName() {
        return this.troncalName;
    }

    public void setTroncalName(String troncalName) {
        this.troncalName = troncalName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getDistancia() {
        return this.distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getLatitud() {
        return this.latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return this.longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getShow() {
        return this.show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public void setDuplicated(boolean duplicated) {
        this.duplicated = duplicated;
    }

    public boolean isDuplicated() {
        return this.duplicated;
    }

    @Override
    public String toString() {
        return "StationView{" +
                "color='" + color + '\'' +
                ", description='" + description + '\'' +
                ", distancia=" + distancia +
                ", duplicated=" + duplicated +
                ", id=" + id +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", name='" + name + '\'' +
                ", show=" + show +
                ", troncal=" + troncal +
                ", troncalName='" + troncalName + '\'' +
                '}';
    }
}
