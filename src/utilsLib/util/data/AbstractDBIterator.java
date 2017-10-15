package utilsLib.util.data;

import java.sql.*;
import java.util.*;

import utilsLib.util.*;




/**
 * <p>Utlizada para a leitura das linhas de uma consulta ao banco de dados.
 * A partir da classe <code>ResultSet</code>, obtém-se um
 * <code>Iterator</code> dos registros.</p>
 *
 * <p>O <code>ResultSet</code> deve já estar aberta na consulta quista, com
 * o cursor no estado de abertura.</p>
 *
 * <p>Depois que todos os registros são percorridos,
 * tanto o <code>ResultSet</code> quanto <code>Statement</code> dele são
 * fechados.</p>
 *
 * <p>A recuperação e instanciação dos objetos fica a cargo da classe que
 * herde desta. No método {@link next()}, que deve ser implementado, é
 * gerenciada toda a obtenção dos valores e movimentação do cursor, além de
 * disparar um {@link setEOF()} com valor <code>true</code>, caso não haja
 * mais registros. Para maiores informações, consulte {@link next()}</p>

 */
public abstract class AbstractDBIterator
    implements Iterator {
  private ResultSet rs;
  private ConnManager connManager;

  /**
   * Variável para uso interno para se armazenar se, incialmente, há dados
   * a serem retornados.
   */
  private boolean EOF = false;

  /**
   * <p>Construtor da classe. Caso a consulta já esteja vazia, já são
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
   * Caso eles já estejam sido fechados, é disparada exceção de erro de
   * execução.
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
   * Rertorna se ainda existem mais elementos. Esta função não precisa de
   * ser implementada, já que ela simplesmente retorna <code>!EOF</code> e
   * esta propriedade é modificada na função {@link next()}.
   *
   * @return <code>True</code> caso haja mais elementos.
   */
  public boolean hasNext() {
    return!EOF;
  }

  /**
   * <p>Função que retorna o elemento atual e coloca o cursor no próximo.
   * Deve ser implementada no que diz respeito à criação do objeto de
   * retorno, movimentação do cursor e, caso não haja mais registros,
   * deve-se chamar {@link setEOF(boolean)} com valor <code>true</code>.
   * Caso seja chamada já se tendo chamada a função citada, é disparada
   * uma exceção.</p>
   *
   *
   * @return Retorna apenas <code>null</code>, pois o retorno deve ser
   *         implementado e este método por si só não tem sentido.
   */
  public Object next() {
    if (EOF) {
      throw new NoSuchElementException();
    }
    return null;
  }

  /**
   * Operação não suportada.
   *
   * @throws UnsupportedOperationException
   */
  public void remove() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Testa se é fim de consulta. Este teste não é realizado diretamente
   * pelo <code>ResultSet</code>, mas sim pela propriedade protegida
   * <code>EOF</code>.
   *
   * @return EOF.
   */
  protected boolean isEOF() {
    return EOF;
  }

  /**
   * <p>Altera o valor do campo <code>EOF</code>. Esta função deve ser chamada
   * na implementação de {@link next()}.</p>
   *
   * <p>Caso se passe o valor <code>true</code>, os objetos de consulta são
   * fechados automaticamente. Caso os objetos já estejam fechados, é
   * disparada uma exceção.</p>
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
   * @return <code>ResultSet</code> associado à instância desta classe.
   */
  protected ResultSet getRs() {
    return rs;
  }
}
