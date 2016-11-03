package co.cristiangarcia.hdtransmileniopro.helpers;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;

import org.ksoap2.serialization.PropertyInfo;
import org.xmlpull.v1.XmlPullParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.cristiangarcia.hdtransmileniopro.R;

public class PreferenceMovilixaManager {
    private String _namespaceWS;
    private String _soapActionWS;
    private String _urlWS;
    private int appID;
    private Context context;
    private SharedPreferences sp;

    public String get_urlWS() {
        return this._urlWS;
    }

    public String get_namespaceWS() {
        return this._namespaceWS;
    }

    public String get_soapActionWS() {
        return this._soapActionWS;
    }

    public PreferenceMovilixaManager(Context context) {
        this.context = context;
        this.appID = context.getResources().getInteger(context.getResources().getIdentifier("appID", "integer", context.getPackageName()));
        this.sp = PreferenceManager.getDefaultSharedPreferences(context);
        this._urlWS = context.getResources().getString(R.string.web_services);
        this._namespaceWS = context.getResources().getString(R.string.web_services_namespace);
        this._soapActionWS = context.getResources().getString(R.string.web_services_namespace);
    }

    public void verifyPremiumInServerAsync(final Context context) {
        new AsyncTask<Void, String, String>() {
            protected String doInBackground(Void... parameters) {
                return PreferenceMovilixaManager.this.verifyPremiumInServer(context);
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                PreferenceMovilixaManager.this.verifyPremiumInServerResult(s);
            }
        }.execute(new Void[0]);
    }

    public String verifyPremiumInServer(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isFB = preferences.getBoolean(MovilixaConstants.IS_AUTHENTICATED_FB, false);
        boolean isGPlus = preferences.getBoolean(MovilixaConstants.IS_AUTHENTICATED_GP, false);
        String userId = preferences.getString(MovilixaConstants.USER_ID, XmlPullParser.NO_NAMESPACE);
        PropertyInfo[] params = new PropertyInfo[5];
        params[0].setName(MovilixaConstants.USER_ID);
        params[0].setValue(userId);
        params[0].setType(String.class);
        params[1] = new PropertyInfo();
        params[1].setName("userTypeId");
        if (isFB) {
            params[1].setValue(AppEventsConstants.EVENT_PARAM_VALUE_YES);
        } else if (isGPlus) {
            params[1].setValue("2");
        } else {
            params[1].setValue(XmlPullParser.NO_NAMESPACE);
        }
        params[1].setType(String.class);
        params[2] = new PropertyInfo();
        params[2].setName("appId");
        params[2].setValue(String.valueOf(this.appID));
        params[2].setType(String.class);
        params[3] = new PropertyInfo();
        params[3].setName("sParam");
        params[3].setValue(MovilixaConstants.S_PARAM);
        params[3].setType(String.class);
        params[4] = new PropertyInfo();
        params[4].setName("sVer");
        params[4].setValue(AppEventsConstants.EVENT_PARAM_VALUE_YES);
        params[4].setType(String.class);
        return Utils.getWebServiceString(this._urlWS, this._namespaceWS, this._soapActionWS, "verifyPremiumUserApp", params);
    }

