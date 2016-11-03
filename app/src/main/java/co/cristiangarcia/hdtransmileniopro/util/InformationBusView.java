package co.cristiangarcia.hdtransmileniopro.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.cristiangarcia.hdtransmileniopro.R;
import co.cristiangarcia.hdtransmileniopro.objects.ExpandableHeightGridView;



public class InformationBusView extends LinearLayout {
    private ExpandableHeightGridView hGrid;
    private ImageView imgInfo;
    private TextView txtBus;
    private TextView txtInfoBus;
    private TextView txtParadas;

    public InformationBusView(Context context) {
        super(context);
        initializeViews(context);
    }

    public InformationBusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.information_bus_layout, this);
        this.txtBus = (TextView) findViewById(R.id.txtBus);
        this.txtInfoBus = (TextView) findViewById(R.id.txtInfoBus);
        this.txtParadas = (TextView) findViewById(R.id.txtInfoParadas);
        this.imgInfo = (ImageView) findViewById(R.id.imgInfo);
    }

    public TextView getTxtBus() {
        return this.txtBus;
    }

    public void setTxtBus(TextView txtBus) {
        this.txtBus = txtBus;
    }

    public TextView getTxtInfoBus() {
        return this.txtInfoBus;
    }

    public void setTxtInfoBus(TextView txtInfoBus) {
        this.txtInfoBus = txtInfoBus;
    }

    public TextView getTxtParadas() {
        return this.txtParadas;
    }

    public void setTxtParadas(TextView txtParadas) {
        this.txtParadas = txtParadas;
    }

    public ImageView getImgInfo() {
        return this.imgInfo;
    }

    public void setImgInfo(ImageView imgInfo) {
        this.imgInfo = imgInfo;
    }

    public ExpandableHeightGridView gethGrid() {
        return this.hGrid;
    }

    public void sethGrid(ExpandableHeightGridView hGrid) {
        this.hGrid = hGrid;
    }
}
