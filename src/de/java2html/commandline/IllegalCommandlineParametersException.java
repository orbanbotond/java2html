package de.java2html.commandline;

/**
 * @author Markus Gebhard
 */
public class IllegalCommandlineParametersException extends Exception {
  public IllegalCommandlineParametersException(String message) {
    super(message);
  }
}