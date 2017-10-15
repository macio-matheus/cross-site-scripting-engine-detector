package utilsLib.util.data;

import java.util.*;

import utilsLib.util.*;




/**
 * <p>Converte um <code>Map</code> em um <code>Iterator</code>, sendo os
     * campos (chave + dado) do <code>Map</code> convertidos para {@link Field}.</p>
 *
 * <p><code>Map</code> � uma interface de recupera��o de dados de forma
 * bidimensional, ou seja, para cada chave existe um valor associdos. O
 * <code>Iterator</code> � unidimensional. Ent�o, para fazer a convers�o
 * de <code>Map</code> para <code>Iterator</code>, faz-se uso de
 * <code>Field</code>, o qual � instanciado para cada par chave-valor existente
 * no <code>Map</code>. Todos os valores objetos do <code>Map</code> s�o
 * convertidos para string.</p>
 */
public class MapIterator
    implements Iterator {
  private Map map; // Mapa que cont�m os valores.
  private Object[] keys; // Armazena os nomes dos campos.
  private int keyIndex; // QUal a chave do array � a atual

  /**
   * Construtor de <code>MapIterator</code>.
   *
   * @param map <code>Map</code> do qual ser�o extra�dos os valores.
   */
  public MapIterator(Map map) {
    if (map == null) {
      throw new IllegalArgumentException();
    }

    this.map = map;
    this.keys = map.keySet().toArray();
    keyIndex = (keys.length == 0) ? -1 : 0;
  }

  public boolean hasNext() {
    return (keyIndex != keys.length - 1);
  }

  public Object next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    Field field = new Field(keys[keyIndex] + "",
                            map.get(keys[keyIndex]) + "",
                            DataType.STRING);
    keyIndex++;
    return field;
  }

  public void remove() {
    /**@todo: Implement this java.util.Iterator method*/
    throw new java.lang.UnsupportedOperationException(
        "Method remove() not yet implemented.");
  }
}
