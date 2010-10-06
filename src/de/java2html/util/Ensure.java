package de.java2html.util;

/**
 * Provides convenient methods for checking contract parameters.
 */
public class Ensure {

  public Ensure() {
    super();
  }

  public static void ensureArgumentNotNull(String message, Object object) throws IllegalArgumentException {
    ensureTrue(message, object != null);
  }

  public static void ensureArgumentNotNull(Object object) throws IllegalArgumentException {
    ensureArgumentNotNull("Object must not be null", object); //$NON-NLS-1$
  }

  public static void ensureArgumentFalse(boolean state) throws IllegalArgumentException {
    ensureTrue("boolean must be false", !state); //$NON-NLS-1$
  }

  public static void ensureArgumentFalse(String message, boolean state) throws IllegalArgumentException {
    ensureTrue(message, !state);
  }

  public static void ensureArgumentTrue(boolean state) throws IllegalArgumentException {
    ensureTrue("boolean must be true", state); //$NON-NLS-1$
  }

  public static void ensureTrue(String message, boolean state) throws IllegalArgumentException {
    if (!state){
      throw new IllegalArgumentException(message);
    } 
  }
}