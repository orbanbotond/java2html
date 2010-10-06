package de.java2html.converter;

import java.io.BufferedWriter;
import java.io.IOException;

import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceType;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleTable;

/**
 * Algorithm and stuff for converting a {@link de.java2html.javasource.JavaSource} object to to a 
 * XML or XHTML representation (experimental!).
 * 
 * @author <a href="mailto:Jan.Tisje@gmx.de">Jan Tisje</a>
 * @version 1.0
 */
public class JavaSource2XmlConverter extends AbstractJavaSourceToXmlConverter {

  protected String createHeader(JavaSourceStyleTable styleTable, String title) {
    return XML_HEADER
        + "<"
        + BLOCK_ROOT
        + "<"
        + BLOCK_STYLE
        + createStyleSheet(styleTable)
        + "</"
        + BLOCK_STYLE
        + "\n";
  }

  private final static String BLOCK_LINE_NUMBERS = "lines>";
  private final static String BLOCK_STYLE = "style>";
  private final static String BLOCK_JAVA = "source>";
  private final static String BLOCK_ROOT = "java>";

  public JavaSource2XmlConverter() {
    super(new ConverterMetaData("xml", "XML", "xml"));
  }

  public String getName() {
    return "xml"; //$NON-NLS-1$
  }

  protected String getHeaderEnd() {
    return "";
  }

  protected String getFooter() {
    return "</" + BLOCK_ROOT;
  }

  public void convert(JavaSource source, JavaSourceConversionOptions options, BufferedWriter writer)
      throws IOException {
    if (source == null) {
      throw new IllegalStateException("Trying to write out converted code without having source set.");
    }

    String sourceCode = source.getCode();
    JavaSourceType[] sourceTypes = source.getClassification();

    if (lineNumbers) {
      writer.write("<" + BLOCK_LINE_NUMBERS);
      for (int i = 1; i <= source.getLineCount(); i++) {
        writer.write(String.valueOf(i) + lineEnd);
        writer.newLine();
      }
      writer.write("</" + BLOCK_LINE_NUMBERS);
    }

    writer.write("<" + BLOCK_JAVA);

    int start = 0;
    int end = 0;
    while (start < sourceTypes.length) {
      while (end < sourceTypes.length - 1
          && (sourceTypes[end + 1] == sourceTypes[start] || sourceTypes[end + 1] == JavaSourceType.BACKGROUND)) {
        ++end;
      }
      toXml(sourceCode, sourceTypes, start, end, writer);
      start = end + 1;
      end = start;
    }

    writer.write("</" + BLOCK_JAVA);
  }
}