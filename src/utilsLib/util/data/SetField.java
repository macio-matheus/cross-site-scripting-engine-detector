package utilsLib.util.data;

import java.util.*;

/**
 * Identifica um campo de dado como um grupo de valores, ou seja, um
 * agrupamento de valores do mesmo tipo.
 *
 * @author Leonardo Rodrigues
 * @version 1.0, 2002/12/24
 */
public class SetField
    extends Field {
  private LinkedList list;

  public SetField(String name, Object[] value, DataType type) {
    super(name, value, type);
    list = new LinkedList();
  }

  /**
   * Reimplementa o métdodo da superclasse, com a mesma semantica de
   * {link #addObj(Object)}.
   *
   * @param value Valor a ser acrescido ao conjunto.
   */
  public void setValue(String value) {
    addObj(value);
  }

  /**
   * Reinmplementa o método da superclasse para que ele retorne o último
   * elemento acrescido ao conjunto.
   *
   * @return  Último valor passado. Se não for posto nenhum, retorna
   *          <code>null</code>.
   */
  public String getValue() {
    if (list.size() > 0) {
      return list.get(list.size() - 1) + "";
    }
    else {
      return null;
    }
  }

  /**
   * Reimplementya o método da superclasse, agora aceitando apenas valores
   * cuja classe seja um array, para serem colocados no grupo.
   *
   * @param value  Array dos valores dos campos. Se for passado
   *               <code>null</code>, o conjunto é zerado.
   */
  public void setObj(Object value) {
    if (value == null) {
      list.clear();
    }
    else {
      Object[] array = null;

      // tenta converter para array.
      try {
        array = (Object[]) value;
      }
      catch (ClassCastException error) {
        throw new IllegalArgumentException("Esta (SetField) classe aceita"
                                           +
            " apenas valores no formato de array.");
      }

      for (int i = 0; i < array.length; i++) {
        list.add(array[i]);
      }
    }
  }

  /**
   * Obtém um conjunto dos valores armazenados até o momento pelo campo.
   *
   * @return  Array contendo os valores armazenados pelo campo.
   */
  public Object getObj() {
    return toArray();
  }

  /**
   * Obtém um conjunto dos valores armazenados até o momento pelo campo. Este
   * método permite que se determine a classe do array, por meio do
   * parametro.
   *
   * @param array Array base para o retorno.
   * @return      Array contendo os valores armazenados pelo campo.
   */
  public Object[] toArray(Object[] array) {
    return list.toArray(array);
  }

  /**
   * Obtém um conjunto dos valores armazenados até o momento pelo campo.
   *
   * @return      Array contendo os valores armazenados pelo campo.
   */
  public Object[] toArray() {
    return list.toArray();
  }

  /**
   * Obtém o número de elementos armazenados na instancia desta classe.
   *
   * @return Número de elementos armazenados até o momento.
   */
  public int getSize() {
    return list.size();
  }

  /**
   * Obtém o elemento, baseado em um dado índice.
   *
   * @param index Índice especificado.
   * @return      Objeto relacionado ao índice.
   */
  public Object get(int index) {
    return list.get(index);
  }

  /**
   *  Acrescenta o objeto passdado ao conjunto de valores.
   *
   * @param value Instancia de objeto a ser acrescido ao conjunto.
   */
  public void addObj(Object value) {
    list.add(value);
  }

  /**
   * Faz uma cópia da instância desta classe.
   *
   * @return <code>SetField</code> cópia da instância.
   */
  public Object clone() {
    return new SetField(getName(), (Object[]) toArray(), getType());
  }
}
