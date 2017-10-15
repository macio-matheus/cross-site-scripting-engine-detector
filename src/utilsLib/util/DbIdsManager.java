package utilsLib.util;

import java.util.HashMap;

/**
 * <p>Gerenciador de identificadores. Deve se ter apenas uma instancia desta
 * classe por projeto, pois, havendo mais, pode ocorrer erros de consistencia
 * de identificadores.</p>
 * <p>Os bancos de dados fornecem meios para gerenciamento de identificadores
 * �nicos de registros, sendo isso dependente de banco para banco. Com
 * esta classe, esta funcionalidade fica encapsulada. Esta implementa��o
 * gerencia os identificadores na mem�ria, buscando o primeiro valor
 * de incremento como sendo o maior �ndice existente no banco de dados,
 * de acordo com uma busca utilizando <code>ObjFactory</code> e
 * o <code>Entity</code> lhe associado.</p>
 */
public class DbIdsManager {
  private HashMap ids;
  private String status;

  public DbIdsManager() {
    this.ids = new HashMap();
  }

  /**
   * Gera o pr�ximo valor.
   *
   * @param idName  Nome do identificador.
   * @return        Inteiro gerado.
   */
  public synchronized int getNext(String idName) {
    if (idName == null) {
      throw new IllegalArgumentException("Parametro inv�lido: idName");
    }

    Object strNumber = ids.get(idName);

    if (strNumber == null) {
      throw new IllegalArgumentException("Parametro inv�lido: " +
          "idName. Ele n�o foi encontrado no conjunto " +
          "dos identificadores.");
    }

    int next = Utils.parseInt(strNumber + "");
    ids.put(idName, (next + 1) + "");

    return next;
  }

  /**
   * Acrescenta um identificador a ser gerenciado.
   *
   * @param idName  Nome do identificador.
   * @param value   Valor inicial da chave. Ser� este o primeiro a ser
   *                recuperado.
   */
  public synchronized void addId(String idName, int value) {
    if (idName == null) {
      throw new IllegalArgumentException("Parametro inv�lido: idName");
    }

    if (ids.containsKey(idName)) {
      throw new IllegalArgumentException("Parametro inv�lido: " +
          "idName. J� existe um identificador com este nome: " + idName);
    }

    ids.put(idName, value + "");
  }

  /**
   * Verifica se o identificador existe.
   *
   * @param idName  Identificador a ser procurado.
   * @return        Verdadeiro, caso o identificador j� esteja cadastrado.
   */
  public synchronized boolean containsId(String idName) {
    return (ids.containsKey(idName));
  }
}
