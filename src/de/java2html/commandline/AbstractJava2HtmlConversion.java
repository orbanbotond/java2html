package de.java2html.commandline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import de.java2html.converter.IJavaSourceConverter;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.util.Ensure;
import de.java2html.util.IoUtilities;

/**
 * @author Markus Gebhard
 */
public abstract class AbstractJava2HtmlConversion implements IJava2HtmlConversion {
  private IJavaSourceConverter converter;
  private final JavaSourceConversionOptions options;

  public AbstractJava2HtmlConversion(IJavaSourceConverter converter, JavaSourceConversionOptions options) {
    Ensure.ensureArgumentNotNull(converter);
    Ensure.ensureArgumentNotNull(options);
    this.options = options;
    this.converter = converter;
  }
  
  public final JavaSourceConversionOptions getConversionOptions() {
    return options;
  }

  public final IJavaSourceConverter getConverter() {
    return converter;
  }

  /**
   * Read the contents from the specified file name.
   * 
   * @param file
   * @return byte[]
   * @throws IOException
   */
  protected byte[] readFile(File file) throws IOException {
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      return IoUtilities.readBytes(inputStream);
    }
    finally {
      IoUtilities.close(inputStream);
    }
  }

  /**
   * Invoke the converter on the specified file and return the converted contents.
   * 
   * @param sourceFile The file to be converted
   * @return a String containing the converted result
   * @throws IOException when there is an io error reading the file. */
  protected String readAndConvert(File sourceFile) throws IOException {
    StringWriter stringWriter = new StringWriter();

    converter.writeDocumentHeader(stringWriter, getConversionOptions(), ""); //$NON-NLS-1$
    JavaSourceParser parser = new JavaSourceParser(getConversionOptions());
    JavaSource source = parser.parse(sourceFile);
    try {
      converter.convert(source, getConversionOptions(), stringWriter);
    }
    catch (IOException e) {
      //Should never happen since we are a StringWriter
      throw new RuntimeException(e);
    }
    converter.writeDocumentFooter(stringWriter, getConversionOptions());
    return stringWriter.toString();
  }

  /**
   * Converts the source file to the targetfile using the specified converter
   * 
   * @param sourceFile
   * @param targetFile the target file to write the output to or <code>null</code> if the converter
   * shall determine an appropriate name for the output file itself.
   */
  protected void convertFile(File sourceFile, File targetFile) {
    String text;
    try {
      text = readAndConvert(sourceFile);
    }
    catch (IOException exception) {
      //TODO Mar 11, 2004 (Markus Gebhard):
      exception.printStackTrace();
      return;
    }
    if (targetFile == null) {
      targetFile = IoUtilities.exchangeFileExtension(sourceFile, getConverter().getMetaData().getDefaultFileExtension());
    }

    IoUtilities.ensureFoldersExist(targetFile.getParentFile());
    try {
      writeFile(targetFile, text.getBytes());
    }
    catch (IOException exception) {
      //TODO Mar 11, 2004 (Markus Gebhard):
      exception.printStackTrace();
    }
  }


  /**
   * Write the contents to the specified file. If the file already exists it will be overwritten.
   * 
   * @param targetFile the file to write the contents to.
   * @param contents the bytes to be written.
   * @throws IOException if the is an error writing the file. */
  private void writeFile(File targetFile, byte[] contents) throws IOException {
    FileOutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(targetFile);
      outputStream.write(contents);
    }
    finally {
      IoUtilities.close(outputStream);
    }
  }
}