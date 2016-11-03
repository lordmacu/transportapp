package co.cristiangarcia.hdtransmileniopro;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;

import org.xmlpull.v1.XmlPullParser;

import co.cristiangarcia.hdtransmileniopro.helpers.AppEventsConstants;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelper;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelperTransmiSitp;
import co.cristiangarcia.hdtransmileniopro.objects.StationView;
import co.cristiangarcia.hdtransmileniopro.util.UtilsXa;

public class MapaWebActivity extends AppCompatActivity {
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
    private DatabaseHelperTransmiSitp dbHelper;
    private  int mapIndi=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_web);
        String appKey = "4c52d9b1663a5fe3a04ff1fd15561990dc7ccc1b0133abad";
        Appodeal.initialize(this, appKey, Appodeal.BANNER);
        this.mWebView = null;
        this.iOrigenDestino = Integer.valueOf(0);
        this.iEstacionOrigen = Integer.valueOf(0);
        this.iEstacionDestino = Integer.valueOf(0);
        this.sAgencyId = XmlPullParser.NO_NAMESPACE;
        this._sArea1 = XmlPullParser.NO_NAMESPACE;
        this._sArea2 = XmlPullParser.NO_NAMESPACE;
        this._sArea3 = XmlPullParser.NO_NAMESPACE;

        this.dbHelper = new DatabaseHelperTransmiSitp(this, 1);


        String pathString= "<area shape=\"rect\" coords=\"1152,607,1260,627\" href=\"http://146\" />        <area shape=\"rect\" coords=\"979,284,1090,308\" href=\"http://154\" /><area shape=\"rect\" coords=\"983,243,1081,266\" href=\"http://153\" /><area shape=\"rect\" coords=\"880,245,934,268\" href=\"http://70\" /><area shape=\"rect\" coords=\"865,279,937,310\" href=\"http://71\" /><area shape=\"rect\" coords=\"300,173,337,204\" href=\"http://23\" /><area shape=\"rect\" coords=\"391,315,419,358\" href=\"http://33\" /><area shape=\"rect\" coords=\"422,332,451,364\" href=\"http://32\" /><area shape=\"rect\" coords=\"452,316,488,357\" href=\"http://31\" /><area shape=\"rect\" coords=\"749,339,787,373\" href=\"http://62\" /><area shape=\"rect\" coords=\"789,317,830,355\" href=\"http://61\" /><area shape=\"rect\" coords=\"864,325,892,357\" href=\"http://60\" /><area shape=\"rect\" coords=\"896,350,922,378\" href=\"http://59\" /><area shape=\"rect\" coords=\"192,365,280,384\" href=\"http://38\" /><area shape=\"rect\" coords=\"136,386,260,408\" href=\"http://39\" /><area shape=\"rect\" coords=\"175,415,251,435\" href=\"http://40\" /><area shape=\"rect\" coords=\"173,439,250,458\" href=\"http://41\" /><area shape=\"rect\" coords=\"164,465,251,489\" href=\"http://42\" /><area shape=\"rect\" coords=\"154,493,252,513\" href=\"http://43\" /><area shape=\"rect\" coords=\"160,516,254,538\" href=\"http://44\" /><area shape=\"rect\" coords=\"784,236,864,270\" href=\"http://117\" /><area shape=\"rect\" coords=\"785,285,858,309\" href=\"http://118\" /><area shape=\"rect\" coords=\"722,384,825,414\" href=\"http://119\" /><area shape=\"rect\" coords=\"715,422,802,442\" href=\"http://120\" /><area shape=\"rect\" coords=\"648,453,777,476\" href=\"http://121\" /><area shape=\"rect\" coords=\"641,484,748,506\" href=\"http://122\" /><area shape=\"rect\" coords=\"687,519,743,540\" href=\"http://123\" /><area shape=\"rect\" coords=\"637,551,747,571\" href=\"http://124\" /><area shape=\"rect\" coords=\"624,585,753,611\" href=\"http://125\" /><area shape=\"rect\" coords=\"640,618,751,644\" href=\"http://126\" /><area shape=\"rect\" coords=\"665,650,744,669\" href=\"http://127\" /><area shape=\"rect\" coords=\"669,680,745,703\" href=\"http://128\" /><area shape=\"rect\" coords=\"644,718,754,754\" href=\"http://129\" /><area shape=\"rect\" coords=\"471,377,535,399\" href=\"http://47\" /><area shape=\"rect\" coords=\"429,419,504,441\" href=\"http://49\" /><area shape=\"rect\" coords=\"446,399,520,419\" href=\"http://48\" /><area shape=\"rect\" coords=\"401,442,481,464\" href=\"http://50\" /><area shape=\"rect\" coords=\"376,464,455,484\" href=\"http://51\" /><area shape=\"rect\" coords=\"352,491,464,511\" href=\"http://52\" /><area shape=\"rect\" coords=\"352,517,497,540\" href=\"http://53\" /><area shape=\"rect\" coords=\"352,543,463,566\" href=\"http://54\" /><area shape=\"rect\" coords=\"353,570,453,593\" href=\"http://55\" /><area shape=\"rect\" coords=\"354,596,444,615\" href=\"http://56\" /><area shape=\"rect\" coords=\"354,621,472,647\" href=\"http://57\" /><area shape=\"rect\" coords=\"364,329,389,370\" href=\"http://34\" /><area shape=\"rect\" coords=\"223,206,266,236\" href=\"http://26\" /><area shape=\"rect\" coords=\"332,323,357,356\" href=\"http://35\" /><area shape=\"rect\" coords=\"300,337,328,365\" href=\"http://36\" /><area shape=\"rect\" coords=\"716,321,749,353\" href=\"http://63\" /><area shape=\"rect\" coords=\"681,339,713,372\" href=\"http://64\" /><area shape=\"rect\" coords=\"647,314,680,357\" href=\"http://65\" /><area shape=\"rect\" coords=\"609,333,646,376\" href=\"http://66\" /><area shape=\"rect\" coords=\"567,314,609,354\" href=\"http://67\" /><area shape=\"rect\" coords=\"491,348,552,377\" href=\"http://46\" /><area shape=\"rect\" coords=\"232,333,299,364\" href=\"http://37\" /><area shape=\"rect\" coords=\"905,311,960,339\" href=\"http://72\" /><area shape=\"rect\" coords=\"936,340,961,366\" href=\"http://58\" /><area shape=\"rect\" coords=\"1101,661,1189,696\" href=\"http://86\" /><area shape=\"rect\" coords=\"1058,666,1096,704\" href=\"http://85\" /><area shape=\"rect\" coords=\"984,662,1056,696\" href=\"http://84\" /><area shape=\"rect\" coords=\"946,634,1051,657\" href=\"http://83\" /><area shape=\"rect\" coords=\"1033,603,1108,631\" href=\"http://82\" /><area shape=\"rect\" coords=\"965,578,1045,601\" href=\"http://81\" /><area shape=\"rect\" coords=\"984,546,1101,567\" href=\"http://80\" /><area shape=\"rect\" coords=\"966,526,1043,543\" href=\"http://79\" /><area shape=\"rect\" coords=\"941,504,1027,521\" href=\"http://78\" /><area shape=\"rect\" coords=\"927,476,1015,501\" href=\"http://77\" /><area shape=\"rect\" coords=\"919,456,1013,474\" href=\"http://76\" /><area shape=\"rect\" coords=\"916,430,994,448\" href=\"http://75\" /><area shape=\"rect\" coords=\"918,405,1016,426\" href=\"http://74\" /><area shape=\"rect\" coords=\"921,380,1020,401\" href=\"http://73\" /><area shape=\"rect\" coords=\"983,336,1013,372\" href=\"http://87\" /><area shape=\"rect\" coords=\"1015,319,1052,360\" href=\"http://88\" /><area shape=\"rect\" coords=\"1054,340,1109,361\" href=\"http://89\" /><area shape=\"rect\" coords=\"1070,363,1160,381\" href=\"http://90\" /><area shape=\"rect\" coords=\"1090,383,1187,405\" href=\"http://91\" /><area shape=\"rect\" coords=\"1113,407,1219,426\" href=\"http://92\" /><area shape=\"rect\" coords=\"1121,428,1207,447\" href=\"http://93\" /><area shape=\"rect\" coords=\"1118,448,1214,469\" href=\"http://94\" /><area shape=\"rect\" coords=\"1118,474,1213,497\" href=\"http://95\" /><area shape=\"rect\" coords=\"1123,504,1204,525\" href=\"http://96\" /><area shape=\"rect\" coords=\"1122,536,1205,556\" href=\"http://97\" /><area shape=\"rect\" coords=\"1251,712,1342,734\" href=\"http://144\" /><area shape=\"rect\" coords=\"1230,685,1371,707\" href=\"http://143\" /><area shape=\"rect\" coords=\"1210,665,1280,683\" href=\"http://142\" /><area shape=\"rect\" coords=\"1183,638,1291,658\" href=\"http://141\" /><area shape=\"rect\" coords=\"1122,561,1249,585\" href=\"http://98\" /><area shape=\"rect\" coords=\"1270,419,1356,456\" href=\"http://114\" /><area shape=\"rect\" coords=\"1271,371,1332,395\" href=\"http://113\" /><area shape=\"rect\" coords=\"1190,359,1267,386\" href=\"http://112\" /><area shape=\"rect\" coords=\"747,8,858,34\" href=\"http://150\" /><area shape=\"rect\" coords=\"862,9,952,35\" href=\"http://151\" /><area shape=\"rect\" coords=\"898,45,1002,68\" href=\"http://152\" /><area shape=\"rect\" coords=\"797,70,838,116\" href=\"http://140\" /><area shape=\"rect\" coords=\"850,77,881,117\" href=\"http://130\" /><area shape=\"rect\" coords=\"889,102,922,146\" href=\"http://131\" /><area shape=\"rect\" coords=\"925,78,961,118\" href=\"http://132\" /><area shape=\"rect\" coords=\"969,76,1010,115\" href=\"http://133\" /><area shape=\"rect\" coords=\"1012,100,1046,132\" href=\"http://134\" /><area shape=\"rect\" coords=\"1047,87,1075,121\" href=\"http://135\" /><area shape=\"rect\" coords=\"1077,102,1105,141\" href=\"http://136\" /><area shape=\"rect\" coords=\"1109,66,1140,115\" href=\"http://137\" /><area shape=\"rect\" coords=\"1141,100,1176,136\" href=\"http://138\" /><area shape=\"rect\" coords=\"1181,93,1263,127\" href=\"http://139\" /><area shape=\"rect\" coords=\"1340,310,1387,361\" href=\"http://111\" /><area shape=\"rect\" coords=\"1303,309,1338,347\" href=\"http://110\" /><area shape=\"rect\" coords=\"1271,322,1301,357\" href=\"http://109\" /><area shape=\"rect\" coords=\"1233,309,1268,344\" href=\"http://108\" /><area shape=\"rect\" coords=\"1145,315,1228,338\" href=\"http://107\" /><area shape=\"rect\" coords=\"1175,279,1261,298\" href=\"http://106\" /><area shape=\"rect\" coords=\"1149,256,1225,276\" href=\"http://105\" /><area shape=\"rect\" coords=\"1131,233,1206,253\" href=\"http://104\" /><area shape=\"rect\" coords=\"1116,213,1191,231\" href=\"http://103\" /><area shape=\"rect\" coords=\"1093,192,1188,211\" href=\"http://102\" /><area shape=\"rect\" coords=\"1027,198,1054,226\" href=\"http://100\" /><area shape=\"rect\" coords=\"1055,178,1084,217\" href=\"http://101\" /><area shape=\"rect\" coords=\"996,179,1025,214\" href=\"http://99\" /><area shape=\"rect\" coords=\"955,193,982,235\" href=\"http://1\" /><area shape=\"rect\" coords=\"929,196,952,216\" href=\"http://2\" /><area shape=\"rect\" coords=\"911,159,950,195\" href=\"http://69\" /><area shape=\"rect\" coords=\"895,203,921,243\" href=\"http://3\" /><area shape=\"rect\" coords=\"490,204,530,235\" href=\"http://16\" /><area shape=\"rect\" coords=\"404,173,439,203\" href=\"http://19\" /><area shape=\"rect\" coords=\"347,175,393,205\" href=\"http://21\" /><area shape=\"rect\" coords=\"388,266,479,288\" href=\"http://68\" /><area shape=\"rect\" coords=\"522,265,569,293\" href=\"http://45\" /><area shape=\"rect\" coords=\"865,165,893,208\" href=\"http://4\" /><area shape=\"rect\" coords=\"819,198,847,225\" href=\"http://5\" /><area shape=\"rect\" coords=\"788,178,817,205\" href=\"http://6\" /><area shape=\"rect\" coords=\"756,203,791,239\" href=\"http://7\" /><area shape=\"rect\" coords=\"733,170,773,203\" href=\"http://8\" /><area shape=\"rect\" coords=\"707,207,743,240\" href=\"http://9\" /><area shape=\"rect\" coords=\"681,170,718,206\" href=\"http://10\" /><area shape=\"rect\" coords=\"648,207,701,237\" href=\"http://11\" /><area shape=\"rect\" coords=\"631,173,665,204\" href=\"http://12\" /><area shape=\"rect\" coords=\"604,207,645,238\" href=\"http://13\" /><area shape=\"rect\" coords=\"569,172,609,206\" href=\"http://14\" /><area shape=\"rect\" coords=\"520,173,555,203\" href=\"http://15\" /><area shape=\"rect\" coords=\"379,206,412,241\" href=\"http://20\" /><area shape=\"rect\" coords=\"429,205,461,243\" href=\"http://18\" /><area shape=\"rect\" coords=\"460,175,502,203\" href=\"http://17\" /><area shape=\"rect\" coords=\"326,206,365,242\" href=\"http://22\" /><area shape=\"rect\" coords=\"180,204,217,244\" href=\"http://28\" /><area shape=\"rect\" coords=\"145,156,181,212\" href=\"http://29\" /><area shape=\"rect\" coords=\"250,168,291,203\" href=\"http://25\" /><area shape=\"rect\" coords=\"275,205,315,248\" href=\"http://24\" /><area shape=\"rect\" coords=\"201,161,241,204\" href=\"http://27\" /><area shape=\"rect\" coords=\"109,194,144,231\" href=\"http://30\" /><area shape=\"rect\" coords=\"58,170,107,209\" href=\"http://145\" />";

        String sScreenView = "Browser";
        UtilsXa.setupWindowAnimations(this, getApplicationContext());
        this._appID = getResources().getInteger(getResources().getIdentifier("appID", "integer", getPackageName()));
        try {
            this._routeClass = Class.forName(getPackageName() + ".ResultadoRutaActivity");
            this._busClass = Class.forName(getPackageName() + ".EstacionActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int resArea1 = getResources().getIdentifier("browserArea1", "string", getPackageName());
        int resArea2 = getResources().getIdentifier("browserArea2", "string", getPackageName());
        int resArea3 = getResources().getIdentifier("browserArea3", "string", getPackageName());
            this._sArea1 = pathString;

        if (resArea2 != 0) {
            this._sArea2 = getResources().getString(resArea2);
        }
        if (resArea3 != 0) {
            this._sArea3 = getResources().getString(resArea3);
        }
        this.lyWebView = (LinearLayout) findViewById(R.id.layWebView);
        this.mWebView = new WebView(getApplicationContext());
        this.mWebView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        if (this._appID == 4) {
            this.mWebView.setInitialScale(1);
            this.mWebView.getSettings().setLoadWithOverviewMode(true);
            this.mWebView.getSettings().setUseWideViewPort(true);
        }
        String page = XmlPullParser.NO_NAMESPACE;
        this.sitpId = Integer.valueOf(getIntent().getIntExtra("SITP_TABLA_ID", 0));


        this.mapIndi=Integer.valueOf(getIntent().getIntExtra("mapIndi", 0));


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


        MapaWebActivity MapaWebActivity = this;
        this.mWebView.setWebViewClient(new MapaWebActivity.MyWebViewClient());
        this.mWebView.loadDataWithBaseURL("fake", page, "text/html", "UTF-8", XmlPullParser.NO_NAMESPACE);
        if (Build.VERSION.SDK_INT >= 11) {
            this.mWebView.setLayerType(0, null);
        }
        this.lyWebView.addView(this.mWebView);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);



        actionBar.setDisplayHomeAsUpEnabled(true);



        actionBar.setTitle("Selector de Ruta en Mapa");
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

 
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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


                if(MapaWebActivity.this.mapIndi==1){

                    intent= new Intent(MapaWebActivity.this,BusesIndiActivity.class);


                    try {
                        dbHelper.openDataBase();
                        StationView station= dbHelper.getStation(iId);
                        dbHelper.close();
                        intent.putExtra("item", station);

                         MapaWebActivity.this.startActivity(intent);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                }else{
                    if (MapaWebActivity.this.iOrigenDestino.intValue() == 1) {
                        MapaWebActivity.this.iOrigenDestino = Integer.valueOf(2);
                        MapaWebActivity.this.iEstacionOrigen = iId;
                        Toast.makeText(MapaWebActivity.this, "Ahora seleccione la estaci\u00f3n de destino", 1).show();
                    } else if (MapaWebActivity.this.iOrigenDestino.intValue() == 2) {
                        MapaWebActivity.this.iOrigenDestino = Integer.valueOf(3);
                        MapaWebActivity.this.iEstacionDestino = iId;
                        intent = new Intent(MapaWebActivity.this, MapaWebActivity.this._routeClass);
                        intent.putExtra("ESTACION_ORIGEN", MapaWebActivity.this.iEstacionOrigen);
                        intent.putExtra("ESTACION_DESTINO", MapaWebActivity.this.iEstacionDestino);
                        intent.putExtra("AGENCY", MapaWebActivity.this.sAgencyId);
                        MapaWebActivity.this.startActivity(intent);
                    }
                }
            }
            return true;
        }
    }

    public void onResume() {
        super.onResume();

        if(MapaWebActivity.this.mapIndi==1){
            Toast.makeText(this, "Seleccione estación a consultar", 1).show();


        }else{
            if (this.iOrigenDestino.intValue() == 3) {
                this.iOrigenDestino = Integer.valueOf(1);
            }
            if (this.iOrigenDestino.intValue() == 1) {
                Toast.makeText(this, "Seleccione la estaci\u00f3n de origen", 1).show();
                this.iEstacionOrigen = Integer.valueOf(0);
                this.iEstacionDestino = Integer.valueOf(0);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.lyWebView.removeAllViews();
        this.mWebView.destroy();
    }

}
