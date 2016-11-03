package co.cristiangarcia.hdtransmileniopro.helpers;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION_RMETRODF = 14;
    private static final int DATABASE_VERSION_RMIO = 27;
    private static final int DATABASE_VERSION_TS = 197;
    private static final int DATABASE_VERSION_XAPASTO = 30;
    protected String DB_PATH;
    private boolean bUpgradeDatabase;
    protected Context context;
    private int iNewVersion;
    private SQLiteDatabase myDataBase;
    protected String sDataBaseName;

    public DatabaseHelper(Context context, int iAppID) {
          super(context, "transmi_sitp", null, 1);
        this.bUpgradeDatabase = false;
        this.iNewVersion = 0;
        this.context = context;
        String str2 = iAppID == 1 ? "transmi_sitp" : iAppID == 2 ? "rutasmio" : iAppID == 3 ? "rutasmetrodf" : "movilixa_pasto";
        this.sDataBaseName = str2;
        str2 = iAppID == 1 ? "transmi_sitp" : iAppID == 2 ? "rutasmio" : iAppID == 3 ? "rutasmetrodf" : "movilixa_pasto";
        this.DB_PATH = context.getDatabasePath("transmi_sitp").getPath();
    }

    public void setUpgradeDatabase(boolean upgradeDatabase) {
        this.bUpgradeDatabase = upgradeDatabase;
    }

    public void createDataBase() throws IOException {
        if (checkDataBase()) {
            getReadableDatabase();
            close();
        }
        if (!checkDataBase()) {
            getReadableDatabase();
            close();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copiando la base de datos.");
            }
        }
    }

    private boolean checkDataBase() {
        return new File(this.DB_PATH).exists();
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

    public void openDataBase() throws SQLException {
        this.myDataBase = SQLiteDatabase.openDatabase(this.DB_PATH, null, 1);
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
}
