package co.cristiangarcia.hdtransmileniopro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import co.cristiangarcia.hdtransmileniopro.helpers.AppEventsConstants;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelper;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelperTransmiSitp;
import co.cristiangarcia.hdtransmileniopro.helpers.IabHelper;
import co.cristiangarcia.hdtransmileniopro.helpers.Network;
import co.cristiangarcia.hdtransmileniopro.objects.BusView;
import co.cristiangarcia.hdtransmileniopro.objects.ExpandableHeightGridView;
import co.cristiangarcia.hdtransmileniopro.util.ConstantsApp;
import co.cristiangarcia.hdtransmileniopro.util.GridHorarioAdapter;
import co.cristiangarcia.hdtransmileniopro.util.InformationBusView;
import co.cristiangarcia.hdtransmileniopro.util.InformationView;
import co.cristiangarcia.hdtransmileniopro.util.IntegerFloat;
import co.cristiangarcia.hdtransmileniopro.util.RecomendacionView;
import co.cristiangarcia.hdtransmileniopro.util.RecorridoBus;
import co.cristiangarcia.hdtransmileniopro.util.RecorridoRuta;
import co.cristiangarcia.hdtransmileniopro.util.StringValue;
import co.cristiangarcia.hdtransmileniopro.util.UtilsXa;

public class ResultadoRutaActivity extends AppCompatActivity {

    public Class<?> _busClass;
    public Class<?> _busStopsClass;
    public Class<?> _mapClass;
    private int appID;
    protected Boolean bCaminarEnDestino = Boolean.valueOf(false);
    protected Boolean bCaminarEnOrigen = Boolean.valueOf(false);
    protected Boolean bDomingoFestivo = Boolean.valueOf(false);
    protected Boolean bJuevesViernes = Boolean.valueOf(false);
    protected Boolean bLunesMiercoles = Boolean.valueOf(false);
    protected Boolean bLunesViernes = Boolean.valueOf(false);
    protected Boolean bSabado = Boolean.valueOf(false);
    private boolean calculoTroncal = false;
    protected Cursor cursor;
    protected SQLiteDatabase db;
    private DatabaseHelperTransmiSitp dbHelper;
    protected TextView destinoText;
    protected Boolean esFestivo = Boolean.valueOf(false);
    protected TextView fechaText;
    protected Integer iEstacionDestino = Integer.valueOf(-1);
    protected Integer iEstacionDestinoOriginal = Integer.valueOf(-1);
    protected Integer iEstacionOrigen = Integer.valueOf(-1);
    protected Integer iEstacionOrigenOriginal = Integer.valueOf(-1);
    protected Integer iUltimaEstacion = Integer.valueOf(-1);
    private int imgCalendario;
    protected Location lDestination = null;
    protected Location lOrigin = null;
    protected LinearLayout layParadas1;
    protected LinearLayout layParadas2;
    protected String listDestination = XmlPullParser.NO_NAMESPACE;
    protected String listOrigin = XmlPullParser.NO_NAMESPACE;
    protected Time mainHour = null;
    protected Integer mainWeekDay;
    protected TextView origenText;
    private ProgressDialog pDialog;
    private int peatDistance;
    protected String sAgencyId = XmlPullParser.NO_NAMESPACE;
    protected String sBusNoStopsIds = XmlPullParser.NO_NAMESPACE;
    protected String[] sDias = new String[]{"Domingo (festivo)", "Lunes", "Martes", "Mi\u00e9rcoles", "Jueves", "Viernes", "S\u00e1bado"};
    protected String sFechaActual = XmlPullParser.NO_NAMESPACE;
    private String strPrimaryColor;
    private String strToShare = XmlPullParser.NO_NAMESPACE;
    private TextView txtNotFoundAdicionalServices;
    private TextView txtNotFoundServices;
    private TextView txtOthers;
    private TextView txtServiciosDirectos;
    protected UtilsXa ut;
    FragmentPagerAdapter adapterViewPager;
    android.support.v7.app.ActionBar actionBar;
    public class GetAllDataAsyncTask extends AsyncTask {
        private Time hora;
        private Integer iWeekDay;

        public GetAllDataAsyncTask(Time hora, Integer iWeekDay) {
            this.hora = hora;
            this.iWeekDay = iWeekDay;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ResultadoRutaActivity.this.pDialog = ProgressDialog.show(ResultadoRutaActivity.this, XmlPullParser.NO_NAMESPACE, "Buscando la mejor ruta.\nEspera un momento.", true);
        }

