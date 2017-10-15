package utilsLib.util.data;

import java.sql.*;
import java.util.*;

import utilsLib.util.*;




/**
 * <p>Utlizada para a leitura das linhas de uma consulta ao banco de dados.
 * A partir da classe <code>ResultSet</code>, obt�m-se um
 * <code>Iterator</code> dos registros.</p>
 *
 * <p>O <code>ResultSet</code> deve j� estar aberta na consulta quista, com
 * o cursor no estado de abertura.</p>
 *
 * <p>Depois que todos os registros s�o percorridos,
 * tanto o <code>ResultSet</code> quanto <code>Statement</code> dele s�o
 * fechados.</p>
 *
 * <p>A recupera��o e instancia��o dos objetos fica a cargo da classe que
 * herde desta. No m�todo {@link next()}, que deve ser implementado, �
 * gerenciada toda a obten��o dos valores e movimenta��o do cursor, al�m de
 * disparar um {@link setEOF()} com valor <code>true</code>, caso n�o haja
 * mais registros. Para maiores informa��es, consulte {@link next()}</p>

 */
public abstract class AbstractDBIterator
    implements Iterator {
  private ResultSet rs;
  private ConnManager connManager;

  /**
   * Vari�vel para uso interno para se armazenar se, incialmente, h� dados
   * a serem retornados.
   */
  private boolean EOF = false;

  /**
   * <p>Construtor da classe. Caso a consulta j� esteja vazia, j� s�o
   * fechados os objetos de consulta.</p>
   *
   * @param rs   <code>ResultSet</code> no estado de cursos incial.
   * @param stmt <code>Statement</code> associado.
   * @throws SQLException
   */
  public AbstractDBIterator(ResultSet rs) throws SQLException {
    // Testes da validade dos argumentos.
    if (rs == null) {
      throw new IllegalArgumentException();
    }

    this.rs = rs;
    setEOF(!rs.next());
  }

  /**
   * Fecha os objetos de <code>rs</code> e o <code>smtp</code> associado.
   * Caso eles j� estejam sido fechados, � disparada exce��o de erro de
   * execu��o.
   */
  protected void closeObjects() {
    try {
      if (rs != null) {
        Statement stmt = rs.getStatement();
        rs.close();
        stmt.close();
        rs = null;
      }
    }
    catch (SQLException error) {
      throw new RuntimeException("Erro no objeto de SQL: " +
                                 error.getMessage());
    }
  }

  /**
   * Rertorna se ainda existem mais elementos. Esta fun��o n�o precisa de
   * ser implementada, j� que ela simplesmente retorna <code>!EOF</code> e
   * esta propriedade � modificada na fun��o {@link next()}.
   *
   * @return <code>True</code> caso haja mais elementos.
   */
  public boolean hasNext() {
    return!EOF;
  }

  /**
   * <p>Fun��o que retorna o elemento atual e coloca o cursor no pr�ximo.
   * Deve ser implementada no que diz respeito � cria��o do objeto de
   * retorno, movimenta��o do cursor e, caso n�o haja mais registros,
   * deve-se chamar {@link setEOF(boolean)} com valor <code>true</code>.
   * Caso seja chamada j� se tendo chamada a fun��o citada, � disparada
   * uma exce��o.</p>
   *
   *
   * @return Retorna apenas <code>null</code>, pois o retorno deve ser
   *         implementado e este m�todo por si s� n�o tem sentido.
   */
  public Object next() {
    if (EOF) {
      throw new NoSuchElementException();
    }
    return null;
  }

  /**
   * Opera��o n�o suportada.
   *
   * @throws UnsupportedOperationException
   */
  public void remove() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Testa se � fim de consulta. Este teste n�o � realizado diretamente
   * pelo <code>ResultSet</code>, mas sim pela propriedade protegida
   * <code>EOF</code>.
   *
   * @return EOF.
   */
  protected boolean isEOF() {
    return EOF;
  }

  /**
   * <p>Altera o valor do campo <code>EOF</code>. Esta fun��o deve ser chamada
   * na implementa��o de {@link next()}.</p>
   *
   * <p>Caso se passe o valor <code>true</code>, os objetos de consulta s�o
   * fechados automaticamente. Caso os objetos j� estejam fechados, �
   * disparada uma exce��o.</p>
   *
   * @param EOF Novo valor do campo <code>EOF</code>.
   */
  protected void setEOF(boolean EOF) {
    this.EOF = EOF;
    if (EOF) {
      closeObjects();
    }
  }

  /**
   * Retorna o <code>ResultSet</code> associado.
   *
   * @return <code>ResultSet</code> associado � inst�ncia desta classe.
   */
  protected ResultSet getRs() {
    return rs;
  }
}
