package saythespiremod;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SayTheSpireServer {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 10463;
    private static final int BACKLOG = 1;

    private static final String HEADER_ALLOW = "Allow";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final int STATUS_OK = 200;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private static final int NO_RESPONSE_LENGTH = -1;

    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_OPTIONS;

    public static void start() {
        HttpServer server = null;

        try {
            server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }

        server.createContext("/monsters", he -> {
            try {
                final Headers headers = he.getResponseHeaders();
                final String requestMethod = he.getRequestMethod().toUpperCase();
                if (requestMethod != METHOD_GET) {
                    headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                    he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                } else {
                    String responseBody = SayTheSpireApi.getMonsterData();
                    headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                    final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
                    he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                    he.getResponseBody().write(rawResponseBody);
                }
            } finally {
                he.close();
            }
        });
        server.createContext("/potions", he -> {
            try {
                final Headers headers = he.getResponseHeaders();
                final String requestMethod = he.getRequestMethod().toUpperCase();
                if (requestMethod != METHOD_GET) {
                    headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                    he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                } else {
                    String responseBody = SayTheSpireApi.getPotionData();
                    headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                    final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
                    he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                    he.getResponseBody().write(rawResponseBody);
                }
            } finally {
                he.close();
            }
        });
        server.start();
    }
}
