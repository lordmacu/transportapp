package co.cristiangarcia.hdtransmileniopro.objects;

/**
 * Created by cristiangarcia on 24/10/16.
 */

public class HorarioItem {
    private String contentDesc;
    private String dias;
    private String hora;

    public HorarioItem(String dias, String hora, String contentDesc) {
        this.dias = dias;
        this.hora = hora;
        this.contentDesc = contentDesc;
    }

    public String getDias() {
        return this.dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getHora() {
        return this.hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getContentDesc() {
        return this.contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }

    @Override
    public String toString() {
        return "HorarioItem{" +
                "contentDesc='" + contentDesc + '\'' +
                ", dias='" + dias + '\'' +
                ", hora='" + hora + '\'' +
                '}';
    }
}
