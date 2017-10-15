package utilsLib.util;

/**
 * Exce��o que indica a inv�lidade de e-mails.
 */
public class InvalidEmailException
    extends Exception {

  public InvalidEmailException() {
    super("N�o foi definido um e-mail v�lido.");
  }

  public InvalidEmailException(String s) {
    super(s);
  }
}
