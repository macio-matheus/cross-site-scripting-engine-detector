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
   * Reimplementa o m�tdodo da superclasse, com a mesma semantica de
   * {link #addObj(Object)}.
   *
   * @param value Valor a ser acrescido ao conjunto.
   */
  public void setValue(String value) {
    addObj(value);
  }

  /**
   * Reinmplementa o m�todo da superclasse para que ele retorne o �ltimo
   * elemento acrescido ao conjunto.
   *
   * @return  �ltimo valor passado. Se n�o for posto nenhum, retorna
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
   * Reimplementya o m�todo da superclasse, agora aceitando apenas valores
   * cuja classe seja um array, para serem colocados no grupo.
   *
   * @param value  Array dos valores dos campos. Se for passado
   *               <code>null</code>, o conjunto � zerado.
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
   * Obt�m um conjunto dos valores armazenados at� o momento pelo campo.
   *
   * @return  Array contendo os valores armazenados pelo campo.
   */
  public Object getObj() {
    return toArray();
  }

  /**
   * Obt�m um conjunto dos valores armazenados at� o momento pelo campo. Este
   * m�todo permite que se determine a classe do array, por meio do
   * parametro.
   *
   * @param array Array base para o retorno.
   * @return      Array contendo os valores armazenados pelo campo.
   */
  public Object[] toArray(Object[] array) {
    return list.toArray(array);
  }

  /**
   * Obt�m um conjunto dos valores armazenados at� o momento pelo campo.
   *
   * @return      Array contendo os valores armazenados pelo campo.
   */
  public Object[] toArray() {
    return list.toArray();
  }

  /**
   * Obt�m o n�mero de elementos armazenados na instancia desta classe.
   *
   * @return N�mero de elementos armazenados at� o momento.
   */
  public int getSize() {
    return list.size();
  }

  /**
   * Obt�m o elemento, baseado em um dado �ndice.
   *
   * @param index �ndice especificado.
   * @return      Objeto relacionado ao �ndice.
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
   * Faz uma c�pia da inst�ncia desta classe.
   *
   * @return <code>SetField</code> c�pia da inst�ncia.
   */
  public Object clone() {
    return new SetField(getName(), (Object[]) toArray(), getType());
  }
}
