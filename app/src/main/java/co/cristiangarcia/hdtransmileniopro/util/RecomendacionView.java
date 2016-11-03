package co.cristiangarcia.hdtransmileniopro.util;

import android.content.Context;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.cristiangarcia.hdtransmileniopro.R;

public class RecomendacionView extends LinearLayout {
    private int color;
    private ImageView imgMap;
    private ImageView imgShare;
    private TextView txtRecomendacion;

    public RecomendacionView(Context context) {
        super(context);
        initializeViews(context);
    }

    public RecomendacionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.recomendacion_layout, this);
        this.txtRecomendacion = (TextView) findViewById(R.id.txtRecomendacion);
//        this.imgMap = (ImageView) findViewById(R.id.imgMap);
        this.imgShare = (ImageView) findViewById(R.id.imgShare);
    }

    public void setRecomendacionText(Spanned text) {
        this.txtRecomendacion.setText(text);
    }

    public void setOnClikImageMap(OnClickListener onClick) {
        //this.imgMap.setOnClickListener(onClick);
    }

    public void setOnClikImageShare(OnClickListener onClick) {
        this.imgShare.setOnClickListener(onClick);
    }

    public ImageView getImgShare() {
        return this.imgShare;
    }
}
