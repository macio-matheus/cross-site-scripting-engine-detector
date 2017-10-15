package utilsLib.util;

/**
 * Indica que n�o encontrou o objeto procurado. � uma exce��o gen�rica
 * para m�todos que retornam um objeto que podem retornar nulo.

 */
public class ObjectNotFoundException
    extends Exception {
  public ObjectNotFoundException() {
    super("O objeto procurado n�o foi encontrado.");
  }

  public ObjectNotFoundException(String msg) {
    super(msg);
  }
}
