package org.ksoap2.transport;

import com.facebook.internal.Utility;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class HttpTransportSE extends Transport {
    public HttpTransportSE(String url) {
        super(null, url);
    }

    public HttpTransportSE(Proxy proxy, String url) {
        super(proxy, url);
    }

    public HttpTransportSE(String url, int timeout) {
        super(url, timeout);
    }

    public HttpTransportSE(Proxy proxy, String url, int timeout) {
        super(proxy, url, timeout);
    }

    public HttpTransportSE(String url, int timeout, int contentLength) {
        super(url, timeout);
    }

    public HttpTransportSE(Proxy proxy, String url, int timeout, int contentLength) {
        super(proxy, url, timeout);
    }

    public void call(String soapAction, SoapEnvelope envelope) throws HttpResponseException, IOException, XmlPullParserException {
        call(soapAction, envelope, null);
    }

    public List call(String soapAction, SoapEnvelope envelope, List headers) throws HttpResponseException, IOException, XmlPullParserException {
        return call(soapAction, envelope, headers, null);
    }

    public List call(String soapAction, SoapEnvelope envelope, List headers, File outputFile) throws HttpResponseException, IOException, XmlPullParserException {
        String str;
        int i;
        if (soapAction == null) {
            soapAction = "\"\"";
        }
        byte[] requestData = createRequestData(envelope, "UTF-8");
        if (this.debug) {
            String str2 = new String(requestData);
        } else {
            str = null;
        }
        this.requestDump = str;
        this.responseDump = null;
        ServiceConnection connection = getServiceConnection();
        connection.setRequestProperty("User-Agent", "ksoap2-android/2.6.0+");
        if (envelope.version != 120) {
            connection.setRequestProperty("SOAPAction", soapAction);
        }
        if (envelope.version == 120) {
            connection.setRequestProperty("Content-Type", "application/soap+xml;charset=utf-8");
        } else {
            connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
        }
        connection.setRequestProperty("Accept-Encoding", "gzip");
        if (headers != null) {
            for (i = 0; i < headers.size(); i++) {
                HeaderProperty hp = (HeaderProperty) headers.get(i);
                connection.setRequestProperty(hp.getKey(), hp.getValue());
            }
        }
        connection.setRequestMethod("POST");
        sendData(requestData, connection, envelope);
        InputStream is = null;
        List retHeaders = null;
        int contentLength = Utility.DEFAULT_STREAM_BUFFER_SIZE;
        boolean gZippedContent = false;
        boolean xmlContent = false;
        int status = connection.getResponseCode();
        retHeaders = connection.getResponseProperties();
        for (i = 0; i < retHeaders.size(); i++) {
            hp = (HeaderProperty) retHeaders.get(i);
            if (hp.getKey() != null) {
                if (hp.getKey().equalsIgnoreCase("content-length") && hp.getValue() != null) {
                    try {
                        contentLength = Integer.parseInt(hp.getValue());
                    } catch (NumberFormatException e) {
                        contentLength = Utility.DEFAULT_STREAM_BUFFER_SIZE;
                    }
                }
                try {
                    if (hp.getKey().equalsIgnoreCase("Content-Type") && hp.getValue().contains("xml")) {
                        xmlContent = true;
                    }
                    if (hp.getKey().equalsIgnoreCase("Content-Encoding") && hp.getValue().equalsIgnoreCase("gzip")) {
                        gZippedContent = true;
                    }
                } catch (IOException e2) {
                    if (contentLength > 0) {
                        if (gZippedContent) {
                            is = getUnZippedInputStream(new BufferedInputStream(connection.getErrorStream(), contentLength));
                        } else {
                            is = new BufferedInputStream(connection.getErrorStream(), contentLength);
                        }
                    }
                    if ((e2 instanceof HttpResponseException) && !xmlContent) {
                        if (this.debug && is != null) {
                            readDebug(is, contentLength, outputFile);
                        }
                        connection.disconnect();
                        throw e2;
                    }
                }
            }
        }
        if (status != 200) {
            throw new HttpResponseException("HTTP request failed, HTTP status: " + status, status, retHeaders);
        }
        if (contentLength > 0) {
            if (gZippedContent) {
                is = getUnZippedInputStream(new BufferedInputStream(connection.openInputStream(), contentLength));
            } else {
                is = new BufferedInputStream(connection.openInputStream(), contentLength);
            }
        }
        if (this.debug) {
            is = readDebug(is, contentLength, outputFile);
        }
        parseResponse(envelope, is, retHeaders);
        connection.disconnect();
        return retHeaders;
    }

    protected void sendData(byte[] requestData, ServiceConnection connection, SoapEnvelope envelope) throws IOException {
        connection.setRequestProperty("Content-Length", XmlPullParser.NO_NAMESPACE + requestData.length);
        connection.setFixedLengthStreamingMode(requestData.length);
        OutputStream os = connection.openOutputStream();
        os.write(requestData, 0, requestData.length);
        os.flush();
        os.close();
    }

    protected void parseResponse(SoapEnvelope envelope, InputStream is, List returnedHeaders) throws XmlPullParserException, IOException {
        parseResponse(envelope, is);
    }

    private InputStream readDebug(InputStream is, int contentLength, File outputFile) throws IOException {
        OutputStream bos;
        if (outputFile != null) {
            bos = new FileOutputStream(outputFile);
        } else {
            if (contentLength <= 0) {
                contentLength = ServiceConnection.DEFAULT_BUFFER_SIZE;
            }
            bos = new ByteArrayOutputStream(contentLength);
        }
        byte[] buf = new byte[256];
        while (true) {
            int rd = is.read(buf, 0, 256);
            if (rd == -1) {
                break;
            }
            bos.write(buf, 0, rd);
        }
        bos.flush();
        if (bos instanceof ByteArrayOutputStream) {
            buf = ((ByteArrayOutputStream) bos).toByteArray();
        }
        this.responseDump = new String(buf);
        is.close();
        if (outputFile != null) {
            return new FileInputStream(outputFile);
        }
        return new ByteArrayInputStream(buf);
    }

    private InputStream getUnZippedInputStream(InputStream inputStream) throws IOException {
        try {
            return (GZIPInputStream) inputStream;
        } catch (ClassCastException e) {
            return new GZIPInputStream(inputStream);
        }
    }

    public ServiceConnection getServiceConnection() throws IOException {
        return new ServiceConnectionSE(this.proxy, this.url, this.timeout);
    }
}
