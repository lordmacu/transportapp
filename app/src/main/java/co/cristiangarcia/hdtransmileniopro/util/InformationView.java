package co.cristiangarcia.hdtransmileniopro.util;

import android.content.Context;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.cristiangarcia.hdtransmileniopro.R;

public class InformationView extends LinearLayout {
    private ImageView imgInfo;
    private ImageView imgInfo2;
    private TextView txtInfo;

    public InformationView(Context context) {
        super(context);
        initializeViews(context);
    }

    public InformationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.information_layout, this);
        this.txtInfo = (TextView) findViewById(R.id.txtInfo);
        this.imgInfo = (ImageView) findViewById(R.id.imgInfo);
     }

    public void setInfoText(Spanned text) {
        this.txtInfo.setText(text);
    }

    public String getInfoText() {
        return this.txtInfo.getText().toString();
    }

    public void setOnClikImageInfo(OnClickListener onClick) {
        this.imgInfo.setOnClickListener(onClick);
    }



    public void setImgInfo(int draw) {
        this.imgInfo.setImageResource(draw);
    }

    public void setImgInfo2(int draw) {
        this.imgInfo2.setImageResource(draw);
    }

    public void setContentImgInfo(String text) {
        this.imgInfo.setContentDescription(text);
    }

    public void setContentImgInfo2(String text) {
        this.imgInfo2.setContentDescription(text);
    }

    public ImageView getImgInfo2() {
        return this.imgInfo2;
    }
}
