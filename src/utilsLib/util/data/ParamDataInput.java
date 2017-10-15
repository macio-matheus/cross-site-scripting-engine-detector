package utilsLib.util.data;

import java.util.*;

import utilsLib.util.*;




/**
 * <p>Classe abstrata para leitura de entrada parametrizada. Realiza a leitura
 * de dados por meio de chaves.</p>
 *
 * @author Leonardo Rodrigues
 * @version 1.1, 2004-01-06
 */
public abstract class ParamDataInput {
  // Armazena os novos par�metros.
  private String extra;

  /**
   * Construtor sem par�metros.
   */
  public ParamDataInput(String extra) {
    this.extra = extra;
  }

  /**
   * Retorna o valor inteiro associado a uma determinada chave. Caso
   * o valor n�o possa ser convertido para inteiro, � retornado
   * <code>Utils.UNDEFINED_NUMBER</code>.
   *
   * @param paramName Nome da chave.
   * @return          Valor inteiro associado � chave.
   */
  public abstract int getInt(String paramName);

  /**
   * Retorna o valor real associado a uma determinada chave. Caso
   * o valor n�o possa ser convertido para real, � retornado
   * <code>Utils.UNDEFINED_NUMBER</code>.
   *
   * @param paramName Nome da chave.
   * @return          Valor inteiro associado � chave.
   */
  public abstract double getDouble(String paramName);


  public double getDouble(String paramName, int defaultDouble) {
      try {
          return getDouble(paramName);
      } catch (Exception error) {
          return defaultDouble;
      }
  }

  public int getInt(String paramName, int errorInt) {
      try {
          int value = this.getInt(paramName);

          if (value == -1) {
              value = errorInt;
          }

          return value;
      } catch (Exception error) {
          return errorInt;
      }
  }

  /**
   * Retorna o valor booleano associado a uma determinada chave. Caso
   * o valor n�o possa ser convertido para booleano, � retornado
   * <code>false</code>.
   *
   * @param paramName Nome da chave.
   * @return          Valor booleano associado � chave.
   */
  public abstract boolean getBoo(String paramName);

  /**
   * Retorna o valor em <code>String</code> associado a uma determinada
   * chave. Caso o valor n�o possa ser convertido para <code>String</code>,
   * � retornado <code>null</code>.
   *
   * @param paramName Nome da chave.
   * @return          Valor <code>String</code> associado � chave.
   */
  public abstract String getStr(String paramName);

  public String[] getStrArray(String paramName, char divisor) {
      String text = getStr(paramName);
      String[] texts = null;

      if (text != null) {
          texts = Utils.getLines(text, divisor);
      }

      return texts;
  }

  public int[] getIntArray(String paramName, char divisor) {
      String text = getStr(paramName);
      int[] values = null;

      if (text != null) {
          values = Utils.parseInt(Utils.getLines(text, divisor));
      }

      return values;
  }

  public String[] getStrArray(String paramName, char divisor, boolean trimAll) {
      String text = getStr(paramName);
      String[] texts = null;

      if (text != null) {
          texts = Utils.getLines(text, divisor);
      }

      if (texts != null && trimAll) {
          for (int i = 0; i < texts.length; i++) {
              texts[i] = texts[i].trim();
          }
      }

      return texts;
  }

  /**
   * Retorna o objeto associado a uma determinada
   * chave. Caso o valor n�o possa ser convertido ser obtido como Objeto,
   * � retornado <code>null</code>.
   *
   * @param paramName Nome da chave.
   * @return          Objeto associado � chave.
   */
  public Object getObject(String paramName) {
      return null;
  }

  /**
   * Retorna <code>GregorianCalendar</code> a partir do valor de uma
   * determinada chave. Caso o valor n�o possa ser convertido para
   * <code>GregorianCalendar</code>, � retornado <code>null</code>.
   *
   * @param paramName   Nome da chave.
   * @param pattern     Se for nulo, este m�todo far� tudo o possivel para
   *                    transformar em data, ate disparar exece��o.
   * @return            Objeto associado � chave.
   */
  public Date getDate(String paramName, String pattern) {
      return null;
  }

  /**
   * Retorna <code>GregorianCalendar</code> a partir do valor de uma
   * determinada chave. Caso o valor n�o possa ser convertido para
   * <code>GregorianCalendar</code>, � retornado <code>null</code>.
   *
   * @param paramName Nome da chave.
   * @return          Objeto associado � chave.
   */
  public Date getDate(String paramName) {
      return getDate(paramName, Utils.getDatePattern());
  }

  /**
   * Converte a data e se n�o conseguir coloca uma data padrao.
   *
   * @param paramName Nome do parametro a ser transformado em data.
   * @param errorDate Data que ser� colocada caso de erro. Pode ser nula.
   * @return          Data tranformada ou de erro.
   */
  public Date getDate(String paramName, Date errorDate) {
      try {
          return getDate(paramName, Utils.getDatePattern());
      } catch (Exception error) {
          return errorDate;
      }
  }

  public Date getDateTime(String paramName, Date errorDate) {
      try {
          return getDateTime(paramName);
      } catch (Exception error) {
          return errorDate;
      }
  }

  public Date getDateTime(String paramName) {
      return getDate(paramName, Utils.getDateTimePattern());
  }

  public Date getTime(String paramName) {
      return getDate(paramName, Utils.getTimePattern());
  }

  /**
   * <p>Este m�todo obt�m o <code>Iterator</code> dos campos. Ele anexa
   * os par�metros origin�rios da implementa��o aos novos par�metros.
   * Para se obter os novos
   * par�metros, � utilizdo o m�todo {@link getNewParams()}.</p>
   *
   * <p>O objeto que estara associado ao <code>Iterator</code> �
   * {@link Field}. Desta forma, pode-se obter o nome, valor e tipo de dado.
   * </p>
   *
   * <p>Este m�todo � abstrato, ent�o, a jun��o dos par�metros novos com os
   * originais deve ser realizada na implementa��o. Como exemplo, pode-se
   * retornar os campos originais anexados aos novos da seguinte forma:<br>
   * <br>
   * <code>
   *     Iterator[] its = {NovosCamposIterator, getNewParams());<br>
   *     return new IteratorJoiner(its);<br>
   * </code><br>
   * Desta forma, os campos originais e os novos s�o retornados, nesta ordem.
   * </p>
   *
   * @return <code>Iterator</code> de todos os campos.
   */
  public abstract Iterator getParams();

  public String getExtra() {
    return extra;
  }

  public void setExtra(String extra) {
    this.extra = extra;
  }
}
