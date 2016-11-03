package co.cristiangarcia.hdtransmileniopro.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.text.format.Time;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xmlpull.v1.XmlPullParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

import co.cristiangarcia.hdtransmileniopro.objects.BusView;
import co.cristiangarcia.hdtransmileniopro.objects.HorarioItem;
import co.cristiangarcia.hdtransmileniopro.objects.MyItem;
import co.cristiangarcia.hdtransmileniopro.objects.PuntoRecargaView;
import co.cristiangarcia.hdtransmileniopro.objects.StationView;
import co.cristiangarcia.hdtransmileniopro.objects.Troncal;
import co.cristiangarcia.hdtransmileniopro.objects.UbicacionMapa;
import co.cristiangarcia.hdtransmileniopro.util.IntegerFloat;
import co.cristiangarcia.hdtransmileniopro.util.RecorridoBus;
import co.cristiangarcia.hdtransmileniopro.util.RecorridoRuta;

/**
 * Created by cristiangarcia on 22/10/16.
 */



public class DatabaseAccess extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION_RMETRODF = 10;
    private static final int DATABASE_VERSION_RMIO = 19;
    private static final int DATABASE_VERSION_TS = 131;
    private static final int DATABASE_VERSION_XAPASTO = 1;
    protected String DB_PATH;
    private int appID;
    private boolean bUpgradeDatabase;
    protected Context context;
    private int iNewVersion;
    private SQLiteDatabase myDataBase;
    protected String sDataBaseName;


    public DatabaseAccess(Context context, int iAppID) {
        super(context, "transmi_sitp", null, 1);
        this.bUpgradeDatabase = false;
        this.iNewVersion = 0;
        this.context = context;
        String str2 = iAppID == DATABASE_VERSION_XAPASTO ? "transmi_sitp" : iAppID == 2 ? "rutasmio" : iAppID == 3 ? "rutasmetrodf" : "movilixa_pasto";
        this.sDataBaseName = str2;
        str2 = iAppID == DATABASE_VERSION_XAPASTO ? "transmi_sitp" : iAppID == 2 ? "rutasmio" : iAppID == 3 ? "rutasmetrodf" : "movilixa_pasto";
        this.DB_PATH = context.getDatabasePath(str2).getPath();
        this.appID = iAppID;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = this.context.getAssets().open(this.sDataBaseName);
        OutputStream myOutput = new FileOutputStream(this.DB_PATH);
        byte[] buffer = new byte[1024];
        while (true) {
            int length = myInput.read(buffer);
            if (length > 0) {
                myOutput.write(buffer, 0, length);
            } else {
                myOutput.flush();
                myOutput.close();
                myInput.close();
                return;
            }
        }
    }

    public void openDataBase() throws SQLiteException {
        this.myDataBase = SQLiteDatabase.openDatabase(this.DB_PATH, null, DATABASE_VERSION_XAPASTO);
    }

    public synchronized void close() {
        if (this.myDataBase != null) {
            this.myDataBase.close();
        }
        super.close();
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onOpen(SQLiteDatabase db) {
        if (this.bUpgradeDatabase) {
            if (db != null) {
                if (db.isOpen()) {
                    db.close();
                }
            }
            this.context.deleteDatabase(this.sDataBaseName);
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            db = SQLiteDatabase.openDatabase(this.DB_PATH, null, 0);
            this.bUpgradeDatabase = false;
            db.setVersion(this.iNewVersion);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.bUpgradeDatabase = true;
        this.iNewVersion = newVersion;
    }


    public List<StationView> getAllStations(int appID, int position) throws Throwable {
        String SELECT_QUERY = XmlPullParser.NO_NAMESPACE;
        if (appID == DATABASE_VERSION_XAPASTO || appID == 2 || appID == 4) {
            if (position == 0) {
                SELECT_QUERY = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id ORDER BY e.name";
            } else if (position == DATABASE_VERSION_XAPASTO) {
                SELECT_QUERY = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.fk_troncal > 0 ORDER BY e.name";
            } else if (position == 2) {
                SELECT_QUERY = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.fk_troncal < 1 ORDER BY e.name";
            }
        } else if (appID == 3) {
            if (position == 0) {
                SELECT_QUERY = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, t.fk_agency FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id ORDER BY e.name";
            } else if (position == DATABASE_VERSION_XAPASTO) {
                SELECT_QUERY = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, t.fk_agency FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE t.fk_agency = 'METRO' ORDER BY e.name";
            } else if (position == 2) {
                SELECT_QUERY = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, t.fk_agency FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE t.fk_agency = 'MB' ORDER BY e.name";
            } else if (position == 3) {
                SELECT_QUERY = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, t.fk_agency FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE t.fk_agency = 'SUB' ORDER BY e.name";
            }
        }
        return armaListaEstaciones(this.myDataBase.rawQuery(SELECT_QUERY, null), DATABASE_VERSION_XAPASTO);
    }

    public List<StationView> getAllStations() throws Throwable {
        String SELECT_QUERY = XmlPullParser.NO_NAMESPACE;
        SELECT_QUERY = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, e.latitud, e.longitud, t.color FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.fk_troncal > 0 ORDER BY e.name";

        return armaListaEstaciones(this.myDataBase.rawQuery(SELECT_QUERY, null), DATABASE_VERSION_XAPASTO);
    }


    public List<StationView> getAllStations(int stopType,String zona, String busqueda) throws Throwable {



        String SELECT_QUERY = XmlPullParser.NO_NAMESPACE;

        if(busqueda.isEmpty()){
            SELECT_QUERY = "SELECT e.pk_id as _id, e.fk_troncal, t.description as troncalDescription, t.name as troncalName,t.color as troncalColor, e.name, e.description, e.latitud, e.longitud, t.color FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.fk_stopType ="+stopType+" AND t.name LIKE \"%"+zona+"%\" ORDER BY e.name";

        }else{
            SELECT_QUERY = "SELECT e.pk_id as _id, e.fk_troncal, t.description as troncalDescription, t.name as troncalName,t.color as troncalColor, e.name, e.description, e.latitud, e.longitud, t.color FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.fk_stopType ="+stopType+" AND t.name LIKE \"%"+zona+"%\" AND e.clearname LIKE \"%"+busqueda+"%\" ORDER BY e.name";

        }

        Log.v("this.myDataBaseaccess",SELECT_QUERY);

        return armaListaEstaciones(this.myDataBase.rawQuery(SELECT_QUERY, null), DATABASE_VERSION_XAPASTO);
    }

    public List<Troncal> getTroncales() {
        List<Troncal> result = new ArrayList();
        Cursor cursor = this.myDataBase.rawQuery("select pk_id, name, description, color, subDescription from troncal where pk_id>0", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    result.add(new Troncal(cursor.getInt(0), cursor.getString(DATABASE_VERSION_XAPASTO), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                } while (cursor.moveToNext());
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getTroncales from this.myDataBase");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public List<StationView> getStationsTroncal(int id) throws Throwable {
        return armaListaEstaciones(this.myDataBase.rawQuery("SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id  where fk_troncal = " + String.valueOf(id) + "  ORDER BY e.pk_id", null), DATABASE_VERSION_XAPASTO);
    }

    public List<StationView> searchAllStationsByName(String strSearch, int position) throws Throwable {
        Cursor cursor = null;
        String[] strArr;
        if (this.appID == DATABASE_VERSION_XAPASTO || this.appID == 2 || this.appID == 4) {
            if (position == 0) {
                strArr = new String[DATABASE_VERSION_XAPASTO];
                strArr[0] = "%" + getStringWithoutAccent(strSearch) + "%";
                cursor = this.myDataBase.rawQuery("SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.name || ' ' || e.description || ' ' || e.clearname LIKE ? ORDER BY e.name", strArr);
            } else if (position == DATABASE_VERSION_XAPASTO) {
                strArr = new String[DATABASE_VERSION_XAPASTO];
                strArr[0] = "%" + getStringWithoutAccent(strSearch) + "%";
                cursor = this.myDataBase.rawQuery("SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.fk_troncal > 0 and ( e.name || ' ' || e.description || ' ' || e.clearname LIKE ?) ORDER BY e.name", strArr);
            } else if (position == 2) {
                strArr = new String[DATABASE_VERSION_XAPASTO];
                strArr[0] = "%" + getStringWithoutAccent(strSearch) + "%";
                cursor = this.myDataBase.rawQuery("SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.fk_troncal < 1 and ( e.name || ' ' || e.description || ' ' || e.clearname LIKE ?) ORDER BY e.name", strArr);
            }
        } else if (this.appID == 3) {
            if (position == 0) {
                strArr = new String[DATABASE_VERSION_XAPASTO];
                strArr[0] = "%" + getStringWithoutAccent(strSearch) + "%";
                cursor = this.myDataBase.rawQuery("SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, t.fk_agency FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.name || ' ' || e.description || ' ' || e.clearname LIKE ? ORDER BY e.name", strArr);
            } else if (position == DATABASE_VERSION_XAPASTO) {
                strArr = new String[DATABASE_VERSION_XAPASTO];
                strArr[0] = "%" + getStringWithoutAccent(strSearch) + "%";
                cursor = this.myDataBase.rawQuery("SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, t.fk_agency FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE t.fk_agency = 'METRO' and (e.name || ' ' || e.description || ' ' || e.clearname LIKE ?) ORDER BY e.name", strArr);
            } else if (position == 2) {
                strArr = new String[DATABASE_VERSION_XAPASTO];
                strArr[0] = "%" + getStringWithoutAccent(strSearch) + "%";
                cursor = this.myDataBase.rawQuery("SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, t.fk_agency FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE t.fk_agency = 'MB' and (e.name || ' ' || e.description || ' ' || e.clearname LIKE ?) ORDER BY e.name", strArr);
            } else if (position == 3) {
                strArr = new String[DATABASE_VERSION_XAPASTO];
                strArr[0] = "%" + getStringWithoutAccent(strSearch) + "%";
                cursor = this.myDataBase.rawQuery("SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, t.fk_agency FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE t.fk_agency = 'SUB' and (e.name || ' ' || e.description || ' ' || e.clearname LIKE ?) ORDER BY e.name", strArr);
            }
        }
        return armaListaEstaciones(cursor, DATABASE_VERSION_XAPASTO);
    }

    public List<StationView> getStationByGps(Double latitude, Double longitude, int position) throws Throwable {
        String strQuery = "SELECT pk_id as _id, latitud, longitud FROM estacion";
        Cursor cLocations = this.myDataBase.rawQuery(strQuery, null);
        ArrayList<IntegerFloat> dis = new ArrayList();
        Location aLocation = new Location("Point A");
        aLocation.setLatitude(latitude.doubleValue());
        aLocation.setLongitude(longitude.doubleValue());
        if (cLocations.moveToFirst()) {
            do {
                float fLat = cLocations.getFloat(cLocations.getColumnIndex("latitud"));
                float fLon = cLocations.getFloat(cLocations.getColumnIndex("longitud"));
                if (!(fLat == 0.0f || fLon == 0.0f)) {
                    Location bLocation = new Location("Point B");
                    bLocation.setLatitude((double) fLat);
                    bLocation.setLongitude((double) fLon);
                    dis.add(new IntegerFloat(cLocations.getInt(cLocations.getColumnIndex("_id")), aLocation.distanceTo(bLocation)));
                }
            } while (cLocations.moveToNext());
        }
        cLocations.close();
        dis = IntegerFloat.sort(dis);
        String sUnion = ((((((("SELECT " + ((IntegerFloat) dis.get(0)).name + " as fk_estacion, " + ((IntegerFloat) dis.get(0)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(DATABASE_VERSION_XAPASTO)).name + " as fk_estacion, " + ((IntegerFloat) dis.get(DATABASE_VERSION_XAPASTO)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(2)).name + " as fk_estacion, " + ((IntegerFloat) dis.get(2)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(3)).name + " as fk_estacion, " + ((IntegerFloat) dis.get(3)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(4)).name + " as fk_estacion, " + ((IntegerFloat) dis.get(4)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(5)).name + " as fk_estacion, " + ((IntegerFloat) dis.get(5)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(6)).name + " as fk_estacion, " + ((IntegerFloat) dis.get(6)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(7)).name + " as fk_estacion, " + ((IntegerFloat) dis.get(7)).value + " as distancia";
        if (position == 0) {
            strQuery = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, u.distancia FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id INNER JOIN (" + sUnion + ") u ON e.pk_id = u.fk_estacion ORDER BY u.distancia";
        } else if (position == DATABASE_VERSION_XAPASTO) {
            strQuery = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, u.distancia FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id INNER JOIN (" + sUnion + ") u ON e.pk_id = u.fk_estacion WHERE e.fk_troncal > 0 ORDER BY u.distancia";
        } else if (position == 2) {
            strQuery = "SELECT e.pk_id as _id, e.fk_troncal, t.name as troncalName, e.name, e.description, t.color, u.distancia FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id INNER JOIN (" + sUnion + ") u ON e.pk_id = u.fk_estacion WHERE e.fk_troncal < 1 ORDER BY u.distancia";
        }
        return armaListaEstaciones(this.myDataBase.rawQuery(strQuery, null), 2);
    }

    public StationView getStation(int _estacionId) throws Throwable {
        Throwable th;
        StationView stationView = null;
        Cursor c = this.myDataBase.rawQuery("SELECT pk_id as _id, latitud, longitud, name, description as direccion, fk_troncal as troncal FROM estacion WHERE pk_id = " + String.valueOf(_estacionId), null);
        try {
            if (c.moveToFirst()) {
                StationView result = new StationView();
                try {
                    result.setId(c.getInt(0));
                    result.setLatitud(c.getDouble(DATABASE_VERSION_XAPASTO));
                    result.setLongitud(c.getDouble(2));
                    result.setName(c.getString(3));
                    result.setDescription(c.getString(4));
                    result.setTroncal(c.getInt(5));
                    stationView = result;
                } catch (Exception e) {
                    stationView = result;
                    try {
                        Log.d(DatabaseAccess.class.toString(), "Error while trying to getStation from database");
                        c.close();
                        return stationView;
                    } catch (Throwable th2) {
                        th = th2;
                        if (!(c == null || c.isClosed())) {
                            c.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    stationView = result;
                    c.close();
                    throw th;
                }
            }
            if (!(c == null || c.isClosed())) {
                c.close();
            }
        } catch (Exception e2) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getStation from database");
            if (!(c == null || c.isClosed())) {
                c.close();
            }
            return stationView;
        }
        return stationView;
    }

    public String getStationName(String stationId) {
        String result = "Ubicaci\u00f3n Seleccionada";
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = stationId;
        Cursor cursor = this.myDataBase.rawQuery("SELECT name FROM estacion WHERE pk_id = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getStationName from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public String getListStringNearStation(Location myLocation, float distance, String stopTypeIds) {
        String dis = XmlPullParser.NO_NAMESPACE;
        String sQuery = "SELECT pk_id as _id, latitud, longitud FROM estacion";
        if (stopTypeIds.length() > 0) {
            sQuery = sQuery + " WHERE fk_stopType in (" + stopTypeIds + ")";
        }
        Cursor cursor = this.myDataBase.rawQuery(sQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    float fLat = cursor.getFloat(cursor.getColumnIndex("latitud"));
                    float fLon = cursor.getFloat(cursor.getColumnIndex("longitud"));
                    if (!(fLat == 0.0f || fLon == 0.0f)) {
                        Location bLocation = new Location("Point B");
                        bLocation.setLatitude((double) fLat);
                        bLocation.setLongitude((double) fLon);
                        if (myLocation.distanceTo(bLocation) < distance) {
                            dis = dis.equals(XmlPullParser.NO_NAMESPACE) ? cursor.getString(cursor.getColumnIndex("_id")) : dis + "," + cursor.getString(cursor.getColumnIndex("_id"));
                        }
                    }
                } while (cursor.moveToNext());
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getListStringNearStation from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return dis;
    }

    public ArrayList<IntegerFloat> getListNearStation(Location myLocation, float distance, String stopTypeIds) {
        String sQuery = "SELECT pk_id as _id, latitud, longitud FROM estacion";
        if (stopTypeIds.length() > 0) {
            sQuery = sQuery + " WHERE fk_stopType in (" + stopTypeIds + ")";
        }
        Cursor cLocations = this.myDataBase.rawQuery(sQuery, null);
        ArrayList<IntegerFloat> dis = new ArrayList();
        try {
            if (cLocations.moveToFirst()) {
                do {
                    float fLat = cLocations.getFloat(cLocations.getColumnIndex("latitud"));
                    float fLon = cLocations.getFloat(cLocations.getColumnIndex("longitud"));
                    if (!(fLat == 0.0f || fLon == 0.0f)) {
                        Location bLocation = new Location("Point B");
                        bLocation.setLatitude((double) fLat);
                        bLocation.setLongitude((double) fLon);
                        float fDis = myLocation.distanceTo(bLocation);
                        if (fDis < distance) {
                            dis.add(new IntegerFloat(cLocations.getInt(cLocations.getColumnIndex("_id")), fDis));
                        }
                    }
                } while (cLocations.moveToNext());
            }
            if (!(cLocations == null || cLocations.isClosed())) {
                cLocations.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getListNearStation from database");
            if (!(cLocations == null || cLocations.isClosed())) {
                cLocations.close();
            }
        } catch (Throwable th) {
            if (!(cLocations == null || cLocations.isClosed())) {
                cLocations.close();
            }
        }
        return dis;
    }

    public String getMsgEstacionBus(String sPlantilla, int iEstacion, int iBus, boolean bFinal, boolean bCaminataFinal) {
        String ret = sPlantilla;
        String sStationTemp = XmlPullParser.NO_NAMESPACE;
        String sStation = XmlPullParser.NO_NAMESPACE;
        Cursor cInter;
        if (iBus == -2) {
            cInter = this.myDataBase.rawQuery("SELECT e.name as name, ec.fk_estacionOrigen as connection from estacion e inner join estacion_conexion ec on e.pk_id = ec.fk_estacionDestino WHERE ec.fk_tipoConexion = 1 AND ec.fk_estacionDestino = ? UNION SELECT e.name as name, ec.fk_estacionDestino as connection from estacion e inner join estacion_conexion ec on e.pk_id = ec.fk_estacionOrigen WHERE ec.fk_tipoConexion = 1 AND ec.fk_estacionOrigen = ?", new String[]{XmlPullParser.NO_NAMESPACE + iEstacion, XmlPullParser.NO_NAMESPACE + iEstacion});
            try {
                if (cInter.moveToFirst()) {
                    sStation = cInter.getString(cInter.getColumnIndex(MovilixaConstants.NAME));
                    iEstacion = cInter.getInt(cInter.getColumnIndex("connection"));
                }
                if (!(cInter == null || cInter.isClosed())) {
                    cInter.close();
                }
            } catch (Exception e) {
                Log.d(DatabaseAccess.class.toString(), "Error while trying to getMsgEstacionBus from database");
                if (!(cInter == null || cInter.isClosed())) {
                    cInter.close();
                }
            } catch (Throwable th) {
                if (!(cInter == null || cInter.isClosed())) {
                    cInter.close();
                }
            }
            if (bFinal) {
                return ret.replace("XXX", getStationNameAddress(iEstacion)).replace("YYY", "<br />Caminar a <b>" + sStation + "</b>");
            }
            return ret.replace("XXX", sStation).replace("YYY", "<br />Caminar a <b>" + getStationNameAddress(iEstacion) + "</b>");
        } else if (iBus <= 0) {
            return ret.replace("XXX", getStationNameAddress(iEstacion)).replace("YYY", XmlPullParser.NO_NAMESPACE);
        } else {
            cInter = this.myDataBase.rawQuery("select e.name as name, be.show as show, be.connection as connection from bus b inner join bus_estacion be on b.pk_id = be.fk_bus inner join estacion e on be.fk_estacion = e.pk_id WHERE e.pk_id = ? AND b.pk_id = ?", new String[]{XmlPullParser.NO_NAMESPACE + iEstacion, XmlPullParser.NO_NAMESPACE + iBus});
            try {
                if (cInter.moveToFirst()) {
                    int iShow = cInter.getInt(cInter.getColumnIndex("show"));
                    sStationTemp = cInter.getString(cInter.getColumnIndex(MovilixaConstants.NAME));
                    if (iShow > 0) {
                        sStation = cInter.getString(cInter.getColumnIndex(MovilixaConstants.NAME));
                    } else {
                        iEstacion = cInter.getInt(cInter.getColumnIndex("connection"));
                    }
                }
                if (!(cInter == null || cInter.isClosed())) {
                    cInter.close();
                }
            } catch (Exception e2) {
                Log.d(DatabaseAccess.class.toString(), "Error while trying to getMsgEstacionBus from database");
                if (!(cInter == null || cInter.isClosed())) {
                    cInter.close();
                }
            } catch (Throwable th2) {
                if (!(cInter == null || cInter.isClosed())) {
                    cInter.close();
                }
            }
            if (sStation.equals(XmlPullParser.NO_NAMESPACE)) {
                if (!bFinal) {
                    return ret.replace("XXX", sStationTemp).replace("YYY", "<br />Caminar a <b>" + getStationNameAddress(iEstacion) + "</b>");
                }
                if (bCaminataFinal) {
                    return ret.replace("XXX", getStationNameAddress(iEstacion)).replace("YYY", "<br />Caminar a <b>" + getStationNameAddress(0) + "</b>");
                }
                return ret.replace("XXX", getStationNameAddress(iEstacion)).replace("YYY", "<br />Caminar a <b>" + sStationTemp + "</b>");
            } else if (bCaminataFinal) {
                return ret.replace("XXX", sStation).replace("YYY", "<br />Caminar a <b>" + getStationNameAddress(0) + "</b>");
            } else {
                return ret.replace("XXX", sStation).replace("YYY", XmlPullParser.NO_NAMESPACE);
            }
        }
    }

    public String getStationNameAddress(int stationId) {
        String sStationName = XmlPullParser.NO_NAMESPACE;
        String sStationAddress = XmlPullParser.NO_NAMESPACE;
        if (stationId == 0) {
            return "Ubicaci\u00f3n Seleccionada";
        }
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = XmlPullParser.NO_NAMESPACE + stationId;
        Cursor cursor = this.myDataBase.rawQuery("SELECT name, description FROM estacion WHERE pk_id = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                sStationName = cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME));
                sStationAddress = cursor.getString(cursor.getColumnIndex(ShareConstants.WEB_DIALOG_PARAM_DESCRIPTION));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getStationNameAddress from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        if (sStationName.equals(sStationAddress)) {
            return sStationName;
        }
        return sStationName + " (" + sStationAddress + ")";
    }

    public int calculaParadas(int stationDestino, int stationOrigen, int id) {
        Cursor cParadas = this.myDataBase.rawQuery("SELECT _id, count(fk_bus) as nParadas FROM (SELECT b1.pk_id as _id, be1.rowid as rowInicial, beTemp1.rowid as rowFinal FROM bus b1 INNER JOIN bus_estacion beTemp1 ON b1.pk_id = beTemp1.fk_bus INNER JOIN bus_estacion be1 ON b1.pk_id = be1.fk_bus WHERE be1.show = 1 AND beTemp1.show = 1 AND beTemp1.rowid > be1.rowid AND beTemp1.fk_estacion = ? AND be1.fk_estacion = ?) q1 INNER JOIN bus_estacion be ON q1._id = be.fk_bus WHERE be.show = 1 AND _id = ? AND be.rowid > q1.rowInicial AND be.rowid <= q1.rowFinal GROUP BY _id", new String[]{String.valueOf(stationDestino), String.valueOf(stationOrigen), String.valueOf(id)});
        if (cParadas.moveToFirst()) {
            return cParadas.getInt(cParadas.getColumnIndex("nParadas"));
        }
        return 0;
    }

    public int calculaDistancia(int stationDestino, int stationOrigen, int id) {
        Cursor cParadas = this.myDataBase.rawQuery("SELECT _id, sum(distance) as nDistancia FROM (SELECT b1.pk_id as _id, be1.rowid as rowInicial, beTemp1.rowid as rowFinal FROM bus b1 INNER JOIN bus_estacion beTemp1 ON b1.pk_id = beTemp1.fk_bus INNER JOIN bus_estacion be1 ON b1.pk_id = be1.fk_bus WHERE be1.show = 1 AND beTemp1.show = 1 AND beTemp1.rowid > be1.rowid AND beTemp1.fk_estacion = ? AND be1.fk_estacion = ?) q1 INNER JOIN bus_estacion be ON q1._id = be.fk_bus WHERE be.show = 1 AND _id = ? AND be.rowid > q1.rowInicial AND be.rowid <= q1.rowFinal GROUP BY _id", new String[]{String.valueOf(stationDestino), String.valueOf(stationOrigen), String.valueOf(id)});
        if (cParadas.moveToFirst()) {
            return cParadas.getInt(cParadas.getColumnIndex("nDistancia"));
        }
        return 0;
    }

    public int calculaDistanciaPeatonal(int stationDestino, int stationOrigen, int id) {
        int iMin;
        int iMax;
        if (stationOrigen > stationDestino) {
            iMin = stationDestino;
            iMax = stationOrigen;
        } else {
            iMin = stationOrigen;
            iMax = stationDestino;
        }
        Cursor cParadas = this.myDataBase.rawQuery("SELECT w as nDistancia FROM estacion_conexion ec WHERE ec.fk_estacionOrigen = ? AND ec.fk_estacionDestino = ?", new String[]{String.valueOf(iMin), String.valueOf(iMax)});
        if (cParadas.moveToFirst()) {
            return cParadas.getInt(cParadas.getColumnIndex("nDistancia"));
        }
        return 0;
    }

    public float calculaDistanciaUbicacion(int stationDestino, int stationOrigen) throws Throwable {
        float fLat;
        float fLon;
        Location secondLocation;
        Throwable th;
        Location location = null;
        Location location2 = null;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = String.valueOf(stationDestino);
        Cursor cLocation = this.myDataBase.rawQuery("SELECT latitud, longitud FROM estacion WHERE pk_id = ?", strArr);
        try {
            if (cLocation.moveToFirst()) {
                fLat = cLocation.getFloat(cLocation.getColumnIndex("latitud"));
                fLon = cLocation.getFloat(cLocation.getColumnIndex("longitud"));
                if (!(fLat == 0.0f || fLon == 0.0f)) {
                    Location firstLocation = new Location("Point A");
                    try {
                        firstLocation.setLatitude((double) fLat);
                        firstLocation.setLongitude((double) fLon);
                        location = firstLocation;
                    } catch (Exception e) {
                        location = firstLocation;
                        try {
                            Log.d(DatabaseAccess.class.toString(), "Error while trying to calculaDistanciaUbicacion from database");
                            if (!(cLocation == null || cLocation.isClosed())) {
                                cLocation.close();
                            }
                            strArr = new String[DATABASE_VERSION_XAPASTO];
                            strArr[0] = String.valueOf(stationOrigen);
                            cLocation = this.myDataBase.rawQuery("SELECT latitud, longitud FROM estacion WHERE pk_id = ?", strArr);
                            if (cLocation.moveToFirst()) {
                                fLat = cLocation.getFloat(cLocation.getColumnIndex("latitud"));
                                fLon = cLocation.getFloat(cLocation.getColumnIndex("longitud"));
                                secondLocation = new Location("Point B");
                                try {
                                    secondLocation.setLatitude((double) fLat);
                                    secondLocation.setLongitude((double) fLon);
                                    location2 = secondLocation;
                                } catch (Exception e2) {
                                    location2 = secondLocation;
                                    try {
                                        Log.d(DatabaseAccess.class.toString(), "Error while trying to calculaDistanciaUbicacion from database");
                                        cLocation.close();
                                        if (location != null) {
                                        }
                                        return 0.0f;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        if (!(cLocation == null || cLocation.isClosed())) {
                                            cLocation.close();
                                        }
                                        throw th;
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    location2 = secondLocation;
                                    cLocation.close();
                                    throw th;
                                }
                            }
                            cLocation.close();
                            if (location != null) {
                            }
                            return 0.0f;
                        } catch (Throwable th4) {
                            th = th4;
                            cLocation.close();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        location = firstLocation;
                        if (!(cLocation == null || cLocation.isClosed())) {
                            cLocation.close();
                        }
                        throw th;
                    }
                }
            }
            if (!(cLocation == null || cLocation.isClosed())) {
                cLocation.close();
            }
        } catch (Exception e3) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to calculaDistanciaUbicacion from database");
            cLocation.close();
            strArr = new String[DATABASE_VERSION_XAPASTO];
            strArr[0] = String.valueOf(stationOrigen);
            cLocation = this.myDataBase.rawQuery("SELECT latitud, longitud FROM estacion WHERE pk_id = ?", strArr);
            if (cLocation.moveToFirst()) {
                fLat = cLocation.getFloat(cLocation.getColumnIndex("latitud"));
                fLon = cLocation.getFloat(cLocation.getColumnIndex("longitud"));
                secondLocation = new Location("Point B");
                secondLocation.setLatitude((double) fLat);
                secondLocation.setLongitude((double) fLon);
                location2 = secondLocation;
            }
            cLocation.close();
            if (location != null) {
            }
            return 0.0f;
        }
        strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = String.valueOf(stationOrigen);
        cLocation = this.myDataBase.rawQuery("SELECT latitud, longitud FROM estacion WHERE pk_id = ?", strArr);
        try {
            if (cLocation.moveToFirst()) {
                fLat = cLocation.getFloat(cLocation.getColumnIndex("latitud"));
                fLon = cLocation.getFloat(cLocation.getColumnIndex("longitud"));
                if (!(fLat == 0.0f || fLon == 0.0f)) {
                    secondLocation = new Location("Point B");
                    secondLocation.setLatitude((double) fLat);
                    secondLocation.setLongitude((double) fLon);
                    location2 = secondLocation;
                }
            }
            if (!(cLocation == null || cLocation.isClosed())) {
                cLocation.close();
            }
        } catch (Exception e4) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to calculaDistanciaUbicacion from database");
            if (!(cLocation == null || cLocation.isClosed())) {
                cLocation.close();
            }
            if (location != null) {
            }
            return 0.0f;
        }
        if (location != null || location2 == null) {
            return 0.0f;
        }
        return location.distanceTo(location2);
    }

    public double getFactorPeatonal(int iEstacionOrigen, int iEstacionDestino) {
        double dFactor = 0.0d;
        Cursor cursor = this.myDataBase.rawQuery("SELECT w, fk_tipoConexion FROM estacion_conexion WHERE fk_estacionOrigen = ? AND fk_estacionDestino = ? UNION SELECT w, fk_tipoConexion FROM estacion_conexion WHERE fk_estacionDestino = ? AND fk_estacionOrigen = ?", new String[]{XmlPullParser.NO_NAMESPACE + iEstacionOrigen, XmlPullParser.NO_NAMESPACE + iEstacionDestino, XmlPullParser.NO_NAMESPACE + iEstacionOrigen, XmlPullParser.NO_NAMESPACE + iEstacionDestino});
        try {
            if (cursor.moveToFirst()) {
                dFactor = cursor.getDouble(cursor.getColumnIndex("w"));
            } else {
                dFactor = (double) calculaDistanciaUbicacion(iEstacionOrigen, iEstacionDestino);
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getFactorPeatonal from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return dFactor;
    }

    public boolean getSonTroncales(int iEstacionOrigen, int iEstacionDestino) {
        boolean bSonTroncales = false;
        Cursor cursor = this.myDataBase.rawQuery("SELECT fk_troncal FROM estacion WHERE (pk_id = ? OR pk_id = ?) AND fk_troncal>0", new String[]{XmlPullParser.NO_NAMESPACE + iEstacionOrigen, XmlPullParser.NO_NAMESPACE + iEstacionDestino});
        try {
            if (cursor.moveToFirst() && cursor.moveToNext()) {
                bSonTroncales = true;
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getSonTroncales from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return bSonTroncales;
    }

    public Location getLocationStation(int stationId) throws Throwable {
        Throwable th;
        Location location = null;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = XmlPullParser.NO_NAMESPACE + stationId;
        Cursor cursor = this.myDataBase.rawQuery("SELECT longitud, latitud FROM estacion WHERE pk_id = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                Location ret = new Location(String.valueOf(stationId));
                try {
                    ret.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitud")));
                    ret.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitud")));
                    location = ret;
                } catch (Exception e) {
                    location = ret;
                    try {
                        Log.d(DatabaseAccess.class.toString(), "Error while trying to getLocationStation from database");
                        if (!(cursor == null || cursor.isClosed())) {
                            cursor.close();
                        }
                        return location;
                    } catch (Throwable th2) {
                        th = th2;
                        cursor.close();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    location = ret;
                    if (!(cursor == null || cursor.isClosed())) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e2) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getLocationStation from database");
            cursor.close();
            return location;
        }
        return location;
    }

    public String getBusesWithOriginAndDestinationInMio(String listOrigin, String listDestination, String sWhere) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT DISTINCT b.pk_id FROM bus b INNER JOIN bus_horario bh ON b.pk_id = bh.fk_bus INNER JOIN bus_estacion be1 ON b.pk_id = be1.fk_bus INNER JOIN bus_estacion be2 ON b.pk_id = be2.fk_bus WHERE be1.show = 1 AND be2.show = 1 AND be1.fk_estacion IN (" + listOrigin + ") AND be2.fk_estacion IN (" + listDestination + ") AND " + sWhere, null);
        String ret = XmlPullParser.NO_NAMESPACE;
        try {
            if (cursor.moveToFirst()) {
                do {
                    ret = ret.equals(XmlPullParser.NO_NAMESPACE) ? cursor.getString(cursor.getColumnIndex("pk_id")) : ret + "," + cursor.getString(cursor.getColumnIndex("pk_id"));
                } while (cursor.moveToNext());
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getBusesWithOriginAndDestinationIn from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return ret;
    }

    public List<StationView> getStationsBus(int busId) throws Throwable {
        Throwable th;
        List<StationView> list = null;
        Cursor cursor = this.myDataBase.rawQuery("SELECT e.pk_id as _id, t.name as troncalName, e.name, e.description as address, t.color, be.show, be.fk_duplicado FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id INNER JOIN bus_estacion be ON e.pk_id = be.fk_estacion WHERE be.show!=0 AND be.fk_bus =" + String.valueOf(busId) + " ORDER BY be.rowid", null);
        try {
            if (cursor.moveToFirst()) {
                List<StationView> result = new ArrayList();
                do {
                    try {
                        StationView sv = new StationView();
                        sv.setName(cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME)));
                        sv.setDescription(cursor.getString(cursor.getColumnIndex("address")));
                        sv.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                        sv.setShow(cursor.getInt(cursor.getColumnIndex("show")));
                        sv.setTroncalName(cursor.getString(cursor.getColumnIndex("troncalName")));
                        sv.setColor(cursor.getString(cursor.getColumnIndex("color")));
                        sv.setDuplicated(cursor.getInt(cursor.getColumnIndex("fk_duplicado")) != 0);
                        result.add(sv);
                    } catch (Exception e) {
                        list = result;
                    } catch (Throwable th2) {
                        th = th2;
                        list = result;
                    }
                } while (cursor.moveToNext());
                list = result;
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e2) {
            try {
                Log.d(DatabaseAccess.class.toString(), "Error while trying to getStationsBus from database");
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
                return list;
            } catch (Throwable th3) {
                th = th3;
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
                throw th;
            }
        }
        return list;
    }

    public List<StationView> getStationsSearch(String strSearch, String strAgency) throws Throwable {
        Throwable th;
        List<StationView> list = null;
        String strQuery = XmlPullParser.NO_NAMESPACE;
        if (this.appID == DATABASE_VERSION_XAPASTO) {
            if (strAgency.equals("TRANSMILENIO")) {
                strQuery = "SELECT e.pk_id as _id, e.pk_id, e.name as name, e.description as address, t.name as troncalName, t.color as troncalColor FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.fk_stopType in(1,3) AND e.name || ' ' || e.description || ' ' || e.clearname like ?";
            } else if (strAgency.contains("SITP")) {
                strQuery = "SELECT e.pk_id as _id, e.pk_id, e.name as name, e.description as address FROM estacion e WHERE e.fk_stopType in(2,3) AND e.name || ' ' || e.description || ' ' || e.clearname like ? ORDER BY e.clearname ASC";
            }
        } else if (this.appID == 2) {
            strAgency = XmlPullParser.NO_NAMESPACE;
            strQuery = "SELECT e.pk_id as _id, e.pk_id, e.name as name, e.description as address, t.name as troncalName, t.color as troncalColor FROM estacion e INNER JOIN troncal t ON e.fk_troncal = t.pk_id WHERE e.name || ' ' || e.description || ' ' || e.clearname like ?";
        } else if (this.appID == 4) {
            strQuery = "SELECT e.pk_id as _id, e.pk_id, e.name as name, e.description as address FROM estacion e WHERE e.name || ' ' || e.description || ' ' || e.clearname like ? ORDER BY e.clearname ASC";
        }
        SQLiteDatabase sQLiteDatabase = this.myDataBase;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = "%" + strSearch + "%";
        Cursor cursor = sQLiteDatabase.rawQuery(strQuery, strArr);
        try {
            if (cursor.moveToFirst()) {
                List<StationView> result = new ArrayList();
                do {
                    try {
                        StationView sv = new StationView();
                        sv.setName(cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME)));
                        sv.setDescription(cursor.getString(cursor.getColumnIndex("address")));
                        sv.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                        if ((this.appID == DATABASE_VERSION_XAPASTO || this.appID == 2) && !strAgency.equals("SITP")) {
                            sv.setTroncalName(cursor.getString(cursor.getColumnIndex("troncalName")));
                            sv.setColor(cursor.getString(cursor.getColumnIndex("troncalColor")));
                        }
                        sv.setDistancia(-1.0d);
                        result.add(sv);
                    } catch (Exception e) {
                        list = result;
                    } catch (Throwable th2) {
                        th = th2;
                        list = result;
                    }
                } while (cursor.moveToNext());
                list = result;
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e2) {
            try {
                Log.d(DatabaseAccess.class.toString(), "Error while trying to getStationsBus from database");
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
                return list;
            } catch (Throwable th3) {
                th = th3;
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
                throw th;
            }
        }
        return list;
    }

    public List<StationView> armaListaEstaciones(Cursor cursor, int type) throws Throwable {
        Throwable th;
        List<StationView> list = null;
        try {
            if (cursor.moveToFirst()) {
                List<StationView> result = new ArrayList();
                do {
                    try {
                        StationView station = new StationView();
                        station.setId(cursor.getInt(0));
                        station.setTroncal(cursor.getInt(cursor.getColumnIndex("fk_troncal")));
                        station.setTroncalName(cursor.getString(cursor.getColumnIndex("troncalName")));
                        station.setName(cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME)));
                        station.setDescription(cursor.getString(cursor.getColumnIndex(ShareConstants.WEB_DIALOG_PARAM_DESCRIPTION)));
                        station.setColor(cursor.getString(5));
                        if (type == DATABASE_VERSION_XAPASTO) {
                            station.setDistancia(-1.0d);
                        } else if (type == 2) {
                            station.setDistancia(cursor.getDouble(cursor.getColumnIndex("distancia")));
                        }
                        result.add(station);
                    } catch (Exception e) {
                        list = result;
                    } catch (Throwable th2) {
                        th = th2;
                        list = result;
                    }
                } while (cursor.moveToNext());
                list = result;
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e2) {
            try {
                Log.d(DatabaseAccess.class.toString(), "Error while trying to armaListaEstaciones from database");
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
                return list;
            } catch (Throwable th3) {
                th = th3;
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
                throw th;
            }
        }
        return list;
    }

    public boolean isFestivo(String strFechaActual) {
        boolean result = false;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = strFechaActual;
        Cursor cursor = this.myDataBase.rawQuery("SELECT rowid as _id, pk_id FROM festivo WHERE pk_id = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                result = true;
            } else {
                result = false;
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to isFestivo from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public String getFirstAgency() {
        String result = XmlPullParser.NO_NAMESPACE;
        Cursor cursor = this.myDataBase.rawQuery("SELECT fk_agency FROM agency LIMIT 1", null);
        try {
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex("fk_agency"));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to get agency from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public boolean isSitp(String idBus) {
        boolean result = false;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = idBus;
        Cursor cursor = this.myDataBase.rawQuery("SELECT fk_agency as agency FROM bus WHERE pk_id = ?", strArr);
        try {
            if (!cursor.moveToFirst()) {
                result = false;
            } else if (cursor.getString(cursor.getColumnIndex("agency")).startsWith("SITP")) {
                result = true;
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to isSitp from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public boolean isSitpStation(String idStation) {
        boolean result = false;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = idStation;
        Cursor cursor = this.myDataBase.rawQuery("SELECT fk_troncal as troncal FROM estacion WHERE pk_id = ?", strArr);
        try {
            if (!cursor.moveToFirst()) {
                result = false;
            } else if (cursor.getInt(cursor.getColumnIndex("troncal")) <= 0) {
                result = true;
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to isSitp from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public String getTablaRuta(String idBus) {
        String result = XmlPullParser.NO_NAMESPACE;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = idBus;
        Cursor cursor = this.myDataBase.rawQuery("SELECT picture FROM bus WHERE pk_id = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(ShareConstants.WEB_DIALOG_PARAM_PICTURE));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getTablaRuta from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public String getBusExternalId(String busId) {
        String result = XmlPullParser.NO_NAMESPACE;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = busId;
        Cursor cursor = this.myDataBase.rawQuery("SELECT externalID FROM bus WHERE pk_id = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex("externalID"));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getBusExternalId from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public String getStationExternalId(String stationId) {
        String result = XmlPullParser.NO_NAMESPACE;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = stationId;
        Cursor cursor = this.myDataBase.rawQuery("SELECT externalID FROM estacion WHERE pk_id = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex("externalID"));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getStationExternalId from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public int getStationByExternalId(String externalId) {
        String result = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = externalId;
        Cursor cursor = this.myDataBase.rawQuery("SELECT pk_id FROM estacion WHERE externalID = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex("pk_id"));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getStationByExternalId from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return Integer.valueOf(result).intValue();
    }

    public int getStationByExternalIdContains(String externalId) {
        String result = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        Cursor cursor = this.myDataBase.rawQuery("SELECT pk_id FROM estacion WHERE externalID = ? OR externalID like ? OR externalID like ? OR externalID like ?", new String[]{externalId, externalId + ",%", "%," + externalId + ",%", "%," + externalId});
        try {
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex("pk_id"));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getStationByExternalIdContains from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return Integer.valueOf(result).intValue();
    }

    public int getRouteByExternalId(String externalId) {
        String result = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = externalId;
        Cursor cursor = this.myDataBase.rawQuery("SELECT pk_id FROM bus WHERE externalID = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex("pk_id"));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getRouteByExternalId from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return Integer.valueOf(result).intValue();
    }

    public List<BusView> getBuses(int appId, String agency, String strFechaActual, boolean esFestivo) {
        List<BusView> result = null;
        String SELECT_QUERY = XmlPullParser.NO_NAMESPACE;
        if (appId == DATABASE_VERSION_XAPASTO || appId == 3 || appId == 4) {
            SELECT_QUERY = "select DISTINCT dias, * from (select  b.pk_id as id, b.fk_agency as agency, b.name as name, b.color as color, b.description as description,  b.orden as orden, d.description as dias, bh.fk_dia as dia, bh.desde as desde, bh.hasta as hasta from bus b INNER JOIN bus_horario bh ON b.pk_id = bh.fk_bus INNER JOIN dia d ON bh.fk_dia = d.pk_id INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id where agency ='" + agency + "' order by orden)";
        } else if (appId == 2) {
            SELECT_QUERY = "select DISTINCT dias, * from (select  b.pk_id as id, b.fk_agency as agency, b.name as name, b.color as color, b.description as description,  b.orden as orden, d.description as dias, bh.fk_dia as dia, bh.desde as desde, bh.hasta as hasta from bus b INNER JOIN bus_horario bh ON b.pk_id = bh.fk_bus INNER JOIN dia d ON bh.fk_dia = d.pk_id INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id where b.name like '" + agency + "%' order by orden)";
        }
        Cursor cursor = this.myDataBase.rawQuery(SELECT_QUERY, null);
        new Time().setToNow();
        try {
            result = armaListaBuses(cursor, strFechaActual, esFestivo, DATABASE_VERSION_XAPASTO);
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getBuses from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public BusView getBus(String busId, String strFechaActual, boolean esFestivo) {
        BusView result = null;
        Cursor cursor = this.myDataBase.rawQuery("select DISTINCT dias, * from (select  b.pk_id as id, b.fk_agency as agency, b.name as name, b.color as color, b.description as description,  b.orden as orden, d.description as dias, bh.fk_dia as dia, bh.desde as desde, bh.hasta as hasta from bus b INNER JOIN bus_horario bh ON b.pk_id = bh.fk_bus INNER JOIN dia d ON bh.fk_dia = d.pk_id INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id where b.pk_id =" + busId + " order by orden)", null);
        new Time().setToNow();
        try {
            result = (BusView) armaListaBuses(cursor, strFechaActual, esFestivo, DATABASE_VERSION_XAPASTO).get(0);
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getBus from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public List<BusView> getBusesFavorites(List<String> favorites, String strFechaActual, boolean esFestivo) {
        List<BusView> result = null;
        String strWhere = XmlPullParser.NO_NAMESPACE;
        if (favorites.size() > 0 && !((String) favorites.get(0)).isEmpty()) {
            for (int i = 0; i < favorites.size(); i += DATABASE_VERSION_XAPASTO) {
                if (favorites.size() - i == DATABASE_VERSION_XAPASTO) {
                    strWhere = strWhere + "b.pk_id=" + ((String) favorites.get(i)) + " ";
                } else {
                    strWhere = strWhere + "b.pk_id=" + ((String) favorites.get(i)) + " or ";
                }
            }
        }
        String SELECT_QUERY = XmlPullParser.NO_NAMESPACE;
        if (!strWhere.isEmpty()) {
            Cursor cursor = this.myDataBase.rawQuery("select DISTINCT dias, * from (select  b.pk_id as id, b.fk_agency as agency, b.name as name, b.color as color, b.description as description, d.description as dias, bh.fk_dia as dia, bh.desde as desde, bh.hasta as hasta from bus b INNER JOIN bus_horario bh ON b.pk_id = bh.fk_bus INNER JOIN dia d ON bh.fk_dia = d.pk_id INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id where " + strWhere + "order by name)", null);
            new Time().setToNow();
            try {
                result = armaListaBuses(cursor, strFechaActual, esFestivo, DATABASE_VERSION_XAPASTO);
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Exception e) {
                Log.d(DatabaseAccess.class.toString(), "Error while trying to getBusesFavorites from database");
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            }
        }
        return result;
    }

    public List<BusView> getBusesFromSearch(int position, String strFechaActual, String strSearch, boolean esFestivo) {
        String SELECT_QUERY;
        List<BusView> result = null;
        String agency = null;
        if (this.appID == DATABASE_VERSION_XAPASTO) {
            if (position == 0) {
                agency = "TRANSMILENIO";
            } else if (position == DATABASE_VERSION_XAPASTO) {
                agency = "SITP-U";
            } else if (position == 2) {
                agency = "SITP-C";
            } else if (position == 3) {
                agency = "SITP-E";
            } else if (position == 4) {
                agency = "ALIMENTADOR";
            }
        } else if (this.appID == 2) {
            if (position == 0) {
                agency = "E";
            } else if (position == DATABASE_VERSION_XAPASTO) {
                agency = "T";
            } else if (position == 2) {
                agency = "P";
            } else if (position == 3) {
                agency = "A";
            }
        } else if (this.appID == 3) {
            if (position == 0) {
                agency = "METRO";
            } else if (position == DATABASE_VERSION_XAPASTO) {
                agency = "MB";
            } else if (position == 2) {
                agency = "SUB";
            }
        }
        String SELECT_SEARCH = "select  DISTINCT bus as nameBus  from (select b.name as bus, e.name as nombreParada, clearname, show from bus b inner join bus_estacion be on b.pk_id=be.fk_bus inner join estacion e on be.fk_estacion=e.pk_id) where (bus like '%" + strSearch + "%' or clearname like '%" + strSearch + "%' or nombreParada like '%" + strSearch + "%' ) and show=1";
        if (this.appID == 2) {
            SELECT_QUERY = "select DISTINCT dias, * from (select  b.pk_id as id, b.fk_agency as agency, b.name as name, b.color as color, b.description as description, b.orden as orden, d.description as dias, bh.fk_dia as dia, bh.desde as desde, bh.hasta as hasta   from bus b INNER JOIN bus_horario bh ON b.pk_id = bh.fk_bus INNER JOIN dia d ON bh.fk_dia = d.pk_id INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id where b.name like '" + agency + "%' and b.name in (" + SELECT_SEARCH + ")  order by orden)";
        } else if (agency == null) {
            SELECT_QUERY = "select DISTINCT dias, * from (select  b.pk_id as id, b.fk_agency as agency, b.name as name, b.color as color, b.description as description, b.orden as orden, d.description as dias, bh.fk_dia as dia, bh.desde as desde, bh.hasta as hasta   from bus b INNER JOIN bus_horario bh ON b.pk_id = bh.fk_bus INNER JOIN dia d ON bh.fk_dia = d.pk_id INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id where b.name in (" + SELECT_SEARCH + ") order by orden)";
        } else {
            SELECT_QUERY = "select DISTINCT dias, * from (select  b.pk_id as id, b.fk_agency as agency, b.name as name, b.color as color, b.description as description, b.orden as orden, d.description as dias, bh.fk_dia as dia, bh.desde as desde, bh.hasta as hasta   from bus b INNER JOIN bus_horario bh ON b.pk_id = bh.fk_bus INNER JOIN dia d ON bh.fk_dia = d.pk_id INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id where agency ='" + agency + "' and b.name in (" + SELECT_SEARCH + ")  order by orden)";
        }
        Cursor cursor = this.myDataBase.rawQuery(getStringWithoutAccent(SELECT_QUERY), null);
        try {
            result = armaListaBuses(cursor, strFechaActual, esFestivo, DATABASE_VERSION_XAPASTO);
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getBusesFromSearch from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public List<BusView> armaListaBuses(Cursor cursor, String strFechaActual, boolean esFestivo, int type) {
        Time hora = new Time();
        hora.setToNow();
        List<BusView> result = new ArrayList();
        BusView bv = null;
        List itemsHorario = null;
        boolean enOperacion = false;
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(ShareConstants.WEB_DIALOG_PARAM_ID));
                String d = cursor.getString(cursor.getColumnIndex("dias"));
                List<HorarioItem> itemsHorario2 = null;
                Integer iDiaId;
                Time horaFrom;
                Time horaTo;
                String strDias;
                if (bv == null || id != bv.getId()) {
                    if (bv != null) {
                        bv.setHorarios(itemsHorario2);
                        result.add(bv);
                        itemsHorario2 = new ArrayList();
                    }
                    bv = new BusView();
                    enOperacion = false;
                    if (itemsHorario == null) {
                        itemsHorario = new ArrayList();
                    }
                    bv.setId(cursor.getInt(cursor.getColumnIndex(ShareConstants.WEB_DIALOG_PARAM_ID)));
                    bv.setName(cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME)));
                    bv.setColor(cursor.getString(cursor.getColumnIndex("color")));
                    bv.setDescripcion(cursor.getString(cursor.getColumnIndex(ShareConstants.WEB_DIALOG_PARAM_DESCRIPTION)));
                    iDiaId = Integer.valueOf(cursor.getInt(cursor.getColumnIndex("dia")));
                    horaFrom = new Time();
                    horaTo = new Time();
                    horaFrom.parse(strFechaActual + "T" + ConvertUtil.toStringHourGTFS(cursor.getString(cursor.getColumnIndex("desde"))));
                    horaTo.parse(strFechaActual + "T" + ConvertUtil.toStringHourGTFS(cursor.getString(cursor.getColumnIndex("hasta"))));
                    if (horaFrom.after(horaTo)) {
                        horaTo.set(horaTo.toMillis(false) + 86400000);
                    }
                    if (hora.after(horaFrom) && hora.before(horaTo)) {
                        if ((hora.weekDay == 0 || esFestivo) && iDiaId.intValue() == 4) {
                            enOperacion = true;
                        } else if (!esFestivo) {
                            if (hora.weekDay == 6 && (iDiaId.intValue() == 2 || iDiaId.intValue() == 3)) {
                                enOperacion = true;
                            } else if (hora.weekDay > 0 && hora.weekDay < 6 && (iDiaId.intValue() == DATABASE_VERSION_XAPASTO || iDiaId.intValue() == 2)) {
                                enOperacion = true;
                            }
                        }
                    }
                    strDias = cursor.getString(cursor.getColumnIndex("dias"));
                    itemsHorario.add(new HorarioItem(strDias, horaFrom.format("%I:%M %p").replace(". ", XmlPullParser.NO_NAMESPACE).replace(".", XmlPullParser.NO_NAMESPACE) + " - " + horaTo.format("%I:%M %p").replace(". ", XmlPullParser.NO_NAMESPACE).replace(".", XmlPullParser.NO_NAMESPACE), horaFrom.format("%I:%M %p").replace(". ", XmlPullParser.NO_NAMESPACE).replace(".", XmlPullParser.NO_NAMESPACE).replace("AM", "A M") + " A " + horaTo.format("%I:%M %p").replace(". ", XmlPullParser.NO_NAMESPACE).replace(".", XmlPullParser.NO_NAMESPACE).replace("AM", "A M")));
                    bv.setEnOperacion(enOperacion);
                    bv.setHorarios(itemsHorario);
                    if (type == DATABASE_VERSION_XAPASTO) {
                        bv.setVagon(XmlPullParser.NO_NAMESPACE);
                    }
                    if (type == 2) {
                        bv.setVagon(cursor.getString(cursor.getColumnIndex("vagon")));
                    }
                } else {
                    List<HorarioItem> horarios = bv.getHorarios();
                    if (!(horarios == null || horarios.isEmpty())) {
                        horaFrom = new Time();
                        horaTo = new Time();
                        horaFrom.parse(strFechaActual + "T" + ConvertUtil.toStringHourGTFS(cursor.getString(cursor.getColumnIndex("desde"))));
                        horaTo.parse(strFechaActual + "T" + ConvertUtil.toStringHourGTFS(cursor.getString(cursor.getColumnIndex("hasta"))));
                        strDias = cursor.getString(cursor.getColumnIndex("dias"));
                        itemsHorario2.add(new HorarioItem(strDias, horaFrom.format("%I:%M %p").replace(". ", XmlPullParser.NO_NAMESPACE).replace(".", XmlPullParser.NO_NAMESPACE) + " - " + horaTo.format("%I:%M %p").replace(". ", XmlPullParser.NO_NAMESPACE).replace(".", XmlPullParser.NO_NAMESPACE), horaFrom.format("%I:%M %p").replace(". ", XmlPullParser.NO_NAMESPACE).replace(".", XmlPullParser.NO_NAMESPACE).replace("AM", "A M") + " A " + horaTo.format("%I:%M %p").replace(". ", XmlPullParser.NO_NAMESPACE).replace(".", XmlPullParser.NO_NAMESPACE).replace("AM", "A M")));
                        bv.setHorarios(itemsHorario2);
                        if (!enOperacion) {
                            iDiaId = Integer.valueOf(cursor.getInt(cursor.getColumnIndex("dia")));
                            horaFrom.parse(strFechaActual + "T" + ConvertUtil.toStringHourGTFS(cursor.getString(cursor.getColumnIndex("desde"))));
                            horaTo.parse(strFechaActual + "T" + ConvertUtil.toStringHourGTFS(cursor.getString(cursor.getColumnIndex("hasta"))));
                            if (horaFrom.after(horaTo)) {
                                horaTo.set(horaTo.toMillis(false) + 86400000);
                            }
                            if (hora.after(horaFrom) && hora.before(horaTo)) {
                                if ((hora.weekDay == 0 || esFestivo) && iDiaId.intValue() == 4) {
                                    enOperacion = true;
                                    bv.setEnOperacion(true);
                                } else if (!esFestivo) {
                                    if (hora.weekDay == 6 && (iDiaId.intValue() == 2 || iDiaId.intValue() == 3)) {
                                        enOperacion = true;
                                        bv.setEnOperacion(true);
                                    } else if (hora.weekDay > 0 && hora.weekDay < 6 && (iDiaId.intValue() == DATABASE_VERSION_XAPASTO || iDiaId.intValue() == 2)) {
                                        enOperacion = true;
                                        bv.setEnOperacion(true);
                                    }
                                }
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
            bv.setHorarios(itemsHorario);
            result.add(bv);
        }
        return result;
    }

    public List<PuntoRecargaView> getPuntosRecarga(int position) throws Throwable {
        String SELECT_QUERY = XmlPullParser.NO_NAMESPACE;
        if (position == 0) {
            SELECT_QUERY = "Select * from recarga";
        } else if (position == DATABASE_VERSION_XAPASTO) {
            SELECT_QUERY = "Select * from recarga where fk_tipo=1";
        } else if (position == 2) {
            SELECT_QUERY = "Select * from recarga where fk_tipo=2";
        }
             return armaListaPuntos(this.myDataBase.rawQuery(SELECT_QUERY, null), DATABASE_VERSION_XAPASTO);

    }

    public List<PuntoRecargaView> getPuntosRecargaFromName(int position, String strSearch) throws Throwable {
        String SELECT_QUERY = XmlPullParser.NO_NAMESPACE;
        if (position == 0) {
            SELECT_QUERY = "Select * from recarga where name like '%" + getStringWithoutAccent(strSearch) + "%' or direccion like '%" + getStringWithoutAccent(strSearch) + "%'";
        } else if (position == DATABASE_VERSION_XAPASTO) {
            SELECT_QUERY = "Select * from recarga where fk_tipo=1 and (name like '%" + getStringWithoutAccent(strSearch) + "%' or direccion like '%" + getStringWithoutAccent(strSearch) + "%')";
        } else if (position == 2) {
            SELECT_QUERY = "Select * from recarga where fk_tipo=2 and (name like '%" + getStringWithoutAccent(strSearch) + "%' or direccion like '%" + getStringWithoutAccent(strSearch) + "%')";
        }
            return armaListaPuntos(this.myDataBase.rawQuery(SELECT_QUERY, null), DATABASE_VERSION_XAPASTO);

    }

    public List<PuntoRecargaView> getPuntosRecargaByGPS(Double latitude, Double longitude, int position) throws Throwable {
        String SELECT_QUERY_LOCATIONS = XmlPullParser.NO_NAMESPACE;
        if (position == 0) {
            SELECT_QUERY_LOCATIONS = "Select pk_id as _id, latitud, longitud from recarga";
        } else if (position == DATABASE_VERSION_XAPASTO) {
            SELECT_QUERY_LOCATIONS = "Select pk_id as _id, latitud, longitud from recarga where fk_tipo=1";
        } else if (position == 2) {
            SELECT_QUERY_LOCATIONS = "Select pk_id as _id, latitud, longitud from recarga where fk_tipo=2";
        }
        Cursor cLocations = this.myDataBase.rawQuery(SELECT_QUERY_LOCATIONS, null);
        ArrayList<IntegerFloat> dis = new ArrayList();
        Location aLocation = new Location("Point A");
        aLocation.setLatitude(latitude.doubleValue());
        aLocation.setLongitude(longitude.doubleValue());
        if (cLocations.moveToFirst()) {
            do {
                float fLat = cLocations.getFloat(cLocations.getColumnIndex("latitud"));
                float fLon = cLocations.getFloat(cLocations.getColumnIndex("longitud"));
                if (!(fLat == 0.0f || fLon == 0.0f)) {
                    Location bLocation = new Location("Point B");
                    bLocation.setLatitude((double) fLat);
                    bLocation.setLongitude((double) fLon);
                    dis.add(new IntegerFloat(cLocations.getInt(cLocations.getColumnIndex("_id")), aLocation.distanceTo(bLocation)));
                }
            } while (cLocations.moveToNext());
        }
        cLocations.close();
        dis = IntegerFloat.sort(dis);
        String sUnion = (((((("SELECT " + ((IntegerFloat) dis.get(0)).name + " as fk_tullave, " + ((IntegerFloat) dis.get(0)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(DATABASE_VERSION_XAPASTO)).name + " as fk_tullave, " + ((IntegerFloat) dis.get(DATABASE_VERSION_XAPASTO)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(2)).name + " as fk_tullave, " + ((IntegerFloat) dis.get(2)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(3)).name + " as fk_tullave, " + ((IntegerFloat) dis.get(3)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(4)).name + " as fk_tullave, " + ((IntegerFloat) dis.get(4)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(5)).name + " as fk_tullave, " + ((IntegerFloat) dis.get(5)).value + " as distancia UNION ") + "SELECT " + ((IntegerFloat) dis.get(6)).name + " as fk_tullave, " + ((IntegerFloat) dis.get(6)).value + " as distancia";
        String SELECT_QUERY = XmlPullParser.NO_NAMESPACE;
        if (position == 0) {
            SELECT_QUERY = "SELECT tl.pk_id as _id, tl.name, tl.direccion, tl.fk_tipo, u.distancia FROM recarga tl INNER JOIN (" + sUnion + ") u ON tl.pk_id = u.fk_tullave ORDER BY u.distancia";
        } else if (position == DATABASE_VERSION_XAPASTO) {
            SELECT_QUERY = "SELECT tl.pk_id as _id, tl.name, tl.direccion, tl.fk_tipo, u.distancia FROM recarga tl INNER JOIN (" + sUnion + ") u ON tl.pk_id = u.fk_tullave WHERE fk_tipo = 1 ORDER BY u.distancia";
        } else if (position == 2) {
            SELECT_QUERY = "SELECT tl.pk_id as _id, tl.name, tl.direccion, tl.fk_tipo, u.distancia FROM recarga tl INNER JOIN (" + sUnion + ") u ON tl.pk_id = u.fk_tullave WHERE fk_tipo = 2 ORDER BY u.distancia";
        }
             return armaListaPuntos(this.myDataBase.rawQuery(SELECT_QUERY, null), 2);

    }

    public String getStringWithoutAccent(String strText) {
        return Normalizer.normalize(strText, Form.NFD).replaceAll("[^\\p{ASCII}]", XmlPullParser.NO_NAMESPACE);
    }

    public List<PuntoRecargaView> armaListaPuntos(Cursor cursor, int type) throws Throwable {
        Throwable th;
        List<PuntoRecargaView> result = null;
        List<PuntoRecargaView> result2;
        PuntoRecargaView prv;
        if (type == DATABASE_VERSION_XAPASTO) {
            try {
                if (cursor.moveToFirst()) {
                    result2 = new ArrayList();
                    do {
                        try {
                            prv = new PuntoRecargaView();
                            prv.setId(cursor.getInt(0));
                            prv.setNombre(cursor.getString(DATABASE_VERSION_XAPASTO));
                            prv.setDireccion(cursor.getString(2));
                            prv.setTipo(cursor.getInt(5));
                            prv.setDistancia(-1.0d);
                            result2.add(prv);
                        } catch (Exception e) {
                            result = result2;
                        } catch (Throwable th2) {
                            th = th2;
                            result = result2;
                        }
                    } while (cursor.moveToNext());
                    result = result2;
                }
            } catch (Exception e2) {
                try {
                    Log.d(DatabaseAccess.class.toString(), "Error while trying to armaListaPuntos from database");
                    if (!(cursor == null || cursor.isClosed())) {
                        cursor.close();
                    }
                    return result;
                } catch (Throwable th3) {
                    th = th3;
                    if (!(cursor == null || cursor.isClosed())) {
                        cursor.close();
                    }
                    throw th;
                }
            }
        } else if (type == 2) {
            if (cursor.moveToFirst()) {
                result2 = new ArrayList();
                do {
                    prv = new PuntoRecargaView();
                    prv.setId(cursor.getInt(0));
                    prv.setNombre(cursor.getString(DATABASE_VERSION_XAPASTO));
                    prv.setDireccion(cursor.getString(2));
                    prv.setTipo(cursor.getInt(3));
                    prv.setDistancia(cursor.getDouble(4));
                    result2.add(prv);
                } while (cursor.moveToNext());
                result = result2;
            }
        }
        if (!(cursor == null || cursor.isClosed())) {
            cursor.close();
        }
        return result;
    }

    public List<BusView> getBusesFromStation(int id, String strFechaActual, boolean esFestivo) {
        List<BusView> result = null;
        Cursor cursor = this.myDataBase.rawQuery("select DISTINCT dias, * from (select  b.pk_id as id, b.fk_agency as agency, b.name as name, b.color as color, b.description as description, d.description as dias, bh.fk_dia as dia, bh.desde as desde, bh.hasta as hasta , be.vagon as vagon  from bus b INNER JOIN bus_horario bh ON b.pk_id = bh.fk_bus INNER JOIN dia d ON bh.fk_dia = d.pk_id INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id where b.pk_id in ( select fk_bus from bus_estacion where fk_estacion = " + String.valueOf(id) + " and show=1) and be.fk_estacion=" + String.valueOf(id) + " order by name)", null);
        try {
            result = armaListaBuses(cursor, strFechaActual, esFestivo, 2);
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getBusesFromStation from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    /*
    public List<Troncal> getTroncales() {
        List<Troncal> result = new ArrayList();
        Cursor cursor = this.myDataBase.rawQuery("select pk_id, name, description, color from troncal where pk_id>0", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    result.add(new Troncal(cursor.getInt(0), cursor.getString(DATABASE_VERSION_XAPASTO), cursor.getString(2), cursor.getString(3)));
                } while (cursor.moveToNext());
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getTroncales from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }
*/
    public PuntoRecargaView getPuntoRecarga(int id) throws Throwable {
        Throwable th;
        PuntoRecargaView puntoRecargaView = null;
        Cursor cursor = this.myDataBase.rawQuery("SELECT pk_id as _id, latitud, longitud, name, direccion FROM recarga WHERE pk_id = " + String.valueOf(id), null);
        try {
            if (cursor.moveToFirst()) {
                PuntoRecargaView result = new PuntoRecargaView();
                try {
                    result.setLatitud(cursor.getDouble(DATABASE_VERSION_XAPASTO));
                    result.setLongitud(cursor.getDouble(2));
                    result.setNombre(cursor.getString(3));
                    result.setDireccion(cursor.getString(4));
                    puntoRecargaView = result;
                } catch (Exception e) {
                    puntoRecargaView = result;
                    try {
                        Log.d(DatabaseAccess.class.toString(), "Error while trying to getPuntoRecarga from database");
                        if (!(cursor == null || cursor.isClosed())) {
                            cursor.close();
                        }
                        return puntoRecargaView;
                    } catch (Throwable th2) {
                        th = th2;
                        if (!(cursor == null || cursor.isClosed())) {
                            cursor.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    puntoRecargaView = result;
                    cursor.close();
                    throw th;
                }
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e2) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getPuntoRecarga from database");
            cursor.close();
            return puntoRecargaView;
        }
        return puntoRecargaView;
    }

    public List<UbicacionMapa> getRouteBus(PolylineOptions pOptions, double dLonI, double dLatI, boolean bPolyline, int busId) throws Throwable {
        Throwable th;
        List<UbicacionMapa> list = null;
        SQLiteDatabase sQLiteDatabase = this.myDataBase;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = String.valueOf(busId);
        Cursor _cursor = sQLiteDatabase.rawQuery("SELECT be.rowid as _id, e.name, e.description as direccion, e.longitud, e.latitud, be.show, b.fk_agency from bus_estacion be inner join estacion e ON be.fk_estacion = e.pk_id inner join bus b on be.fk_bus = b.pk_id WHERE be.show!=0 AND be.fk_bus = ? ORDER BY be.rowid", strArr);
        int i = 0;
        try {
            if (_cursor.moveToFirst()) {
                List<UbicacionMapa> result = new ArrayList();
                do {
                    try {
                        double dLat = _cursor.getDouble(_cursor.getColumnIndex("latitud"));
                        double dLon = _cursor.getDouble(_cursor.getColumnIndex("longitud"));
                        int iShow = _cursor.getInt(_cursor.getColumnIndex("show"));
                        if (!(!bPolyline || dLat == 0.0d || dLon == 0.0d)) {
                            pOptions.add(new LatLng(dLat, dLon));
                        }
                        if (!(iShow != DATABASE_VERSION_XAPASTO || dLat == 0.0d || dLon == 0.0d)) {
                            result.add(new UbicacionMapa(dLat, dLon, _cursor.getString(_cursor.getColumnIndex(MovilixaConstants.NAME)), _cursor.getString(_cursor.getColumnIndex("direccion")), _cursor.getString(_cursor.getColumnIndex("fk_agency"))));
                        }
                        if (iShow == DATABASE_VERSION_XAPASTO) {
                            i += DATABASE_VERSION_XAPASTO;
                        }
                    } catch (Exception e) {
                        list = result;
                    } catch (Throwable th2) {
                        th = th2;
                        list = result;
                    }
                } while (_cursor.moveToNext());
                list = result;
            }
            if (!(_cursor == null || _cursor.isClosed())) {
                _cursor.close();
            }
        } catch (Exception e2) {
            try {
                Log.d(DatabaseAccess.class.toString(), "Error while trying to getRouteBus from database");
                if (!(_cursor == null || _cursor.isClosed())) {
                    _cursor.close();
                }
                return list;
            } catch (Throwable th3) {
                th = th3;
                if (!(_cursor == null || _cursor.isClosed())) {
                    _cursor.close();
                }
                throw th;
            }
        }
        return list;
    }

    public String getBusInfo(String idBus, String estacionOrigen, int iAppID) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT be.vagon FROM bus_estacion be WHERE fk_bus=" + idBus + " and fk_estacion=" + estacionOrigen, null);
        String idVag = XmlPullParser.NO_NAMESPACE;
        try {
            if (cursor.moveToFirst() && !cursor.isNull(cursor.getColumnIndex("vagon"))) {
                idVag = cursor.getString(cursor.getColumnIndex("vagon"));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getBusInfo from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = idBus;
        cursor = this.myDataBase.rawQuery("SELECT be.rowid as _id, b.name, b.fk_agency, e.name as destino FROM bus b INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id WHERE be.show = 1 AND b.pk_id = ? ORDER BY be.rowid DESC LIMIT 1", strArr);
        String sDestino = XmlPullParser.NO_NAMESPACE;
        String sBus = XmlPullParser.NO_NAMESPACE;
        String sAgency = XmlPullParser.NO_NAMESPACE;
        String sMsg = XmlPullParser.NO_NAMESPACE;
        try {
            if (cursor.moveToFirst()) {
                sDestino = cursor.getString(cursor.getColumnIndex("destino"));
                sBus = cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME));
                sAgency = cursor.getString(cursor.getColumnIndex("fk_agency"));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e2) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getBusInfo from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th2) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        if (iAppID == 3) {
            sMsg = "L\u00ednea " + sBus + " del " + sAgency + " con destino " + sDestino;
        } else if (iAppID == 4) {
            sMsg = "Ruta " + sBus + " con destino " + sDestino;
        } else {
            sMsg = "Bus " + sBus + " con destino " + sDestino;
        }
        if (!(idVag == null || idVag.compareTo(XmlPullParser.NO_NAMESPACE) == 0)) {
            sMsg = sMsg + ", vag\u00f3n " + idVag;
        }
        return sMsg;
    }

    public int getStopsCount() {
        int iTotal = 0;
        Cursor cursor = this.myDataBase.rawQuery("SELECT max(pk_id) as total FROM estacion", null);
        try {
            if (cursor.moveToFirst()) {
                iTotal = cursor.getInt(cursor.getColumnIndex("total"));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getStopsCount from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return iTotal;
    }

    public double calcularTiempoMio(int _id, int stationDestino, int stationOrigen, double _factor) {
        Cursor cursor;
        double result = 0.0d;
        if (_id >= 0) {
            cursor = this.myDataBase.rawQuery("SELECT sum(distance) as nDistancia FROM (SELECT b1.pk_id as _id, be1.rowid as rowInicial, beTemp1.rowid as rowFinal FROM bus b1 INNER JOIN bus_estacion beTemp1 ON b1.pk_id = beTemp1.fk_bus INNER JOIN bus_estacion be1 ON b1.pk_id = be1.fk_bus WHERE beTemp1.rowid > be1.rowid AND beTemp1.fk_estacion = ? AND be1.fk_estacion = ? AND b1.pk_id = ?) q1 INNER JOIN bus_estacion be ON q1._id = be.fk_bus WHERE be.show = 1 AND be.rowid > q1.rowInicial AND be.rowid <= q1.rowFinal", new String[]{String.valueOf(stationDestino), String.valueOf(stationOrigen), String.valueOf(_id)});
            try {
                if (cursor.moveToFirst()) {
                    result = _factor * ((double) cursor.getInt(cursor.getColumnIndex("nDistancia")));
                }
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Exception e) {
                Log.d(DatabaseAccess.class.toString(), "Error while trying to calcularTiempoMio from database");
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            }
        } else {
            cursor = this.myDataBase.rawQuery("SELECT w FROM estacion_conexion WHERE fk_estacionOrigen = ? AND fk_estacionDestino = ? UNION SELECT w FROM estacion_conexion WHERE fk_estacionDestino = ? AND fk_estacionOrigen = ?", new String[]{String.valueOf(stationOrigen), String.valueOf(stationDestino), String.valueOf(stationOrigen), String.valueOf(stationDestino)});
            try {
                if (cursor.moveToFirst()) {
                    result = _factor * cursor.getDouble(cursor.getColumnIndex("w"));
                }
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Exception e2) {
                Log.d(DatabaseAccess.class.toString(), "Error while trying to calcularTiempo from database");
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Throwable th2) {
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            }
        }
        return result;
    }

    public ArrayList<RecorridoRuta> getRecorridosBusList(String query, int _appID, int iEstacionDestino, int iEstacionOrigen, double BRT_FACTOR, double BUS_FACTOR) {
        ArrayList<RecorridoRuta> result = new ArrayList();
        Cursor cursor = this.myDataBase.rawQuery(query, new String[]{String.valueOf(iEstacionDestino), String.valueOf(iEstacionOrigen)});
        try {
            if (cursor.moveToFirst()) {
                do {
                    double dFactor = BUS_FACTOR;
                    if (_appID == DATABASE_VERSION_XAPASTO) {
                        if (cursor.getString(cursor.getColumnIndex("fk_agency")).equals("TRANSMILENIO")) {
                            dFactor = BRT_FACTOR;
                        } else {
                            dFactor = BUS_FACTOR;
                        }
                    }
                    ArrayList<RecorridoBus> listaBuses = new ArrayList();
                    listaBuses.add(new RecorridoBus(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME)), cursor.getString(cursor.getColumnIndex("color")), iEstacionOrigen, iEstacionDestino, cursor.getInt(cursor.getColumnIndex("nParadas")), cursor.getInt(cursor.getColumnIndex("nDistancia")), dFactor));
                    result.add(new RecorridoRuta(listaBuses));
                } while (cursor.moveToNext());
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getRecorridosBusList from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public double getFactor(int iBus, int _appID, double BRT_FACTOR, double BUS_FACTOR) {
        double dFactor = BUS_FACTOR;
        if (_appID == DATABASE_VERSION_XAPASTO) {
            String[] strArr = new String[DATABASE_VERSION_XAPASTO];
            strArr[0] = String.valueOf(iBus);
            Cursor cursor = this.myDataBase.rawQuery("SELECT b.pk_id as _id, b.fk_agency FROM bus b WHERE b.pk_id = ?", strArr);
            try {
                if (cursor.moveToFirst() && cursor.getString(cursor.getColumnIndex("fk_agency")).equals("TRANSMILENIO")) {
                    dFactor = BRT_FACTOR;
                }
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Exception e) {
                Log.d(DatabaseAccess.class.toString(), "Error while trying to getFactor from database");
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            }
        }
        return dFactor;
    }

    public float getDistanceToStation(Location lLocationStart, int stationId) throws Throwable {
        Throwable th;
        Location location = null;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = String.valueOf(stationId);
        Cursor cursor = this.myDataBase.rawQuery("SELECT longitud, latitud FROM estacion WHERE pk_id = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                Location lStation = new Location(String.valueOf(stationId));
                try {
                    lStation.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitud")));
                    lStation.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitud")));
                    location = lStation;
                } catch (Exception e) {
                    location = lStation;
                    try {
                        Log.d(DatabaseAccess.class.toString(), "Error while trying to getDistanceToStation from database");
                        if (!(cursor == null || cursor.isClosed())) {
                            cursor.close();
                        }
                        return lLocationStart.distanceTo(location);
                    } catch (Throwable th2) {
                        th = th2;
                        cursor.close();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    location = lStation;
                    if (!(cursor == null || cursor.isClosed())) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e2) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getDistanceToStation from database");
            cursor.close();
            return lLocationStart.distanceTo(location);
        }
        return lLocationStart.distanceTo(location);
    }

    public String getIdBusFromName(String busName) {
        String result = XmlPullParser.NO_NAMESPACE;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = busName;
        Cursor cursor = this.myDataBase.rawQuery("SELECT pk_id FROM bus WHERE name = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                do {
                    result = result + ";" + String.valueOf(cursor.getInt(0));
                } while (cursor.moveToNext());
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getIdBusFromName from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public ArrayList<MyItem> getAllItems(String sAgency) {
        ArrayList<MyItem> result = new ArrayList();
        String strQuery = XmlPullParser.NO_NAMESPACE;
        if (this.appID != DATABASE_VERSION_XAPASTO) {
            strQuery = "SELECT pk_id as _id, latitud, longitud FROM estacion";
        } else if (sAgency.equals("TRANSMILENIO")) {
            strQuery = "SELECT pk_id as _id, latitud, longitud FROM estacion WHERE fk_troncal >= 0";
        } else if (sAgency.equals("SITP")) {
            strQuery = "SELECT pk_id as _id, latitud, longitud FROM estacion WHERE fk_troncal < 0";
        }
        Cursor cursor = this.myDataBase.rawQuery(strQuery, new String[0]);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Double dLat = Double.valueOf(cursor.getDouble(cursor.getColumnIndex("latitud")));
                    Double dLon = Double.valueOf(cursor.getDouble(cursor.getColumnIndex("longitud")));
                    MyItem myItem = new MyItem(cursor.getInt(cursor.getColumnIndex("_id")));
                    myItem.setmPosition(new LatLng(dLat.doubleValue(), dLon.doubleValue()));
                    result.add(myItem);
                } while (cursor.moveToNext());
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getIdBusFromName from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public MyItem getItem(int idStation, int _resPin_mapTM, int _resPin_mapSITP) throws Throwable {
        MyItem myItem = null;
        String[] strArr = new String[DATABASE_VERSION_XAPASTO];
        strArr[0] = String.valueOf(idStation);
        Cursor cursor = this.myDataBase.rawQuery("SELECT pk_id as _id, latitud, longitud, name, description as direccion FROM estacion WHERE _id = ?", strArr);
        try {
            if (cursor.moveToFirst()) {
                do {
                    MyItem result = myItem;
                    try {
                        int pin;
                        Double dLat = Double.valueOf(cursor.getDouble(cursor.getColumnIndex("latitud")));
                        Double dLon = Double.valueOf(cursor.getDouble(cursor.getColumnIndex("longitud")));
                        int _estacionId = cursor.getInt(cursor.getColumnIndex("_id"));
                        if (isSitpStation(String.valueOf(_estacionId))) {
                            pin = _resPin_mapSITP;
                        } else {
                            pin = _resPin_mapTM;
                        }
                        myItem = new MyItem(new LatLng(dLat.doubleValue(), dLon.doubleValue()), cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME)), cursor.getString(cursor.getColumnIndex("direccion")), BitmapDescriptorFactory.fromResource(pin), _estacionId);
                    } catch (Exception e) {
                        myItem = result;
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        myItem = result;
                    }
                } while (cursor.moveToNext());
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e2) {
        }
        return myItem;

    }

    public boolean isStationInBusStops(int stationId, int busId) {
        boolean result = false;
        Cursor cursor = this.myDataBase.rawQuery("SELECT fk_bus FROM bus_estacion WHERE fk_bus = ? AND fk_estacion = ? AND show = 1", new String[]{String.valueOf(busId), String.valueOf(stationId)});
        try {
            if (cursor.moveToFirst()) {
                result = true;
            } else {
                result = false;
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to isStationInBusStops from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return result;
    }

    public int getBusStopConnection(int busId, int stationId) {
        int result = stationId;
        Cursor cursor = this.myDataBase.rawQuery("SELECT connection FROM bus_estacion WHERE fk_bus = ? AND fk_estacion = ? AND show = 0", new String[]{String.valueOf(busId), String.valueOf(stationId)});
        try {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(cursor.getColumnIndex("connection"));
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(DatabaseAccess.class.toString(), "Error while trying to getBusStopConnection from database");
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        if (result == 0) {
            return stationId;
        }
        return result;
    }
}