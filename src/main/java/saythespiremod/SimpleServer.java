package saythespiremod;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

public class SimpleServer {
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
    private static final String METHOD_POST = "POST";
    private static final String METHOD_OPTIONS = "OPTIONS";

    public HttpServer httpServer = null;
    public Logger logger = null;

    public interface EndPointHandler {
        String handle(Map<String, List<String>> requestParameters);
    }

    public SimpleServer(Logger logger) {
        this.logger = logger;

        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void start() {
        this.httpServer.start();
    }

    public void createGetEndpoint(String path, EndPointHandler handler) {
        this.httpServer.createContext(path, he -> {
            try {
                final Headers headers = he.getResponseHeaders();
                final String requestMethod = he.getRequestMethod().toUpperCase();
                switch (requestMethod) {
                    case METHOD_GET:
                        final Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());

                        String responseBody = "";
                        try {
                            responseBody = handler.handle(requestParameters);
                        } catch (final Exception ex) {
                            this.logger.error("SimpleServer caught error: " + ex.getMessage());
                            responseBody = "{\"error\": \"there was an error, check log\"}";
                        }

                        headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                        final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
                        he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                        he.getResponseBody().write(rawResponseBody);
                        break;
                    case METHOD_OPTIONS:
                        headers.set(HEADER_ALLOW, METHOD_GET + "," + METHOD_OPTIONS);
                        he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        headers.set(HEADER_ALLOW, METHOD_GET + "," + METHOD_OPTIONS);
                        he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                he.close();
            }
        });
    }

    public void createPostEndpoint(String path, EndPointHandler handler) {
        this.httpServer.createContext(path, he -> {
            try {
                final Headers headers = he.getResponseHeaders();
                final String requestMethod = he.getRequestMethod().toUpperCase();
                switch (requestMethod) {
                    case METHOD_POST:
                        final Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());

                        String responseBody = "";
                        try {
                            responseBody = handler.handle(requestParameters);
                        } catch (final Exception ex) {
                            this.logger.error("SimpleServer caught error: " + ex.getMessage());
                            responseBody = "{\"error\": \"there was an error, check log\"}";
                        }

                        headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                        final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
                        he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                        he.getResponseBody().write(rawResponseBody);
                        break;
                    case METHOD_OPTIONS:
                        headers.set(HEADER_ALLOW, METHOD_POST + "," + METHOD_OPTIONS);
                        he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        headers.set(HEADER_ALLOW, METHOD_POST + "," + METHOD_OPTIONS);
                        he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                he.close();
            }
        });
    }

    private static Map<String, List<String>> getRequestParameters(final URI requestUri) {
        final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();
        if (requestQuery != null) {
            final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
            for (final String rawRequestParameter : rawRequestParameters) {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0]);
                requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
                final String requestParameterValue = requestParameter.length > 1
                        ? decodeUrlComponent(requestParameter[1])
                        : null;
                requestParameters.get(requestParameterName).add(requestParameterValue);
            }
        }
        return requestParameters;
    }

    private static String decodeUrlComponent(final String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, CHARSET.name());
        } catch (final UnsupportedEncodingException ex) {
            throw new InternalError(ex);
        }
    }
}
