package co.cristiangarcia.hdtransmileniopro.objects;

/**
 * Created by cristiangarcia on 24/10/16.
 */

public class UbicacionMapa {
    String agency;
    double latitud;
    double longitud;
    String subTitle;
    String title;

    public UbicacionMapa(double latitud, double longitud, String title, String subTitle, String agency) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.title = title;
        this.subTitle = subTitle;
        this.agency = agency;
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getAgency() {
        return this.agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }
}
