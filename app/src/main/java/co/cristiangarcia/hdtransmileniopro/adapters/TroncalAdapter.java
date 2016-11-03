package co.cristiangarcia.hdtransmileniopro.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import co.cristiangarcia.hdtransmileniopro.R;
import co.cristiangarcia.hdtransmileniopro.objects.StationView;
import co.cristiangarcia.hdtransmileniopro.objects.Troncal;

/**
 * Created by cristiangarcia on 25/10/16.
 */

public class TroncalAdapter extends ArrayAdapter<Troncal> {

    /**
     * The part of the location string from the USGS service that we use to determine
     * whether or not there is a location offset present ("5km N of Cairo, Egypt").
     */
    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * Constructs a new {@link StationView}.
     *
     * @param context     of the app
     * @param troncales is the list of earthquakes, which is the data source of the adapter
     */
    public TroncalAdapter(Context context, List<Troncal> troncales) {
        super(context, 0, troncales);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.troncal_list, parent, false);
        }

        Troncal currentEstation = getItem(position);

        RelativeLayout contenedorTitulo= (RelativeLayout) listItemView.findViewById(R.id.contenedorTitulo);
        TextView tituloEstacion = (TextView) listItemView.findViewById(R.id.tituloEstacion);
        TextView textoDireccion= (TextView) listItemView.findViewById(R.id.textoDireccion);
        TextView textoLocation= (TextView) listItemView.findViewById(R.id.textoLocation);

        textoLocation.setText(currentEstation.getDescription());
        textoDireccion.setText(currentEstation.getSubDescription());
        tituloEstacion.setText(currentEstation.getName());
        contenedorTitulo.setBackgroundColor(Color.parseColor(currentEstation.getColor()));


        return listItemView;
    }

}