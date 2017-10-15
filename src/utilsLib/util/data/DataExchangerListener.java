package utilsLib.util.data;

/**
 * Interface de recebimento de enventos associados a extração de dados
 * de {@link DataManager}.
 *
 */
public interface DataExchangerListener {
  /**
   * Evento disparado quando executa o método que extrai os dados do db
   * e os armazena em XML. Capta todas as vezes que um novo XML é escrito.
   *
   * @param extractedCount   Quantidade de consultas extarídas.
   * @param path             Caminho do arquivo criado.
   */
  public void extractDataEvent(int extractedCount, String path);

  /**
   * Evento disparado quando executa o método que povoa o banco com os
   * dados do XML.
   *
   * @param currTable     Índice da tabela atual, de acordo com os nomes dos
   *                      agrupamento de dados.
   * @param currRegister  Registro atual.
   * @param sql           SQL utilizada para a inserção.
   */
  public void populateDataEvent(int currTable, int currRound, int currRegister, String sql);
}
