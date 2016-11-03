package co.cristiangarcia.hdtransmileniopro.helpers;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.wap.Wbxml;
import org.xmlpull.v1.XmlPullParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class Utils {
    public Boolean bNetwork = Boolean.valueOf(false);
    private final Activity mActivity;

    public Utils(Activity activity) {
        this.mActivity = activity;
        NetworkInfo networkInfo = ((ConnectivityManager) this.mActivity.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            this.bNetwork = Boolean.valueOf(true);
        }
    }




    public static String getWebServiceString(String sUrl, String sNamespace, String sSoapAction, String sMethod, PropertyInfo[] params) {
        String sRet = XmlPullParser.NO_NAMESPACE;
        SoapObject request = new SoapObject(sNamespace, sMethod);
        for (PropertyInfo pi : params) {
            request.addProperty(pi);
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        try {
            new HttpTransportSE(sUrl).call(sSoapAction + sMethod, envelope);
            sRet = ((SoapPrimitive) envelope.getResponse()).toString();
        } catch (Exception e) {
            Log.i(Utils.class.toString(), e.toString());
        }
        return sRet;
    }

}