    public void verifyPremiumInServerResult(String result) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        Editor editor = preferences.edit();
        if (result.length() > 4) {
            Date expirationDate = new Date(0);
            try {
                expirationDate = new SimpleDateFormat("yyyyMMdd").parse(result);
            } catch (ParseException e) {
            }
            if (expirationDate.getTime() <= new Date().getTime()) {
                editor.putBoolean(MovilixaConstants.IS_PREMIUM, false);
                editor.commit();
                return;
            } else if (!preferences.getBoolean(MovilixaConstants.IS_PREMIUM, false)) {
                editor.putBoolean(MovilixaConstants.IS_PREMIUM, true);
                editor.putLong(MovilixaConstants.EXPIRATION_NO_ADS_YEAR, expirationDate.getTime());
                editor.commit();
                  return;
            } else {
                return;
            }
        }
        editor.putBoolean(MovilixaConstants.IS_PREMIUM, false);
        editor.commit();
    }

    public String convertFavoritos(String text) {
        String result = XmlPullParser.NO_NAMESPACE;
        DatabaseHelperTransmiSitp db = new DatabaseHelperTransmiSitp(this.context, 1);
        db.openDataBase();
        for (String bus : text.split(";")) {
            if (bus.contains(",")) {
                result = result + db.getIdBusFromName(bus.substring(2));
            }
        }
        db.close();
        return result.substring(1);
    }

    public String verifyFavoritesInServer(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isFB = preferences.getBoolean(MovilixaConstants.IS_AUTHENTICATED_FB, false);
        boolean isGPlus = preferences.getBoolean(MovilixaConstants.IS_AUTHENTICATED_GP, false);
        String userId = preferences.getString(MovilixaConstants.USER_ID, XmlPullParser.NO_NAMESPACE);
        PropertyInfo[] params = new PropertyInfo[5];
        params[0].setName(MovilixaConstants.USER_ID);
        params[0].setValue(userId);
        params[0].setType(String.class);
        params[1] = new PropertyInfo();
        params[1].setName("userTypeId");
        if (isFB) {
            params[1].setValue(AppEventsConstants.EVENT_PARAM_VALUE_YES);
        } else if (isGPlus) {
            params[1].setValue("2");
        } else {
            params[1].setValue(XmlPullParser.NO_NAMESPACE);
        }
        params[1].setType(String.class);
        params[2] = new PropertyInfo();
        params[2].setName("appId");
        params[2].setValue(String.valueOf(this.appID));
        params[2].setType(String.class);
        params[3] = new PropertyInfo();
        params[3].setName("sParam");
        params[3].setValue(MovilixaConstants.S_PARAM);
        params[3].setType(String.class);
        params[4] = new PropertyInfo();
        params[4].setName("sVer");
        params[4].setValue(AppEventsConstants.EVENT_PARAM_VALUE_YES);
        params[4].setType(String.class);
        String sFavorites = XmlPullParser.NO_NAMESPACE;
        if (preferences.getString(MovilixaConstants.FAVORITES, XmlPullParser.NO_NAMESPACE).isEmpty()) {
            sFavorites = Utils.getWebServiceString(this._urlWS, this._namespaceWS, this._soapActionWS, "getFavoritesUserApp", params);
        }
        return sFavorites;
    }

    public void setFavoritesFromServer(String sFavorites) {
        if (sFavorites.contains(",")) {
            sFavorites = convertFavoritos(sFavorites);
        }
        if (!sFavorites.isEmpty() && !sFavorites.equals(" ")) {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(this.context).edit();
            editor.putString(MovilixaConstants.FAVORITES, sFavorites);
            editor.commit();
        }
    }

    public void setDateSyncStatus(Date date) {
        Editor editor = this.sp.edit();
        editor.putString(MovilixaConstants.SYNC_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date));
        editor.commit();
    }

    public Date getDateSyncStatus() {
        Date date = new Date(0);
        String sSyncDate = this.sp.getString(MovilixaConstants.SYNC_DATE, XmlPullParser.NO_NAMESPACE);
        if (!sSyncDate.isEmpty()) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(sSyncDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public boolean isAuthenticated() {
        return this.sp.getBoolean(MovilixaConstants.IS_AUTHENTICATED, false);
    }

    public boolean isAuthentucatedGPlus() {
        return this.sp.getBoolean(MovilixaConstants.IS_AUTHENTICATED_GP, false);
    }

    public boolean isAuthentucatedFB() {
        return this.sp.getBoolean(MovilixaConstants.IS_AUTHENTICATED_FB, false);
    }

    public void reviewNoAdsYear() {
        if (this.sp.getBoolean(MovilixaConstants.IS_PREMIUM, false)) {
            if (new Date(System.currentTimeMillis()).getTime() > new Date(this.sp.getLong(MovilixaConstants.EXPIRATION_NO_ADS_YEAR, 0)).getTime()) {
                Editor editor = this.sp.edit();
                editor.putBoolean(MovilixaConstants.IS_PREMIUM, false);
                editor.commit();
            }
        }
    }

    public void setResultAndCloseActivity(Activity activity) {
        activity.setResult(-1);
        activity.finish();
    }

    public void preferencesLogOut() {
        Editor editor = this.sp.edit();
        editor.putBoolean(MovilixaConstants.IS_AUTHENTICATED, false);
        editor.putBoolean(MovilixaConstants.IS_AUTHENTICATED_FB, false);
        editor.putBoolean(MovilixaConstants.IS_AUTHENTICATED_GP, false);
        editor.putString(MovilixaConstants.NAME, XmlPullParser.NO_NAMESPACE);
        editor.putString(MovilixaConstants.EMAIL, XmlPullParser.NO_NAMESPACE);
        editor.putString(MovilixaConstants.IMG_USER, XmlPullParser.NO_NAMESPACE);
        editor.putBoolean(MovilixaConstants.IS_PREMIUM, false);
        editor.putBoolean(MovilixaConstants.VALIDATING_EMAIL, false);
        editor.putBoolean(MovilixaConstants.EMAIL_VALIDATED, false);
        editor.commit();
    }

    public Bitmap getImageUser() {
        String encodedImage = PreferenceManager.getDefaultSharedPreferences(this.context).getString(MovilixaConstants.IMG_USER, XmlPullParser.NO_NAMESPACE);
        if (encodedImage.equalsIgnoreCase(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        byte[] b = Base64.decode(encodedImage, 0);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
}
