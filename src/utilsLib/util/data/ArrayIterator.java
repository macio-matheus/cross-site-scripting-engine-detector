package utilsLib.util.data;

import java.util.*;

public class ArrayIterator
    implements Iterator {
  private Object[] objs;
  private int i = 0;

  public ArrayIterator(Object[] objs) {
    if (objs == null) {
      throw new IllegalArgumentException();
    }

    this.objs = objs;
    this.i = 0;
  }

  public boolean hasNext() {
    return (i < objs.length);
  }

  public Object next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    Object oReturn = objs[i];
    i++;
    return oReturn;
  }

  public void remove() {
    throw new java.lang.UnsupportedOperationException();
  }
}
