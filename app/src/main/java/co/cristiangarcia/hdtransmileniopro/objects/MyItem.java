package co.cristiangarcia.hdtransmileniopro.objects;

/**
 * Created by cristiangarcia on 24/10/16.
 */
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyItem {
    private String _address;
    private BitmapDescriptor _icon;
    private int _id;
    private String _idMarker;
    private String _name;
    private LatLng mPosition = null;

    public MyItem(LatLng geoPoint, String name, String address, BitmapDescriptor icon, int idStation) {
        this.mPosition = geoPoint;
        this._name = name;
        this._address = address;
        this._icon = icon;
        this._id = idStation;
        this._idMarker = "no";
    }

    public MyItem(int _id) {
        this._id = _id;
        this._idMarker = "no";
    }

    public LatLng getPosition() {
        return this.mPosition;
    }

    public MarkerOptions getMarkerOptions() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(getPosition());
        markerOptions.title(this._name);
        markerOptions.snippet(this._address);
        markerOptions.icon(this._icon);
        return markerOptions;
    }

    public String getIdMarker() {
        return this._idMarker;
    }

    public void setIdMarker(String idMarker) {
        this._idMarker = idMarker;
    }

    public int getId() {
        return this._id;
    }

    public LatLng getmPosition() {
        return this.mPosition;
    }

    public String get_name() {
        return this._name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_address() {
        return this._address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public BitmapDescriptor get_icon() {
        return this._icon;
    }

    public void set_icon(BitmapDescriptor _icon) {
        this._icon = _icon;
    }

    public int get_id() {
        return this._id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_idMarker() {
        return this._idMarker;
    }

    public void set_idMarker(String _idMarker) {
        this._idMarker = _idMarker;
    }

    public void setmPosition(LatLng mPosition) {
        this.mPosition = mPosition;
    }
}
