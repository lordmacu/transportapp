package co.cristiangarcia.hdtransmileniopro.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.transition.Slide;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.GeofencingRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import co.cristiangarcia.hdtransmileniopro.R;
import co.cristiangarcia.hdtransmileniopro.helpers.DfpAdsXa;

public class UtilsXa {
    public Boolean bNetwork;
    private final Activity mActivity;

    /* renamed from: util.UtilsXa.1 */
    class C07201 implements Runnable {
        final /* synthetic */ Dialog val$d;

        C07201(Dialog dialog) {
            this.val$d = dialog;
        }

        public void run() {
            this.val$d.dismiss();
        }
    }

    /* renamed from: util.UtilsXa.2 */
    class C07212 implements OnClickListener {
        C07212() {
        }

        public void onClick(DialogInterface dialog, int which) {
            UtilsXa.this.mActivity.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        }
    }

    /* renamed from: util.UtilsXa.3 */
    class C07223 implements OnClickListener {
        C07223() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    /* renamed from: util.UtilsXa.4 */
    class C07234 implements OnClickListener {
        C07234() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (VERSION.SDK_INT >= 16) {
                UtilsXa.this.mActivity.startActivity(new Intent("android.settings.NFC_SETTINGS"));
            } else {
                UtilsXa.this.mActivity.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
            }
        }
    }

    /* renamed from: util.UtilsXa.5 */
    class C07245 implements OnClickListener {
        C07245() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    public UtilsXa(Activity activity) {
        this.bNetwork = Boolean.valueOf(false);
        this.mActivity = activity;
        NetworkInfo networkInfo = ((ConnectivityManager) this.mActivity.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            this.bNetwork = Boolean.valueOf(true);
        }
    }

    public PublisherAdView obtainAd(int appID) {
        PublisherAdView adView = new PublisherAdView(this.mActivity);
        switch (appID) {
            case GeofencingRequest.INITIAL_TRIGGER_ENTER /*1*/:
                adView.setAdUnitId(DfpAdsXa.STANDARD_BANNER_TS.adunitId);
                break;
            case GeofencingRequest.INITIAL_TRIGGER_EXIT /*2*/:
                adView.setAdUnitId(DfpAdsXa.STANDARD_BANNER_RMIO.adunitId);
                break;
            case DetectedActivity.STILL /*3*/:
                adView.setAdUnitId(DfpAdsXa.STANDARD_BANNER_RMETRO_DF.adunitId);
                break;
        }
        adView.setAdSizes(AdSize.BANNER);
        LayoutParams lAdParams = new LayoutParams(-1, -2);
        lAdParams.gravity = Gravity.CENTER_HORIZONTAL;
        adView.setLayoutParams(lAdParams);
        return adView;
    }

    public PublisherAdView obtain50x50Ad(int appID) {
        PublisherAdView adView = new PublisherAdView(this.mActivity);
        switch (appID) {
            case GeofencingRequest.INITIAL_TRIGGER_ENTER /*1*/:
                adView.setAdUnitId(DfpAdsXa.SMALL_BANNER_TS.adunitId);
                break;
            case GeofencingRequest.INITIAL_TRIGGER_EXIT /*2*/:
                adView.setAdUnitId(DfpAdsXa.SMALL_BANNER_RMIO.adunitId);
                break;
            case DetectedActivity.STILL /*3*/:
                adView.setAdUnitId(DfpAdsXa.SMALL_BANNER_RMETRO_DF.adunitId);
                break;
        }
        adView.setAdSizes(new AdSize(50, 50));
        adView.setLayoutParams(new LayoutParams(-2, -2));
        return adView;
    }

    public static String getUrlText(String mUrl) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(mUrl).openConnection().getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static JSONArray getJSONfromURL(String url) {
        try {
            return new JSONArray(getUrlText(url));
        } catch (JSONException e) {
            return null;
        }
    }

    public boolean isFullyConnected() {
        if (!this.bNetwork.booleanValue()) {
            return false;
        }
        try {
            if (((HttpURLConnection) new URL("http://resultadodelaloteria.com/ping.htm").openConnection()).getResponseCode() != Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void ShowMessage(Context c, String message) {
        timerDelayRemoveDialog(2000, ProgressDialog.show(c, XmlPullParser.NO_NAMESPACE, message, false));
    }

    private void timerDelayRemoveDialog(long time, Dialog d) {
        new Handler().postDelayed(new C07201(d), time);
    }

    public int hasGps() {
        if (ContextCompat.checkSelfPermission(this.mActivity, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            LocationManager locationManager = (LocationManager) this.mActivity.getSystemService(Context.LOCATION_SERVICE);
            if (Boolean.valueOf(locationManager.isProviderEnabled("gps")).booleanValue()) {
                return 2;
            }
            if (locationManager.getAllProviders().contains("gps")) {
                return 1;
            }
            return 0;
        }
        ActivityCompat.requestPermissions(this.mActivity, new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 1);
        return -1;
    }

    public void showSettingsAlert() {
        Builder alertDialog = new Builder(this.mActivity);
        alertDialog.setTitle("Configuraci\u00f3n GPS");
        alertDialog.setMessage("El GPS no se encuentra activado. \u00bfDesea ir al men\u00fa de configuraci\u00f3n?");
        alertDialog.setPositiveButton("Configuraci\u00f3n", new C07212());
        alertDialog.setNegativeButton("Cancel", new C07223());
        alertDialog.show();
    }

    public void showSettingsAlertNFC() {
        Builder alertDialog = new Builder(this.mActivity);
        alertDialog.setTitle("Configuraci\u00f3n NFC");
        alertDialog.setMessage("El NFC no se encuentra activado. \u00bfDesea ir al men\u00fa de configuraci\u00f3n?");
        alertDialog.setPositiveButton("Configuraci\u00f3n", new C07234());
        alertDialog.setNegativeButton("Cancel", new C07245());
        alertDialog.show();
    }

    public static int generateViewId() {
        int result;
        AtomicInteger sNextGeneratedId = new AtomicInteger(12);
        int newValue;
        do {
            result = sNextGeneratedId.get();
            newValue = result + 1;
            if (newValue > ViewCompat.MEASURED_SIZE_MASK) {
                newValue = 1;
            }
        } while (!sNextGeneratedId.compareAndSet(result, newValue));
        return result;
    }

    public WakeLock acquireScreen(Context context) {
        WakeLock _wakelock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(805306394, "GPS LOCK");
        if (!_wakelock.isHeld()) {
            _wakelock.acquire();
        }
        return _wakelock;
    }

    public void releaseScreen(WakeLock _wakelock) {
        if (_wakelock != null) {
            _wakelock.release();
        }
    }

    public static int getPrimaryColor(Activity act) {
        TypedValue value = new TypedValue();
        act.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    public static int getAccentColor(Activity act) {
        TypedValue value = new TypedValue();
        act.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    public static void setupWindowAnimations(Activity activity, Context context) {
        if (VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.LEFT);
            slideTransition.setDuration((long) context.getResources().getInteger(R.integer.anim_duration_long));
            activity.getWindow().setReenterTransition(slideTransition);
            activity.getWindow().setExitTransition(slideTransition);
        }
    }
}
