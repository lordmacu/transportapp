package co.cristiangarcia.hdtransmileniopro.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StringValue {
    public String name;
    public Integer value;

    /* renamed from: util.StringValue.1 */
    static class C07181 implements Comparator<StringValue> {
        C07181() {
        }

        public int compare(StringValue lhs, StringValue rhs) {
            return lhs.value.compareTo(rhs.value);
        }
    }

    public StringValue(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public int getValue() {
        return this.value.intValue();
    }

    public String toString() {
        return this.name;
    }

    public static ArrayList<StringValue> sort(ArrayList<StringValue> asv) {
        Collections.sort(asv, new C07181());
        return asv;
    }
}
