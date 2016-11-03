package co.cristiangarcia.hdtransmileniopro.util;

import android.content.Context;
import android.text.Html;
 import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelperTransmiSitp;

public class RecorridoRuta {
    private boolean _caminarEnDestino;
    private boolean _caminarEnOrigen;
    private Integer _distancia;
    private ArrayList<RecorridoBus> _listaBuses;
    private Integer _paradas;
    private Double _tiempo;
    private double _tiempoAdicionalDestino;
    private double _tiempoAdicionalOrigen;

    /* renamed from: util.RecorridoRuta.1 */
    static class C07151 implements Comparator<RecorridoRuta> {
        C07151() {
        }

        public int compare(RecorridoRuta lhs, RecorridoRuta rhs) {
            return lhs._tiempo.compareTo(rhs._tiempo);
        }
    }

    public RecorridoRuta(ArrayList<RecorridoBus> listaBuses) {
        this._caminarEnOrigen = false;
        this._caminarEnDestino = false;
        this._listaBuses = listaBuses;
        this._paradas = Integer.valueOf(getTotalParadas());
        this._distancia = Integer.valueOf(getTotalDistancia());
        this._tiempo = Double.valueOf(0.0d);
        this._tiempoAdicionalOrigen = 0.0d;
        this._tiempoAdicionalDestino = 0.0d;
    }

    public void setCaminarEnOrigen(boolean caminarEnOrigen) {
        this._caminarEnOrigen = caminarEnOrigen;
    }

    public void setCaminarEnDestino(boolean caminarEnDestino) {
        this._caminarEnDestino = caminarEnDestino;
    }

    public boolean getCaminarEnDestino() {
        return this._caminarEnDestino;
    }

    public void setTiempoAdicionalOrigen(double tiempoAdicionalOrigen) {
        this._tiempoAdicionalOrigen = tiempoAdicionalOrigen;
    }

    public void setTiempoAdicionalDestino(double tiempoAdicionalDestino) {
        this._tiempoAdicionalDestino = tiempoAdicionalDestino;
    }

