package de.java2html.converter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import de.java2html.Version;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceIterator;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.javasource.JavaSourceRun;
import de.java2html.javasource.JavaSourceType;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleTable;
import de.java2html.util.RGB;

/**
 * Algorithm and stuff for converting a
 * {@link de.java2html.javasource.JavaSource} object to to a TeX string
 * representation (experimental!).
 * 
 * For questions, suggestions, bug-reports, enhancement-requests etc. I may be
 * contacted at: <a href="mailto:markus@jave.de">markus@jave.de</a>
 * 
 * The Java2html home page is located at: <a href="http://www.java2html.de">
 * http://www.java2html.de</a>
 * 
 * @author <a href="mailto:markus@jave.de">Markus Gebhard</a>
 * @version 2.0, 05/07/02
 * 
 * Copyright (C) Markus Gebhard 2000-2002
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
public class JavaSource2TeXConverter extends AbstractJavaSourceConverter {
  private static String[] texFormats;
  static {
    JavaSourceType[] allTypes = JavaSourceType.getAll();
    texFormats = new String[allTypes.length];
    for (int i = 0; i < allTypes.length; ++i) {
      texFormats[i] = "\\jttstyle" + (char) ('a' + i) + " ";
    }
  }

  private String createFormatDefinition(JavaSourceStyleTable styleTable) {
    StringBuffer sb = new StringBuffer();
    sb.append("%Java2TeX style definitions\n");
    sb.append("%You can modify them to fit your needs\n");

    JavaSourceType[] allTypes = JavaSourceType.getAll();
    for (int i = 0; i < allTypes.length; ++i) {
      sb.append("\\newcommand{\\jttstyle");
      sb.append((char) ('a' + i));
      sb.append("}{\\color[rgb]{");

      RGB color = styleTable.get(allTypes[i]).getColor();
      float[] cs = new float[]{ color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, };
      sb.append(floatToCharArray(cs[0]));
      sb.append(',');
      sb.append(floatToCharArray(cs[1]));
      sb.append(',');
      sb.append(floatToCharArray(cs[2]));

      sb.append("}} %");
      sb.append(allTypes[i].getName());
      sb.append('\n');

    }
    sb.append('\n');
    return sb.toString();
  }

  /** Document header */
  protected final static String DOCUMENT_HEADER = "\\documentclass[11pt,a4paper]{article}\n"
      + "\n"
      + "\\usepackage{color}\n"
      + "\n"
      + "\\begin{document}\n"
      + "\n";

  /** Document header */
  protected final static String DOCUMENT_FOOTER = "\\end{document}";

  /** Block seperator for between two blocks of converted source code */
  protected final static String DOCUMENT_BLOCK_SEPARATOR = "\n\n";

  protected final static String BLOCK_HEADER = "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n"
      + "%  Java Sourcecode to TeX automatically converted code\n"
      + "%  "
      + Version.getJava2HtmlConverterTitle()
      + " "
      + Version.getBuildDate()
      + "by Markus Gebhard  markus@jave.de\n"
      + "%     Further information: http://www.java2html.de\n";

  public JavaSource2TeXConverter() {
    super(new ConverterMetaData("tex", "TeX", "tex"));
  }

  protected final static char[] floatToCharArray(float f) {
    if (f >= 1.0) {
      return new char[]{ '1', '.', '0', '0' };
    }

    return new char[]{ '.', (char) ('0' + f * 10), (char) ('0' + f * 100 % 10), (char) ('0' + f * 1000 % 10) };
  }

  public String getDocumentHeader(JavaSourceConversionOptions options, String title) {
    return DOCUMENT_HEADER + createFormatDefinition(options.getStyleTable());
  }

  public String getDocumentFooter(JavaSourceConversionOptions options) {
    return DOCUMENT_FOOTER;
  }

  public String getBlockSeparator(JavaSourceConversionOptions options) {
    return DOCUMENT_BLOCK_SEPARATOR;
  }

  /**
   * Converts the parsed source code to HTML by adding color information,
   * adding line breaks and replacing characters as needed for HTML. Also adds
   * a table with line numbers etc.
   */
  public void convert(JavaSource source, JavaSourceConversionOptions options, BufferedWriter writer)
      throws IOException {
    if (source == null) {
      throw new IllegalStateException("Trying to write out converted code without having source set.");
    }

    writer.write(BLOCK_HEADER);

    //1) Header with filename if available
    if (options.isShowFileName() && source.getFileName() != null) {
      //TODO: Pretty print file name
    }

    writer.write("{");
    writer.newLine();
    writer.write("\\noindent \\ttfamily");
    writer.newLine();

    int lineCount = source.getLineCount();
    int lineNumber = 1;
    JavaSourceIterator iterator = source.getIterator();
    while (iterator.hasNext()) {
      JavaSourceRun run = iterator.getNext();
      if (run.isAtStartOfLine() && options.isShowLineNumbers()) {
        writeLineNumber(writer, lineNumber++, lineCount);
      }
      toTeX(run, writer);
      if (run.isAtEndOfLine()) {
        writer.write("\\\\");
        writer.newLine();
      }
    }

    writer.newLine();
    writer.write("}");
    writer.newLine();
  }

  public void writeLineNumber(BufferedWriter writer, int lineNumber, int lineCount) throws IOException {
    writer.write(texFormats[JavaSourceType.LINE_NUMBERS.getID()]);
    writer.write(leftSpace(lineNumber, lineCount));
    writer.write(String.valueOf(lineNumber));
    writer.write('~');
  }

  protected void toTeX(JavaSourceRun run, BufferedWriter writer) throws IOException {
    writer.write(texFormats[run.getType().getID()]);

    //Replace white space by non-breaking space and line breaks by \\
    //Also enclose special characters in \\verb# #
    String text = run.getCode();
    for (int i = 0; i < text.length(); ++i) {
      char ch = text.charAt(i);
      if (ch == ' ')
        writer.write('~');
      else if (ch == '_'
          || ch == '\\'
          || ch == '^'
          || ch == '~'
          || ch == '\"'
          || ch == '|'
          || ch == '<'
          || ch == '>'
          || ch == '*')
        writer.write("\\verb#" + ch + "#");
      else if (ch == '{' || ch == '}' || ch == '_' || ch == '&' || ch == '%' || ch == '$' || ch == '#') {
        writer.write("\\" + ch);
      }
      else {
        writer.write(ch);
      }
    }
  }

  protected final static String[] WHITESPACES = { "", "~", "~~", "~~~", "~~~~", };

  protected final static String whiteSpace(int size) {
    if (size < WHITESPACES.length)
      return WHITESPACES[size];

    char[] result = new char[size];
    while (size > 0)
      result[--size] = '~';

    return new String(result);
  }

  protected final static String leftSpace(int num, int max) {
    int count = (int) (Math.log(max) / Math.log(10)) - (int) (Math.log(num) / Math.log(10));

    return whiteSpace(count);
  }

  //TODO Sep 13, 2004 (Markus Gebhard): Convert this into JDemo demos
  public static void main(String args[]) throws IOException {
    long time0 = System.currentTimeMillis();
    JavaSource source = (new JavaSourceParser()).parse(new java.io.File("JavaSourceParser.java"));

    long time1 = System.currentTimeMillis();
    JavaSource2TeXConverter conn1 = new JavaSource2TeXConverter();
    conn1.convert(source, JavaSourceConversionOptions.getDefault(), new StringWriter());
    long time2 = System.currentTimeMillis();
    JavaSource2HTMLConverter conn2 = new JavaSource2HTMLConverter();
    conn2.convert(source, JavaSourceConversionOptions.getDefault(), new StringWriter());
    long time3 = System.currentTimeMillis();

    System.out.println("Parse:  " + (time1 - time0) + "ms");
    System.out.println("toTeX:  " + (time2 - time1) + "ms");
    System.out.println("toHTML: " + (time3 - time2) + "ms");
  }
}