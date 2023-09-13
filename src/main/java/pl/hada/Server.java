package pl.hada;

import com.sun.net.httpserver.HttpServer;
import pl.hada.controller.Controller;
import pl.hada.exception.HttpStatusCodeExceptionResolver;
import pl.hada.model.Device;
import pl.hada.model.Entity;
import pl.hada.model.Intern;
import pl.hada.service.ServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Server {
  private static final int PORT = 8080;

  private static final Logger logger = Logger.getLogger(Server.class.getSimpleName());

  public static void main(String[] args) throws IOException {
    start(PORT);
  }

  public static void start(int port) throws IOException {
    HttpStatusCodeExceptionResolver resolver = new HttpStatusCodeExceptionResolver();
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    Stream.of(
            Intern.class,
            Device.class)
            .forEach(type -> createContext(server, type, resolver));
    server.start();
    logger.log(Level.INFO, "Server started");
  }

  private static <T extends Entity> void createContext(
      HttpServer server, Class<T> type, HttpStatusCodeExceptionResolver resolver) {
    String path = type.getSimpleName().toLowerCase() + "s";
    server.createContext("/" + path, new Controller<>(new ServiceImpl<>(type), resolver, path));
  }
}
