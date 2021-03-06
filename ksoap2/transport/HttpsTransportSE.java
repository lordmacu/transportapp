package org.ksoap2.transport;

import java.io.IOException;
import java.net.Proxy;

public class HttpsTransportSE extends HttpTransportSE {
    static final String PROTOCOL = "https";
    private static final String PROTOCOL_FULL = "https://";
    private HttpsServiceConnectionSE connection;
    protected final String file;
    protected final String host;
    protected final int port;

    public HttpsTransportSE(String host, int port, String file, int timeout) {
        super(PROTOCOL_FULL + host + ":" + port + file, timeout);
        this.host = host;
        this.port = port;
        this.file = file;
    }

    public HttpsTransportSE(Proxy proxy, String host, int port, String file, int timeout) {
        super(proxy, PROTOCOL_FULL + host + ":" + port + file);
        this.host = host;
        this.port = port;
        this.file = file;
        this.timeout = timeout;
    }

    public ServiceConnection getServiceConnection() throws IOException {
        if (this.connection != null) {
            return this.connection;
        }
        this.connection = new HttpsServiceConnectionSE(this.proxy, this.host, this.port, this.file, this.timeout);
        return this.connection;
    }
}
