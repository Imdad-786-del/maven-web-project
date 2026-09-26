package com.example.webapp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * A tiny, dependency-free Java web application.
 * Built on the JDK's built-in com.sun.net.httpserver, so it needs no
 * external libraries to compile or run - ideal for a Jenkins
 * Build -> Test -> Package -> Deploy pipeline demo.
 *
 * Run:   java -jar webapp.jar [port]
 * Then open: http://localhost:8080/
 */
public class App {

    public static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port '" + args[0] + "', using default " + DEFAULT_PORT);
            }
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new HomeHandler());
        server.createContext("/health", new HealthHandler());
        server.setExecutor(null); // default executor
        server.start();

        System.out.println("Server started on http://localhost:" + port + "/");
    }

    /** Serves the homepage. */
    static class HomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = buildHomePage();
            sendResponse(exchange, 200, html, "text/html; charset=UTF-8");
        }
    }

    /** Simple health-check endpoint, handy for a Jenkins "smoke test" or Deploy stage. */
    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            sendResponse(exchange, 200, "{\"status\":\"UP\"}", "application/json");
        }
    }

    static String buildHomePage() {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "  <meta charset=\"UTF-8\">\n"
                + "  <title>Jenkins Demo Webapp</title>\n"
                + "  <style>\n"
                + "    body { font-family: Arial, sans-serif; background:#f4f6f8; margin:0; padding:40px; }\n"
                + "    .card { max-width:600px; margin:0 auto; background:#fff; border-radius:8px;\n"
                + "            box-shadow:0 2px 8px rgba(0,0,0,0.1); padding:32px; }\n"
                + "    h1 { color:#2c3e50; }\n"
                + "    .badge { display:inline-block; background:#27ae60; color:#fff; padding:4px 10px;\n"
                + "             border-radius:4px; font-size:12px; }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <div class=\"card\">\n"
                + "    <span class=\"badge\">RUNNING</span>\n"
                + "    <h1>Hello from your Jenkins pipeline!</h1>\n"
                + "    <p>This page is served by a plain Java app packaged as a runnable .jar.</p>\n"
                + "    <p>Server time: " + LocalDateTime.now() + "</p>\n"
                + "    <p>Health check: <a href=\"/health\">/health</a></p>\n"
                + "  </div>\n"
                + "</body>\n"
                + "</html>\n";
    }

    static void sendResponse(HttpExchange exchange, int statusCode, String body, String contentType) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
