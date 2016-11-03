package co.cristiangarcia.hdtransmileniopro;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appodeal.ads.Appodeal;

import java.util.List;

import co.cristiangarcia.hdtransmileniopro.adapters.TroncalAdapter;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelperTransmiSitp;
import co.cristiangarcia.hdtransmileniopro.objects.Troncal;

public class TroncalesActivity extends AppCompatActivity{
    private TroncalAdapter mAdapter;
    public static Activity actualActivity;
    protected SQLiteDatabase db;
    private DatabaseHelperTransmiSitp dbHelper;
    private static final int DATABASE_VERSION_XAPASTO = 1;
    private int appID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troncales);
        this.dbHelper = new DatabaseHelperTransmiSitp(this, this.appID);

        String appKey = "4c52d9b1663a5fe3a04ff1fd15561990dc7ccc1b0133abad";
        Appodeal.initialize(this, appKey, Appodeal.BANNER);
        actualActivity=this;
        ListView troncales= (ListView) findViewById(R.id.troncales);

        Bundle extras = getIntent().getExtras();
        final Integer type =extras.getInt("type");

        dbHelper.openDataBase();
        List<Troncal> troncalesList=  dbHelper.getTroncales();
        dbHelper.close();
        mAdapter = new TroncalAdapter(this, troncalesList);
        troncales.setAdapter(mAdapter);
        troncales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Troncal troncal = (Troncal) parent.getItemAtPosition(position);

                Intent intent = new Intent(TroncalesActivity.this, EstacionActivity.class);
                intent.putExtra("item", troncal);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(type==1){
            actionBar.setTitle(R.string.origin_troncal);
        }else if(type==2){
            actionBar.setTitle(R.string.destination_troncal);
        }else{
            actionBar.setTitle(R.string.troncales);

        }

        Appodeal.show(this, Appodeal.BANNER_BOTTOM);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
