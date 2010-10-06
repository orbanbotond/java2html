package de.java2html.converter;

import java.io.BufferedWriter;
import java.io.IOException;

import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceType;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleTable;

/**
 * @author Markus Gebhard
 */
public class JavaSource2XhtmlConverter extends AbstractJavaSourceToXmlConverter {

  private final static String XHTML_HEADER_END = "</head>\n<body>\n";
  private final static String XHTML_FOOTER = "</body>\n</html>";

  public JavaSource2XhtmlConverter() {
    super(new ConverterMetaData("xhtml", "XHTML 1.0 transitional (with stylesheet)", "xhtml"));
  }

  protected String getHeaderEnd() {
    return XHTML_HEADER_END;
  }

  protected String getFooter() {
    return XHTML_FOOTER;
  }

  protected String createHeader(JavaSourceStyleTable styleTable, String title) {
    return XML_HEADER + createHeaderStart(styleTable, title);
  }

  private String createHeaderStart(JavaSourceStyleTable styleTable, String title) {
    if (title == null) {
      title = ""; //$NON-NLS-1$
    }
    return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
        + "<head>\n<style>\n"
        + createStyleSheet(styleTable)
        + "</style>\n"
        + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
        + "<title>"
        + title
        + "</title>\n";
  }

  public void convert(JavaSource source, JavaSourceConversionOptions options, BufferedWriter writer)
      throws IOException {
    if (source == null) {
      throw new IllegalStateException("Trying to write out converted code without having source set.");
    }

    String sourceCode = source.getCode();
    JavaSourceType[] sourceTypes = source.getClassification();

    if (lineNumbers) {
      writer.write("<table class=\"java\">\n<tr class=\"java\">\n<td valign=\"top\" class=\"java-ln\">");
      writer.newLine();

      if (pre) {
        writer.write("<pre class=\"java-ln\">");
      }
      else {
        writer.write("<tt class=\"java-ln\">");
      }
      for (int i = 1; i <= source.getLineCount(); i++) {
        writer.write(String.valueOf(i) + lineEnd);
        writer.newLine();
      }
      //      sb.append(new Integer(source.getLineCount()).toString());

      if (pre) {
        writer.write("</pre>\n");
      }
      else {
        writer.write("</tt>\n");
      }
      writer.write("</td>\n");
      writer.write("<td valign=\"top\" class=\"java\">");
      writer.newLine();
    }

    else if (pre) {
      writer.write("<pre class=\"java\">");
    }
    else {
      writer.write("<tt class=\"java\">");
    }

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

    if (pre) {
      writer.write("</pre>");
    }
    else {
      writer.write("</tt>");
    }

    if (lineNumbers) {
      writer.write("</td>\n</tr>\n</table>");
      writer.newLine();
    }
  }
}