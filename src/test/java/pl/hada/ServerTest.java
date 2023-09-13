package pl.hada;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ServerTest {

    private static final int PORT = 8080;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_TOKEN = "mySecretToken";
    private static final URI url = URI.create("http://localhost:" + PORT + "/interns");

    @BeforeAll
    static void startServer() throws IOException {
        Server.start(PORT);
    }

    @Test
    public void testInternsEndpoint() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);
        assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
    }

    @Test
    public void testDevicesEndpoint() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_TOKEN);
        assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
    }
}
