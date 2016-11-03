package co.cristiangarcia.hdtransmileniopro;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;

import java.util.List;

import co.cristiangarcia.hdtransmileniopro.adapters.EstacionAdapter;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelperTransmiSitp;
import co.cristiangarcia.hdtransmileniopro.objects.StationView;
import co.cristiangarcia.hdtransmileniopro.objects.Troncal;


public class EstacionActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener, Button.OnClickListener {
    ListView estacionlist;
    private EstacionAdapter mAdapter;
    String zonaNombre;
    SearchView searchView;
    private DatabaseHelperTransmiSitp dbHelper;
    private int appID=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacion);

        final Troncal troncal = (Troncal) getIntent().getSerializableExtra("item");
        Bundle extras = getIntent().getExtras();
        final Integer type =extras.getInt("type");

        this.dbHelper = new DatabaseHelperTransmiSitp(this, this.appID);

        String appKey = "4c52d9b1663a5fe3a04ff1fd15561990dc7ccc1b0133abad";
        Appodeal.initialize(this, appKey, Appodeal.BANNER);

        ListView estacionlist = (ListView) findViewById(R.id.estacionlist);

        estacionlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                StationView station = (StationView) parent.getItemAtPosition(position);


                if(type==3){

                    Intent BusIntent= new Intent(EstacionActivity.this,BusesIndiActivity.class);
                    BusIntent.putExtra("item",station);
                        startActivity(BusIntent);
                }else{
                    SharedPreferences sp = getSharedPreferences("LoginInfos", 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("idEstacion",station.getId());
                    editor.putString("nameEstacion",station.getName());
                    editor.putInt("type",type);
                    editor.commit();
                    finish();
                    TroncalesActivity.actualActivity.finish();
                }

            }
        });



        TextView zonaTexto= (TextView) findViewById(R.id.zonaTexto);
        zonaNombre=troncal.getName();
        zonaTexto.setText(R.string.select_station);

        try {

            dbHelper.openDataBase();
           List<StationView> stations= dbHelper.getAllStations(1,zonaNombre,"");
            dbHelper.close();
              mAdapter = new EstacionAdapter(this, stations);
            estacionlist.setAdapter(mAdapter);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);



        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(troncal.getColor())));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(troncal.getColor()));
        }
        RelativeLayout estacionesView= (RelativeLayout) findViewById(R.id.estacionesViewRelative);

        if(type==1){
            actionBar.setTitle(R.string.origin_station);
            estacionesView.setVisibility(View.VISIBLE);

        }else if(type==2){

            actionBar.setTitle(R.string.destination_station);
            estacionesView.setVisibility(View.VISIBLE);

        }else{
            actionBar.setTitle(R.string.stations);
            estacionesView.setVisibility(View.GONE);
        }

        Appodeal.show(this, Appodeal.BANNER_BOTTOM);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.search_menu, menu);
        // Associate searchable configuration with the SearchView


        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        setupSearchView();

        return true;
    }

    private void setupSearchView() {

        searchView.setIconifiedByDefault(true);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            // Try to use the "applications" global search provider
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            searchView.setSearchableInfo(info);
        }

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
    }




    @Override
    public boolean onClose() {
        mAdapter.clear();
        List<StationView> stations= null;

        try {
            dbHelper.openDataBase();
            stations = dbHelper.getAllStations(1,zonaNombre,"");
            dbHelper.close();
            mAdapter.addAll(stations);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        List<StationView> stations= null;
        try {
            mAdapter.clear();
            dbHelper.openDataBase();
            stations = dbHelper.getAllStations(1,zonaNombre,query);
            dbHelper.close();
            mAdapter.addAll(stations);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<StationView> stations= null;
        try {
            mAdapter.clear();
            dbHelper.close();
            stations = dbHelper.getAllStations(1,zonaNombre,newText);
            dbHelper.close();
            mAdapter.addAll(stations);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return false;
    }


    public void onClick(View view) {

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
