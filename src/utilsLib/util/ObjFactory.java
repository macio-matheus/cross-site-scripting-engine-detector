package utilsLib.util;

import utilsLib.util.data.Entity;
import utilsLib.util.data.ParamDataInput;

/**
 * <p>Interface de opera��es b�sicas de uma f�brica de objetos.</p>
 *
 * <p>A classe implementadora desta interface deve possuir campos estat�ticos
 * representativos das classes envolvidas na f�brica.</p>
 *
 */

public interface ObjFactory {
  /**
   * Retorna a entidade padr�o associada ao identificador passado por
   * par�metro. A inst�ncia retornada � um esqueleto da classe e n�o
   * deve ser diretamente utilizada. Caso se queira faz�-lo, deve-se
   * criar uma c�pia pelo m�todo <code>clone()</code> de
   * {@link Entity}</code>.
   *
   * @param classID Identificador �nico da classe cujo <code>Entity</code>
   *                se deseja obter.
   * @return        Entidade modelo relacionada � classe.
   */
  public Entity getClassEntity(int classID);

  /**
   * <p>Obt�m a entidade da inst�ncia do objeto passado por par�metro. Esta
   * entidade � um instant�neo dos campos do objeto.</p>
   *
   * <p>O objeto passado deve ser de uma classe utilizada na f�brica.</p>
   *
   * @param obj Inst�ncia de um objeto utilizado na f�brica.
   * @return    Entidade do objeto passado.
   */
  public Entity getEntity(Object obj);

  /**
   * <p>Obt�m-se uma inst�ncia do objeto da classe pedida pelo par�metro
   * <code>classID</code>, obtendo-se os valores dos campos via
   * {@link ParamDataInput}.</p>.
   *
   * <p><code>ParamDataInput</code> � utilizada, pois � uma forma
   * gen�rica de se obter dados parametrizados de qualquer fonte, como
   * de consultas a bancos de dados, query string de p�ginas ou at� mesmo
   * de {@link Entity}.</p>
   *
   * <p>De acodo com a classe pedida, este m�todo busca em <code>pdi</code>
   * pelos valores dos campos, cujos nomes casem com os definidos para cada
   * campo da classe, que � rezlizada implementa��o desta
   * interface. Ent�o, deve-se ter pelo menos todos os campos da classe
   * pedida no <code>pdi</code>, sendo todos os outros ignorados.</p>
   *
   * @param pdi     Fonte dos dados par�metrizados.
   * @param classID ID da classe da qual se deseja inst�nciar.
   * @return        Inst�ncia do objeto.
   */
  public Object paramDataToObj(ParamDataInput pdi);

  /**
   * Obt�m um arary da classes especificada.
   *
   * @param classID Identificador da classe.
   * @param size    Tamanho do array.
   * @return        Array do tamanho especificado.
   */
  public Object[] getArray(int classID, int size);
}
