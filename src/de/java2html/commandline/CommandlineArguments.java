package de.java2html.commandline;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Markus Gebhard
 */
public class CommandlineArguments {
  private Map valueByParameterName = new HashMap();
  private Set flags = new HashSet();

  public CommandlineArguments(String[] args) throws IllegalCommandlineParametersException {
    for (int i = 0; i < args.length; i++) {
      if (isValidFlagName(args[i])) {
        String flagName = args[i].substring(1).toLowerCase();
        String value = null;
        if (i + 1 < args.length) {
          if (!isValidFlagName(args[i + 1])) {
            value = args[i + 1];
            ++i;
          }
        }
        if (flags.contains(flagName) || valueByParameterName.containsKey(flagName)) {
          throw new IllegalCommandlineParametersException("Argument '"
              + flagName
              + "' has been defined more often than once.");
        }
        if (value == null) {
          flags.add(flagName);
        }
        else {
          valueByParameterName.put(flagName, value);
        }
      }
      else {
        throw new IllegalCommandlineParametersException("Unexpected parameter '" + args[i] + "'");
      }
    }
  }

  private boolean isValidFlagName(String name) {
    if (!name.startsWith("-")) {
      return false;
    }
    try {
      Double.parseDouble(name);
      return false;
    }
    catch (NumberFormatException e) {
      return true;
    }
  }

  public String getParameterStringValue(String parameterName) {
    return (String) valueByParameterName.get(parameterName.toLowerCase());
  }

  public String getParameterStringValue(String parameterName, String defaultValue) {
    String value = getParameterStringValue(parameterName);
    return value != null ? value : defaultValue;
  }

  public int getParameterPositiveIntValue(String parameterName, int defaultValue)
      throws IllegalCommandlineParametersException {
    String value = getParameterStringValue(parameterName);
    if (value == null) {
      return defaultValue;
    }

    int i = parseInt(value);
    if (i < 0) {
      throw new IllegalCommandlineParametersException(
          "Positive value for int argument expected, was '" + value + "'");
    }
    return i;
  }

  private int parseInt(String value) throws IllegalCommandlineParametersException {
    try {
      return Integer.parseInt(value);
    }
    catch (NumberFormatException e) {
      throw new IllegalCommandlineParametersException("Illegal format for a int value '"
          + value
          + "'");
    }
  }

  public boolean isFlagSet(String flagName) {
    return flags.contains(flagName.toLowerCase());
  }

  public boolean isParameterSet(String flagName) {
    return valueByParameterName.containsKey(flagName.toLowerCase());
  }

  public String getRequiredParameterStringValue(String parameterName)
      throws IllegalCommandlineParametersException {
    String value = getParameterStringValue(parameterName);
    if (value == null) {
      throw new IllegalCommandlineParametersException("Missing required parameter value for '"
          + parameterName
          + "'");
    }
    return value;
  }

  public void assertContainsNoUnsupportedFlags(String[] supportedFlagNames)
      throws IllegalCommandlineParametersException {
    Set supportedFlagNameSet = createLowerCaseNameSet(supportedFlagNames);
    Iterator iterator = flags.iterator();
    while (iterator.hasNext()) {
      String flagName = (String) iterator.next();
      if (!supportedFlagNameSet.contains(flagName)) {
        throw new IllegalCommandlineParametersException("The flag '"
            + flagName
            + "' is not supported (or missing required parameter value).");
      }
    }
  }

  public void assertContainsNoUnsupportedParameters(String[] supportedParameterNames)
      throws IllegalCommandlineParametersException {
    Set supportedParameterNameSet = createLowerCaseNameSet(supportedParameterNames);
    Iterator iterator = valueByParameterName.keySet().iterator();
    while (iterator.hasNext()) {
      String flagName = (String) iterator.next();
      if (!supportedParameterNameSet.contains(flagName)) {
        throw new IllegalCommandlineParametersException("The parameter '"
            + flagName
            + "' is not supported.");
      }
    }
  }

  private Set createLowerCaseNameSet(String[] names) {
    Set supportedFlagNameSet = new HashSet();
    for (int i = 0; i < names.length; i++) {
      supportedFlagNameSet.add(names[i].toLowerCase());
    }
    return supportedFlagNameSet;
  }
}