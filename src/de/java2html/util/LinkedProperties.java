package de.java2html.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * A properties implementation that remembers the order of its entries.
 * 
 * @author Markus Gebhard
 */
public class LinkedProperties extends Properties {
  private LinkedHashMap map = new LinkedHashMap();

  public synchronized Object put(Object key, Object value) {
    return map.put(key, value);
  }

  public synchronized Object get(Object key) {
    return map.get(key);
  }

  public synchronized void clear() {
    map.clear();
  }

  public synchronized Object clone() {
    throw new UnsupportedOperationException();
  }

  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  public synchronized boolean contains(Object value) {
    return containsValue(value);
  }

  public synchronized boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  public synchronized Enumeration elements() {
    return new IteratorEnumeration(map.values().iterator());    
  }

  public Set entrySet() {
    return map.entrySet();
  }

  public synchronized boolean equals(Object o) {
    throw new UnsupportedOperationException();
  }

  public synchronized boolean isEmpty() {
    return map.isEmpty();
  }

  public synchronized Enumeration keys() {
    return new IteratorEnumeration(map.keySet().iterator());
  }

  public Set keySet() {
    return map.keySet();
  }

  public Enumeration propertyNames() {
    throw new UnsupportedOperationException();
  }

  public synchronized void putAll(Map t) {
    map.putAll(t);
  }
  public synchronized Object remove(Object key) {
    return map.remove(key);
  }

  public synchronized int size() {
    return map.size();
  }

  public Collection values() {
    throw new UnsupportedOperationException();
  }
  
  public String getProperty(String key) {
    Object oval = get(key);
    String sval = (oval instanceof String) ? (String)oval : null;
    return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
  }
}