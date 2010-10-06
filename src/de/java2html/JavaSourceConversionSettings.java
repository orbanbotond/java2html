package de.java2html;

import de.java2html.converter.IJavaSourceConverter;
import de.java2html.converter.JavaSourceConverterProvider;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.util.Ensure;

/**
 * @author Markus Gebhard
 */
public class JavaSourceConversionSettings {

  private final String converterName;
  private final JavaSourceConversionOptions options;

  public JavaSourceConversionSettings(JavaSourceConversionOptions options, String converterName) {
    Ensure.ensureArgumentNotNull(options);
    Ensure.ensureArgumentNotNull(converterName);
    this.converterName = converterName;
    this.options = options;
  }

  public JavaSourceConversionSettings(JavaSourceConversionOptions options) {
    this(options, JavaSourceConverterProvider.getAllConverterNames()[0]);
  }

  public IJavaSourceConverter createConverter() {
    return JavaSourceConverterProvider.getJavaSourceConverterByName(converterName);
  }

  public JavaSourceConversionOptions getConversionOptions() {
    return options;
  }

  public static JavaSourceConversionSettings getDefault() {
    return new JavaSourceConversionSettings(JavaSourceConversionOptions.getDefault(), JavaSourceConverterProvider
        .getAllConverterNames()[0]);
  }

}