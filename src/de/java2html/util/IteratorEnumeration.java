package de.java2html.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Markus Gebhard
 */
public class IteratorEnumeration implements Enumeration {
  private Iterator iterator;

  public IteratorEnumeration(Iterator iterator) {
    this.iterator = iterator;
  }

  public boolean hasMoreElements() {
    return iterator.hasNext();
  }

  public Object nextElement() {
    return iterator.next();
  }
}
