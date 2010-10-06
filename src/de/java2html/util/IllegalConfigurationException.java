package de.java2html.util;


/**
 * @author Markus Gebhard
 */
public class IllegalConfigurationException extends RuntimeException {

  public IllegalConfigurationException(String message) {
    super(message);
  }

  public IllegalConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}