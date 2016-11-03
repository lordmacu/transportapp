package co.cristiangarcia.hdtransmileniopro.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Iterator;

import co.cristiangarcia.hdtransmileniopro.util.IntegerFloat;

/**
 * Created by cristiangarcia on 26/10/16.
 */

public class DbUtils {
    private Context _context;
    private int _iAppID;

    public DbUtils(Context context, int iAppID) {
        this._context = context;
        this._iAppID = iAppID;
    }

    public ArrayList<IntegerFloat> getListNearTuLlavePoints(double latitude, double longitude) {
        SQLiteDatabase db = new DatabaseHelper(this._context, this._iAppID).getReadableDatabase();
        Cursor cLocations = db.rawQuery("SELECT pk_id as _id, latitud, longitud FROM recarga", null);
        ArrayList<IntegerFloat> dis = new ArrayList();
        Location aLocation = new Location("Point A");
        aLocation.setLatitude(latitude);
        aLocation.setLongitude(longitude);
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
        db.close();
        return IntegerFloat.sort(dis);
    }

    public String getQueryNearTuLlavePoints(ArrayList<IntegerFloat> dis, int count) {
        String sUnion = "SELECT " + ((IntegerFloat) dis.get(0)).name + " as fk_tullave, " + ((IntegerFloat) dis.get(0)).value + " as distancia UNION ";
        int i = 1;
        while (i < count - 1) {
            sUnion = sUnion + "SELECT " + ((IntegerFloat) dis.get(i)).name + " as fk_tullave, " + ((IntegerFloat) dis.get(i)).value + " as distancia UNION ";
            i++;
        }
        return sUnion + "SELECT " + ((IntegerFloat) dis.get(i)).name + " as fk_tullave, " + ((IntegerFloat) dis.get(i)).value + " as distancia";
    }

