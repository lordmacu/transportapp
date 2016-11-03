package co.cristiangarcia.hdtransmileniopro.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.text.Html;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import co.cristiangarcia.hdtransmileniopro.R;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelperTransmiSitp;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelper;
import co.cristiangarcia.hdtransmileniopro.helpers.DbUtils;
import co.cristiangarcia.hdtransmileniopro.objects.BusView;

public class RecorridoBus {
    private String _color;
    private int _distancia;
    private int _estacionDestino;
    private int _estacionOrigen;
    private double _factor;
    private int _id;
    private String _name;
    private int _paradas;
    private Double _tiempo;

    /* renamed from: util.RecorridoBus.1 */
    static class C07141 implements Comparator<RecorridoBus> {
        C07141() {
        }

        public int compare(RecorridoBus lrb, RecorridoBus rrb) {
            return lrb._tiempo.compareTo(rrb._tiempo);
        }
    }

    public RecorridoBus(int id, String color, int estacionOrigen, int estacionDestino, int paradas, double factor) {
        this._distancia = 0;
        this._tiempo = Double.valueOf(0.0d);
        this._id = id;
        this._name = XmlPullParser.NO_NAMESPACE;
        this._color = color;
        this._estacionOrigen = estacionOrigen;
        this._estacionDestino = estacionDestino;
        this._paradas = paradas;
        this._tiempo = Double.valueOf(0.0d);
        this._factor = factor;
    }

    public RecorridoBus(int id, String name, String color, int estacionOrigen, int estacionDestino, int paradas, double factor) {
        this._distancia = 0;
        this._tiempo = Double.valueOf(0.0d);
        this._id = id;
        this._name = name;
        this._color = color;
        this._estacionOrigen = estacionOrigen;
        this._estacionDestino = estacionDestino;
        this._paradas = paradas;
        this._tiempo = Double.valueOf(0.0d);
        this._factor = factor;
    }

    public RecorridoBus(int id, String name, String color, int estacionOrigen, int estacionDestino, int paradas, int distancia, double factor) {
        this._distancia = 0;
        this._tiempo = Double.valueOf(0.0d);
        this._id = id;
        this._name = name;
        this._color = color;
        this._estacionOrigen = estacionOrigen;
        this._estacionDestino = estacionDestino;
        this._paradas = paradas;
        this._distancia = distancia;
        this._tiempo = Double.valueOf(0.0d);
        this._factor = factor;
    }

    public void calcularTiempo(Context context, DatabaseHelperTransmiSitp db, int iAppID, double iPeatMul) {
        this._tiempo = Double.valueOf(0.0d);
        this._tiempo = Double.valueOf(db.calcularTiempoMio(this._id, getEstacionDestino(), getEstacionOrigen(), this._factor));
        this._tiempo = Double.valueOf(this._tiempo.doubleValue() + (((double) getParadas()) / 2.0d));
    }

    public int getId() {
        return this._id;
    }

    public String getNombre() {
        return this._name;
    }

    public String getColor() {
        return this._color;
    }

    public int getEstacionDestino() {
        return this._estacionDestino;
    }

    public int getEstacionOrigen() {
        return this._estacionOrigen;
    }

    public int getParadas() {
        return this._paradas;
    }

    public int getDistancia() {
        return this._distancia;
    }

    public double getTiempo() {
        return this._tiempo.doubleValue();
    }

    public void setEstacionOrigen(int estacionOrigen) {
        this._estacionOrigen = estacionOrigen;
    }

    public void setEstacionDestino(int estacionDestino) {
        this._estacionDestino = estacionDestino;
    }

    public void setDestinoTiempo(int estacionDestino, double tiempo) {
        this._estacionDestino = estacionDestino;
        this._tiempo = Double.valueOf(tiempo);
    }

    public void setTiempo(double tiempo) {
        if (tiempo > 0.0d) {
            this._tiempo = Double.valueOf(tiempo);
        } else {
            this._tiempo = Double.valueOf(1.0d);
        }
    }

    public void applyColor(Context context, int iAppID) {
        if (this._color.length() == 0) {
            SQLiteDatabase db = new DatabaseHelper(context, iAppID).getReadableDatabase();


             Cursor cColor = db.rawQuery("SELECT b.rowid as _id, b.color FROM bus b WHERE b.pk_id = ?", new String[]{XmlPullParser.NO_NAMESPACE + getId()});
            if (cColor.moveToFirst()) {
                this._color = cColor.getString(cColor.getColumnIndex("color"));
            }
            cColor.close();
            db.close();
        }
    }

    public void applyName(Context context, int iAppID) {
        if (this._name.length() == 0) {
            SQLiteDatabase db = new DatabaseHelper(context, iAppID).getReadableDatabase();


            Cursor cName = db.rawQuery("SELECT b.rowid as _id, b.name FROM bus b WHERE b.pk_id = ?", new String[]{XmlPullParser.NO_NAMESPACE + getId()});
            if (cName.moveToFirst()) {
                this._name = cName.getString(cName.getColumnIndex("name"));
            }
            cName.close();
            db.close();
        }
    }

