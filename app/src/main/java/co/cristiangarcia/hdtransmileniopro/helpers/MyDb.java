package co.cristiangarcia.hdtransmileniopro.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by cristiangarcia on 24/10/16.
 */

public class MyDb extends SQLiteAssetHelper {
    private static final String DB_NAME = "transmi_sitp.sqlite";
    private static final int DB_VERSION = 1;
    public MyDb(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public Cursor getData(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM estacion";

        Cursor mCur = db.rawQuery(query,null);

        mCur.moveToFirst();
        return mCur;
    }
}