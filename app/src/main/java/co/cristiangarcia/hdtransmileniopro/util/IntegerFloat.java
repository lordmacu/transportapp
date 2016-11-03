package co.cristiangarcia.hdtransmileniopro.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IntegerFloat {
    public int name;
    public float value;

    /* renamed from: util.IntegerFloat.1 */
    static class C07131 implements Comparator<IntegerFloat> {
        C07131() {
        }

        public int compare(IntegerFloat lhs, IntegerFloat rhs) {
            if (lhs.value > rhs.value) {
                return 1;
            }
            return -1;
        }
    }

    public IntegerFloat(int name, float value) {
        this.name = name;
        this.value = value;
    }

    public float getValue() {
        return this.value;
    }

    public int getName() {
        return this.name;
    }

    public static ArrayList<IntegerFloat> sort(ArrayList<IntegerFloat> aif) {
        Collections.sort(aif, new C07131());
        return aif;
    }
}
