package de.java2html.converter;

import java.io.IOException;
import java.io.Writer;

import de.java2html.javasource.JavaSource;
import de.java2html.options.JavaSourceConversionOptions;

/**
 * @author Markus Gebhard
 */
public interface IJavaSourceConverter {

  public ConverterMetaData getMetaData();

  /** Returns the default filename extension for the output format of this converter,
   * e.g. "html" or "tex". 
   * @deprecated As of 25.01.2006 (Markus Gebhard), replaced by {@link #getMetaData()}, {@link ConverterMetaData#getDefaultFileExtension()}
   */
  public String getDefaultFileExtension();

  /**
   * Converts the given source code to the giveen writer, using the specified conversion options.
   * @param source The source code to be converted to the output format specified
   *         by this converter.
   * @param writer The writer to write the output to.
   * @param options the options to be used for conversion.
   * @throws IOException if an output error occures while writing to the given writer.
   */
  public void convert(JavaSource source, JavaSourceConversionOptions options, Writer writer) throws IOException;

  /** 
   * @param title An optional title (e.g. for the html title tag) or an empty string or <code>null</code> if none. 
   */
  public void writeDocumentHeader(Writer writer, JavaSourceConversionOptions options, String title)
      throws IOException;

  public void writeDocumentFooter(Writer writer, JavaSourceConversionOptions options) throws IOException;

  public void writeBlockSeparator(Writer writer, JavaSourceConversionOptions options) throws IOException;

}