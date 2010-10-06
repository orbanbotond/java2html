package de.java2html.options;

/**
 * @author Markus Gebhard
 */
public class ConversionOptionsUtilities {
  private ConversionOptionsUtilities() {
    //nothing to do
  }

  public static String[] getPredefinedStyleTableNames() {
    JavaSourceStyleTable[] tables = JavaSourceStyleTable.getPredefinedTables();
    String[] names = new String[tables.length];
    for (int i = 0; i < tables.length; i++) {
      names[i] = tables[i].getName();
    }
    return names;
  }

  public static String getPredefinedStyleTableNameString() {
    String[] names = getPredefinedStyleTableNames();
    return ConversionOptionsUtilities.getCommaSeparatedString(names);
  }

  public static String[] getAvailableHorizontalAlignmentNames() {
    HorizontalAlignment[] tables = HorizontalAlignment.getAll();
    String[] names = new String[tables.length];
    for (int i = 0; i < tables.length; i++) {
      names[i] = tables[i].getName();
    }
    return names;
  }

  public static String getAvailableHorizontalAlignmentNameString() {
    String[] names = getAvailableHorizontalAlignmentNames();
    return ConversionOptionsUtilities.getCommaSeparatedString(names);
  }

  private static String getCommaSeparatedString(String[] names) {
    return getSeparatedString(names, ", ");
  }

  public static String getSeparatedString(String[] strings, String separator) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < strings.length; i++) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(strings[i]);
    }
    return sb.toString();
  }
}