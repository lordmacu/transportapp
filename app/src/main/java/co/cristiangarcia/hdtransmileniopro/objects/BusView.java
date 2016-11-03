package co.cristiangarcia.hdtransmileniopro.objects;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by cristiangarcia on 24/10/16.
 */

public class BusView {
    private String color;
    private String descripcion;
    private boolean enOperacion;
    private boolean favorite;
    private List<HorarioItem> horarios;
    private int id;
    private Drawable imgStart;
    private String name;
    private String vagon;
    private String desde;
    private String hasta;



    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getColor() {
        if (this.color.contains("#")) {
            return this.color;
        }
        return "#" + this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVagon() {
        return this.vagon;
    }

    public void setVagon(String vagon) {
        this.vagon = vagon;
    }

    public boolean isEnOperacion() {
        return this.enOperacion;
    }

    public boolean isFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setEnOperacion(boolean enOperacion) {
        this.enOperacion = enOperacion;
    }

    public List<HorarioItem> getHorarios() {
        return this.horarios;
    }

    public void setHorarios(List<HorarioItem> horarios) {
        this.horarios = horarios;
    }

    public Drawable getImgStart() {
        return this.imgStart;
    }

    public void setImgStart(Drawable imgStart) {
        this.imgStart = imgStart;
    }

    @Override
    public String toString() {
        return "BusView{" +
                "color='" + color + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", enOperacion=" + enOperacion +
                ", favorite=" + favorite +
                ", horarios=" + horarios +
                ", id=" + id +
                ", imgStart=" + imgStart +
                ", name='" + name + '\'' +
                ", vagon='" + vagon + '\'' +
                '}';
    }
}
