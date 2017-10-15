package utilsLib.util;

import java.util.HashMap;

/**
 * <p>Gerenciador de identificadores. Deve se ter apenas uma instancia desta
 * classe por projeto, pois, havendo mais, pode ocorrer erros de consistencia
 * de identificadores.</p>
 * <p>Os bancos de dados fornecem meios para gerenciamento de identificadores
 * únicos de registros, sendo isso dependente de banco para banco. Com
 * esta classe, esta funcionalidade fica encapsulada. Esta implementação
 * gerencia os identificadores na memória, buscando o primeiro valor
 * de incremento como sendo o maior índice existente no banco de dados,
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
   * Gera o próximo valor.
   *
   * @param idName  Nome do identificador.
   * @return        Inteiro gerado.
   */
  public synchronized int getNext(String idName) {
    if (idName == null) {
      throw new IllegalArgumentException("Parametro inválido: idName");
    }

    Object strNumber = ids.get(idName);

    if (strNumber == null) {
      throw new IllegalArgumentException("Parametro inválido: " +
          "idName. Ele não foi encontrado no conjunto " +
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
   * @param value   Valor inicial da chave. Será este o primeiro a ser
   *                recuperado.
   */
  public synchronized void addId(String idName, int value) {
    if (idName == null) {
      throw new IllegalArgumentException("Parametro inválido: idName");
    }

    if (ids.containsKey(idName)) {
      throw new IllegalArgumentException("Parametro inválido: " +
          "idName. Já existe um identificador com este nome: " + idName);
    }

    ids.put(idName, value + "");
  }

  /**
   * Verifica se o identificador existe.
   *
   * @param idName  Identificador a ser procurado.
   * @return        Verdadeiro, caso o identificador já esteja cadastrado.
   */
  public synchronized boolean containsId(String idName) {
    return (ids.containsKey(idName));
  }
}
