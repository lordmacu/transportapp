package org.ksoap2.serialization;

public class SoapPrimitive extends AttributeContainer {
    public static final Object NullNilElement = new Object();
    public static final Object NullSkip = new Object();
    protected String name;
    protected String namespace;
    protected Object value;

    public SoapPrimitive(String namespace, String name, Object value) {
        this.namespace = namespace;
        this.name = name;
        this.value = value;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
        r6 = this;
        r2 = 1;
        r3 = 0;
        r4 = r7 instanceof org.ksoap2.serialization.SoapPrimitive;
        if (r4 != 0) goto L_0x0007;
    L_0x0006:
        return r3;
    L_0x0007:
        r0 = r7;
        r0 = (org.ksoap2.serialization.SoapPrimitive) r0;
        r4 = r6.name;
        r5 = r0.name;
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0039;
    L_0x0014:
        r4 = r6.namespace;
        if (r4 != 0) goto L_0x002f;
    L_0x0018:
        r4 = r0.namespace;
        if (r4 != 0) goto L_0x0039;
    L_0x001c:
        r4 = r6.value;
        if (r4 != 0) goto L_0x003b;
    L_0x0020:
        r4 = r0.value;
        if (r4 != 0) goto L_0x0039;
    L_0x0024:
        r1 = r2;
    L_0x0025:
        if (r1 == 0) goto L_0x0046;
    L_0x0027:
        r4 = r6.attributesAreEqual(r0);
        if (r4 == 0) goto L_0x0046;
    L_0x002d:
        r3 = r2;
        goto L_0x0006;
    L_0x002f:
        r4 = r6.namespace;
        r5 = r0.namespace;
        r4 = r4.equals(r5);
        if (r4 != 0) goto L_0x001c;
    L_0x0039:
        r1 = r3;
        goto L_0x0025;
    L_0x003b:
        r4 = r6.value;
        r5 = r0.value;
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0039;
    L_0x0045:
        goto L_0x0024;
    L_0x0046:
        r2 = r3;
        goto L_0x002d;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ksoap2.serialization.SoapPrimitive.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return (this.namespace == null ? 0 : this.namespace.hashCode()) ^ this.name.hashCode();
    }

    public String toString() {
        return this.value != null ? this.value.toString() : null;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }
}
