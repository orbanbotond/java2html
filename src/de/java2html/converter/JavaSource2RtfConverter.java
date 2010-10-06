package de.java2html.converter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceIterator;
import de.java2html.javasource.JavaSourceRun;
import de.java2html.javasource.JavaSourceType;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleEntry;
import de.java2html.options.JavaSourceStyleTable;
import de.java2html.util.RGB;

/**
 * @author Markus Gebhard
 */
public class JavaSource2RtfConverter extends AbstractJavaSourceConverter {

  public JavaSource2RtfConverter() {
    super(new ConverterMetaData("rtf", "RTF (Rich Text Format)", "rtf"));
  }

  private final static int FONT_SIZE = 9;
  private final static String FONT_NAME = "Courier New";

  public String getDocumentHeader(JavaSourceConversionOptions options, String title) {
    return "";
  }

  public String getDocumentFooter(JavaSourceConversionOptions options) {
    return "";
  }

  public String getBlockSeparator(JavaSourceConversionOptions options) {
    return "";
  }

  public void convert(JavaSource source, JavaSourceConversionOptions options, BufferedWriter writer)
      throws IOException {
    writer.write("{\\rtf1\\ansi\\deff0{\\fonttbl{");
    writer.write("\\f0\\fmodern ");
    writer.write(FONT_NAME);
    writer.write(";");
    writer.write("}");
    writer.write("}\n");

    writer.write("{\\colortbl");
    JavaSourceType[] types = JavaSourceType.getAll();
    for (int i = 0; i < types.length; ++i) {
      JavaSourceStyleEntry style = options.getStyleTable().get(types[i]);
      writeColor(style.getColor(), writer);
    }
    writer.write("}\n");

    writer.write("\\deflang1031\\pard\\plain\\f0");
    writer.write("\\fs" + FONT_SIZE * 2);
    writer.write("\\cb" + JavaSourceType.BACKGROUND.getID());

    int lineCifferCount = String.valueOf(source.getLineCount()).length();
    JavaSourceIterator iterator = source.getIterator();
    int lineNumber = 1;
    while (iterator.hasNext()) {
      JavaSourceRun run = iterator.getNext();

      if (run.isAtStartOfLine()) {
        if (lineNumber > 1) {
          writer.newLine();
          writer.write("\\par ");
        }

        if (options.isShowLineNumbers()) {
          writeLineNumber(writer, lineCifferCount, lineNumber, options);
        }
        ++lineNumber;
      }
      writeText(options, run, writer);
    }

    writer.write("}");
  }

  private void writeText(JavaSourceConversionOptions options, JavaSourceRun run, BufferedWriter writer)
      throws IOException {
    writeText(options.getStyleTable(), writer, run.getCode(), run.getType());
  }

  private void writeText(JavaSourceStyleTable styleTable, BufferedWriter writer, String text, JavaSourceType type)
      throws IOException {
    JavaSourceStyleEntry style = styleTable.get(type);

    writer.write("{\\f0");
    if (style.isBold()) {
      writer.write("\\b");
    }
    if (style.isItalic()) {
      writer.write("\\i");
    }

    writer.write("\\cf" + type.getID() + " ");

    for (int i = 0; i < text.length(); ++i) {
      char ch = text.charAt(i);
      if (ch == '\\') {
        writer.write("\\\\");
      }
      else if (ch == '{') {
        writer.write("\\{");
      }
      else if (ch == '}') {
        writer.write("\\}");
      }
      else {
        writer.write(ch);
      }
    }

    writer.write("}");
  }

  private void writeLineNumber(
      BufferedWriter writer,
      int lineCifferCount,
      int lineNumber,
      JavaSourceConversionOptions options) throws IOException {

    String text = String.valueOf(lineNumber);
    int cifferCount = lineCifferCount - text.length();
    while (cifferCount > 0) {
      text = '0' + text;
      --cifferCount;
    }
    text = text + " ";

    writeText(options.getStyleTable(), writer, text, JavaSourceType.LINE_NUMBERS);
  }

  private static void writeColor(RGB color, Writer writer) throws IOException {
    writer.write("\\red" + color.getRed()); //$NON-NLS-1$
    writer.write("\\green" + color.getGreen()); //$NON-NLS-1$
    writer.write("\\blue" + color.getBlue()); //$NON-NLS-1$
    writer.write(";"); //$NON-NLS-1$
  }
}