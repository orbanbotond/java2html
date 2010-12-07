package de.java2html.converter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.MessageFormat;

import de.java2html.Version;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceIterator;
import de.java2html.javasource.JavaSourceRun;
import de.java2html.javasource.JavaSourceType;
import de.java2html.options.HorizontalAlignment;
import de.java2html.options.IHorizontalAlignmentVisitor;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleEntry;
import de.java2html.options.JavaSourceStyleTable;
import de.java2html.util.HtmlUtilities;
import de.java2html.util.StringHolder;

/**
 * Algorithm and stuff for converting a
 * {@link de.java2html.javasource.JavaSource} object to to a HTML string
 * representation.
 *
 * The result is XHTML1.0 Transitional compliant.
 *
 * For questions, suggestions, bug-reports, enhancement-requests etc. I may be
 * contacted at: <a href="mailto:markus@jave.de">markus@jave.de</a>
 *
 * The Java2html home page is located at: <a href="http://www.java2html.de">
 * http://www.java2html.de</a>
 *
 * @author <a href="mailto:markus@jave.de">Markus Gebhard</a>
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
public class JavaSource2HTMLConverter extends AbstractJavaSourceConverter {
  /**
   * Flag indication whether html output contains a link to the
   * Java2Html-Homepage or not.
   *
   * @deprecated As of Jan 2, 2004 (Markus Gebhard) replaced by
   *             {@link de.java2html.options.JavaSourceConversionOptions#setShowJava2HtmlLink(boolean)}
   */
  @Deprecated
public static boolean java2HtmlHomepageLinkEnabled = false;

  /**
   * Site header for a html page. Is not used by this class, but can be used
   * from outside to add it to one or more converted
   */
  private final static String HTML_SITE_HEADER = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
      //     "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"
      // \"http://www.w3.org/TR/html4/loose.dtd\">\n"
      + "<html><head>\n"
      + "<title>{0}</title>\n"
      + "  <style type=\"text/css\">\n"
      + "    <!--code '{' font-family: Courier New, Courier; font-size: 10pt; margin: 0px; '}'-->\n"
      + "  </style>\n"
      + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\n"
      + "</head><body>\n";

  /**
   * Site footer for a html page. Is not used by this class, but can be used
   * from outside to add it to one or more converted
   */
  private final static String HTML_SITE_FOOTER = "\n</body></html>";

  /** Block seperator for between two blocks of converted source code */
  private final static String HTML_BLOCK_SEPARATOR = "<p />\n";

  /** HTML-Header for a block (!) of converted code */
  private final static String HTML_BLOCK_HEADER = "\n\n"
      + "<!-- ======================================================== -->\n"
      + "<!-- = Java Sourcecode to HTML automatically converted code = -->\n"
      + "<!-- =   "
      + Version.getJava2HtmlConverterTitle()
      + " "
      + Version.getBuildDate()
      + " by Markus Gebhard  markus@jave.de   = -->\n"
      + "<!-- =     Further information: http://www.java2html.de     = -->\n"
      + "<div align=\"{0}\" class=\"java\">\n"
      + "<table border=\"{1}\" cellpadding=\"3\" "
      + "cellspacing=\"0\" bgcolor=\"{2}\">\n";

  /** HTML-code for before the headline */
  private final static String HTML_HEAD_START = "  <!-- start headline -->\n"
      + "   <tr>\n"
      + "    <td colspan=\"2\">\n"
      + "     <center><font size=\"+2\">\n"
      + "      <code><b>\n";

  /** HTML-code for after the headline */
  private final static String HTML_HEAD_END = "      </b></code>\n"
      + "     </font></center>\n"
      + "    </td>\n"
      + "   </tr>\n"
      + "  <!-- end headline -->\n";

  /**
   * HTML-code for before the second column (contaning the converted source
   * code)
   */
  private final static String HTML_COL2_START = "<code>\n";

  /**
   * HTML-code for after the second column (contaning the converted source
   * code)
   */
  private final static String HTML_COL2_END = "</code>\n";

  /**
   * HTML-code for the bottom line (containing a little link to the
   * java2html-homepage). Can be disabled by setting
   * {@link #java2HtmlHomepageLinkEnabled}
   */
  private final static String HTML_LINK = "  <!-- start Java2Html link -->\n"
      + "   <tr>\n"
      + "    <td align=\"right\">\n"
      + "<small>\n"
      + "<a href=\"http://www.java2html.de\" target=\"_blank\">Java2html</a>\n"
      + "</small>\n"
      + "    </td>\n"
      + "   </tr>\n"
      + "  <!-- end Java2Html link -->\n";

  /** HTML-code for after the end of the block */
  private final static String HTML_BLOCK_FOOTER =
  //"\n"+ sieht schoener aus, wenn kein Zeilenumbruch mehr!
  "</table>\n"
      + "</div>\n"
      + "<!-- =       END of automatically generated HTML code       = -->\n"
      + "<!-- ======================================================== -->\n\n";

  /**
   * The html representation of the colors used for different source
   */

  private int lineCifferCount;

  public JavaSource2HTMLConverter() {
    super(new ConverterMetaData("html", "XHTML 1.0 Transitional (inlined fonts)", "html"));
  }

  @Override
