package utilsLib.util;

import utilsLib.util.data.Entity;
import utilsLib.util.data.ParamDataInput;

/**
 * <p>Interface de operações básicas de uma fábrica de objetos.</p>
 *
 * <p>A classe implementadora desta interface deve possuir campos estatáticos
 * representativos das classes envolvidas na fábrica.</p>
 *
 */

public interface ObjFactory {
  /**
   * Retorna a entidade padrão associada ao identificador passado por
   * parâmetro. A instância retornada é um esqueleto da classe e não
   * deve ser diretamente utilizada. Caso se queira fazê-lo, deve-se
   * criar uma cópia pelo método <code>clone()</code> de
   * {@link Entity}</code>.
   *
   * @param classID Identificador único da classe cujo <code>Entity</code>
   *                se deseja obter.
   * @return        Entidade modelo relacionada à classe.
   */
  public Entity getClassEntity(int classID);

  /**
   * <p>Obtém a entidade da instância do objeto passado por parâmetro. Esta
   * entidade é um instantâneo dos campos do objeto.</p>
   *
   * <p>O objeto passado deve ser de uma classe utilizada na fábrica.</p>
   *
   * @param obj Instância de um objeto utilizado na fábrica.
   * @return    Entidade do objeto passado.
   */
  public Entity getEntity(Object obj);

  /**
   * <p>Obtém-se uma instância do objeto da classe pedida pelo parâmetro
   * <code>classID</code>, obtendo-se os valores dos campos via
   * {@link ParamDataInput}.</p>.
   *
   * <p><code>ParamDataInput</code> é utilizada, pois é uma forma
   * genérica de se obter dados parametrizados de qualquer fonte, como
   * de consultas a bancos de dados, query string de páginas ou até mesmo
   * de {@link Entity}.</p>
   *
   * <p>De acodo com a classe pedida, este método busca em <code>pdi</code>
   * pelos valores dos campos, cujos nomes casem com os definidos para cada
   * campo da classe, que é rezlizada implementação desta
   * interface. Então, deve-se ter pelo menos todos os campos da classe
   * pedida no <code>pdi</code>, sendo todos os outros ignorados.</p>
   *
   * @param pdi     Fonte dos dados parâmetrizados.
   * @param classID ID da classe da qual se deseja instânciar.
   * @return        Instância do objeto.
   */
  public Object paramDataToObj(ParamDataInput pdi);

  /**
   * Obtém um arary da classes especificada.
   *
   * @param classID Identificador da classe.
   * @param size    Tamanho do array.
   * @return        Array do tamanho especificado.
   */
  public Object[] getArray(int classID, int size);
}
