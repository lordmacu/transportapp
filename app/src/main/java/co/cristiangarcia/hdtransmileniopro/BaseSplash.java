package co.cristiangarcia.hdtransmileniopro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelper;
import co.cristiangarcia.hdtransmileniopro.helpers.MovilixaConstants;
import co.cristiangarcia.hdtransmileniopro.helpers.PreferenceMovilixaManager;
import co.cristiangarcia.hdtransmileniopro.util.UtilsXa;

public class BaseSplash extends AppCompatActivity {
    private int _appID;
    private int _iVersionApp;
    private boolean _isUpgrade = false;
    private CreateDatabaseAsync _task = null;
    private boolean _timeElapsed = false;
    Timer _timer;
    private TimerTask _timerTask = null;
    Timer _timerTimeOut;
    private TimerTask _timerTimeOutTask = null;
    private boolean _tokenGCM = false;
     private int backgroundFloorSplash;
    private int backgroundSplash;
    private int buildings;
    private int clouds;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Class<?> mainClass;
    private PreferenceMovilixaManager pmm;
    private Class<?> registrationIntentService;
    private int road;
    private int splashLogo;
    private int splashResource;

    public class CreateDatabaseAsync extends AsyncTask<String, Void, Boolean> {
        protected Boolean bCreatedDatabase = Boolean.valueOf(false);

        protected void onPostExecute(Boolean success) {
            if (BaseSplash.this._timeElapsed && !BaseSplash.this.isFinishing() && BaseSplash.this.getApplicationContext() != null && BaseSplash.this._tokenGCM) {
                BaseSplash.this.startActivity(new Intent().setClass(BaseSplash.this, BaseSplash.this.mainClass));
                BaseSplash.this.finish();
            }
            this.bCreatedDatabase = Boolean.valueOf(true);
        }

        protected Boolean doInBackground(String... arg0) {
            DatabaseHelper myDbHelper = new DatabaseHelper(BaseSplash.this, BaseSplash.this._appID);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseSplash.this);
            int iDatabaseVersion = sharedPreferences.getInt(MovilixaConstants.DATABASE_VERSION, 0);
            if (iDatabaseVersion == 0 || iDatabaseVersion != BaseSplash.this._iVersionApp) {
                myDbHelper.setUpgradeDatabase(true);
                Editor editor = sharedPreferences.edit();
                editor.putInt(MovilixaConstants.DATABASE_VERSION, BaseSplash.this._iVersionApp);
                editor.commit();
            }
            try {
                myDbHelper.createDataBase();
                return Boolean.valueOf(true);
            } catch (IOException e) {
                throw new Error("Error creando las rutas");
            }
        }

