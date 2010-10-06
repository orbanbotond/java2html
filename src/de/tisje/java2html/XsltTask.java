package de.tisje.java2html;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import de.java2html.converter.AbstractJavaSourceToXmlConverter;
import de.java2html.converter.JavaSource2XhtmlConverter;
import de.java2html.converter.JavaSource2XmlConverter;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.util.IoUtilities;

/**
 * This class is an interface between XSL and Java2Html.
 * Before invoking, a namespace def must be added to the <code>xsl:stylesheet</code> tag:<br>
 * <code>xmlns:j2h="de.tisje.java2html.XsltTask"</code><br>
 * After that, it may be used this way:
 * <pre>
 *    &lt;xsl:value-of select="j2h:setSource(.)"/>
 *    &lt;xsl:value-of select="j2h:writeFile('temp.xml')"/>
 *    &lt;xsl:copy-of select="document('temp.xml')"/>
 * </pre>
 *
 * @author <a href="mailto:Jan.Tisje@gmx.de">Jan Tisje</a>
 * @version 1.0
 */
//TODO Mar 11, 2004 (Markus Gebhard): This class urgently needs refactoring. Static instance variables - arg...
public class XsltTask {

  private static JavaSourceConversionOptions options = JavaSourceConversionOptions.getDefault();
  private static AbstractJavaSourceToXmlConverter converter = new JavaSource2XhtmlConverter();
  private static JavaSource source = null;
  private static boolean xhtml = false;

  /**
   * set options from xsl.
   * @param lineNumbers if line numbers should be in the output code.
   * @param pre if output code should be formatted using non-breaking spaces and &lt;br>.
   * @param xhtml if output should be viewable stand-alone.
   */
  public static void setOptions(boolean lineNumbers, boolean pre, boolean xhtml) {
    if (xhtml) {
      converter = new JavaSource2XhtmlConverter();
    }else {
      converter = new JavaSource2XmlConverter();
    }
    converter.setOptions(lineNumbers, pre);
    XsltTask.xhtml = xhtml;
  }

  /** hand over java source read from main xml file. */
  public static void setSource(String javaSource) {
    source = new JavaSourceParser().parse(javaSource);
  }

  /** read java source from file. */
  public static void readFile(String javaFile) throws IOException {
    source = new JavaSourceParser().parse(new File(javaFile));
  }

  /** return java source in text form, html codes will be escaped. */
  public static String getSource() throws IOException {
    StringWriter writer = new StringWriter();
    writer.write(converter.getDocumentHeader(options, "")); //$NON-NLS-1$
    converter.convert(source, options, writer);
    writer.write(converter.getDocumentFooter(options));
    return writer.getBuffer().toString();
  }

  /** output file to a separate xml file, less problems. 
   * @deprecated As of Mar 11, 2004 (Markus Gebhard), replaced by {@link #writeFile(File)}*/
  public static void writeFile(String filename) throws IOException {
    writeFile(new File(filename));
  }

  /** output file to a separate xml file */ 
  public static void writeFile(File file) throws IOException {
    FileWriter fileWriter = new FileWriter(file);
    try {
      fileWriter.write(converter.getDocumentHeader(options, "")); //$NON-NLS-1$
      converter.convert(source, options, fileWriter);
      fileWriter.write(converter.getDocumentFooter(options));
    }
    finally {
      IoUtilities.close(fileWriter);
    }
  }

  /** use this class like a common comandline tool.
   changing of options is not supported, yet */
  public static void main(String args[]) {
    String ext = ".xhtml"; //$NON-NLS-1$
    if (!xhtml)
      ext = ".xml"; //$NON-NLS-1$
    for (int i = 0; args.length > i; i++) {
      try {
        readFile(args[i]);
        writeFile(args[i] + ext);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}