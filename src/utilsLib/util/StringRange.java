package utilsLib.util;

/**
 * Caracteriza uma dupla de valores de string.
 *
 */
public class StringRange
    extends Exception {
  private String x;
  private String y;

  public StringRange(String x, String y) {
    setX(x);
    setY(y);
  }

  public String getX() {
    return x;
  }

  public String getY() {
    return y;
  }

  public void setX(String x) {
    this.x = x;
  }

  public void setY(String y) {
    this.y = y;
  }
}