        protected Object doInBackground(Object[] params) {
             ResultadoRutaActivity.this.getAllData(this.hora, this.iWeekDay);
             return null;
        }

        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (!ResultadoRutaActivity.this.isFinishing() && ResultadoRutaActivity.this.pDialog.isShowing()) {
                ResultadoRutaActivity.this.pDialog.dismiss();
            }
        }
    }

    public class mBusOnClickListener implements OnClickListener {
        int busId;
        String busName;

        public mBusOnClickListener(int busId, String busName) {
            this.busId = busId;
            this.busName = busName;
        }

        public void onClick(View v) {
           /* Intent intent = new Intent(ResultadoRutaActivity.this, ResultadoRutaActivity.this._busStopsClass);
            intent.putExtra("BUS_ID", this.busId);
            intent.putExtra("BUS_NAME", this.busName);
            ResultadoRutaActivity.this.startActivity(intent);
            */
        }
    }

    public class mEstacionOnClickListener implements OnClickListener {
        Integer estacionId;

        public mEstacionOnClickListener(Integer estacionId) {
            this.estacionId = estacionId;
        }

        public void onClick(View v) {
           /* Intent intent = new Intent(ResultadoRutaActivity.this, ResultadoRutaActivity.this._busClass);
            intent.putExtra("ESTACION_ID", this.estacionId);
            ResultadoRutaActivity.this.startActivity(intent);
            */
        }
    }

    public class mPathClickListener implements OnClickListener {
        private ArrayList<Integer> _busIds;
        private ArrayList<Integer> _busStops;
        private Location _lDestination;
        private Location _lOrigin;

        public mPathClickListener(ArrayList<Integer> busIds, ArrayList<Integer> busStops) {
            this._busIds = busIds;
            this._busStops = busStops;
        }

        public void addLocationOrigin(Location _lOrigin) {
            this._lOrigin = _lOrigin;
        }

        public void addLocationDestination(Location _lDestination) {
            this._lDestination = _lDestination;
        }

        public void onClick(View v) {

            /*
            Intent intent = new Intent(ResultadoRutaActivity.this, ResultadoRutaActivity.this._mapClass);
            intent.putExtra("BUS_IDS", this._busIds);
            intent.putExtra("BUS_STOPS", this._busStops);
            if (ResultadoRutaActivity.this.bCaminarEnOrigen.booleanValue()) {
                intent.putExtra("LOCATION_ORIGEN", this._lOrigin);
            }
            if (ResultadoRutaActivity.this.bCaminarEnDestino.booleanValue()) {
                intent.putExtra("LOCATION_DESTINO", this._lDestination);
            }
            ResultadoRutaActivity.this.startActivity(intent);

            */
        }
    }

    public native int[] getPath(Network[] networkArr, int i, int i2, int i3);

    static {
        System.loadLibrary("movilixa");
    }

    public void setPeatDistance(int peatDistance) {
        this.peatDistance = peatDistance;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String appKey = "4c52d9b1663a5fe3a04ff1fd15561990dc7ccc1b0133abad";
        Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL);
        Appodeal.initialize(this, appKey, Appodeal.BANNER);

        this.ut = new UtilsXa(this);
        this.appID = 1;
             //this._mapClass = Class.forName(getPackageName() + ".Map");
           // this._busClass = Class.forName(getPackageName() + ".Station");
           // this._busStopsClass = Class.forName(getPackageName() + ".BusStops");

        this.imgCalendario = getResources().getIdentifier("zzz_calendar", "drawable", getPackageName());
        UtilsXa utilsXa = this.ut;
        this.strPrimaryColor = Integer.toHexString(UtilsXa.getPrimaryColor(this));
        this.strPrimaryColor = this.strPrimaryColor.substring(2);
        this.strPrimaryColor = "#" + this.strPrimaryColor;
        ((ImageView) findViewById(R.id.imgCalendar)).setImageResource(this.imgCalendario);
        this.mainHour = new Time();
        this.mainHour.setToNow();
        Time hora = new Time();
        hora.setToNow();
        this.mainWeekDay = Integer.valueOf(this.mainHour.weekDay);
        this.sFechaActual = this.mainHour.toString().substring(0, 8);
        this.dbHelper = new DatabaseHelperTransmiSitp(this, this.appID);

        this.iEstacionOrigen = Integer.valueOf(getIntent().getIntExtra("ESTACION_ORIGEN", -1));
        this.iEstacionDestino = Integer.valueOf(getIntent().getIntExtra("ESTACION_DESTINO", -1));

       // this.iEstacionOrigen = 3;
        //this.iEstacionDestino =  6;
        if (getIntent().getStringExtra("AGENCY") != null) {
            this.sAgencyId = getIntent().getStringExtra("AGENCY");
        } else if (this.appID == 1) {
            this.sAgencyId = "TRANSMILENIO";
        }
        this.dbHelper.openDataBase();
        this.iUltimaEstacion = Integer.valueOf(this.dbHelper.getStopsCount());
        this.dbHelper.close();
        if (this.iEstacionOrigen.intValue() == 0) {
            this.lOrigin = (Location) getIntent().getParcelableExtra("LOCATION_ORIGEN");
            this.bCaminarEnOrigen = Boolean.valueOf(true);
        }
        if (this.iEstacionDestino.intValue() == 0) {
            this.lDestination = (Location) getIntent().getParcelableExtra("LOCATION_DESTINO");
            this.bCaminarEnDestino = Boolean.valueOf(true);
            this.iEstacionDestino = Integer.valueOf(-1);
        }
        this.origenText = (TextView) findViewById(R.id.txtOdOri);
        this.destinoText = (TextView) findViewById(R.id.txtOdDesti);
       // this.fechaText = (TextView) findViewById(R.id.txtOdFec);
        this.txtNotFoundServices = (TextView) findViewById(R.id.txtNotFoundServices);
        this.txtNotFoundAdicionalServices = (TextView) findViewById(R.id.txtNotFoundAdicionalServices);
        this.txtServiciosDirectos = (TextView) findViewById(R.id.txtServiciosDirectos);
        this.txtOthers = (TextView) findViewById(R.id.txtOthers);
        this.layParadas1 = (LinearLayout) findViewById(R.id.layOdDinContent1);
        this.layParadas2 = (LinearLayout) findViewById(R.id.layOdDinContent2);
        this.dbHelper.openDataBase();
        this.esFestivo = Boolean.valueOf(this.dbHelper.isFestivo(this.sFechaActual));
        this.origenText.setText(this.dbHelper.getStationName(String.valueOf(this.iEstacionOrigen)));
        this.destinoText.setText(this.dbHelper.getStationName(String.valueOf(this.iEstacionDestino)));
        this.dbHelper.close();
        this.layParadas1.removeAllViews();
        this.layParadas2.removeAllViews();
        new GetAllDataAsyncTask(hora, Integer.valueOf(hora.weekDay)).execute(new Object[0]);




         actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);



        actionBar.setDisplayHomeAsUpEnabled(true);

        setDiaHora(this.mainHour, this.mainWeekDay, this.sDias[this.mainHour.weekDay], Boolean.valueOf(false));



        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            private Toast mToast;

            @Override
            public void onInterstitialLoaded(boolean isPrecache) {

            }

            @Override
            public void onInterstitialFailedToLoad() {
                finishApplication();
            }

            @Override
            public void onInterstitialShown() {
             }

            @Override
            public void onInterstitialClicked() {
             }

            @Override
            public void onInterstitialClosed() {
                finishApplication();
            }

            void showToast(final String text) {
                if (mToast == null) {
                    mToast = Toast.makeText(ResultadoRutaActivity.this, text, Toast.LENGTH_SHORT);
                }
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);


    }

    public void finishApplication(){
        ResultadoRutaActivity.this.finish();
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



    public void onDestroy() {
        if (!(this.cursor == null || this.cursor.isClosed())) {
            this.cursor.close();
        }
        if (this.db != null && this.db.isOpen()) {
            this.db.close();
        }
        if (this.pDialog != null && this.pDialog.isShowing()) {
            this.pDialog.dismiss();
        }
        super.onDestroy();
    }

    protected void setServiciosDirectosTitle() {
        if (this.iEstacionOrigen.intValue() == this.iEstacionDestino.intValue()) {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.txtServiciosDirectos.setText("El origen y el destino deben ser diferentes");
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.txtServiciosDirectos.setText("Servicios directos");
                }
            });
        }
    }

    protected String getBaseWhere(Time hora) {
       // String sBaseWhere = "XXX.pk_id IN (SELECT bh.fk_bus FROM bus_horario bh WHERE bh.fk_dia IN DDD AND (CASE WHEN time(desde) < time(hasta) THEN time(desde)<=time('19:22:57') AND time(hasta)>=time('19:22:57') ELSE time(desde)<=time('19:22:57') OR time(hasta)>=time('19:22:57') END) AND bh.fk_bus = XXX.pk_id)";
        String sBaseWhere = "XXX.pk_id IN (SELECT bh.fk_bus FROM bus_horario bh WHERE bh.fk_dia IN DDD AND (CASE WHEN time(desde) < time(hasta) THEN time(desde)<=time('" + hora.format("%H:%M:%S") + "') AND time(hasta)>=time('" + hora.format("%H:%M:%S") + "') ELSE time(desde)<=time('" + hora.format("%H:%M:%S") + "') OR time(hasta)>=time('" + hora.format("%H:%M:%S") + "') END) AND bh.fk_bus = XXX.pk_id)";
        if (this.bDomingoFestivo.booleanValue()) {
            sBaseWhere = sBaseWhere.replace("DDD", "(4)");
        } else if (this.bSabado.booleanValue()) {
            sBaseWhere = sBaseWhere.replace("DDD", "(2,3)");
        } else if (this.bLunesMiercoles.booleanValue()) {
            sBaseWhere = sBaseWhere.replace("DDD", "(1,2,5)");
        } else if (this.bJuevesViernes.booleanValue()) {
            sBaseWhere = sBaseWhere.replace("DDD", "(1,2,6)");
        } else if (this.bLunesViernes.booleanValue()) {
            sBaseWhere = sBaseWhere.replace("DDD", "(1,2)");
        }
        if (this.appID == 1 || this.sAgencyId.equals(XmlPullParser.NO_NAMESPACE)) {
            return sBaseWhere;
        }
        return sBaseWhere + " AND XXX.fk_agency = '" + this.sAgencyId + "'";
    }

    protected String getBaseWhereDijkstra(Time hora) {
        String sWhere = "bh.fk_dia IN DDD AND (CASE WHEN time(desde) < time(hasta) THEN time(desde)<=time('" + hora.format("%H:%M:%S") + "') AND time(hasta)>=time('" + hora.format("%H:%M:%S") + "') ELSE time(desde)<=time('" + hora.format("%H:%M:%S") + "') OR time(hasta)>=time('" + hora.format("%H:%M:%S") + "') END)";
        if (this.bDomingoFestivo.booleanValue()) {
            return sWhere.replace("DDD", "(4)");
        }
        if (this.bSabado.booleanValue()) {
            return sWhere.replace("DDD", "(2,3)");
        }
        if (this.bLunesMiercoles.booleanValue()) {
            return sWhere.replace("DDD", "(1,2,5)");
        }
        if (this.bJuevesViernes.booleanValue()) {
            return sWhere.replace("DDD", "(1,2,6)");
        }
        if (this.bLunesViernes.booleanValue()) {
            return sWhere.replace("DDD", "(1,2)");
        }
        return sWhere;
    }

    protected ImageView addHeaderRecorrido(RecorridoRuta rr, int i, boolean esDirecto) {
        ArrayList<Integer> busIds = new ArrayList();
        ArrayList<Integer> busStops = new ArrayList();
        for (int k = 0; k < rr.getListaBuses().size(); k++) {
            RecorridoBus recBus = (RecorridoBus) rr.getListaBuses().get(k);
            busIds.add(Integer.valueOf(recBus.getId()));
            busStops.add(Integer.valueOf(recBus.getEstacionOrigen()));
            if (rr.getListaBuses().size() == 1) {
                busStops.add(Integer.valueOf(recBus.getEstacionDestino()));
            } else if (k == rr.getListaBuses().size() - 1) {
                busStops.add(Integer.valueOf(recBus.getEstacionDestino()));
            }
        }
        mPathClickListener mPathLis = new mPathClickListener(busIds, busStops);
        if (this.bCaminarEnOrigen.booleanValue()) {
            mPathLis.addLocationOrigin(this.lOrigin);
        }
        if (this.bCaminarEnDestino.booleanValue()) {
            mPathLis.addLocationDestination(this.lDestination);
        }
        final RecomendacionView recView = rr.getHeaderRecomendacion(this, i);
//        recView.setOnClikImageMap(mPathLis);
        if (esDirecto) {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.layParadas1.addView(recView);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.layParadas2.addView(recView);
                }
            });
        }
        return recView.getImgShare();
    }

    protected String addLytOrigen(RecorridoBus rb, boolean esDirecto, int iTimeOrigin) {
        String result = XmlPullParser.NO_NAMESPACE;
        if (this.bCaminarEnOrigen.booleanValue() && (rb.getEstacionOrigen() != this.iEstacionOrigenOriginal.intValue() || this.iEstacionOrigenOriginal.intValue() == 0)) {
            RecorridoBus temp = new RecorridoBus(-1, XmlPullParser.NO_NAMESPACE, 0, rb.getEstacionOrigen(), 1, ConstantsApp.WALK_FACTOR);
            return ((result + addLineaEstacion(temp, esDirecto, 0, false)) + "\n") + addLineaPeaton(temp, esDirecto, false, iTimeOrigin);
        } else if (rb.getEstacionOrigen() == this.iEstacionOrigenOriginal.intValue()) {
            return result + addLineaEstacion(rb, esDirecto, rb.getEstacionOrigen(), false);
        } else {
            return result + addLineaEstacion(rb, esDirecto, this.iEstacionOrigen.intValue(), false);
        }
    }

    protected String addLytOrigenDestinoPeat(RecorridoBus rb, boolean esDirecto) {
        int estacionOrigen;
        int estacionOrigen2;
        String result = XmlPullParser.NO_NAMESPACE;
        String str = XmlPullParser.NO_NAMESPACE;
        if (rb.getEstacionOrigen() > 0) {
            estacionOrigen = rb.getEstacionOrigen();
        } else {
            estacionOrigen = 0;
        }
        RecorridoBus temp = new RecorridoBus(-1, str, estacionOrigen, rb.getEstacionDestino() > 0 ? rb.getEstacionDestino() : 0, 1, ConstantsApp.WALK_FACTOR);
        StringBuilder append = new StringBuilder().append(result);
        if (rb.getEstacionOrigen() > 0) {
            estacionOrigen2 = rb.getEstacionOrigen();
        } else {
            estacionOrigen2 = 0;
        }
        return (append.append(addLineaEstacion(temp, esDirecto, estacionOrigen2, false)).toString() + "\n") + addLineaPeatonInicioFin(temp, esDirecto);
    }

    private String addLineaEstacion(RecorridoBus rb, boolean esDirecto, int iStation, boolean bFinal) {
        return addLineaEstacion(rb, esDirecto, iStation, bFinal, false);
    }

    private String addLineaEstacion(RecorridoBus rb, boolean esDirecto, int iStation, boolean bFinal, boolean bCaminataFinal) {
        InformationView informationView;
        if (bFinal) {
            informationView = rb.getEstacionDestinoView(this, this.dbHelper, this.appID, iStation, bCaminataFinal);
        } else {
            informationView = rb.getEstacionOrigenView(this, this.dbHelper, this.appID, iStation, this.strPrimaryColor);
        }

        final InformationView finalInformationView = informationView;
        if (esDirecto) {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.layParadas1.addView(finalInformationView);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.layParadas2.addView(finalInformationView);
                }
            });
        }
        return informationView.getInfoText();
    }

    private String addLineaPeaton(RecorridoBus rb, boolean esDirecto, boolean bFinal, int iTime) {
        final InformationView layLinPeaton = rb.getLytLineaPeaton(this, this.dbHelper, this.appID, ConstantsApp.WALK_FACTOR, bFinal, iTime, this.strPrimaryColor);

        if (esDirecto) {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.layParadas1.addView(layLinPeaton);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.layParadas2.addView(layLinPeaton);
                }
            });
        }
        return layLinPeaton.getInfoText().toString();
    }

    private String addLineaPeatonInicioFin(RecorridoBus rb, boolean esDirecto) {
        final InformationView layLinPeaton = rb.getLytLineaPeatonInicioFin(this, this.appID);
        if (esDirecto) {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.layParadas1.addView(layLinPeaton);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.layParadas2.addView(layLinPeaton);
                }
            });
        }
        return layLinPeaton.getInfoText().toString();
    }

    private String addLineaBus(RecorridoBus rb, boolean esDirecto) {

        rb.applyColor(this, this.appID);
        rb.applyName(this, this.appID);
        int idBus = rb.getId();
        this.dbHelper.openDataBase();
        final BusView bv = this.dbHelper.getBus(String.valueOf(idBus), this.sFechaActual, this.esFestivo.booleanValue());
         String busInfo = this.dbHelper.getBusInfo(String.valueOf(idBus), String.valueOf(rb.getEstacionOrigen()), this.appID);
        this.dbHelper.close();


        final InformationBusView layLinBus = rb.getInfBusView(this, bv, busInfo, this.strPrimaryColor);
//        layLinBus.getImgInfo().setOnClickListener(new mBusOnClickListener(bv.getId(), bv.getName()));


        if (esDirecto) {
            runOnUiThread(new Runnable() {
                public void run() {
                    ExpandableHeightGridView hGrid = new ExpandableHeightGridView(ResultadoRutaActivity.this.getBaseContext());
                    hGrid.setNumColumns(2);
                    hGrid.setHorizontalSpacing(2);
                    hGrid.setVerticalSpacing(2);
                    hGrid.setExpanded(true);
                    LayoutParams p = new LayoutParams(-1, -2);
                    p.setMargins((int) ResultadoRutaActivity.this.getResources().getDimension(R.dimen.defaultPadding), 0, (int) ResultadoRutaActivity.this.getResources().getDimension(R.dimen.defaultPadding), 0);
                    hGrid.setLayoutParams(p);
                    hGrid.setAdapter(new GridHorarioAdapter(ResultadoRutaActivity.this.getBaseContext(), bv.getHorarios()));



                    ResultadoRutaActivity.this.layParadas1.addView(layLinBus);
                    ResultadoRutaActivity.this.layParadas1.addView(hGrid);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    ExpandableHeightGridView hGrid = new ExpandableHeightGridView(ResultadoRutaActivity.this.getBaseContext());
                    hGrid.setNumColumns(2);
                    hGrid.setHorizontalSpacing(2);
                    hGrid.setVerticalSpacing(2);
                    hGrid.setExpanded(true);
                    LayoutParams p = new LayoutParams(-1, -2);
                    p.setMargins((int) ResultadoRutaActivity.this.getResources().getDimension(R.dimen.defaultPadding), 0, (int) ResultadoRutaActivity.this.getResources().getDimension(R.dimen.defaultPadding), 0);
                    hGrid.setLayoutParams(p);
                    hGrid.setAdapter(new GridHorarioAdapter(ResultadoRutaActivity.this.getBaseContext(), bv.getHorarios()));
                    ResultadoRutaActivity.this.layParadas2.addView(layLinBus);
                    ResultadoRutaActivity.this.layParadas2.addView(hGrid);
                }
            });
        }

        return busInfo;
    }

    protected String addLytIntercambio(RecorridoBus rb, boolean esDirecto, boolean bFinal) {

        if (rb.getId() > 0) {
            return addLineaBus(rb, esDirecto);
        }
        return addLineaPeaton(rb, esDirecto, bFinal, 0);
     }

    protected String addLytDestinoMIO(RecorridoBus rb, boolean esDirecto, boolean caminataFinal) {
        if (!caminataFinal) {
            return addLineaEstacion(rb, esDirecto, this.iEstacionDestino.intValue(), true);
        }
        return addLineaEstacion(rb, esDirecto, rb.getEstacionDestino(), true, true);
    }

    protected String addLytCambioTransbordo(int iEstacionIntermedia, int iBusIntercambio, boolean esDirecto) {
        final InformationView informationView = new InformationView(getApplicationContext());
        this.dbHelper.openDataBase();
        informationView.setInfoText(Html.fromHtml(this.dbHelper.getMsgEstacionBus("Hasta <b><font color=\"" + this.strPrimaryColor + "\">XXX</font></b>YYY Despu\u00e9s", iEstacionIntermedia, iBusIntercambio, false, false)));
       this.dbHelper.close();
        informationView.setImgInfo(R.drawable.zzz_swap_vertical);
         if (esDirecto) {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.layParadas1.addView(informationView);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    ResultadoRutaActivity.this.layParadas2.addView(informationView);
                }
            });
        }
        return informationView.getInfoText().toString();
    }

    protected String obtainQueryDirectoMio(String sWhereSinTransbordo) {
        return "SELECT q2._id, q2.nDistancia, q2.nParadas, b.name, b.color, b.fk_agency FROM (SELECT _id, sum(distance) as nDistancia, count(fk_bus) as nParadas FROM (SELECT b1.pk_id as _id, be1.rowid as rowInicial, beTemp1.rowid as rowFinal FROM bus b1 INNER JOIN bus_estacion beTemp1 ON b1.pk_id = beTemp1.fk_bus INNER JOIN bus_estacion be1 ON b1.pk_id = be1.fk_bus WHERE be1.show != 2 AND beTemp1.show != 2 AND beTemp1.rowid > be1.rowid AND beTemp1.fk_estacion = ? AND be1.fk_estacion = ?) q1 INNER JOIN bus_estacion be ON q1._id = be.fk_bus WHERE be.show = 1 AND be.rowid > q1.rowInicial AND be.rowid <= q1.rowFinal GROUP BY _id) q2 INNER JOIN bus b ON q2._id = b.pk_id " + sWhereSinTransbordo + " ORDER BY nDistancia";
    }

    protected String obtainQueryTransbordoMio(Integer iEstOrigen, Integer iEstDestino, String sWhere1, String sWhere2) {
        return "SELECT q1.pk_id as _id, q1.name1, q1.nDistancia1, q1.nParadas1, q1.fk_agency1, q1.fk_estacion, q2.pk_id as BusFinal, q2.name2, q2.fk_agency2, q2.nDistancia2, q2.nParadas2, (q1.nDistancia1 + q2.nDistancia2) as tDistancia, (q1.nParadas1 + q2.nParadas2) as tParadas FROM (SELECT qInt1.pk_id as pk_id, qInt1.name as name1, qInt1.fk_agency as fk_agency1, qInt1.fk_estacion as fk_estacion, sum(beInt1.distance) as nDistancia1, count(beInt1.fk_estacion) as nParadas1 FROM (SELECT b1.pk_id as pk_id, b1.name, b1.fk_agency, be1.fk_estacion as fk_estacion, beTemp1.rowid as rowInicial, be1.rowid as rowIntermedia1 FROM bus b1 INNER JOIN bus_estacion beTemp1 ON b1.pk_id = beTemp1.fk_bus INNER JOIN bus_estacion be1 ON b1.pk_id = be1.fk_bus WHERE be1.show = 1 AND beTemp1.show != 2 AND beTemp1.rowid < be1.rowid AND beTemp1.fk_estacion = " + iEstOrigen + " AND be1.fk_estacion != " + iEstDestino + ") qInt1 " + "INNER JOIN bus_estacion beInt1 ON qInt1.pk_id = beInt1.fk_bus " + "WHERE beInt1.show = 1 AND beInt1.rowid > qInt1.rowInicial AND beInt1.rowid <= qInt1.rowIntermedia1" + sWhere1 + "GROUP BY qInt1.pk_id, qInt1.fk_estacion" + ")" + " q1 " + "INNER JOIN " + "(SELECT qInt2.pk_id as pk_id, qInt2.name as name2, qInt2.fk_agency as fk_agency2, qInt2.fk_estacion as fk_estacion, sum(beInt2.distance) as nDistancia2, count(beInt2.fk_estacion) as nParadas2 " + "FROM (SELECT b2.pk_id as pk_id, b2.name, b2.fk_agency, be2.fk_estacion as fk_estacion, be2.rowid as rowIntermedia2, beTemp2.rowid as rowFinal FROM bus b2 " + "INNER JOIN bus_estacion beTemp2 ON b2.pk_id = beTemp2.fk_bus INNER JOIN bus_estacion be2 ON b2.pk_id = be2.fk_bus " + "WHERE be2.show != 2 AND beTemp2.show != 2 AND beTemp2.rowid > be2.rowid AND beTemp2.fk_estacion = " + iEstDestino + " AND be2.fk_estacion!= " + iEstOrigen + ") qInt2 " + "INNER JOIN bus_estacion beInt2 ON qInt2.pk_id = beInt2.fk_bus " + "WHERE beInt2.show = 1 AND beInt2.rowid > qInt2.rowIntermedia2 AND beInt2.rowid <= qInt2.rowFinal " + sWhere2 + "GROUP BY qInt2.pk_id, qInt2.fk_estacion " + ") q2 " + "ON q1.fk_estacion = q2.fk_estacion WHERE q1.pk_id != q2.pk_id " + "ORDER BY tDistancia";
    }

    protected void getAllData(Time hora, Integer iWeekDay) {
        setServiciosDirectosTitle();
        if (this.iEstacionOrigen.intValue() != this.iEstacionDestino.intValue()) {
            this.dbHelper.openDataBase();
            this.calculoTroncal = this.dbHelper.getSonTroncales(this.iEstacionOrigen.intValue(), this.iEstacionDestino.intValue());

            this.dbHelper.close();
            this.iEstacionOrigenOriginal = this.iEstacionOrigen;
            this.iEstacionDestinoOriginal = this.iEstacionDestino;
            if (!this.calculoTroncal) {
                if (this.lOrigin == null) {
                    try {
                        this.lOrigin = this.dbHelper.getLocationStation(this.iEstacionOrigen.intValue());
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                if (this.lDestination == null) {
                    try {
                        this.lDestination = this.dbHelper.getLocationStation(this.iEstacionDestino.intValue());
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                this.iEstacionOrigen = Integer.valueOf(0);
                this.iEstacionDestino = Integer.valueOf(-1);
                this.bCaminarEnOrigen = Boolean.valueOf(true);
                this.bCaminarEnDestino = Boolean.valueOf(true);
            }
            String sWhere = getBaseWhereDijkstra(hora);
            if (this.appID == 1) {
                sWhere = this.sAgencyId.equals("SITP") ? sWhere + " AND b.pk_id >= 1000 " : sWhere + " AND b.pk_id < 1000 ";
            } else if (this.appID == 3) {
                if (this.sAgencyId.compareTo("Todos") != 0) {
                    sWhere = sWhere + " AND b.fk_agency = '" + this.sAgencyId + "'";
                }
            } else if (!this.sAgencyId.equals(XmlPullParser.NO_NAMESPACE)) {
                sWhere = sWhere + " AND b.fk_agency = '" + this.sAgencyId + "'";
            }
            if (!this.calculoTroncal) {
                String stopTypesIds = XmlPullParser.NO_NAMESPACE;
                if (this.appID == 1) {
                    if (this.sAgencyId.equals("SITP")) {
                        stopTypesIds = "2,3";
                    } else {
                        stopTypesIds = "1,3";
                    }
                } else if (this.appID == 4 || this.appID == 2) {
                    stopTypesIds = AppEventsConstants.EVENT_PARAM_VALUE_YES;
                }
                this.listOrigin = this.dbHelper.getListStringNearStation(this.lOrigin, (float) this.peatDistance, stopTypesIds);
                this.listDestination = this.dbHelper.getListStringNearStation(this.lDestination, (float) this.peatDistance, stopTypesIds);
                this.sBusNoStopsIds = this.dbHelper.getBusesWithOriginAndDestinationInMio(this.listOrigin, this.listDestination, sWhere);
            }
            ArrayList<RecorridoRuta> recRuta = obtainRecorridoDijkstra(sWhere);
            boolean addToRecDirectos = false;
            if (recRuta.size() > 0) {
                if (((RecorridoRuta) recRuta.get(0)).getListaBuses().size() == 1 && ((RecorridoBus) ((RecorridoRuta) recRuta.get(0)).getListaBuses().get(0)).getId() == -1) {
                    addToRecDirectos = true;
                } else if (this.iEstacionOrigen.equals(Integer.valueOf(0)) || this.iEstacionDestino.equals(Integer.valueOf(-1))) {
                    if (this.iEstacionOrigen.equals(Integer.valueOf(0))) {
                        ((RecorridoRuta) recRuta.get(0)).setCaminarEnOrigen(true);
                        if (this.lOrigin != null) {
                            try {
                                ((RecorridoRuta) recRuta.get(0)).setTiempoAdicionalOrigen((((double) this.dbHelper.getDistanceToStation(this.lOrigin, ((RecorridoRuta) recRuta.get(0)).getFirstStation())) * ConstantsApp.WALK_FACTOR) / 10.0d);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                        ((RecorridoRuta) recRuta.get(0)).deleteFirstBus();
                    }
                    if (this.iEstacionDestino.equals(Integer.valueOf(-1))) {
                        ((RecorridoRuta) recRuta.get(0)).setCaminarEnDestino(true);
                        if (this.lDestination != null) {
                            try {
                                ((RecorridoRuta) recRuta.get(0)).setTiempoAdicionalDestino((((double) this.dbHelper.getDistanceToStation(this.lDestination, ((RecorridoRuta) recRuta.get(0)).getLastStation())) * ConstantsApp.WALK_FACTOR) / 10.0d);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                        ((RecorridoRuta) recRuta.get(0)).deleteLastBus();
                    }
                }
            }


           if (this.iEstacionOrigen.intValue() != this.iEstacionDestino.intValue()) {

                Iterator it;
                RecorridoRuta rr;
                int j = 1;
                String sBaseWhere = getBaseWhere(hora);
                ArrayList<RecorridoRuta> recDirectosBUS = null;
                if (this.appID == 1) {
                    if (this.sAgencyId.equals("SITP")) {
                        sBaseWhere = sBaseWhere + " AND XXX.pk_id >= 1000 ";
                    } else {
                        sBaseWhere = sBaseWhere + " AND XXX.pk_id < 1000 ";
                    }
                }


                if (!this.sBusNoStopsIds.equals(XmlPullParser.NO_NAMESPACE)) {
                    recDirectosBUS = obtainRecorridoBusListDIRECTO();
                }
                String sWhere1 = " AND " + sBaseWhere.replace("XXX", "qInt1");
                String sWhere2 = " AND " + sBaseWhere.replace("XXX", "qInt2");
                ArrayList<RecorridoRuta> recDirectos = obtainRecorridoBusList(" WHERE " + sBaseWhere.replace("XXX", "b"));



               if (recDirectos.size() > 0) {
                    it = recDirectos.iterator();
                    while (it.hasNext()) {
                        rr = (RecorridoRuta) it.next();
                        verifyRecorridoBus((RecorridoBus) rr.getListaBuses().get(0), rr, 0);
                    }
                }

                if (recDirectosBUS != null) {
                    ArrayList<RecorridoRuta> recDirectosNews = new ArrayList();
                    it = recDirectos.iterator();
                    while (it.hasNext()) {
                        RecorridoRuta rdORI = (RecorridoRuta) it.next();
                        boolean bFound = false;
                        Iterator it2 = recDirectosBUS.iterator();
                        while (it2.hasNext()) {
                            if (((RecorridoRuta) it2.next()).getLastBus() == rdORI.getLastBus()) {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound) {
                            recDirectosNews.add(rdORI);
                        }
                    }
                    recDirectosBUS.addAll(recDirectosNews);
                    recDirectos = recDirectosBUS;
                }

                recDirectos = addTimeCaminarOrigenDestino(recDirectos);
                if (addToRecDirectos) {
                    recDirectos.addAll(recRuta);
                    recRuta.remove(0);
                }

                if (recDirectos.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ResultadoRutaActivity.this.txtNotFoundServices.setVisibility(0);
                            ResultadoRutaActivity.this.txtOthers.setVisibility(0);
                        }
                    });
                } else {
                    it = RecorridoRuta.sortByTime(this, this.dbHelper, this.appID, ConstantsApp.WALK_FACTOR, recDirectos).iterator();
                    while (it.hasNext()) {
                        rr = (RecorridoRuta) it.next();
                        if (j >= 15) {
                            break;
                        }


                         printOptionRoute(rr, j, true);
                        j++;
                    }
                }

                this.iEstacionOrigen = this.iEstacionOrigenOriginal;
                this.iEstacionDestino = this.iEstacionDestinoOriginal;
                if (!recRuta.isEmpty()) {
                    if (this.iEstacionOrigen.equals(Integer.valueOf(0))) {
                        this.iEstacionOrigen = Integer.valueOf(((RecorridoRuta) recRuta.get(0)).getFirstStation());
                    }
                    if (this.iEstacionDestino.equals(Integer.valueOf(-1)) && ((RecorridoRuta) recRuta.get(0)).getLastStation() != 0) {
                        this.iEstacionDestino = Integer.valueOf(((RecorridoRuta) recRuta.get(0)).getLastStation());
                    }
                }
                if (recRuta.size() == 1 && ((RecorridoRuta) recRuta.get(0)).isDirectOrOneChange() && (((RecorridoRuta) recRuta.get(0)).getListaBuses().size() < 2 || (((RecorridoRuta) recRuta.get(0)).getListaBuses().size() >= 2 && this.iEstacionOrigen.intValue() == ((RecorridoRuta) recRuta.get(0)).getFirstStationWithPeat() && this.iEstacionDestino.intValue() == ((RecorridoRuta) recRuta.get(0)).getLastStationWithPeat()))) {
                    recRuta.remove(0);
                }
                if (this.iEstacionOrigen.intValue() > 0 && this.iEstacionDestino.intValue() > 0) {
                    recRuta.addAll(obtainRecorridoTransbordoList(sWhere1, sWhere2));
                }
                recRuta = addTimeCaminarOrigenDestino(recRuta);
                j = 1;
                if (recRuta.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ResultadoRutaActivity.this.txtNotFoundAdicionalServices.setVisibility(0);
                        }
                    });
                    return;
                }
               this.dbHelper.openDataBase();
                it = RecorridoRuta.sortByTime(this, this.dbHelper, this.appID, ConstantsApp.WALK_FACTOR, recRuta).iterator();
               this.dbHelper.close();
                while (it.hasNext()) {
                    rr = (RecorridoRuta) it.next();
                    if (j < 15) {
                        printOptionRoute(rr, j, false);
                        j++;
                    } else {
                        return;
                    }
                }

            }
        }
    }

    public void printOptionRoute(RecorridoRuta rr, int pos, boolean isDirect) {

        final String strToShareFinal;
        String strToShare = XmlPullParser.NO_NAMESPACE;
        ImageView imgShare = addHeaderRecorrido(rr, pos, isDirect);
        if (rr.getListaBuses().size() == 1 && ((RecorridoBus) rr.getListaBuses().get(0)).getId() == -1) {
            strToShareFinal = strToShare + addLytOrigenDestinoPeat((RecorridoBus) rr.getListaBuses().get(0), isDirect);
            imgShare.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent sharingIntent = new Intent("android.intent.action.SEND");
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra("android.intent.extra.TEXT", strToShareFinal);
                    ResultadoRutaActivity.this.startActivity(Intent.createChooser(sharingIntent, "Compartir"));
                }
            });
            return;
        }

        strToShare = strToShare + addLytOrigen((RecorridoBus) rr.getListaBuses().get(0), isDirect, (int) Math.round(rr.getTiempoOrigen()));


        int k = 0;
        while (k < rr.getListaBuses().size()) {

            RecorridoBus recBus = (RecorridoBus) rr.getListaBuses().get(k);


            if (recBus.getId() != -2) {

                if (k == rr.getListaBuses().size() - 1 && recBus.getId() <= 0) {
                    recBus.setEstacionDestino(this.iEstacionDestino.intValue());
                }

                if (recBus.getEstacionDestino() != this.iEstacionDestino.intValue() && recBus.getId() <= 0) {

                    RecorridoBus recBusTemp = (RecorridoBus) rr.getListaBuses().get(k + 1);
                    while (recBusTemp.getId() <= 0) {

                        k++;

                        recBus.setDestinoTiempo(recBusTemp.getEstacionDestino(), recBus.getTiempo() + recBusTemp.getTiempo());


                        if (recBus.getEstacionDestino() == this.iEstacionDestino.intValue() || recBus.getId() > 0) {
                            break;
                        }
                          recBusTemp = (RecorridoBus) rr.getListaBuses().get(k + 1);

                    }

                }

               strToShare = (strToShare + "\n")
                       + addLytIntercambio(
                       recBus, isDirect, recBus.getEstacionDestino() == this.iEstacionDestino.intValue()
               );

            }

            if (recBus.getEstacionDestino() == ((RecorridoBus) rr.getListaBuses().get(rr.getListaBuses().size() - 1)).getEstacionOrigen() && ((RecorridoBus) rr.getListaBuses().get(rr.getListaBuses().size() - 1)).getId() < 0) {
                break;
            }
            if (recBus.getEstacionDestino() != this.iEstacionDestino.intValue() && recBus.getId() > 0 && rr.getListaBuses().size() != k + 1) {
                strToShare = (strToShare + "\n") + addLytCambioTransbordo(recBus.getEstacionDestino(), ((RecorridoBus) rr.getListaBuses().get(k + 1)).getId(), isDirect);
            } else if (recBus.getEstacionDestino() != this.iEstacionDestino.intValue() && recBus.getId() > 0 && rr.getListaBuses().size() == k + 1 && !rr.getCaminarEnDestino()) {
                rr.setCaminarEnDestino(true);
            }

            k++;
        }
        strToShareFinal = (strToShare + "\n") + addLytDestinoMIO((RecorridoBus) rr.getListaBuses().get(rr.getListaBuses().size() - 1), isDirect, rr.getCaminarEnDestino());


        imgShare.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent sharingIntent = new Intent("android.intent.action.SEND");
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra("android.intent.extra.TEXT", strToShareFinal);
                ResultadoRutaActivity.this.startActivity(Intent.createChooser(sharingIntent, "Compartir"));
            }
        });
    }

    public ArrayList<RecorridoRuta> obtainRecorridoDijkstra(String sWhere) {
        ArrayList<RecorridoRuta> recRuta = new ArrayList();
        int[] iPath = null;
        HashMap<String, Integer> hashReverseTotal = new HashMap();
        Network[] nnet = getNetwork(sWhere, (float) this.peatDistance, hashReverseTotal);
        if (nnet.length > 0) {
            try {
                iPath = this.iEstacionOrigen.intValue() == 0 ? getPath(nnet, hashReverseTotal.size() + 1, 0, hashReverseTotal.size() + 1) : getPath(nnet, hashReverseTotal.size(), ((Integer) hashReverseTotal.get(this.iEstacionOrigen + "_-1")).intValue(), ((Integer) hashReverseTotal.get(this.iEstacionDestino + "_-1")).intValue());
            } catch (Exception e) {
            }
        }
        ArrayList<String> lBusPath = new ArrayList();
        if (iPath != null && iPath.length > 1) {
            int minValue;
            int maxValue;
            RecorridoBus rb;
            ArrayList<RecorridoBus> listaBuses = new ArrayList();
            int previousBus = IabHelper.IABHELPER_ERROR_BASE;
            int firstStation = IabHelper.IABHELPER_ERROR_BASE;
            int lastStation = IabHelper.IABHELPER_ERROR_BASE;
            if (this.iEstacionOrigen.intValue() == 0) {
                minValue = 1;
                maxValue = iPath.length - 1;
            } else {
                minValue = 0;
                maxValue = iPath.length;
            }
            HashMap<Integer, String> hashTotal = new HashMap();
            for (Entry<String, Integer> e2 : hashReverseTotal.entrySet()) {
                hashTotal.put(e2.getValue(), e2.getKey());
            }
            for (int i = minValue; i < maxValue; i++) {
                String sBusStop = (String) hashTotal.get(Integer.valueOf(iPath[i]));
                lBusPath.add(sBusStop);
                int currentBus = Integer.parseInt(sBusStop.substring(sBusStop.indexOf("_") + 1));
                int currentStation = Integer.parseInt(sBusStop.substring(0, sBusStop.indexOf("_")));
                if (currentBus != previousBus) {
                    if (lastStation > 0 && firstStation != lastStation) {
                        rb = new RecorridoBus(previousBus, XmlPullParser.NO_NAMESPACE, firstStation, lastStation, 0, previousBus == -1 ? ConstantsApp.WALK_FACTOR : this.dbHelper.getFactor(previousBus, this.appID, ConstantsApp.BRT_FACTOR, ConstantsApp.BUS_FACTOR));
                        rb.calculateParadas(this.dbHelper, rb.getEstacionDestino(), rb.getEstacionOrigen(), rb.getId());
                        rb.calculateDistancia(this.dbHelper, rb.getEstacionDestino(), rb.getEstacionOrigen(), rb.getId());
                        listaBuses.add(rb);
                    }
                    firstStation = currentStation;
                    lastStation = currentStation;
                    previousBus = currentBus;
                } else if (i != maxValue - 1) {
                    lastStation = currentStation;
                } else if (currentStation > 0 && firstStation != currentStation) {
                    rb = new RecorridoBus(previousBus, XmlPullParser.NO_NAMESPACE, firstStation, currentStation, 0, previousBus == -1 ? ConstantsApp.WALK_FACTOR : this.dbHelper.getFactor(previousBus, this.appID, ConstantsApp.BRT_FACTOR, ConstantsApp.BUS_FACTOR));
                    rb.calculateParadas(this.dbHelper, rb.getEstacionDestino(), rb.getEstacionOrigen(), rb.getId());
                    rb.calculateDistancia(this.dbHelper, rb.getEstacionDestino(), rb.getEstacionOrigen(), rb.getId());
                    listaBuses.add(rb);
                }
            }
            if (listaBuses.size() == 0 && previousBus == -1) {
                rb = new RecorridoBus(previousBus, XmlPullParser.NO_NAMESPACE, this.iEstacionOrigenOriginal.intValue(), this.iEstacionDestinoOriginal.intValue(), 1, ConstantsApp.WALK_FACTOR);
                try {
                    rb.calculateDistanciaUbicacion(this.dbHelper, rb.getEstacionDestino(), rb.getEstacionOrigen(), this.lOrigin, this.lDestination);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                rb.setTiempo(ConstantsApp.WALK_FACTOR * ((double) rb.getDistancia()));
                listaBuses.add(rb);
                recRuta.add(new RecorridoRuta(listaBuses));
                ((RecorridoRuta) recRuta.get(0)).setTotalParadas();
                ((RecorridoRuta) recRuta.get(0)).setTotalDistancia();
            } else {
                recRuta.add(new RecorridoRuta(listaBuses));
            }
        }
        return recRuta;
    }



    private Network[] getNetwork(String sWhere, float fPeatDistance, HashMap<String, Integer> hashReverseTotal) {
        ArrayList<Network> vertices = new ArrayList();
        try {
            String stopTypeIds;
            Iterator it;
            IntegerFloat val;
            //this.dbHelper.agregarConexionesMio(this.appID, sWhere, vertices, getAssets(), this.calculoTroncal, hashReverseTotal);
            if (this.iEstacionOrigen.intValue() == 0 && ((this.appID == 1 && !this.calculoTroncal) || this.appID == 4 || (this.appID == 2 && !this.calculoTroncal))) {
                stopTypeIds = XmlPullParser.NO_NAMESPACE;
                if (this.appID != 1) {
                    stopTypeIds = AppEventsConstants.EVENT_PARAM_VALUE_YES;
                } else if (this.sAgencyId.equals("SITP")) {
                    stopTypeIds = "2,3";
                } else {
                    stopTypeIds = "1,3";
                }
                it = this.dbHelper.getListNearStation(this.lOrigin, fPeatDistance, stopTypeIds).iterator();
                while (it.hasNext()) {
                    val = (IntegerFloat) it.next();
                    Integer iOriginHash = (Integer) hashReverseTotal.get(val.name + "_-1");
                    if (iOriginHash != null) {
                        vertices.add(new Network(0, iOriginHash.intValue(), ConstantsApp.WALK_FACTOR * ((double) (val.value / 10.0f))));
                    }
                }
            }
            if (this.iEstacionDestino.intValue() == -1 && ((this.appID == 1 && !this.calculoTroncal) || this.appID == 4 || (this.appID == 2 && !this.calculoTroncal))) {
                stopTypeIds = XmlPullParser.NO_NAMESPACE;
                if (this.appID == 1) {
                    stopTypeIds = this.sAgencyId.equals("SITP") ? "2,3" : "1,3";
                } else if (this.appID == 4 || (this.appID == 2 && !this.calculoTroncal)) {
                    stopTypeIds = AppEventsConstants.EVENT_PARAM_VALUE_YES;
                }
                it = this.dbHelper.getListNearStation(this.lDestination, fPeatDistance, stopTypeIds).iterator();
                while (it.hasNext()) {
                    val = (IntegerFloat) it.next();
                    Integer iDestinationHash = (Integer) hashReverseTotal.get(val.name + "_-1");
                    if (iDestinationHash != null) {
                        vertices.add(new Network(iDestinationHash.intValue(), hashReverseTotal.size() + 1, ConstantsApp.WALK_FACTOR * ((double) (val.value / 10.0f))));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Network[]) vertices.toArray(new Network[0]);
    }

    public void cambiarHora(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Cambiar d\u00eda/hora");
        View DialogView = getLayoutInflater().inflate(R.layout.day_hour, null);
        alertDialogBuilder.setView(DialogView);
        final Spinner spi = (Spinner) DialogView.findViewById(R.id.spnDia);
        final TimePicker tPick = (TimePicker) DialogView.findViewById(R.id.dtHora);

        ArrayAdapter<StringValue> SpiAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new StringValue[]{new StringValue("Domingo (festivo)", Integer.valueOf(0)), new StringValue("Lunes", Integer.valueOf(1)), new StringValue("Martes", Integer.valueOf(2)), new StringValue("Mi\u00e9rcoles", Integer.valueOf(3)), new StringValue("Jueves", Integer.valueOf(4)), new StringValue("Viernes", Integer.valueOf(5)), new StringValue("S\u00e1bado", Integer.valueOf(6))});
        SpiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

         spi.setAdapter(SpiAdapter);
        this.mainHour.setToNow();
        this.mainWeekDay = Integer.valueOf(this.mainHour.weekDay);
        spi.setSelection(this.mainWeekDay.intValue());
        alertDialogBuilder.setMessage("Seleccione el dia y la hora").setCancelable(true).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int id) {
                ResultadoRutaActivity.this.layParadas1.removeAllViews();
                ResultadoRutaActivity.this.layParadas2.removeAllViews();
                ResultadoRutaActivity.this.txtNotFoundServices.setVisibility(8);
                ResultadoRutaActivity.this.txtNotFoundAdicionalServices.setVisibility(8);
                StringValue myItem = (StringValue) spi.getSelectedItem();
                ResultadoRutaActivity.this.mainHour.hour = tPick.getCurrentHour().intValue();
                ResultadoRutaActivity.this.mainHour.minute = tPick.getCurrentMinute().intValue();
                ResultadoRutaActivity.this.mainHour.second = 0;
                ResultadoRutaActivity.this.mainWeekDay = myItem.value;
                ResultadoRutaActivity.this.setDiaHora(ResultadoRutaActivity.this.mainHour, ResultadoRutaActivity.this.mainWeekDay, myItem.name, Boolean.valueOf(true));
                ResultadoRutaActivity.this.iEstacionOrigen = ResultadoRutaActivity.this.iEstacionOrigenOriginal;
                ResultadoRutaActivity.this.iEstacionDestino = ResultadoRutaActivity.this.iEstacionDestinoOriginal;
                new GetAllDataAsyncTask(ResultadoRutaActivity.this.mainHour, ResultadoRutaActivity.this.mainWeekDay).execute(new Object[0]);
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alertDialogBuilder.create().show();
    }

    public void setDiaHora(Time hora, Integer iWeekDay, String sDiaSemana, Boolean manual) {
        this.bDomingoFestivo = Boolean.valueOf(false);
        this.bSabado = Boolean.valueOf(false);
        this.bLunesViernes = Boolean.valueOf(false);
        this.bLunesMiercoles = Boolean.valueOf(false);
        this.bJuevesViernes = Boolean.valueOf(false);
        if (manual.booleanValue()) {
            this.esFestivo = Boolean.valueOf(false);
        }
        if (iWeekDay.intValue() == 0 || this.esFestivo.booleanValue()) {
            this.bDomingoFestivo = Boolean.valueOf(true);

                        actionBar.setTitle(sDiaSemana + " " + hora.format("%I:%M %p"));

         } else if (!this.esFestivo.booleanValue()) {
            if (iWeekDay.intValue() == 6) {
                this.bSabado = Boolean.valueOf(true);
            } else if (iWeekDay.intValue() == 1 || iWeekDay.intValue() == 2 || iWeekDay.intValue() == 3) {
                this.bLunesMiercoles = Boolean.valueOf(true);
            } else if (iWeekDay.intValue() == 4 || iWeekDay.intValue() == 5) {
                this.bJuevesViernes = Boolean.valueOf(true);
            } else {
                this.bLunesViernes = Boolean.valueOf(true);
            }
            actionBar.setTitle("Ruta para: "+sDiaSemana + " " + hora.format("%I:%M %p"));

        }
    }

    private ArrayList<RecorridoRuta> obtainRecorridoBusList(String sWhereSinTransbordo) {
        this.dbHelper.openDataBase();
        ArrayList<RecorridoRuta> getRecorridosBusList = this.dbHelper.getRecorridosBusList(obtainQueryDirectoMio(sWhereSinTransbordo), this.appID, this.iEstacionDestino.intValue(), this.iEstacionOrigen.intValue(), ConstantsApp.BRT_FACTOR, ConstantsApp.BUS_FACTOR);
       this.dbHelper.close();
        return getRecorridosBusList;
    }

    private ArrayList<RecorridoRuta> obtainRecorridoBusListDIRECTO() {
        ArrayList<RecorridoRuta> recDirectos = new ArrayList();
        String[] rutasDIRECTAS = this.sBusNoStopsIds.split(",");
        String sWhereSITP = XmlPullParser.NO_NAMESPACE;
        if (this.appID == 1) {
            if (this.sAgencyId.equals("SITP")) {
                sWhereSITP = "b.pk_id >= 1000 AND ";
            } else {
                sWhereSITP = "b.pk_id < 1000 AND ";
            }
        }
        float distance = 100000.0f;
        int iNearestStationOrigin = 0;
        int iPositionStationOrigin = 0;
        int iNearestStationDestination = 0;
        int iPositionStationDestination = 0;
        String sBusName = XmlPullParser.NO_NAMESPACE;
        String sBusColor = XmlPullParser.NO_NAMESPACE;
        int iBusId = 0;
        this.db = new DatabaseHelper(this, this.appID).getReadableDatabase();
        for (int i = 0; i < rutasDIRECTAS.length; i++) {
            float fLat;
            float fLon;
            float fDis;
            this.cursor = this.db.rawQuery("SELECT be.rowId as _id, b.pk_id as busId, e.pk_id as estacionId, e.latitud, e.longitud FROM bus b INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id WHERE " + sWhereSITP + "be.show = 1 AND e.pk_id IN (" + this.listOrigin + ") AND b.pk_id = " + rutasDIRECTAS[i], null);
            if (this.cursor.moveToFirst()) {
                do {
                    fLat = this.cursor.getFloat(this.cursor.getColumnIndex("latitud"));
                    fLon = this.cursor.getFloat(this.cursor.getColumnIndex("longitud"));
                    if (!(fLat == 0.0f || fLon == 0.0f)) {
                        Location bLocation = new Location("Point B");
                        bLocation.setLatitude((double) fLat);
                        bLocation.setLongitude((double) fLon);
                        fDis = this.lOrigin.distanceTo(bLocation);
                        if (fDis < distance) {
                            distance = fDis;
                            iNearestStationOrigin = this.cursor.getInt(this.cursor.getColumnIndex("estacionId"));
                            iPositionStationOrigin = this.cursor.getInt(this.cursor.getColumnIndex("_id"));
                        }
                    }
                } while (this.cursor.moveToNext());
            }
            this.cursor.close();
            distance = 100000.0f;
            this.cursor = this.db.rawQuery("SELECT be.rowId as _id, b.pk_id as busId, b.name as busName, b.color as busColor, e.pk_id as estacionId, e.longitud, e.latitud FROM bus b INNER JOIN bus_estacion be ON b.pk_id = be.fk_bus INNER JOIN estacion e ON be.fk_estacion = e.pk_id WHERE " + sWhereSITP + "be.show = 1 AND e.pk_id IN (" + this.listDestination + ") AND b.pk_id = " + rutasDIRECTAS[i], null);
            if (this.cursor.moveToFirst()) {
                do {
                    fLat = this.cursor.getFloat(this.cursor.getColumnIndex("latitud"));
                    fLon = this.cursor.getFloat(this.cursor.getColumnIndex("longitud"));
                    if (!(fLat == 0.0f || fLon == 0.0f)) {
                        Location bLocation = new Location("Point B");
                        bLocation.setLatitude((double) fLat);
                        bLocation.setLongitude((double) fLon);
                        fDis = this.lDestination.distanceTo(bLocation);
                        if (fDis < distance) {
                            distance = fDis;
                            iNearestStationDestination = this.cursor.getInt(this.cursor.getColumnIndex("estacionId"));
                            iPositionStationDestination = this.cursor.getInt(this.cursor.getColumnIndex("_id"));
                            iBusId = this.cursor.getInt(this.cursor.getColumnIndex("busId"));
                            sBusName = this.cursor.getString(this.cursor.getColumnIndex("busName"));
                            sBusColor = this.cursor.getString(this.cursor.getColumnIndex("busColor"));
                        }
                    }
                } while (this.cursor.moveToNext());
            }
            if (iPositionStationDestination > iPositionStationOrigin) {
                ArrayList<RecorridoBus> listaBuses = new ArrayList();
                RecorridoBus rb = new RecorridoBus(iBusId, sBusName, sBusColor, iNearestStationOrigin, iNearestStationDestination, 0, ConstantsApp.BUS_FACTOR);
                rb.calculateParadas(this.dbHelper, rb.getEstacionDestino(), rb.getEstacionOrigen(), rb.getId());
                rb.calculateDistancia(this.dbHelper, rb.getEstacionDestino(), rb.getEstacionOrigen(), rb.getId());
                listaBuses.add(rb);
                recDirectos.add(new RecorridoRuta(listaBuses));
            }
            this.cursor.close();
            distance = 100000.0f;
            iPositionStationDestination = 0;
            iPositionStationOrigin = 0;
        }
        this.db.close();
        return recDirectos;
    }

    private ArrayList<RecorridoRuta> addTimeCaminarOrigenDestino(ArrayList<RecorridoRuta> recDirectos) {
        if (this.bCaminarEnOrigen.booleanValue() || this.bCaminarEnDestino.booleanValue()) {
            Iterator it = recDirectos.iterator();
            while (it.hasNext()) {
                RecorridoRuta rr = (RecorridoRuta) it.next();
                if (this.bCaminarEnOrigen.booleanValue()) {
                    rr.setCaminarEnOrigen(true);
                    if (this.lOrigin != null) {
                        try {
                            rr.setTiempoAdicionalOrigen((((double) this.dbHelper.getDistanceToStation(this.lOrigin, rr.getFirstStation())) * ConstantsApp.WALK_FACTOR) / 10.0d);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
                if (this.bCaminarEnDestino.booleanValue()) {
                    rr.setCaminarEnDestino(true);
                    if (this.lDestination != null) {
                        try {
                            rr.setTiempoAdicionalDestino((((double) this.dbHelper.getDistanceToStation(this.lDestination, rr.getLastStation())) * ConstantsApp.WALK_FACTOR) / 10.0d);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
            }
        }
        return recDirectos;
    }

    private ArrayList<RecorridoRuta> obtainRecorridoTransbordoList(String sWhere1, String sWhere2) {
        ArrayList<RecorridoRuta> recRuta = new ArrayList();
        this.db = new DatabaseHelper(this, this.appID).getReadableDatabase();
        this.cursor = this.db.rawQuery(obtainQueryTransbordoMio(this.iEstacionOrigen, this.iEstacionDestino, sWhere1, sWhere2), null);
        int j = 1;
        if (this.cursor != null && this.cursor.moveToFirst()) {
            while (j < 30) {
                int k = 0;
                Iterator it = recRuta.iterator();
                while (it.hasNext()) {
                    RecorridoRuta temp = (RecorridoRuta) it.next();
                    if (((RecorridoBus) temp.getListaBuses().get(0)).getId() == this.cursor.getInt(this.cursor.getColumnIndex("_id")) && ((RecorridoBus) temp.getListaBuses().get(1)).getId() == this.cursor.getInt(this.cursor.getColumnIndex("BusFinal"))) {
                        k++;
                    }
                }
                if (k < 2) {
                    ArrayList<RecorridoBus> listaBuses = new ArrayList();
                    double dFactor1 = ConstantsApp.BUS_FACTOR;
                    double dFactor2 = ConstantsApp.BUS_FACTOR;
                    if (this.appID == 1) {
                        dFactor1 = this.cursor.getString(this.cursor.getColumnIndex("fk_agency1")).equals("TRANSMILENIO") ? ConstantsApp.BRT_FACTOR : ConstantsApp.BUS_FACTOR;
                        dFactor2 = this.cursor.getString(this.cursor.getColumnIndex("fk_agency2")).equals("TRANSMILENIO") ? ConstantsApp.BRT_FACTOR : ConstantsApp.BUS_FACTOR;
                    }
                    listaBuses.add(new RecorridoBus(this.cursor.getInt(this.cursor.getColumnIndex("_id")), this.cursor.getString(this.cursor.getColumnIndex("name1")), XmlPullParser.NO_NAMESPACE, this.iEstacionOrigen.intValue(), this.cursor.getInt(this.cursor.getColumnIndex("fk_estacion")), this.cursor.getInt(this.cursor.getColumnIndex("nParadas1")), this.cursor.getInt(this.cursor.getColumnIndex("nDistancia1")), dFactor1));
                    listaBuses.add(new RecorridoBus(this.cursor.getInt(this.cursor.getColumnIndex("BusFinal")), this.cursor.getString(this.cursor.getColumnIndex("name2")), XmlPullParser.NO_NAMESPACE, this.cursor.getInt(this.cursor.getColumnIndex("fk_estacion")), this.iEstacionDestino.intValue(), this.cursor.getInt(this.cursor.getColumnIndex("nParadas2")), this.cursor.getInt(this.cursor.getColumnIndex("nDistancia2")), dFactor2));
                    RecorridoRuta recorridoRuta = new RecorridoRuta(listaBuses);
                    verifyRecorridoBus((RecorridoBus) recorridoRuta.getListaBuses().get(0), recorridoRuta, 0);
                    verifyRecorridoBus((RecorridoBus) recorridoRuta.getListaBuses().get(recorridoRuta.getListaBuses().size() - 1), recorridoRuta, recorridoRuta.getListaBuses().size() - 1);
                    recRuta.add(recorridoRuta);
                    j++;
                }
                if (!this.cursor.moveToNext()) {
                    break;
                }
            }
        }
        this.cursor.close();
        this.db.close();
        return recRuta;
    }

    public void verifyRecorridoBus(RecorridoBus rb, RecorridoRuta rr, int pos) {
        if (rb.getId() > 0 && rb.getEstacionOrigen() > 0 && rb.getEstacionDestino() > 0) {
            int idStopConnection;
            this.dbHelper.openDataBase();
            if (!this.dbHelper.isStationInBusStops(rb.getEstacionOrigen(), rb.getId())) {
                idStopConnection = this.dbHelper.getBusStopConnection(rb.getId(), rb.getEstacionOrigen());
                if (idStopConnection != rb.getEstacionOrigen()) {
                    rr.getListaBuses().add(pos, new RecorridoBus(-2, XmlPullParser.NO_NAMESPACE, rb.getEstacionOrigen(), idStopConnection, 1, ConstantsApp.WALK_FACTOR));
                    rb.setEstacionOrigen(idStopConnection);
                    pos++;
                }
            }
            pos++;
            if (!this.dbHelper.isStationInBusStops(rb.getEstacionDestino(), rb.getId())) {
                idStopConnection = this.dbHelper.getBusStopConnection(rb.getId(), rb.getEstacionDestino());
                if (idStopConnection != rb.getEstacionDestino()) {
                    rr.getListaBuses().add(pos, new RecorridoBus(-2, XmlPullParser.NO_NAMESPACE, idStopConnection, rb.getEstacionDestino(), 1, ConstantsApp.WALK_FACTOR));
                    rb.setEstacionDestino(idStopConnection);
                }
            }
            this.dbHelper.close();
        }
    }

    @Override
    public void onBackPressed() {
        Appodeal.show(this, Appodeal.INTERSTITIAL);
    }


 }
