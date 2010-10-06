package de.java2html.converter;

import de.java2html.util.Ensure;

public class ConverterMetaData {

  private final String defaultFileExtension;
  private final String printName;
  private final String name;

  public ConverterMetaData(String name, String printName, String defaultFileExtension) {
    Ensure.ensureArgumentNotNull(name);
    Ensure.ensureArgumentNotNull(printName);
    Ensure.ensureArgumentNotNull(defaultFileExtension);
    this.name = name;
    this.printName = printName;
    this.defaultFileExtension = defaultFileExtension;
  }

  /** Returns the default filename extension for the output format of this converter,
   * e.g. "html" or "tex". */
  public String getDefaultFileExtension() {
    return defaultFileExtension;
  }

  /** @return a String that can be used as logical name for this converter, i.e. for specifying this converter in an ant task. */
  public String getName() {
    return name;
  }

  /** @return a String that can be used in user interfaces as name for the converter. */
  public String getPrintName() {
    return printName;
  }
}