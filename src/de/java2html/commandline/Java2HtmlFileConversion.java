package de.java2html.commandline;

import java.io.File;

import de.java2html.converter.IJavaSourceConverter;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.util.Ensure;

/**
 * @author Markus Gebhard
 */
public class Java2HtmlFileConversion extends AbstractJava2HtmlConversion {
  private final File srcFile;
  private final File targetFile;

  public Java2HtmlFileConversion(
      File srcFile,
      File targetFile,
      IJavaSourceConverter converter,
      JavaSourceConversionOptions options) {
    super(converter, options);
    Ensure.ensureArgumentNotNull(srcFile);
    this.srcFile = srcFile;
    this.targetFile = targetFile;
  }

  public void execute() {
    convertFile(srcFile, targetFile);
  }
}