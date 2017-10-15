package utilsLib.util.data;

import java.util.*;
import javax.servlet.http.*;

import utilsLib.util.*;




/**
 * Implementa <code>Iterator</code> para recuperação dos valores de um request.
 * Os objetos recuperados são da classe {@link Field}.
 *
 * @author Francisco Rodrigues, Leonardo Rodrigues
 * @version 1.0, 2002/02/06
 */
public class RequestHttpIterator
    implements Iterator {
  private Enumeration enum1; // Recupera os nomes da chaves.
  private HttpServletRequest request; // Armazena o request,

  /**
   * Construtor.
   *
   * @param request Request ativo para recuperação dos valores.
   */
  public RequestHttpIterator(HttpServletRequest request) {
    if (request == null) {
      throw new IllegalArgumentException();
    }
    enum1 = request.getParameterNames();
    this.request = request;
  }

  public boolean hasNext() {
    return enum1.hasMoreElements();
  }

  public Object next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    String name = enum1.nextElement() + "";
    Field field = new Field(name, request.getParameter(name),
                            DataType.STRING);

    return field;
  }

  public void remove() {
    throw new java.lang.UnsupportedOperationException();
  }
}
