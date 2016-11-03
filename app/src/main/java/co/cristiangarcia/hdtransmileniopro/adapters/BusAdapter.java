package co.cristiangarcia.hdtransmileniopro.adapters;

/**
 * Created by cristiangarcia on 30/10/16.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import co.cristiangarcia.hdtransmileniopro.R;
import co.cristiangarcia.hdtransmileniopro.helpers.DatabaseHelperTransmiSitp;
import co.cristiangarcia.hdtransmileniopro.objects.BusView;

public class BusAdapter extends ArrayAdapter<BusView> {
    private DatabaseHelperTransmiSitp db;

    /**
     * The part of the location string from the USGS service that we use to determine
     * whether or not there is a location offset present ("5km N of Cairo, Egypt").
     */
    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * Constructs a new {@link BusView}.
     *
     * @param context     of the app
     * @param stations is the list of earthquakes, which is the data source of the adapter
     */
    public BusAdapter(Context context, List<BusView> stations) {
        super(context, 0, stations);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.db = new DatabaseHelperTransmiSitp(getContext(), 1);

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.bus_list, parent, false);
        }

        BusView currentEstation = getItem(position);

        Log.v("bus i",currentEstation.toString());

        TextView bustexto = (TextView) listItemView.findViewById(R.id.bustexto);

        bustexto.setText(currentEstation.getName());

        TextView textDescBus= (TextView) listItemView.findViewById(R.id.textDescBus);
        textDescBus.setText(currentEstation.getDescripcion());
        RelativeLayout contenedorBusTexto= (RelativeLayout)  listItemView.findViewById(R.id.contenedorBusTexto);
        contenedorBusTexto.setBackgroundColor(Color.parseColor(currentEstation.getColor()));

        TextView estadoBus= (TextView) listItemView.findViewById(R.id.estadoBus);
        if(currentEstation.isEnOperacion()){
            estadoBus.setText(R.string.in_operation);
            estadoBus.setBackgroundResource(R.color.successColor);
        }else{
            estadoBus.setText(R.string.no_service);
        }




        return listItemView;
    }

}