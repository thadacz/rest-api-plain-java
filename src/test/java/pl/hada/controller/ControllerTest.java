package pl.hada.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.hada.Server;
import pl.hada.model.Device;

class ControllerTest {

    private static final int PORT = 8080;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_TOKEN = "mySecretToken";

    @BeforeAll
    static void startServer() throws IOException {
        Server.start(PORT);
    }

    @Test
    public void testGetDeviceByIdEndpointWhenDeviceNotExists() throws IOException {
        URI url = URI.create("http://localhost:" + PORT + "/devices/1");
        HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
    }

    @Test
    public void testGetDeviceByIdEndpointWhenUrlIsIncorrect() throws IOException {
        URI url = URI.create("http://localhost:" + PORT + "/devices/1/1");
        HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
    }

    @Test
    public void testGetAllDevicesEndpoint() throws IOException {
        URI url = URI.create("http://localhost:" + PORT + "/devices");
        HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);
        assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
    }

    @Test
    public void testUnauthorizedEndpoint() throws IOException {
        URI url = URI.create("http://localhost:" + PORT + "/devices");
        HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
        connection.setRequestMethod("GET");
        assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, connection.getResponseCode());
    }

    @Test
    public void testCreateReadUpdateDeleteDeviceEndpoints() throws IOException {
        // CREATE
        URI url = URI.create("http://localhost:" + PORT + "/devices");
        HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);

        Device device = new Device("iPhone 14", "AS1312");
        String requestBody = new Gson().toJson(device);

        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestBody.getBytes());
        outputStream.flush();
        outputStream.close();

        int statusCode = connection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_CREATED, statusCode);

        // FIND

        URI url2 = URI.create("http://localhost:" + PORT + "/devices/1");
        HttpURLConnection connection2 = (HttpURLConnection) url2.toURL().openConnection();
        connection2.setRequestMethod("GET");
        connection2.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);

        statusCode = connection2.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_OK, statusCode);

        // UPDATE
        URI url4 = URI.create("http://localhost:" + PORT + "/devices/1");
        HttpURLConnection connectio4 = (HttpURLConnection) url4.toURL().openConnection();
        connectio4.setRequestMethod("PUT");
        connectio4.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);

        Device device1 = new Device("iPhone 13", "AS4132");
        String requestBody1 = new Gson().toJson(device1);

        connectio4.setDoOutput(true);
        OutputStream outputStream1 = connectio4.getOutputStream();
        outputStream1.write(requestBody1.getBytes());
        outputStream1.flush();
        outputStream1.close();

        statusCode = connectio4.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_OK, statusCode);

        // DELETE

        URI url5 = URI.create("http://localhost:" + PORT + "/devices/1");
        HttpURLConnection connection5 = (HttpURLConnection) url5.toURL().openConnection();
        connection5.setRequestMethod("DELETE");
        connection5.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);

        statusCode = connection5.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, statusCode);
    }

    @Test
    public void testBadMethodEndpoint() throws IOException {
        URI url = URI.create("http://localhost:" + PORT + "/devices/1");
        HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
        connection.setRequestMethod("HEAD");
        connection.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);
        assertEquals(HttpURLConnection.HTTP_BAD_METHOD, connection.getResponseCode());
    }

    @Test
    public void testWrongCreateEndpoint() throws IOException {
        URI url = URI.create("http://localhost:" + PORT + "/devices/1/1/1");
        HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);

        Device device = new Device("iPhone 14", "AS1312");
        String requestBody = new Gson().toJson(device);

        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestBody.getBytes());
        outputStream.flush();
        outputStream.close();

        int statusCode = connection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, statusCode);
    }
}