    public void calculateParadas(DatabaseHelperTransmiSitp db, int destino, int origen, int id) {
        db.openDataBase();

        if (this._paradas != 0) {
            return;
        }
        if (getId() > 0) {
            this._paradas = db.calculaParadas(destino, origen, id);
        } else {
            this._paradas = 1;
        }
        db.close();
    }

    public void calculateDistancia(DatabaseHelperTransmiSitp db, int destino, int origen, int id) {
        db.openDataBase();
        if (this._distancia != 0) {
            return;
        }
        if (getId() > 0) {
            this._distancia = db.calculaDistancia(destino, origen, id);
        } else if (getId() == -1) {
            this._distancia = db.calculaDistanciaPeatonal(destino, origen, id);
        } else {
            this._distancia = 1;
        }
        db.close();
    }

    public void calculateDistanciaUbicacion(DatabaseHelperTransmiSitp db, int destino, int origen, Location firstLocation, Location secondLocation) throws Throwable {
        Location locationOrigin = db.getLocationStation(origen);
        Location locationDestination = db.getLocationStation(destino);
        if (locationOrigin == null) {
            locationOrigin = firstLocation;
        }
        if (locationDestination == null) {
            locationDestination = secondLocation;
        }
        if (locationOrigin == null || locationDestination == null) {
            this._distancia = 0;
        } else {
            this._distancia = Math.round(locationOrigin.distanceTo(locationDestination) / 10.0f) + 1;
        }
    }

    public InformationBusView getInfBusView(Context context, BusView bv, String busInfo, String strColor) {
        InformationBusView ibv = new InformationBusView(context);
        ibv.getTxtBus().setText(bv.getName());
        ibv.getTxtBus().setBackgroundColor(Color.parseColor(bv.getColor()));
        ibv.getTxtInfoBus().setText(busInfo);
        ibv.getTxtParadas().setText(Html.fromHtml("Paradas: <b><font color=\"" + strColor + "\">" + getParadas() + "</font></b>; Duracion  <b><font color=\"" + strColor + "\">" + ((int) Math.round(getTiempo())) + "</font></b> min aprox"));
        return ibv;
    }

    public InformationView getLytLineaPeaton(Context context, DatabaseHelperTransmiSitp db, int iAppID, double iPeatMul, boolean bFinal, int iTime, String strColor) {
        int iWeight;
        db.openDataBase(); 
        String sStationDestino = db.getStationNameAddress(this._estacionDestino);
        if (iTime > 0) {
            iWeight = iTime;
        } else {
            iWeight = ((int) Math.round(db.getFactorPeatonal(this._estacionOrigen, this._estacionDestino) * iPeatMul)) + 1;
        }
        String sText = "Caminar (Aprox: <b><font color=\"" + strColor + "\">" + String.valueOf(iWeight) + "</font></b> min) hasta<br /><b><font color=\"" + strColor + "\">" + sStationDestino + "</font></b>";
        if (!bFinal) {
            sText = sText + "<br />Despu\u00e9s";
        }
        InformationView iv = new InformationView(context);
        iv.setImgInfo(R.drawable.zzz_walk);
        iv.setInfoText(Html.fromHtml(sText));
        return iv;
    }

    public InformationView getLytLineaPeatonInicioFin(Context context, int iAppID) {
        String sStationDestino = XmlPullParser.NO_NAMESPACE;
        String sText = "Caminar hasta <b>" + new DbUtils(context, iAppID).getStationNameAddress(this._estacionDestino) + "</b><br /><b>Fin del recorrido</b>";
        InformationView iv = new InformationView(context);
        iv.setImgInfo(R.drawable.zzz_walk);
        iv.getImgInfo2().setVisibility(8);
        iv.setInfoText(Html.fromHtml(sText));
        return iv;
    }

    public InformationView getEstacionOrigenView(Context context, DatabaseHelperTransmiSitp db, int iAppID, int iEstacionOrigen, String strColor) {
        String sEstInt = "Inicio en <b><font color=\"" + strColor + "\">XXX</font></b>YYY";
        InformationView iv = new InformationView(context);
      //  iv.setImgInfo(R.drawable.zzz_map_marker_radius);
        db.openDataBase();
        iv.setInfoText(Html.fromHtml(db.getMsgEstacionBus(sEstInt, iEstacionOrigen, this._id, false, false)));
        db.close();
        return iv;
    }

    public InformationView getEstacionDestinoView(Context context, DatabaseHelperTransmiSitp db, int iAppID, int iEstacionDestino, boolean bCaminataFinal) {
        InformationView iv = new InformationView(context);
        db.openDataBase();
        iv.setImgInfo(R.drawable.zzz_clock_end);
        iv.setInfoText(Html.fromHtml(db.getMsgEstacionBus("Llegar a <b>XXX</b>YYY <b>Fin del recorrido</b>", iEstacionDestino, this._id, true, bCaminataFinal)));
        db.close();
        return iv;
    }

    public static ArrayList<RecorridoBus> sortByTime(Context context, DatabaseHelperTransmiSitp db, int iAppID, int iPeatMul, ArrayList<RecorridoBus> arb) {
        Iterator it = arb.iterator();
        while (it.hasNext()) {
            ((RecorridoBus) it.next()).calcularTiempo(context, db, iAppID, (double) iPeatMul);
        }
        Collections.sort(arb, new C07141());
        return arb;
    }
}
