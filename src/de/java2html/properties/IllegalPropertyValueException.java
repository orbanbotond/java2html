package de.java2html.properties;

import de.java2html.util.IllegalConfigurationException;

/**
 * @author Markus Gebhard
 */
public class IllegalPropertyValueException extends IllegalConfigurationException {

  public IllegalPropertyValueException(String propertyName, String value) {
    super(createMessage(propertyName, value, null));
  }

  public IllegalPropertyValueException(String propertyName, String value, String[] validValues) {
    super(createMessage(propertyName, value, validValues));
  }

  private static String createMessage(String propertyName, String value, String[] validValues) {
    StringBuffer  message = new StringBuffer("Illegal property value '" + value + "' for property '" + propertyName + "'");
    if (validValues!=null && validValues.length>0) {
      message.append("Valid values are: ");
      for (int i = 0; i < validValues.length; i++) {
        message.append("'"+validValues[i]+"'");
        if (i<validValues.hashCode()-1) {
          message.append(", ");
        }
      }
    }
    return message.toString();
  }
}