    public void setTotalParadas() {
        int paradas = 0;
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            paradas += ((RecorridoBus) it.next()).getParadas();
        }
        this._paradas = Integer.valueOf(paradas);
    }

    public void setTotalDistancia() {
        int distancia = 0;
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            distancia += ((RecorridoBus) it.next()).getDistancia();
        }
        this._distancia = Integer.valueOf(distancia);
    }

    public double getTiempoOrigen() {
        return this._tiempoAdicionalOrigen;
    }

    public double getTiempoDestino() {
        return this._tiempoAdicionalDestino;
    }

    public Integer getParadas() {
        return this._paradas;
    }

    public double getTiempo() {
        return this._tiempo.doubleValue();
    }

    public ArrayList<RecorridoBus> getListaBuses() {
        return this._listaBuses;
    }

    public int getSizeListaBuses() {
        int size = 0;
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            if (((RecorridoBus) it.next()).getId() > 0) {
                size++;
            }
        }
        return size;
    }

    public boolean isDirectOrOneChange() {
        int size = 0;
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            if (((RecorridoBus) it.next()).getId() > 0) {
                size++;
            }
        }
        if (size > 2) {
            return false;
        }
        return true;
    }

    private int getTotalParadas() {
        int ret = 0;
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            RecorridoBus rb = (RecorridoBus) it.next();
            if (rb.getId() > 0) {
                ret += rb.getParadas();
            }
        }
        return ret;
    }

    private int getTotalDistancia() {
        int ret = 0;
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            RecorridoBus rb = (RecorridoBus) it.next();
            if (rb.getId() > 0) {
                ret += rb.getDistancia();
            }
        }
        return ret;
    }

    public int getFirstStation() {
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            RecorridoBus rb = (RecorridoBus) it.next();
            if (rb.getId() >= 0) {
                return rb.getEstacionOrigen();
            }
        }
        return 0;
    }

    public int getLastStation() {
        int ret = 0;
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            RecorridoBus rb = (RecorridoBus) it.next();
            if (rb.getId() >= 0) {
                ret = rb.getEstacionDestino();
            }
        }
        return ret;
    }

    public int getFirstStationWithPeat() {
        Iterator it = this._listaBuses.iterator();
        if (it.hasNext()) {
            return ((RecorridoBus) it.next()).getEstacionOrigen();
        }
        return 0;
    }

    public int getLastStationWithPeat() {
        int ret = 0;
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            ret = ((RecorridoBus) it.next()).getEstacionDestino();
        }
        return ret;
    }

    public void deleteFirstBus() {
        if (((RecorridoBus) this._listaBuses.get(0)).getId() < 0) {
            this._listaBuses.remove(0);
        }
        this._paradas = Integer.valueOf(getTotalParadas());
    }

    public int getLastBus() {
        return ((RecorridoBus) this._listaBuses.get(this._listaBuses.size() - 1)).getId();
    }

    public void deleteLastBusAssignDestination(int iDestination) {
        if (((RecorridoBus) this._listaBuses.get(this._listaBuses.size() - 1)).getId() < 0) {
            this._listaBuses.remove(this._listaBuses.size() - 1);
            if (!this._listaBuses.isEmpty()) {
                ((RecorridoBus) this._listaBuses.get(this._listaBuses.size() - 1)).setDestinoTiempo(iDestination, ((RecorridoBus) this._listaBuses.get(this._listaBuses.size() - 1)).getTiempo());
            }
        }
        this._paradas = Integer.valueOf(getTotalParadas());
    }

    public void deleteLastBus() {
        if (((RecorridoBus) this._listaBuses.get(this._listaBuses.size() - 1)).getId() < 0) {
            this._listaBuses.remove(this._listaBuses.size() - 1);
        }
        this._paradas = Integer.valueOf(getTotalParadas());
    }

    private double getTotalTiempoMio() {
        double ret = 0.0d;
        int peat = 0;
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            RecorridoBus rb = (RecorridoBus) it.next();
            if (rb.getId() <= 0) {
                peat++;
            }
            ret += rb.getTiempo();
        }
        int esperaBuses = (this._listaBuses.size() - 1) - peat;
        if (esperaBuses > 0) {
            ret += (double) (esperaBuses * 7);
        }
        if (this._tiempoAdicionalOrigen > 0.0d) {
            ret += this._tiempoAdicionalOrigen;
        }
        if (this._tiempoAdicionalDestino > 0.0d) {
            return ret + this._tiempoAdicionalDestino;
        }
        return ret;
    }

    private int getTotalTiempo() {
        int ret = 0;
        int peat = 0;
        Iterator it = this._listaBuses.iterator();
        while (it.hasNext()) {
            RecorridoBus rb = (RecorridoBus) it.next();
            if (rb.getId() <= 0) {
                peat++;
            }
            ret = (int) (((double) ret) + rb.getTiempo());
        }
        int esperaBuses = (this._listaBuses.size() - 1) - peat;
        if (esperaBuses > 0) {
            ret += esperaBuses * 4;
        }
        if (this._tiempoAdicionalOrigen > 0.0d) {
            ret = (int) (((double) ret) + this._tiempoAdicionalOrigen);
        } else if (this._caminarEnOrigen) {
            ret += 7;
        }
        if (this._tiempoAdicionalDestino > 0.0d) {
            return (int) (((double) ret) + this._tiempoAdicionalDestino);
        }
        if (this._caminarEnDestino) {
            return ret + 7;
        }
        return ret;
    }

    public RecomendacionView getHeaderRecomendacion(Context context, int j) {
        RecomendacionView rv = new RecomendacionView(context);
        rv.setRecomendacionText(Html.fromHtml("<b>" + getParadas() + "</b> paradas (<b>" + ((int) Math.round(getTiempo())) + "</b>) min de recorrido"));
        return rv;
    }

    public RecorridoBus getItem(int i) {
        return (RecorridoBus) this._listaBuses.get(i);
    }

    public void addItem(RecorridoBus rb) {
        this._listaBuses.add(rb);
    }

    public static ArrayList<RecorridoRuta> sortByTime(Context context, DatabaseHelperTransmiSitp db, int iAppID, double iPeatMul, ArrayList<RecorridoRuta> rod) {
        Iterator it = rod.iterator();
        db.openDataBase();
        while (it.hasNext()) {
            RecorridoRuta rr = (RecorridoRuta) it.next();
            Iterator it2 = rr._listaBuses.iterator();
            while (it2.hasNext()) {
                RecorridoBus rb = (RecorridoBus) it2.next();
                if (rb.getTiempo() <= 0.0d) {
                    rb.calcularTiempo(context, db, iAppID, iPeatMul);
                }
            }
            rr._tiempo = Double.valueOf(rr.getTotalTiempoMio());
        }
        Collections.sort(rod, new C07151());
        db.close();
        return rod;
    }
}
