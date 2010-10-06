package de.java2html.converter.demo;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import de.java2html.converter.JavaSource2HTMLConverter;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.javasource.JavaSourceType;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleEntry;
import de.java2html.util.RGB;

public class Java2HtmlConversionDemo {
  
  public static void main(String[] args) {
    StringReader stringReader = new StringReader(
      "/** Simple Java2Html Demo */\r\n"+      
      "public static int doThis(String text){ return text.length() + 2; }");
    JavaSource source = null;
    try {
      source = new JavaSourceParser().parse(stringReader);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    JavaSource2HTMLConverter converter = new JavaSource2HTMLConverter();

    JavaSourceConversionOptions options = JavaSourceConversionOptions.getDefault();
    options.getStyleTable().put(JavaSourceType.KEYWORD, new JavaSourceStyleEntry(RGB.ORANGE, true, false));

    StringWriter writer = new StringWriter(); 
    try {
      converter.convert(source, options, writer);
    } catch (IOException e) {
      //can not happen
    }
    System.out.println(writer.toString());
  }
}