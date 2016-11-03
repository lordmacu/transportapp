package org.ksoap2.serialization;

import java.util.Hashtable;
import java.util.Vector;

public class SoapObject extends AttributeContainer implements HasInnerText, KvmSerializable {
    private static final String EMPTY_STRING = "";
    protected Object innerText;
    protected String name;
    protected String namespace;
    protected Vector properties;

    public SoapObject() {
        this(EMPTY_STRING, EMPTY_STRING);
    }

    public SoapObject(String namespace, String name) {
        this.properties = new Vector();
        this.namespace = namespace;
        this.name = name;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SoapObject)) {
            return false;
        }
        SoapObject otherSoapObject = (SoapObject) obj;
        if (!this.name.equals(otherSoapObject.name) || !this.namespace.equals(otherSoapObject.namespace)) {
            return false;
        }
        int numProperties = this.properties.size();
        if (numProperties != otherSoapObject.properties.size()) {
            return false;
        }
        for (int propIndex = 0; propIndex < numProperties; propIndex++) {
            if (!otherSoapObject.isPropertyEqual(this.properties.elementAt(propIndex), propIndex)) {
                return false;
            }
        }
        return attributesAreEqual(otherSoapObject);
    }

    public boolean isPropertyEqual(Object otherProp, int index) {
        if (index >= getPropertyCount()) {
            return false;
        }
        PropertyInfo thisProp = this.properties.elementAt(index);
        if ((otherProp instanceof PropertyInfo) && (thisProp instanceof PropertyInfo)) {
            PropertyInfo otherPropInfo = (PropertyInfo) otherProp;
            PropertyInfo thisPropInfo = thisProp;
            if (otherPropInfo.getName().equals(thisPropInfo.getName()) && otherPropInfo.getValue().equals(thisPropInfo.getValue())) {
                return true;
            }
            return false;
        } else if ((otherProp instanceof SoapObject) && (thisProp instanceof SoapObject)) {
            return ((SoapObject) otherProp).equals((SoapObject) thisProp);
        } else {
            return false;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public Object getProperty(int index) {
        Object prop = this.properties.elementAt(index);
        if (prop instanceof PropertyInfo) {
            return ((PropertyInfo) prop).getValue();
        }
        return (SoapObject) prop;
    }

    public String getPropertyAsString(int index) {
        return ((PropertyInfo) this.properties.elementAt(index)).getValue().toString();
    }

    public Object getProperty(String name) {
        Integer index = propertyIndex(name);
        if (index != null) {
            return getProperty(index.intValue());
        }
        throw new RuntimeException("illegal property: " + name);
    }

    public Object getProperty(String namespace, String name) {
        Integer index = propertyIndex(namespace, name);
        if (index != null) {
            return getProperty(index.intValue());
        }
        throw new RuntimeException("illegal property: " + name);
    }

    public Object getPropertyByNamespaceSafely(String namespace, String name) {
        Integer i = propertyIndex(namespace, name);
        if (i != null) {
            return getProperty(i.intValue());
        }
        return new NullSoapObject();
    }

    public String getPropertyByNamespaceSafelyAsString(String namespace, String name) {
        Integer i = propertyIndex(namespace, name);
        if (i == null) {
            return EMPTY_STRING;
        }
        Object foo = getProperty(i.intValue());
        if (foo == null) {
            return EMPTY_STRING;
        }
        return foo.toString();
    }

    public Object getPropertySafely(String namespace, String name, Object defaultThing) {
        Integer i = propertyIndex(namespace, name);
        if (i != null) {
            return getProperty(i.intValue());
        }
        return defaultThing;
    }

    public String getPropertySafelyAsString(String namespace, String name, Object defaultThing) {
        Integer i = propertyIndex(namespace, name);
        if (i != null) {
            Object property = getProperty(i.intValue());
            if (property != null) {
                return property.toString();
            }
            return EMPTY_STRING;
        } else if (defaultThing != null) {
            return defaultThing.toString();
        } else {
            return EMPTY_STRING;
        }
    }

    public Object getPrimitiveProperty(String namespace, String name) {
        Integer index = propertyIndex(namespace, name);
        if (index != null) {
            PropertyInfo propertyInfo = (PropertyInfo) this.properties.elementAt(index.intValue());
            if (propertyInfo.getType() != SoapObject.class && propertyInfo.getValue() != null) {
                return propertyInfo.getValue();
            }
            propertyInfo = new PropertyInfo();
            propertyInfo.setType(String.class);
            propertyInfo.setValue(EMPTY_STRING);
            propertyInfo.setName(name);
            propertyInfo.setNamespace(namespace);
            return propertyInfo.getValue();
        }
        throw new RuntimeException("illegal property: " + name);
    }

    public String getPrimitivePropertyAsString(String namespace, String name) {
        Integer index = propertyIndex(namespace, name);
        if (index != null) {
            PropertyInfo propertyInfo = (PropertyInfo) this.properties.elementAt(index.intValue());
            if (propertyInfo.getType() == SoapObject.class || propertyInfo.getValue() == null) {
                return EMPTY_STRING;
            }
            return propertyInfo.getValue().toString();
        }
        throw new RuntimeException("illegal property: " + name);
    }

    public Object getPrimitivePropertySafely(String namespace, String name) {
        Integer index = propertyIndex(namespace, name);
        if (index == null) {
            return new NullSoapObject();
        }
        PropertyInfo propertyInfo = (PropertyInfo) this.properties.elementAt(index.intValue());
        if (propertyInfo.getType() != SoapObject.class && propertyInfo.getValue() != null) {
            return propertyInfo.getValue().toString();
        }
        propertyInfo = new PropertyInfo();
        propertyInfo.setType(String.class);
        propertyInfo.setValue(EMPTY_STRING);
        propertyInfo.setName(name);
        propertyInfo.setNamespace(namespace);
        return propertyInfo.getValue();
    }

    public String getPrimitivePropertySafelyAsString(String namespace, String name) {
        Integer index = propertyIndex(namespace, name);
        if (index == null) {
            return EMPTY_STRING;
        }
        PropertyInfo propertyInfo = (PropertyInfo) this.properties.elementAt(index.intValue());
        if (propertyInfo.getType() == SoapObject.class || propertyInfo.getValue() == null) {
            return EMPTY_STRING;
        }
        return propertyInfo.getValue().toString();
    }

    public boolean hasProperty(String namespace, String name) {
        if (propertyIndex(namespace, name) != null) {
            return true;
        }
        return false;
    }

    public String getPropertyAsString(String namespace, String name) {
        Integer index = propertyIndex(namespace, name);
        if (index != null) {
            return getProperty(index.intValue()).toString();
        }
        throw new RuntimeException("illegal property: " + name);
    }

    public String getPropertyAsString(String name) {
        Integer index = propertyIndex(name);
        if (index != null) {
            return getProperty(index.intValue()).toString();
        }
        throw new RuntimeException("illegal property: " + name);
    }

    public boolean hasProperty(String name) {
        if (propertyIndex(name) != null) {
            return true;
        }
        return false;
    }

    public Object getPropertySafely(String name) {
        Integer i = propertyIndex(name);
        if (i != null) {
            return getProperty(i.intValue());
        }
        return new NullSoapObject();
    }

    public String getPropertySafelyAsString(String name) {
        Integer i = propertyIndex(name);
        if (i == null) {
            return EMPTY_STRING;
        }
        Object foo = getProperty(i.intValue());
        if (foo == null) {
            return EMPTY_STRING;
        }
        return foo.toString();
    }

    public Object getPropertySafely(String name, Object defaultThing) {
        Integer i = propertyIndex(name);
        if (i != null) {
            return getProperty(i.intValue());
        }
        return defaultThing;
    }

    public String getPropertySafelyAsString(String name, Object defaultThing) {
        Integer i = propertyIndex(name);
        if (i != null) {
            Object property = getProperty(i.intValue());
            if (property != null) {
                return property.toString();
            }
            return EMPTY_STRING;
        } else if (defaultThing != null) {
            return defaultThing.toString();
        } else {
            return EMPTY_STRING;
        }
    }

    public Object getPrimitiveProperty(String name) {
        Integer index = propertyIndex(name);
        if (index != null) {
            PropertyInfo propertyInfo = (PropertyInfo) this.properties.elementAt(index.intValue());
            if (propertyInfo.getType() != SoapObject.class && propertyInfo.getValue() != null) {
                return propertyInfo.getValue();
            }
            propertyInfo = new PropertyInfo();
            propertyInfo.setType(String.class);
            propertyInfo.setValue(EMPTY_STRING);
            propertyInfo.setName(name);
            return propertyInfo.getValue();
        }
        throw new RuntimeException("illegal property: " + name);
    }

    public String getPrimitivePropertyAsString(String name) {
        Integer index = propertyIndex(name);
        if (index != null) {
            PropertyInfo propertyInfo = (PropertyInfo) this.properties.elementAt(index.intValue());
            if (propertyInfo.getType() == SoapObject.class || propertyInfo.getValue() == null) {
                return EMPTY_STRING;
            }
            return propertyInfo.getValue().toString();
        }
        throw new RuntimeException("illegal property: " + name);
    }

    public Object getPrimitivePropertySafely(String name) {
        Integer index = propertyIndex(name);
        if (index == null) {
            return new NullSoapObject();
        }
        PropertyInfo propertyInfo = (PropertyInfo) this.properties.elementAt(index.intValue());
        if (propertyInfo.getType() != SoapObject.class && propertyInfo.getValue() != null) {
            return propertyInfo.getValue().toString();
        }
        propertyInfo = new PropertyInfo();
        propertyInfo.setType(String.class);
        propertyInfo.setValue(EMPTY_STRING);
        propertyInfo.setName(name);
        return propertyInfo.getValue();
    }

    public String getPrimitivePropertySafelyAsString(String name) {
        Integer index = propertyIndex(name);
        if (index == null) {
            return EMPTY_STRING;
        }
        PropertyInfo propertyInfo = (PropertyInfo) this.properties.elementAt(index.intValue());
        if (propertyInfo.getType() == SoapObject.class || propertyInfo.getValue() == null) {
            return EMPTY_STRING;
        }
        return propertyInfo.getValue().toString();
    }

    private Integer propertyIndex(String name) {
        if (name != null) {
            for (int i = 0; i < this.properties.size(); i++) {
                if (name.equals(((PropertyInfo) this.properties.elementAt(i)).getName())) {
                    return new Integer(i);
                }
            }
        }
        return null;
    }

    private Integer propertyIndex(String namespace, String name) {
        if (!(name == null || namespace == null)) {
            for (int i = 0; i < this.properties.size(); i++) {
                PropertyInfo info = (PropertyInfo) this.properties.elementAt(i);
                if (name.equals(info.getName()) && namespace.equals(info.getNamespace())) {
                    return new Integer(i);
                }
            }
        }
        return null;
    }

    public int getPropertyCount() {
        return this.properties.size();
    }

    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo propertyInfo) {
        getPropertyInfo(index, propertyInfo);
    }

    public void getPropertyInfo(int index, PropertyInfo propertyInfo) {
        PropertyInfo element = this.properties.elementAt(index);
        if (element instanceof PropertyInfo) {
            PropertyInfo p = element;
            propertyInfo.name = p.name;
            propertyInfo.namespace = p.namespace;
            propertyInfo.flags = p.flags;
            propertyInfo.type = p.type;
            propertyInfo.elementType = p.elementType;
            propertyInfo.value = p.value;
            propertyInfo.multiRef = p.multiRef;
            return;
        }
        propertyInfo.name = null;
        propertyInfo.namespace = null;
        propertyInfo.flags = 0;
        propertyInfo.type = null;
        propertyInfo.elementType = null;
        propertyInfo.value = element;
        propertyInfo.multiRef = false;
    }

    public PropertyInfo getPropertyInfo(int index) {
        Object element = this.properties.elementAt(index);
        if (element instanceof PropertyInfo) {
            return (PropertyInfo) element;
        }
        return null;
    }

    public SoapObject newInstance() {
        SoapObject o = new SoapObject(this.namespace, this.name);
        for (int propIndex = 0; propIndex < this.properties.size(); propIndex++) {
            Object prop = this.properties.elementAt(propIndex);
            if (prop instanceof PropertyInfo) {
                o.addProperty((PropertyInfo) ((PropertyInfo) this.properties.elementAt(propIndex)).clone());
            } else if (prop instanceof SoapObject) {
                o.addSoapObject(((SoapObject) prop).newInstance());
            }
        }
        for (int attribIndex = 0; attribIndex < getAttributeCount(); attribIndex++) {
            AttributeInfo newAI = new AttributeInfo();
            getAttributeInfo(attribIndex, newAI);
            o.addAttribute(newAI);
        }
        return o;
    }

    public void setProperty(int index, Object value) {
        Object prop = this.properties.elementAt(index);
        if (prop instanceof PropertyInfo) {
            ((PropertyInfo) prop).setValue(value);
        }
    }

    public SoapObject addProperty(String name, Object value) {
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.name = name;
        propertyInfo.type = value == null ? PropertyInfo.OBJECT_CLASS : value.getClass();
        propertyInfo.value = value;
        return addProperty(propertyInfo);
    }

    public SoapObject addProperty(String namespace, String name, Object value) {
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.name = name;
        propertyInfo.namespace = namespace;
        propertyInfo.type = value == null ? PropertyInfo.OBJECT_CLASS : value.getClass();
        propertyInfo.value = value;
        return addProperty(propertyInfo);
    }

    public SoapObject addPropertyIfValue(String namespace, String name, Object value) {
        if (value != null) {
            return addProperty(namespace, name, value);
        }
        return this;
    }

    public SoapObject addPropertyIfValue(String name, Object value) {
        if (value != null) {
            return addProperty(name, value);
        }
        return this;
    }

    public SoapObject addPropertyIfValue(PropertyInfo propertyInfo, Object value) {
        if (value == null) {
            return this;
        }
        propertyInfo.setValue(value);
        return addProperty(propertyInfo);
    }

    public SoapObject addProperty(PropertyInfo propertyInfo) {
        this.properties.addElement(propertyInfo);
        return this;
    }

    public SoapObject addPropertyIfValue(PropertyInfo propertyInfo) {
        if (propertyInfo.value != null) {
            this.properties.addElement(propertyInfo);
        }
        return this;
    }

    public SoapObject addSoapObject(SoapObject soapObject) {
        this.properties.addElement(soapObject);
        return this;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(EMPTY_STRING + this.name + "{");
        for (int i = 0; i < getPropertyCount(); i++) {
            Object prop = this.properties.elementAt(i);
            if (prop instanceof PropertyInfo) {
                buf.append(EMPTY_STRING).append(((PropertyInfo) prop).getName()).append("=").append(getProperty(i)).append("; ");
            } else {
                buf.append(((SoapObject) prop).toString());
            }
        }
        buf.append("}");
        return buf.toString();
    }

    public Object getInnerText() {
        return this.innerText;
    }

    public void setInnerText(Object innerText) {
        this.innerText = innerText;
    }

    public void removePropertyInfo(Object info) {
        this.properties.remove(info);
    }
}
