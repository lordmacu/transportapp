package co.cristiangarcia.hdtransmileniopro.helpers;

/**
 * Created by cristiangarcia on 26/10/16.
 */


public enum DfpAdsXa {
    STANDARD_BANNER_TS("/1885632/TS_320x50"),
    STANDARD_BANNER_RMIO("/1885632/RutasMio_320x50"),
    STANDARD_BANNER_RMETRO_DF("/1885632/RutasMetroDF_320x50"),
    SMALL_BANNER_TS("/1885632/TS_50x50"),
    SMALL_BANNER_RMIO("/1885632/RutasMio_50x50"),
    SMALL_BANNER_RMETRO_DF("/1885632/RutasMetroDF_50x50");

    public String adunitId;

    private DfpAdsXa(String adUnitId) {
        this.adunitId = adUnitId;
    }
}