public String getDocumentHeader(JavaSourceConversionOptions options, String title) {
    if (title == null) {
      title = ""; //$NON-NLS-1$
    }
    return MessageFormat.format(HTML_SITE_HEADER, new Object[]{ title });
  }

  @Override
public String getDocumentFooter(JavaSourceConversionOptions options) {
    return HTML_SITE_FOOTER;
  }

  @Override
public String getBlockSeparator(JavaSourceConversionOptions options) {
    return HTML_BLOCK_SEPARATOR;
  }

  @Override
public void convert(JavaSource source, JavaSourceConversionOptions options, BufferedWriter writer)
      throws IOException {
    if (source == null) {
      throw new IllegalStateException("Trying to write out converted code without having source set.");
    }
    writeSourceCode(source, options, writer);
  }

  private String getHtmlAlignValue(HorizontalAlignment alignment) {
    final StringHolder stringHolder = new StringHolder();
    alignment.accept(new IHorizontalAlignmentVisitor() {
      public void visitLeftAlignment(HorizontalAlignment horizontalAlignment) {
        stringHolder.setValue("left");
      }

      public void visitRightAlignment(HorizontalAlignment horizontalAlignment) {
        stringHolder.setValue("right");
      }

      public void visitCenterAlignment(HorizontalAlignment horizontalAlignment) {
        stringHolder.setValue("center");
      }
    });
    return stringHolder.getValue();
  }

  private void writeFileName(JavaSource source, BufferedWriter writer) throws IOException {
    writer.write(HTML_HEAD_START);
    writer.write(source.getFileName());
    writer.newLine();
    writer.write(HTML_HEAD_END);
  }

  private void writeSourceCode(JavaSource source, JavaSourceConversionOptions options, BufferedWriter writer)
      throws IOException {
    writer.write(HTML_COL2_START);

    lineCifferCount = String.valueOf(source.getLineCount()).length();

    JavaSourceIterator iterator = source.getIterator();
    int lineNumber = 1;
    while (iterator.hasNext()) {
      JavaSourceRun run = iterator.getNext();

      if (run.isAtStartOfLine()) {
        if (options.isAddLineAnchors()) {
          writeLineAnchorStart(options, writer, lineNumber);
        }
        if (options.isShowLineNumbers()) {
          writeLineNumber(options, writer, lineNumber);
        }
        if (options.isAddLineAnchors()) {
          writeLineAnchorEnd(writer);
        }
        lineNumber++;
      }

      toHTML(options.getStyleTable(), run, writer);
      if (run.isAtEndOfLine() && iterator.hasNext()) {
        writer.write("<br />");
        writer.newLine();
      }
    }
    writer.write(HTML_COL2_END);
  }

  private void writeLineAnchorEnd(BufferedWriter writer) throws IOException {
    writer.write("</a>");
  }

  private void writeLineAnchorStart(JavaSourceConversionOptions options, BufferedWriter writer, int lineNumber)
      throws IOException {
    writer.write("<a name=\"");
    writer.write(options.getLineAnchorPrefix() + lineNumber);
    writer.write("\">");
  }

  private void writeLineNumber(JavaSourceConversionOptions options, BufferedWriter writer, int lineNo)
      throws IOException {
    JavaSourceStyleEntry styleEntry = options.getStyleTable().get(JavaSourceType.LINE_NUMBERS);
    writeStyleStart(writer, styleEntry, null);

    String lineNumber = String.valueOf(lineNo);
    int cifferCount = lineCifferCount - lineNumber.length();
    while (cifferCount > 0) {
      writer.write('0');
      --cifferCount;
    }

    writer.write(lineNumber);
    writeStyleEnd(writer, styleEntry);
    writer.write("&nbsp;");
  }

  private void toHTML(JavaSourceStyleTable styleTable, JavaSourceRun run, BufferedWriter writer)
      throws IOException {
    //  result.append(htmlColors[sourceTypes[start]]);
    JavaSourceType sourceType = run.getType();
    JavaSourceStyleEntry style = styleTable.get(sourceType);

    writeStyleStart(writer, style, sourceType.getName());

    String t = HtmlUtilities.encode(run.getCode(), "\n ");

    for (int i = 0; i < t.length(); ++i) {
      char ch = t.charAt(i);
      if (ch == ' ') {
        writer.write("&nbsp;");
      }
      else {
        writer.write(ch);
      }
    }

    writeStyleEnd(writer, style);
  }

  private void writeStyleStart(BufferedWriter writer, JavaSourceStyleEntry style, String name) throws IOException {
      if(null != name){
          writer.write("<font class=\"" + name.toLowerCase().replaceAll(" ", "_") + "\" >");
      }else{
          writer.write("<font color=\"" + style.getHtmlColor() + "\" >");
      }

    if (style.isBold()) {
      writer.write("<b>");
    }
    if (style.isItalic()) {
      writer.write("<i>");
    }
  }

  private void writeStyleEnd(BufferedWriter writer, JavaSourceStyleEntry style) throws IOException {
    if (style.isItalic()) {
      writer.write("</i>");
    }
    if (style.isBold()) {
      writer.write("</b>");
    }
    writer.write("</font>");
  }
}