    public String findNearTuLlavePoints(double latitude, double longitude, int count) {
        String sTuLlaveIds = XmlPullParser.NO_NAMESPACE;
        ArrayList<IntegerFloat> dis = getListNearTuLlavePoints(latitude, longitude);
        SQLiteDatabase db = new DatabaseHelper(this._context, this._iAppID).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT tl.pk_id as _id FROM recarga tl INNER JOIN (" + getQueryNearTuLlavePoints(dis, count) + ") u ON tl.pk_id = u.fk_tullave ORDER BY u.distancia", null);
        if (cursor.moveToFirst()) {
            do {
                int tTullaveId = cursor.getInt(cursor.getColumnIndex("_id"));
                if (tTullaveId != 0 && sTuLlaveIds.split(";").length < 7) {
                    sTuLlaveIds = sTuLlaveIds + (sTuLlaveIds != XmlPullParser.NO_NAMESPACE ? "," : XmlPullParser.NO_NAMESPACE) + String.valueOf(tTullaveId);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sTuLlaveIds;
    }

    public ArrayList<IntegerFloat> getListNearTransmilenioStation(double latitude, double longitude, String lstStations) {
        SQLiteDatabase db = new DatabaseHelper(this._context, this._iAppID).getReadableDatabase();
        Cursor cLocations = db.rawQuery("SELECT pk_id as _id, latitud, longitud FROM estacion WHERE pk_id IN (" + lstStations + ")", null);
        ArrayList<IntegerFloat> dis = new ArrayList();
        Location aLocation = new Location("Point A");
        aLocation.setLatitude(latitude);
        aLocation.setLongitude(longitude);
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
        db.close();
        return IntegerFloat.sort(dis);
    }

    public ArrayList<IntegerFloat> previousNextTransmilenioStation(Location l1, Location l2, String lstStopStations) {
        ArrayList<IntegerFloat> dis1 = getListNearTransmilenioStation(l1.getLatitude(), l1.getLongitude(), lstStopStations);
        ArrayList<IntegerFloat> dis2 = getListNearTransmilenioStation(l2.getLatitude(), l2.getLongitude(), lstStopStations);
        String[] lStations = lstStopStations.split(",");
        ArrayList<IntegerFloat> aleja = new ArrayList();
        ArrayList<IntegerFloat> acerca = new ArrayList();
        Iterator it = dis1.iterator();
        while (it.hasNext()) {
            IntegerFloat d1 = (IntegerFloat) it.next();
            Iterator it2 = dis2.iterator();
            while (it2.hasNext()) {
                IntegerFloat d2 = (IntegerFloat) it2.next();
                if (d1.name == d2.name) {
                    if (d1.value > d2.value) {
                        acerca.add(d2);
                    } else {
                        aleja.add(d2);
                    }
                }
            }
        }
        acerca = IntegerFloat.sort(acerca);
        aleja = IntegerFloat.sort(aleja);
        if (acerca.isEmpty() || aleja.isEmpty()) {
            return null;
        }
        Boolean bOriginFound = Boolean.valueOf(false);
        Boolean bDestinationFound = Boolean.valueOf(false);
        for (String iStation : lStations) {
            if (bOriginFound.booleanValue()) {
                if (iStation.compareTo(String.valueOf(((IntegerFloat) acerca.get(0)).name)) == 0 && ((IntegerFloat) acerca.get(0)).value < 4000.0f) {
                    bDestinationFound = Boolean.valueOf(true);
                }
            } else {
                bOriginFound = Boolean.valueOf(false);
            }
            if (iStation.compareTo(String.valueOf(((IntegerFloat) aleja.get(0)).name)) == 0 && ((IntegerFloat) aleja.get(0)).value < 4000.0f) {
                bOriginFound = Boolean.valueOf(true);
            }
        }
        if (!bDestinationFound.booleanValue()) {
            return null;
        }
        ArrayList<IntegerFloat> ret = new ArrayList();
        ret.add(acerca.get(0));
        ret.add(aleja.get(0));
        return ret;
    }

    public String getStationName(int stationId) {
        String sStationName = "Ubicaci\u00f3n Seleccionada";
        SQLiteDatabase db = new DatabaseHelper(this._context, this._iAppID).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM estacion WHERE pk_id = ?", new String[]{XmlPullParser.NO_NAMESPACE + stationId});
        if (cursor.moveToFirst()) {
            sStationName = cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME));
        }
        cursor.close();
        db.close();
        return sStationName;
    }

    public String getStationNameAddress(int stationId) {
        String sStationName = XmlPullParser.NO_NAMESPACE;
        String sStationAddress = XmlPullParser.NO_NAMESPACE;
        if (stationId == 0) {
            return "Ubicaci\u00f3n Seleccionada";
        }
        SQLiteDatabase db = new DatabaseHelper(this._context, this._iAppID).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, description FROM estacion WHERE pk_id = ?", new String[]{XmlPullParser.NO_NAMESPACE + stationId});
        if (cursor.moveToFirst()) {
            sStationName = cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME));
            sStationAddress = cursor.getString(cursor.getColumnIndex(ShareConstants.WEB_DIALOG_PARAM_DESCRIPTION));
        }
        cursor.close();
        db.close();
        if (sStationName.equals(sStationAddress)) {
            return sStationName;
        }
        return sStationName + " (" + sStationAddress + ")";
    }

    public String getBusName(int busId) {
        String sBusName = XmlPullParser.NO_NAMESPACE;
        SQLiteDatabase db = new DatabaseHelper(this._context, this._iAppID).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM bus WHERE pk_id = ?", new String[]{XmlPullParser.NO_NAMESPACE + busId});
        if (cursor.moveToFirst()) {
            sBusName = cursor.getString(cursor.getColumnIndex(MovilixaConstants.NAME));
        }
        cursor.close();
        db.close();
        return sBusName;
    }

    public int getTrunkId(int stationId) {
        int iTrunkId = 0;
        SQLiteDatabase db = new DatabaseHelper(this._context, this._iAppID).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fk_troncal FROM estacion WHERE pk_id = ?", new String[]{XmlPullParser.NO_NAMESPACE + stationId});
        if (cursor.moveToFirst()) {
            iTrunkId = cursor.getInt(cursor.getColumnIndex("fk_troncal"));
        }
        cursor.close();
        db.close();
        return iTrunkId;
    }

    public int getBusTypeId(int busId) {
        int iBusTypeId = 0;
        SQLiteDatabase db = new DatabaseHelper(this._context, this._iAppID).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fk_busType FROM bus WHERE pk_id = ?", new String[]{XmlPullParser.NO_NAMESPACE + busId});
        if (cursor.moveToFirst()) {
            iBusTypeId = cursor.getInt(cursor.getColumnIndex("fk_busType"));
        }
        cursor.close();
        db.close();
        return iBusTypeId;
    }
}