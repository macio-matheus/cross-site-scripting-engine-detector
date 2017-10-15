package utilsLib.util;

/**
 * Exceção que indica a inválidade de e-mails.
 */
public class InvalidEmailException
    extends Exception {

  public InvalidEmailException() {
    super("Não foi definido um e-mail válido.");
  }

  public InvalidEmailException(String s) {
    super(s);
  }
}
