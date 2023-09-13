package pl.hada.exception;

public class HttpStatusCodeExceptionResolver {

  public int resolveStatusCode(Exception exception) {
    if (exception instanceof NotFoundException) {
      return 404;
    } else if (exception instanceof ValidationException) {
      return 400;
    } else {
      return 500;
    }
  }
}
