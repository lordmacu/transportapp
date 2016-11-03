package co.cristiangarcia.hdtransmileniopro;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import co.cristiangarcia.hdtransmileniopro.helpers.AppEventsConstants;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelper;
import co.cristiangarcia.hdtransmileniopro.util.UtilsXa;

/**
 * Created by cristiangarcia on 30/10/16.
 */


public class BaseMovilixaBrowser extends AppCompatActivity {
    public int _appID;
    public Class<?> _busClass;
    public Class<?> _routeClass;
    private String _sArea1;
    private String _sArea2;
    private String _sArea3;
    protected Integer iEstacionDestino;
    protected Integer iEstacionOrigen;
    protected Integer iOrigenDestino;
    protected String imageId;
    protected LinearLayout lyWebView;
    protected WebView mWebView;
    protected String sAgencyId;
    protected Integer sitpId;

    private class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String sId = Uri.parse(url).getHost();
            Integer iId = Integer.valueOf(0);
            if (sId.contains(".")) {
                sId = sId.substring(sId.lastIndexOf(".") + 1);
            }
            try {
                iId = Integer.valueOf(Integer.parseInt(sId));
            } catch (NumberFormatException e) {
                iId = Integer.valueOf(0);
            }
            if (iId.intValue() > 0) {
                Intent intent;
                if (BaseMovilixaBrowser.this.iOrigenDestino.intValue() == 1) {
                    BaseMovilixaBrowser.this.iOrigenDestino = Integer.valueOf(2);
                    BaseMovilixaBrowser.this.iEstacionOrigen = iId;
                    Toast.makeText(BaseMovilixaBrowser.this, "Ahora seleccione la estaci\u00f3n de destino", 1).show();
                } else if (BaseMovilixaBrowser.this.iOrigenDestino.intValue() == 2) {
                    BaseMovilixaBrowser.this.iOrigenDestino = Integer.valueOf(3);
                    BaseMovilixaBrowser.this.iEstacionDestino = iId;
                    intent = new Intent(BaseMovilixaBrowser.this, BaseMovilixaBrowser.this._routeClass);
                    intent.putExtra("ESTACION_ORIGEN", BaseMovilixaBrowser.this.iEstacionOrigen);
                    intent.putExtra("ESTACION_DESTINO", BaseMovilixaBrowser.this.iEstacionDestino);
                    intent.putExtra("AGENCY", BaseMovilixaBrowser.this.sAgencyId);
                    BaseMovilixaBrowser.this.startActivity(intent);
                } else {
                    intent = new Intent(BaseMovilixaBrowser.this, BaseMovilixaBrowser.this._busClass);
                    intent.putExtra("ESTACION_ID", iId);
                    BaseMovilixaBrowser.this.startActivity(intent);
                }
            }
            return true;
        }
    }

    public BaseMovilixaBrowser() {
        this.mWebView = null;
        this.iOrigenDestino = Integer.valueOf(0);
        this.iEstacionOrigen = Integer.valueOf(0);
        this.iEstacionDestino = Integer.valueOf(0);
        this.sAgencyId = XmlPullParser.NO_NAMESPACE;
        this._sArea1 = XmlPullParser.NO_NAMESPACE;
        this._sArea2 = XmlPullParser.NO_NAMESPACE;
        this._sArea3 = XmlPullParser.NO_NAMESPACE;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);
        String sScreenView = "Browser";
        UtilsXa.setupWindowAnimations(this, getApplicationContext());
        this._appID = getResources().getInteger(getResources().getIdentifier("appID", "integer", getPackageName()));
        try {
            this._routeClass = Class.forName(getPackageName() + ".Route");
            this._busClass = Class.forName(getPackageName() + ".Station");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int resArea1 = getResources().getIdentifier("browserArea1", "string", getPackageName());

        int resArea2 = getResources().getIdentifier("browserArea2", "string", getPackageName());
        int resArea3 = getResources().getIdentifier("browserArea3", "string", getPackageName());
        if (resArea1 != 0) {
            this._sArea1 = getResources().getString(resArea1);
        }
        if (resArea2 != 0) {
            this._sArea2 = getResources().getString(resArea2);
        }
        if (resArea3 != 0) {
            this._sArea3 = getResources().getString(resArea3);
        }
        this.lyWebView = (LinearLayout) findViewById(R.id.layWebView);
        this.mWebView = new WebView(getApplicationContext());
        this.mWebView.setLayoutParams(new LayoutParams(-1, -1));
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        if (this._appID == 4) {
            this.mWebView.setInitialScale(1);
            this.mWebView.getSettings().setLoadWithOverviewMode(true);
            this.mWebView.getSettings().setUseWideViewPort(true);
        }
        String page = XmlPullParser.NO_NAMESPACE;
        this.sitpId = Integer.valueOf(getIntent().getIntExtra("SITP_TABLA_ID", 0));
        if (this.sitpId.intValue() != 0) {
            SQLiteDatabase db = new DatabaseHelper(this, this._appID).getReadableDatabase();
            Cursor cTabla = db.rawQuery("SELECT tabla, fk_agency FROM bus WHERE pk_id = ?", new String[]{XmlPullParser.NO_NAMESPACE + this.sitpId});
            if (cTabla.moveToFirst()) {
                String sTabRuta = "file:///android_asset/tab.png";
                if (cTabla.getString(cTabla.getColumnIndex("fk_agency")).equals("SITP-C")) {
                    sTabRuta = "file:///android_asset/tab_n.png";
                }
                page = "<html><style type='text/css'>html, body {height:100%; width: 100%; margin: 0; padding: 0; border: 0;} #tc tr td{text-align:center;border:1px solid black} .am{background-color:rgb(253,241,0)} .ne{background-color:rgb(0,0,0);color:#fff} .tab{background: url(" + sTabRuta + ") no-repeat center;color:rgb(255,255,255);text-align:center;height:30px} .ini{width:300px;font-family:Arial,Helvetica,sans-serif;padding:0;margin:0} </style><body><table width='100%' height='100%'><tr><td valign='middle' align='center'><table cellpadding='0' cellspacing='0' class='ini'><tbody id='tc'>" + cTabla.getString(cTabla.getColumnIndex("tabla")) + "</tbody></table></td></tr></table></body></html>";
            }
            if (!(cTabla == null || cTabla.isClosed())) {
                cTabla.close();
            }
            db.close();
            sScreenView = "Tabla";
        } else {
            this.imageId = getIntent().getStringExtra("IMAGE_ID");
            String path = "file:///android_asset/" + this.imageId + ".png";
            String useMap = XmlPullParser.NO_NAMESPACE;
            String mapImage = XmlPullParser.NO_NAMESPACE;
            this.iOrigenDestino = Integer.valueOf(getIntent().getIntExtra("ORIGEN_DESTINO", 0));
            this.sAgencyId = getIntent().getStringExtra("AGENCY");
            useMap = "usemap=\"#Map\" ";
            mapImage = "<map name=\"Map\" id=\"Map\">";
            if (this.imageId.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                mapImage = mapImage + this._sArea1;
            } else if (this.imageId.equals(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
                mapImage = mapImage + this._sArea2;
            } else if (this.imageId.equals("2")) {
                mapImage = mapImage + this._sArea3;
            } else {
                sScreenView = "BusMap";
            }
            mapImage = mapImage + "</map>";
            if (this.iOrigenDestino.intValue() != 1 && this.imageId.length() == 1) {
                Toast.makeText(this, "Haga clic en la estaci\u00f3n que desee consultar", 1).show();
            }
            this.mWebView.getSettings().setUseWideViewPort(true);
            page = "<html><meta name='viewport' content='initial-scale=1.0,maximum-scale=0.5,maximum-scale=5.0'/><style type='text/css'>html, body, #wrapper {height:100%; width: 100%; margin: 0; padding: 0; border: 0;}</style><body><table id=\"wrapper\"><tr><td><img src=\"" + path + "\" " + useMap + "/></td></tr></table>" + mapImage + "</body></html>";
        }
        BaseMovilixaBrowser baseMovilixaBrowser = this;
        this.mWebView.setWebViewClient(new MyWebViewClient());
        this.mWebView.loadDataWithBaseURL("fake", page, "text/html", "UTF-8", XmlPullParser.NO_NAMESPACE);
        if (VERSION.SDK_INT >= 11) {
            this.mWebView.setLayerType(0, null);
        }
        this.lyWebView.addView(this.mWebView);

    }

    public void onResume() {
        super.onResume();
        if (this.iOrigenDestino.intValue() == 3) {
            this.iOrigenDestino = Integer.valueOf(1);
        }
        if (this.iOrigenDestino.intValue() == 1) {
            Toast.makeText(this, "Seleccione la estaci\u00f3n de origen", 1).show();
            this.iEstacionOrigen = Integer.valueOf(0);
            this.iEstacionDestino = Integer.valueOf(0);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.lyWebView.removeAllViews();
        this.mWebView.destroy();
    }
}