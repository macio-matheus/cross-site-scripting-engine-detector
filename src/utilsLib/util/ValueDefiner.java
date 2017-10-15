package utilsLib.util;

/**
 * Respons�vel por definir o valor de algum objeto. Por exemplo,
 * � utilizada para realizar uma configura��o para se um dado objeto
 * entrar numa lista. Caso seja para indicar que o objeto n�o seja para
 * entrar na lista, dispara <code>IllegalArgumentException</code> como indicativo.
 */
public interface ValueDefiner {
  public Object defineValue(Object o);
}
