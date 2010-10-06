package de.java2html.properties;

import java.util.Properties;

import de.java2html.javasource.JavaSourceType;
import de.java2html.options.IConversionOptionsConstants;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleEntry;
import de.java2html.options.JavaSourceStyleTable;
import de.java2html.util.LinkedProperties;
import de.java2html.util.RGB;

/**
 * @author Markus
 */
public class ConversionOptionsPropertiesWriter {

  public Properties write(JavaSourceConversionOptions options) {
    Properties properties = new LinkedProperties();
    properties.setProperty(IConversionOptionsConstants.DEFAULT_STYLE_NAME, options.getStyleTable().getName());
    properties.setProperty(IConversionOptionsConstants.SHOW_FILE_NAME, String.valueOf(options.isShowFileName()));
    properties.setProperty(
      IConversionOptionsConstants.SHOW_TABLE_BORDER,
      String.valueOf(options.isShowTableBorder()));
    properties.setProperty(
      IConversionOptionsConstants.SHOW_LINE_NUMBERS,
      String.valueOf(options.isShowLineNumbers()));
    properties.setProperty(
      IConversionOptionsConstants.SHOW_JAVA2HTML_LINK,
      String.valueOf(options.isShowJava2HtmlLink()));
    properties.setProperty(
      IConversionOptionsConstants.HORIZONTAL_ALIGNMENT,
      options.getHorizontalAlignment().getName());
    properties.setProperty(IConversionOptionsConstants.TAB_SIZE, String.valueOf(options.getTabSize()));

    addStyleEntries(properties, options.getStyleTable());
    return properties;
  }

  private void addStyleEntries(Properties properties, JavaSourceStyleTable table) {
    JavaSourceType[] sourceTypes = JavaSourceType.getAll();
    for (int i = 0; i < sourceTypes.length; i++) {
      JavaSourceType type = sourceTypes[i];
      JavaSourceStyleEntry entry = table.get(type);
      properties.setProperty(
        type.getName() + IConversionOptionsConstants.POSTFIX_COLOR,
        getRgbString(entry.getColor()));
      properties.setProperty(
        type.getName() + IConversionOptionsConstants.POSTFIX_BOLD,
        String.valueOf(entry.isBold()));
      properties.setProperty(
        type.getName() + IConversionOptionsConstants.POSTFIX_ITALIC,
        String.valueOf(entry.isItalic()));
    }
  }

  private String getRgbString(RGB color) {
    return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
  }
}