        Boolean isCreated() {
            return this.bCreatedDatabase;
        }
    }

    public void setMainClass(Class<?> mainClass) {
        this.mainClass = mainClass;
    }

    public void setRegistrationIntentService(Class<?> registrationIntentService) {
        this.registrationIntentService = registrationIntentService;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public void setBuildings(int buildings) {
        this.buildings = buildings;
    }

    public void setBackgroundFloorSplash(int backgroundFloorSplash) {
        this.backgroundFloorSplash = backgroundFloorSplash;
    }

    public void setRoad(int road) {
        this.road = road;
    }

    public void setSplashLogo(int splashLogo) {
        this.splashLogo = splashLogo;
    }

    public void setBackgroundSplash(int backgroundSplash) {
        this.backgroundSplash = backgroundSplash;
    }

    public void set_appID(int _appID) {
        this._appID = _appID;
    }

    public void set_iVersionApp(int iVersionApp) {
        this._iVersionApp = iVersionApp;
    }

    public void setSplashResource(int splashResource) {
        this.splashResource = splashResource;
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        UtilsXa.setupWindowAnimations(this, getApplicationContext());
        super.onCreate(savedInstanceState);
        if (this._appID != 4) {
            setContentView(R.layout.fragment_splash_screen);
            ((LinearLayout) findViewById(R.id.mainLayout)).setBackgroundResource(this.backgroundSplash);
            FrameLayout lytFrmSplashAnim = (FrameLayout) findViewById(R.id.lytFrmSplashAnim);
            Display display = getWindowManager().getDefaultDisplay();
            drawHomeScreen(lytFrmSplashAnim, new Point(display.getWidth(), display.getHeight()));
        } else {
            setContentView(this.splashResource);
        }
        if (this._task == null) {
            this._task = new CreateDatabaseAsync();
            this._task.execute(new String[0]);
        }
        if (this._appID == 1) {
            this.mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MovilixaConstants.SENT_TOKEN_TO_SERVER, false)) {
                    }
                    if (BaseSplash.this._task != null && BaseSplash.this._task.isCreated().booleanValue() && !BaseSplash.this.isFinishing() && BaseSplash.this._timeElapsed) {
                        BaseSplash.this.startActivity(new Intent().setClass(BaseSplash.this, BaseSplash.this.mainClass));
                        BaseSplash.this.finish();
                    }
                    BaseSplash.this._tokenGCM = true;
                }
            };

        } else {
            this._tokenGCM = true;
        }
        this._timer = new Timer();
        this._timerTask = new TimerTask() {
            public void run() {
                if (BaseSplash.this._task == null || BaseSplash.this.isFinishing() || !BaseSplash.this._task.isCreated().booleanValue() || !BaseSplash.this._tokenGCM) {
                    BaseSplash.this._timeElapsed = true;
                    return;
                }
                BaseSplash.this.startActivity(new Intent().setClass(BaseSplash.this, BaseSplash.this.mainClass));
                BaseSplash.this.finish();
            }
        };
        this._timer.schedule(this._timerTask, 2000);
        this._timerTimeOut = new Timer();
        this._timerTimeOutTask = new TimerTask() {
            public void run() {
                if (BaseSplash.this._task == null || BaseSplash.this.isFinishing() || !BaseSplash.this._task.isCreated().booleanValue()) {
                    BaseSplash.this._tokenGCM = true;
                    return;
                }
                BaseSplash.this.startActivity(new Intent().setClass(BaseSplash.this, BaseSplash.this.mainClass));
                BaseSplash.this.finish();
            }
        };
        this._timerTimeOut.schedule(this._timerTimeOutTask, 10000);
        this.pmm = new PreferenceMovilixaManager(this);


    }

    public void onResume() {
        super.onResume();
        if (this._appID == 1) {
            LocalBroadcastManager.getInstance(this).registerReceiver(this.mRegistrationBroadcastReceiver, new IntentFilter(MovilixaConstants.REGISTRATION_COMPLETE));
        }
    }

    public void onPause() {
        if (this._appID == 1) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mRegistrationBroadcastReceiver);
        }
        super.onPause();
    }

    private void drawHomeScreen(FrameLayout lytFrmSplashAnim, Point size) {
        int topMargin = Math.round(((float) size.y) * 0.09f);
        RelativeLayout lytContent = new RelativeLayout(this);
        LayoutParams lpContent = new LayoutParams(-1, -1);
        lpContent.gravity = 1;
        lytContent.setLayoutParams(lpContent);
        lytContent.setBackgroundColor(0);
        ImageView imgImage1 = new ImageView(this);
        imgImage1.setId(UtilsXa.generateViewId());
        RelativeLayout.LayoutParams lpImage1 = new RelativeLayout.LayoutParams(-2, -2);
        lpImage1.addRule(14);
        lpImage1.setMargins(Math.round(((float) size.x) * 0.15f), topMargin, Math.round(((float) size.x) * 0.15f), 0);
        imgImage1.setLayoutParams(lpImage1);
        imgImage1.setImageResource(this.clouds);
        lytContent.addView(imgImage1);
        ImageView imgImage3 = new ImageView(this);
        RelativeLayout.LayoutParams lpImage3 = new RelativeLayout.LayoutParams(-1, Math.round(((float) size.x) * 0.486f));
        lpImage3.addRule(14);
        lpImage3.addRule(12);
        lpImage3.setMargins(0, 0, 0, Math.round(((float) size.x) / 5.45f));
        imgImage3.setScaleType(ScaleType.FIT_XY);
        imgImage3.setLayoutParams(lpImage3);
        imgImage3.setImageResource(this.buildings);
        lytContent.addView(imgImage3);
        View imgBase1 = new View(this);
        imgBase1.setBackgroundResource(this.backgroundFloorSplash);
        RelativeLayout.LayoutParams lpBase = new RelativeLayout.LayoutParams(-1, Math.round(((float) size.x) / 5.45f));
        lpBase.addRule(14);
        lpBase.addRule(12);
        imgBase1.setLayoutParams(lpBase);
        lytContent.addView(imgBase1);
        ImageView imgImage4 = new ImageView(this);
        RelativeLayout.LayoutParams lpImage4 = new RelativeLayout.LayoutParams(-1, Math.round(((float) size.x) / 4.65f));
        lpImage4.addRule(14);
        lpImage4.addRule(12);
        imgImage4.setScaleType(ScaleType.FIT_XY);
        imgImage4.setLayoutParams(lpImage4);
        imgImage4.setImageResource(this.road);
        lytContent.addView(imgImage4);
        ImageView imgImage2 = new ImageView(this);
        RelativeLayout.LayoutParams lpImage2 = new RelativeLayout.LayoutParams(-2, -2);
        lpImage2.addRule(14);
        lpImage2.addRule(3, imgImage1.getId());
        lpImage2.setMargins(Math.round(((float) size.x) * 0.13f), topMargin, Math.round(((float) size.x) * 0.13f), 0);
        imgImage2.setLayoutParams(lpImage2);
        imgImage2.setImageResource(this.splashLogo);
        lytContent.addView(imgImage2);
        lytFrmSplashAnim.addView(lytContent);
    }

    private boolean checkPlayServices() {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != 0) {
            return false;
        }
        return true;
    }
}