package de.java2html.commandline;

import java.io.File;
import java.io.IOException;

import de.java2html.converter.IJavaSourceConverter;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.util.Ensure;
import de.java2html.util.IoUtilities;

/**
 * @author Markus Gebhard
 */
public class Java2HtmlDirectoryConversion extends AbstractJava2HtmlConversion {

  private final File sourceDirectory;
  private final File targetDirectory;
  private final String fileMask;
  private final boolean copyUnprocessedFiles;

  public Java2HtmlDirectoryConversion(
      File sourceDirectory,
      IJavaSourceConverter converter,
      File targetDirectory,
      String fileMask,
      boolean copyUnprocessedFiles, JavaSourceConversionOptions options) {
    super(converter, options);
    Ensure.ensureArgumentNotNull(fileMask);
    Ensure.ensureArgumentNotNull(sourceDirectory);

    this.sourceDirectory = sourceDirectory;
    if (targetDirectory == null) {
      targetDirectory = sourceDirectory;
    }
    this.targetDirectory = targetDirectory;
    this.fileMask = fileMask;
    this.copyUnprocessedFiles = copyUnprocessedFiles;
  }

  public void execute() {
    convertDirectory(sourceDirectory, targetDirectory);
  }

  /**
   * Converts the specified source directory into the target directory 
   * including subdirectories using the specified converter.
   * 
   * @param sourceDirectory may not be <code>null</code>
   * @param targetDirectory may not be <code>null</code>
   */
  private void convertDirectory(File sourceDirectory, File targetDirectory) {
    File[] files = sourceDirectory.listFiles();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      if (file.isDirectory()) {
        File newTargetDirectory = null;
        if (sourceDirectory.equals(targetDirectory)) {
          newTargetDirectory = file;
        }
        else {
          newTargetDirectory = new File(targetDirectory, file.getAbsolutePath().substring(
              sourceDirectory.getAbsolutePath().length()));
        }
        convertDirectory(file, newTargetDirectory);
      }
      else {
        if (matches(file)) {
          File targetFile = null;
          if (sourceDirectory.equals(targetDirectory)) {
            targetFile = file;
          }
          else {
            targetFile = new File(targetDirectory, file.getAbsolutePath().substring(
                sourceDirectory.getAbsolutePath().length()));
          }
          targetFile = IoUtilities.exchangeFileExtension(targetFile, getConverter()
              .getMetaData().getDefaultFileExtension());
          convertFile(file, targetFile);
        }
        else {
          if (copyUnprocessedFiles) {
            if (!sourceDirectory.equals(targetDirectory)) {
              File targetFile = new File(targetDirectory, file.getAbsolutePath().substring(
                  sourceDirectory.getAbsolutePath().length()));
              try {
                IoUtilities.copy(file, targetFile);
              }
              catch (IOException e) {
                System.err.println("ERROR: Could cot copy file to target:  "
                    + file.getAbsolutePath());
              }
            }
          }
        }
      }
    }
  }

  /**
   * Return true if the mask matches the target
   *  
   * e.g. valid mask is *.java, *.ext , etc...
   */
  private boolean matches(File file) {
    return file.getAbsolutePath().toLowerCase().endsWith(fileMask.substring(1));
  }
}