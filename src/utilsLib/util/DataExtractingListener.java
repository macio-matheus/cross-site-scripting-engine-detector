package utilsLib.util;

/**
 * Interface de recebimento de enventos associados a extra��o de dados
 * de {@link DataManager}.
 */
public interface DataExtractingListener {
  /**
   * Evento disparado quando executa o m�todo que extrai os dados do db
   * e os armazena em XML. Capta todas as vezes que um novo XML � escrito.
   *
   * @param extractedCount   Quantidade de consultas extar�das.
   * @param path             Caminho do arquivo criado.
   */
  public void extractDataEvent(int extractedCount, String path);

  /**
   * Evento disparado quando executa o m�todo que povoa o banco com os
   * dados do XML.
   *
   * @param fileName      Arquivo
   * @param currRegister  Registro atual.
   * @param sql           SQL utilizada para a inser��o.
   */
  public void populateDataEvent(String fileName, int currRegister, String sql);
}
