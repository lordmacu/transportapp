package org.ksoap2.serialization;

import com.rutasdeautobuses.transmileniositp.R;
import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

class DM implements Marshal {
    DM() {
    }

    public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo excepted) throws IOException, XmlPullParserException {
        String text = parser.nextText();
        switch (name.charAt(0)) {
            case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /*98*/:
                return new Boolean(SoapEnvelope.stringToBoolean(text));
            case R.styleable.AppCompatTheme_radioButtonStyle /*105*/:
                return new Integer(Integer.parseInt(text));
            case R.styleable.AppCompatTheme_ratingBarStyleSmall /*108*/:
                return new Long(Long.parseLong(text));
            case 's':
                return text;
            default:
                throw new RuntimeException();
        }
    }

    public void writeInstance(XmlSerializer writer, Object instance) throws IOException {
        int cnt;
        int counter;
        AttributeInfo attributeInfo;
        String namespace;
        String name;
        String obj;
        if (instance instanceof AttributeContainer) {
            AttributeContainer attributeContainer = (AttributeContainer) instance;
            cnt = attributeContainer.getAttributeCount();
            for (counter = 0; counter < cnt; counter++) {
                attributeInfo = new AttributeInfo();
                attributeContainer.getAttributeInfo(counter, attributeInfo);
                try {
                    attributeContainer.getAttribute(counter, attributeInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (attributeInfo.getValue() != null) {
                    namespace = attributeInfo.getNamespace();
                    name = attributeInfo.getName();
                    if (attributeInfo.getValue() != null) {
                        obj = attributeInfo.getValue().toString();
                    } else {
                        obj = XmlPullParser.NO_NAMESPACE;
                    }
                    writer.attribute(namespace, name, obj);
                }
            }
        } else if (instance instanceof HasAttributes) {
            HasAttributes soapObject = (HasAttributes) instance;
            cnt = soapObject.getAttributeCount();
            for (counter = 0; counter < cnt; counter++) {
                attributeInfo = new AttributeInfo();
                soapObject.getAttributeInfo(counter, attributeInfo);
                try {
                    soapObject.getAttribute(counter, attributeInfo);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (attributeInfo.getValue() != null) {
                    namespace = attributeInfo.getNamespace();
                    name = attributeInfo.getName();
                    if (attributeInfo.getValue() != null) {
                        obj = attributeInfo.getValue().toString();
                    } else {
                        obj = XmlPullParser.NO_NAMESPACE;
                    }
                    writer.attribute(namespace, name, obj);
                }
            }
        }
        if (instance instanceof ValueWriter) {
            ((ValueWriter) instance).write(writer);
        } else {
            writer.text(instance.toString());
        }
    }

    public void register(SoapSerializationEnvelope cm) {
        cm.addMapping(cm.xsd, "int", PropertyInfo.INTEGER_CLASS, this);
        cm.addMapping(cm.xsd, "long", PropertyInfo.LONG_CLASS, this);
        cm.addMapping(cm.xsd, "string", PropertyInfo.STRING_CLASS, this);
        cm.addMapping(cm.xsd, "boolean", PropertyInfo.BOOLEAN_CLASS, this);
    }
}
