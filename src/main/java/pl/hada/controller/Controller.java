package pl.hada.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.hada.exception.HttpStatusCodeExceptionResolver;
import pl.hada.model.Entity;
import pl.hada.service.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller<T extends Entity> implements HttpHandler {

    Logger logger = Logger.getLogger(Controller.class.getSimpleName());

    private final Gson gson = new Gson();
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_TOKEN = "mySecretToken";

    private final Service<T> service;
    private final HttpStatusCodeExceptionResolver statusCodeExceptionResolver;

    private final String basePath;

    public Controller(
            Service<T> service,
            HttpStatusCodeExceptionResolver statusCodeExceptionResolver,
            String basePath) {
        this.service = service;
        this.statusCodeExceptionResolver = statusCodeExceptionResolver;
        this.basePath = basePath;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize response variables
        String httpResponse = "";
        int httpStatusCode = HttpURLConnection.HTTP_OK; // Status code 200

        try {
            // Extract request information
            String httpMethod = exchange.getRequestMethod();
            String httpPath = exchange.getRequestURI().getPath();
            String[] pathParts = httpPath.split("/");

            // Check authorization token
            String authorization = exchange.getRequestHeaders().getFirst(AUTHORIZATION_HEADER);
            if (!AUTHORIZATION_TOKEN.equals(authorization)) {
                httpStatusCode = HttpURLConnection.HTTP_UNAUTHORIZED;
                httpResponse = "Unauthorized";
                logger.log(Level.WARNING, "Unauthorized request");
            } else {
                // Handle different HTTP methods and paths
                switch (httpMethod) {
                    case "POST" -> {
                        if (pathParts.length == 2 && pathParts[1].equals(basePath)) {
                            httpStatusCode = HttpURLConnection.HTTP_CREATED;
                            httpResponse = handlePostRequest(exchange);
                        } else {
                            httpStatusCode = HttpURLConnection.HTTP_NOT_FOUND; // Status code 404
                        }
                    }
                    case "GET" -> {
                        if (pathParts.length == 2 && pathParts[1].equals(basePath)) {
                            httpResponse = handleGetAllRequest();
                        } else if (pathParts.length == 3 && pathParts[1].equals(basePath)) {
                            Long id = extractIdFromPath(pathParts[2]);
                            httpResponse = handleGetByIdRequest(id);
                        } else {
                            httpStatusCode = HttpURLConnection.HTTP_NOT_FOUND; // Status code 404
                        }
                    }
                    case "PUT" -> {
                        if (pathParts.length == 3 && pathParts[1].equals(basePath)) {
                            Long id = extractIdFromPath(pathParts[2]);
                            httpResponse = handlePutRequest(id, exchange);
                        } else {
                            httpStatusCode = HttpURLConnection.HTTP_NOT_FOUND; // Status code 404
                        }
                    }
                    case "DELETE" -> {
                        if (pathParts.length == 3 && pathParts[1].equals(basePath)) {
                            Long id = extractIdFromPath(pathParts[2]);
                            httpResponse = handleDeleteRequest(id);
                        } else {
                            httpStatusCode = HttpURLConnection.HTTP_NOT_FOUND; // Status code 404
                        }
                    }
                    default -> httpStatusCode = HttpURLConnection.HTTP_BAD_METHOD; // Status code 405
                }
            }
        } catch (Exception e) {
            httpStatusCode = statusCodeExceptionResolver.resolveStatusCode(e);
            httpResponse = e.getMessage();
        }

        // Send response to client
        sendResponse(exchange, httpStatusCode, httpResponse);
    }

    private Long extractIdFromPath(String pathPart) throws NumberFormatException {
        return Long.valueOf(pathPart);
    }

    String handlePostRequest(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        service.create(requestBody);
        logger.log(Level.INFO, "Created successfully :" + requestBody);
        return "Created successfully";
    }

    private String handleGetAllRequest() {
        logger.log(Level.INFO, "Read all successfully " + service.getClass().getName());
        return gson.toJson(service.getAll());
    }

    String handleGetByIdRequest(Long id) {
        service.getById(id);
        logger.log(
                Level.INFO,
                "Created successfully " + service.getById(id).getClass().getName() + " with ID " + id);
        return gson.toJson(service.getById(id));
    }

    private String handlePutRequest(Long id, HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        service.update(id, requestBody);
        logger.log(
                Level.INFO,
                "Updated successfully " + service.getById(id).getClass().getName() + " with ID " + id);
        return "Updated successfully";
    }

    String handleDeleteRequest(Long id) {
        service.delete(id);
        logger.log(
                Level.INFO,
                "Deleted successfully " + service.getById(id).getClass().getName() + " with ID " + id);
        return "Deleted successfully";
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response)
            throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
