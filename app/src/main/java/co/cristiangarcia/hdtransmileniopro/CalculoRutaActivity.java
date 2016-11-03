package co.cristiangarcia.hdtransmileniopro;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.appodeal.ads.Appodeal;

import co.cristiangarcia.hdtransmileniopro.objects.EstacionTemporal;

public class CalculoRutaActivity extends AppCompatActivity {
    private BroadcastReceiver mybroadcast;

     EstacionTemporal estacionUno;
    EstacionTemporal estacionDos;
    EditText toEditText;
    EditText fromEditText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_ruta);


        limpiarBusqueda();

        String appKey = "4c52d9b1663a5fe3a04ff1fd15561990dc7ccc1b0133abad";
        Appodeal.initialize(this, appKey, Appodeal.BANNER);

         fromEditText= (EditText) findViewById(R.id.fromEditText);
         toEditText= (EditText) findViewById(R.id.toEditText);

        ImageView swichTextField= (ImageView) findViewById(R.id.swichTextField);

        Button calculateRoute= (Button) findViewById(R.id.calculateRoute);

        calculateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent resultadoRuta= new Intent(CalculoRutaActivity.this, ResultadoRutaActivity.class);
                resultadoRuta.putExtra("ESTACION_ORIGEN",estacionUno.getId());
                resultadoRuta.putExtra("ESTACION_DESTINO",estacionDos.getId());
                startActivity(resultadoRuta);

            }
        });

        estacionUno= new EstacionTemporal();
        estacionDos= new EstacionTemporal();

        swichTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swichRutas();
            }
        });

        fromEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTroncal = new Intent(CalculoRutaActivity.this,TroncalesActivity.class);
                intentTroncal.putExtra("type",1);
                startActivity(intentTroncal);
            }
        });

        toEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTroncal = new Intent(CalculoRutaActivity.this,TroncalesActivity.class);
                intentTroncal.putExtra("type",2);
                startActivity(intentTroncal);
            }
        });






        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(R.string.route_calculation);
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


    public void swichRutas(){
        EstacionTemporal EstacionUnoTemporal=estacionDos;
        EstacionTemporal EstacionDosTemporal=estacionUno;

        estacionUno=EstacionUnoTemporal;
        estacionDos=EstacionDosTemporal;

        fromEditText.setText(estacionUno.getName());
        toEditText.setText(estacionDos.getName());


    }




    @Override
    public void onResume() {
        SharedPreferences extras = getSharedPreferences("LoginInfos", 0);

        if(extras.getInt("type",0)==1){
            fromEditText.setText(extras.getString("nameEstacion",""));
            estacionUno.setId(extras.getInt("idEstacion",0));
            estacionUno.setName(extras.getString("nameEstacion",""));
            estacionUno.setType(extras.getInt("type",0));
        }else{
            toEditText.setText(extras.getString("nameEstacion",""));
            estacionDos.setId(extras.getInt("idEstacion",0));
            estacionDos.setName(extras.getString("nameEstacion",""));
            estacionDos.setType(extras.getInt("type",0));
        }

        super.onResume();
        Appodeal.onResume(this, Appodeal.BANNER);

    }

    public void limpiarBusqueda(){
        SharedPreferences sp = getSharedPreferences("LoginInfos", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("idEstacion",0);
        editor.putString("nameEstacion","");
        editor.putInt("type",0);
        editor.commit();
    }

}
