package co.cristiangarcia.hdtransmileniopro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import co.cristiangarcia.hdtransmileniopro.helpers.AppEventsConstants;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CardView rutasView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CardView mapaEstacion= (CardView) findViewById(R.id.mapaEstacion);

        mapaEstacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent estationIntent = new Intent(MainActivity.this,MapaWebActivity.class);
                estationIntent.putExtra("ORIGEN_DESTINO", 3);
                estationIntent.putExtra("IMAGE_ID", AppEventsConstants.EVENT_PARAM_VALUE_NO);
                estationIntent.putExtra("mapIndi", 1);
                estationIntent.putExtra("AGENCY", "TRANSMILENIO");
                startActivity(estationIntent);
            }
        });


         rutasView= (CardView) findViewById(R.id.rutasView);
        CardView mapaView = (CardView) findViewById(R.id.mapaView);
        CardView estacionesView = (CardView) findViewById(R.id.estacionesView);

        estacionesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent estationIntent = new Intent(MainActivity.this,TroncalesActivity.class);
                estationIntent.putExtra("type", 3);

                startActivity(estationIntent);
            }
        });

        rutasView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent estationIntent = new Intent(MainActivity.this,CalculoRutaActivity.class);
                startActivity(estationIntent);
            }
        });
        mapaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent estationIntent = new Intent(MainActivity.this,MapaWebActivity.class);
                estationIntent.putExtra("ORIGEN_DESTINO", 1);
                estationIntent.putExtra("IMAGE_ID", AppEventsConstants.EVENT_PARAM_VALUE_NO);
                estationIntent.putExtra("AGENCY", "TRANSMILENIO");
                startActivity(estationIntent);
            }
        });


      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/



        android.support.v7.app.ActionBar actionBar = getSupportActionBar();



        actionBar.setTitle(R.string.transmilenio_plus);





    }



    @Override
    public void onBackPressed() {



        super.onBackPressed();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

     /*   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }




}
