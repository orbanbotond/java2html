package de.java2html;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import de.java2html.commandline.IJava2HtmlConversion;
import de.java2html.commandline.IllegalCommandlineParametersException;
import de.java2html.commandline.Java2HtmlCommandline;
import de.java2html.converter.IJavaSourceConverter;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.options.JavaSourceConversionOptions;

/**
 * A convenience class providing methods to use the Java2Html converter. By using this class you
 * will not have the ability to benefit from all the features of the Java2Html converter library.
 * However for most standard use cases the methods here will fit your needs.
 */

public class Java2Html {
  private Java2Html() {
    //nothing to do
  }

  /** Converts the given <code>String</code> containing Java source code to HTML
   * by using the standard options of the converter.
   * Will return <code>null</code> if an error occures.
   * The result itself does not have &lt;html&gt; and &lt;/html&gt; tags and so
   * can be embedded in any HTML page.
   * 
   * @param javaSource The Java source code as plain text. 
   * @return A HTML representation of the Java source code or <code>null</code>
   *          if an error occures. 
   * @see #convertToHtml(String, JavaSourceConversionOptions) 
   * @see #convertToHtmlPage(String)
   */
  public static String convertToHtml(String javaSource) {
    return convertToHtml(javaSource, (JavaSourceConversionSettings) null);
  }

  /** Converts the given <code>String</code> containing Java source code to HTML
   * by using the given options for the converter.
   * Will return <code>null</code> if an error occures.
   * The result itself does not have &lt;html&gt; and &lt;/html&gt; tags and so
   * can be embedded in any HTML page.
   * 
   * @param javaSource The Java source code as plain text.
   * @param settings conversion options or <code>null</code> to use the standard options 
   * @return A HTML representation of the Java source code or <code>null</code>
   *          if an error occures. 
   * @see #convertToHtml(String)
   * @see #convertToHtmlPage(String, JavaSourceConversionSettings) 
   */
  public static String convertToHtml(String javaSource, JavaSourceConversionSettings settings) {
    if (javaSource == null) {
      return null;
    }
    if (settings == null) {
      settings = JavaSourceConversionSettings.getDefault();
    }

    StringReader stringReader = new StringReader(javaSource);
    JavaSource source = null;
    try {
      source = new JavaSourceParser(settings.getConversionOptions()).parse(stringReader);
    }
    catch (IOException e) {
      return null;
    }

    IJavaSourceConverter converter = settings.createConverter();
    StringWriter writer = new StringWriter();
    try {
      converter.convert(source, settings.getConversionOptions(), writer);
    }
    catch (IOException e) {
      return null;
    }
    return writer.toString();
  }

  /** Converts the given <code>String</code> containing Java source code to a complete
   * HTML page by using the standard options of the converter.
   * Will return <code>null</code> if an error occures.
   * 
   * @param javaSource The Java source code as plain text.
   * @return A HTML representation of the Java source code or <code>null</code>
   *          if an error occures. 
   * @see #convertToHtmlPage(String, JavaSourceConversionSettings)
   * @see #convertToHtml(String)
   */
  public static String convertToHtmlPage(String javaSource) {
    return convertToHtmlPage(javaSource, (JavaSourceConversionSettings) null);
  }

  /** Converts the given <code>String</code> containing Java source code to a complete
   * HTML page by using the given options for the converter.
   * Will return <code>null</code> if an error occures.
   * 
   * @param javaSource The Java source code as plain text.
   * @param settings conversion options or <code>null</code> to use the standard options 
   * @return A HTML representation of the Java source code or <code>null</code>
   *          if an error occures. 
   * @see #convertToHtmlPage(String)
   * @see #convertToHtmlPage(String, JavaSourceConversionSettings)
   */
  public static String convertToHtmlPage(String javaSource, JavaSourceConversionSettings settings) {
    if (settings == null) {
      settings = JavaSourceConversionSettings.getDefault();
    }
    IJavaSourceConverter converter = settings.createConverter();
    StringWriter writer = new StringWriter();
    try {
      converter.writeDocumentHeader(writer, settings.getConversionOptions(), ""); //$NON-NLS-1$
      writer.write(convertToHtml(javaSource, settings));
      converter.writeDocumentFooter(writer, settings.getConversionOptions());
    }
    catch (IOException e) {
      return null;
    }
    return writer.toString();
  }

  /**
   * The commandline conversion from {@link Java2HtmlCommandline} 
   * can be invoked by running this class.
   */
  public static void main(String[] args) {
    IJava2HtmlConversion commandlineConversion = null;
    try {
      commandlineConversion = Java2HtmlCommandline.createCommandlineConversion(args);
    }
    catch (IllegalCommandlineParametersException exception) {
      System.err.println("Illegal commandline parameters: " + exception.getMessage());
      Java2HtmlCommandline.printUsage();
      System.exit(-1);
    }
    commandlineConversion.execute();
  }

  public static String convertToHtml(String text, JavaSourceConversionOptions options) {
    return convertToHtml(text, new JavaSourceConversionSettings(options));
  }

  public static String convertToHtmlPage(String text, JavaSourceConversionOptions options) {
    return convertToHtmlPage(text, new JavaSourceConversionSettings(options));
  }
}