package co.cristiangarcia.hdtransmileniopro.helpers;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by cristiangarcia on 24/10/16.
 */


public final class ConvertUtil {
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private ConvertUtil() {
    }

    public static byte[] toBytes(int a) {
        return new byte[]{(byte) ((a >>> 24) & 255), (byte) ((a >>> 16) & 255), (byte) ((a >>> 8) & 255), (byte) (a & 255)};
    }

    public static int toInt(byte[] b, int s, int n) {
        int ret = 0;
        for (int i = s; i < s + n; i++) {
            ret = (ret << 8) | (b[i] & 255);
        }
        return ret;
    }

    public static int toIntR(byte[] b, int s, int n) {
        int ret = 0;
        int i = s;
        while (i >= 0 && n > 0) {
            ret = (ret << 8) | (b[i] & 255);
            i--;
            n--;
        }
        return ret;
    }

    public static int toInt(byte... b) {
        int ret = 0;
        for (byte a : b) {
            ret = (ret << 8) | (a & 255);
        }
        return ret;
    }

    public static String toHexString(byte[] d, int s, int n) {
        char[] ret = new char[(n * 2)];
        int e = s + n;
        int x = 0;
        for (int i = s; i < e; i++) {
            byte v = d[i];
            int i2 = x + 1;
            ret[x] = HEX[(v >> 4) & 15];
            x = i2 + 1;
            ret[i2] = HEX[v & 15];
        }
        return new String(ret);
    }

    public static String toHexStringR(byte[] d, int s, int n) {
        char[] ret = new char[(n * 2)];
        int x = 0;
        for (int i = (s + n) - 1; i >= s; i--) {
            byte v = d[i];
            int i2 = x + 1;
            ret[x] = HEX[(v >> 4) & 15];
            x = i2 + 1;
            ret[i2] = HEX[v & 15];
        }
        return new String(ret);
    }

    public static int parseInt(String txt, int radix, int def) {
        try {
            return Integer.valueOf(txt, radix).intValue();
        } catch (Exception e) {
            return def;
        }
    }

    public static String toAmountString(float value) {
        return String.format("%.2f", new Object[]{Float.valueOf(value)});
    }

    public static String toStringHourGTFS(String hour) {
        if (hour.indexOf(":") == 1) {
            hour = "0" + hour;
        }
        if (hour.length() == 5) {
            hour = hour + ":00";
        }
        return hour.replace(":", XmlPullParser.NO_NAMESPACE);
    }
}
