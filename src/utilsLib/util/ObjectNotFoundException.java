package utilsLib.util;

/**
 * Indica que não encontrou o objeto procurado. É uma exceção genérica
 * para métodos que retornam um objeto que podem retornar nulo.

 */
public class ObjectNotFoundException
    extends Exception {
  public ObjectNotFoundException() {
    super("O objeto procurado não foi encontrado.");
  }

  public ObjectNotFoundException(String msg) {
    super(msg);
  }
}
