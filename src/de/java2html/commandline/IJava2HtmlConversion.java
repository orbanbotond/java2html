package de.java2html.commandline;

import de.java2html.converter.IJavaSourceConverter;
import de.java2html.options.JavaSourceConversionOptions;

/**
 * @author Markus Gebhard
 */
public interface IJava2HtmlConversion {
  public void execute();

  public IJavaSourceConverter getConverter();
  
  public JavaSourceConversionOptions getConversionOptions();
  
}