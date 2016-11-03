package co.cristiangarcia.hdtransmileniopro.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import co.cristiangarcia.hdtransmileniopro.R;
import co.cristiangarcia.hdtransmileniopro.objects.HorarioItem;

public class GridHorarioAdapter extends BaseAdapter {
    private Context context;
    private List<HorarioItem> items;

    public GridHorarioAdapter(Context context, List<HorarioItem> items) {
        this.context = context;
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.horario_item, null);
        TextView txtDias = (TextView) v.findViewById(R.id.txtDias);
        txtDias.setText(((HorarioItem) this.items.get(position)).getDias());
         TextView txtHoras = (TextView) v.findViewById(R.id.txtHoras);
        txtHoras.setText(((HorarioItem) this.items.get(position)).getHora());
        txtHoras.setContentDescription(((HorarioItem) this.items.get(position)).getContentDesc());
        return v;
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}
