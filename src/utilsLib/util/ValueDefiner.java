package utilsLib.util;

/**
 * Responsável por definir o valor de algum objeto. Por exemplo,
 * é utilizada para realizar uma configuração para se um dado objeto
 * entrar numa lista. Caso seja para indicar que o objeto não seja para
 * entrar na lista, dispara <code>IllegalArgumentException</code> como indicativo.
 */
public interface ValueDefiner {
  public Object defineValue(Object o);
}
