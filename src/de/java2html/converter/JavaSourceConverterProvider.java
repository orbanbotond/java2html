package de.java2html.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Markus Gebhard
 */
public class JavaSourceConverterProvider {

  private final static List/*<IJavaSourceConverter>*/converters = new ArrayList() {
    {
      add(new JavaSource2HTMLConverter());
      add(new JavaSource2Xhtml11Converter());
      add(new JavaSource2XhtmlConverter());
      add(new JavaSource2TeXConverter());
      add(new JavaSource2RtfConverter());
      add(new JavaSource2XmlConverter());
    }
  };

  public static String getDefaultConverterName() {
    return ((IJavaSourceConverter) converters.get(0)).getMetaData().getName().toLowerCase();
  }

  public static IJavaSourceConverter getJavaSourceConverterByName(String name) {
    for (int i = 0; i < converters.size(); ++i) {
      final IJavaSourceConverter converter = (IJavaSourceConverter) converters.get(i);
      if ((converter).getMetaData().getName().equalsIgnoreCase(name)) {
        return converter;
      }
    }
    return null;
  }

  /** @see #getAllConverters() */
  public static String[] getAllConverterNames() {
    String[] names = new String[converters.size()];
    for (int i = 0; i < names.length; ++i) {
      names[i] = ((IJavaSourceConverter) converters.get(i)).getMetaData().getName();
    }
    return names;
  }

  public static IJavaSourceConverter[] getAllConverters() {
    return (IJavaSourceConverter[]) converters.toArray(new IJavaSourceConverter[converters.size()]);
  }

  public static String[] getAllConverterPrintNames() {
    String[] printNames = new String[converters.size()];
    for (int i = 0; i < printNames.length; ++i) {
      printNames[i] = ((IJavaSourceConverter) converters.get(i)).getMetaData().getPrintName();
    }
    return printNames;
  }
}