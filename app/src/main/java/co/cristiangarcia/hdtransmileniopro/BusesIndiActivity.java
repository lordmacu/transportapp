package co.cristiangarcia.hdtransmileniopro;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.List;

import co.cristiangarcia.hdtransmileniopro.adapters.BusAdapter;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelperTransmiSitp;
import co.cristiangarcia.hdtransmileniopro.objects.BusView;
import co.cristiangarcia.hdtransmileniopro.objects.StationView;

public class BusesIndiActivity extends AppCompatActivity {
    private DatabaseHelperTransmiSitp db;
    private boolean isFestivo;
    private boolean setFestivo;
    private String sFechaActual;
    private int idStation;
    private StationView sv;
    public BusAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses_indi);

        this.setFestivo = false;
        this.isFestivo = false;
         this.db = new DatabaseHelperTransmiSitp(getApplicationContext(), 1);

        final StationView estacion = (StationView) getIntent().getSerializableExtra("item");


        Time hora = new Time();
        hora.setToNow();
        this.sFechaActual = hora.toString().substring(0, 8);


        if (!this.setFestivo) {
            this.sv = estacion;
            this.db.openDataBase();
            this.isFestivo = this.db.isFestivo(this.sFechaActual);

            this.db.close();
            this.setFestivo = true;
        }

        ListView busListAdapter= (ListView) findViewById(R.id.busListAdapter);

        this.db.openDataBase();
        List<BusView> listBuses= this.db.getBusesFromStation(estacion.getId(),sFechaActual,isFestivo);
        mAdapter = new BusAdapter(this,listBuses);
        busListAdapter.setAdapter(mAdapter);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);


        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Buses de "+estacion.getName());

        if(estacion.getColor()!=null){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(estacion.getColor())));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(estacion.getColor()));
            }
        }

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
}
