package de.java2html.commandline;

import java.io.File;

import de.java2html.converter.AbstractJavaSourceConverter;
import de.java2html.converter.IJavaSourceConverter;
import de.java2html.converter.JavaSourceConverterProvider;
import de.java2html.options.ConversionOptionsUtilities;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleTable;

/**
 * 
 * Command line tool to invoke the {@link de.java2html.converter.AbstractJavaSourceConverter} without a GUI.
 * Also allows handling of complete source trees.
 * 
 * If the -copyToTargetUnprocessedFiles parameter is supplied all files that are NOT processed
 * by this tool will just be copied to the target directory.
 * 
 * Valid invocations are:
 * <ul>
 *   <li><code>java -jar java2html.jar -srcdir d:/src [-targetdir d:/src/output] [-filemask *.java] [-converter HTML ¦ LaTeX ¦ XHtml ] [-tabs 4] [-copyToTargetUnprocessedFiles] [-style  KAWA ¦ ECLIPSE]</code>
 *   <li><code>java -jar java2html.jar -srcfile d:/src/test.java [-targetfile d:/src/output/target.java]  [-converter HTML ¦ LaTeX ¦ XHtml ] [-tabs 4] [-style  KAWA ¦ ECLIPSE]</code>
 * </ul>
 * 
 * default converter is HTML and default filemask is <code>*.java</code>
 * Other options are set to the default options defined by the <code>java2html.properties</code> file.
 * 
 * @author Sanjay Madhavan
 * @author Markus Gebhard
 */
public class Java2HtmlCommandline {
  private static final String PARAM_TARGETFILE = "targetfile";
  private static final String PARAM_SRCFILE = "srcfile";
  private static final String PARAM_STYLE = "style";
  private static final String FLAG_COPYTOTARGETUNPROCESSEDFILES = "copytotargetunprocessedfiles";
  private static final String PARAM_TABS = "tabs";
  private static final String PARAM_CONVERTER = "converter";
  private static final String PARAM_TARGETDIR = "targetdir";
  private static final String PARAM_SRCDIR = "srcdir";
  private static final String PARAM_FILEMASK = "filemask";

  private static final String DEFAULT_CONVERTER_TYPE_NAME = "HTML";
  private final static String DEFAULT_FILEMASK = "*.java";

  /**
   * Launch this tool with the supplied parameter. See the class comment for valid parameters.
   *  
   * @param args
   */
  public static IJava2HtmlConversion createCommandlineConversion(String[] args)
      throws IllegalCommandlineParametersException {
    CommandlineArguments arguments = new CommandlineArguments(args);
    if (arguments.isParameterSet(PARAM_SRCDIR)) {
      return createDirectoryConversion(arguments);
    }
    else if (arguments.isParameterSet(PARAM_SRCFILE)) {
      return createFileConversion(arguments);
    }
    else {
      throw new IllegalCommandlineParametersException("No parameter -srcdir or -srcfile specified.");
    }
  }

  private static IJava2HtmlConversion createFileConversion(CommandlineArguments arguments)
      throws IllegalCommandlineParametersException {
    arguments.assertContainsNoUnsupportedFlags(new String[]{});
    arguments.assertContainsNoUnsupportedParameters(new String[]{
        PARAM_SRCFILE,
        PARAM_TARGETFILE,
        PARAM_CONVERTER,
        PARAM_TABS,
        PARAM_STYLE });

    String srcFileName = arguments.getRequiredParameterStringValue(PARAM_SRCFILE);
    String targetFileName = arguments.getParameterStringValue(PARAM_TARGETFILE, null);
    String converterType = arguments.getParameterStringValue(PARAM_CONVERTER, DEFAULT_CONVERTER_TYPE_NAME);
    int tabSize = arguments.getParameterPositiveIntValue(PARAM_TABS, JavaSourceConversionOptions
        .getDefault().getTabSize());
    String styleName = arguments
        .getParameterStringValue(PARAM_STYLE, JavaSourceStyleTable.getDefault().getName());

    IJavaSourceConverter converter = getConverter(converterType);
    if (converter == null) {
      System.err.println("ERROR: Unknown converter type: " + converterType);
      printUsage();
      System.exit(1);
    }

    JavaSourceConversionOptions options = JavaSourceConversionOptions.getDefault();
    applyStyle(styleName, options);

    options.setTabSize(tabSize);

    return new Java2HtmlFileConversion(new File(srcFileName), targetFileName == null ? null : new File(
        targetFileName), converter, options);
  }

  private static IJava2HtmlConversion createDirectoryConversion(CommandlineArguments arguments)
      throws IllegalCommandlineParametersException {
    arguments.assertContainsNoUnsupportedFlags(new String[]{ FLAG_COPYTOTARGETUNPROCESSEDFILES });
    arguments.assertContainsNoUnsupportedParameters(new String[]{
        PARAM_SRCDIR,
        PARAM_TARGETDIR,
        PARAM_CONVERTER,
        PARAM_TABS,
        PARAM_STYLE,
        PARAM_FILEMASK });

    String srcDirectoryName = arguments.getRequiredParameterStringValue(PARAM_SRCDIR);
    String targetDirectoryName = arguments.getParameterStringValue(PARAM_TARGETDIR, null);
    String converterType = arguments.getParameterStringValue(PARAM_CONVERTER, DEFAULT_CONVERTER_TYPE_NAME);
    int tabSize = arguments.getParameterPositiveIntValue(PARAM_TABS, JavaSourceConversionOptions
        .getDefault().getTabSize());
    boolean copyUnprocessedFiles = arguments.isFlagSet(FLAG_COPYTOTARGETUNPROCESSEDFILES);
    String styleName = arguments
        .getParameterStringValue(PARAM_STYLE, JavaSourceStyleTable.getDefault().getName());

    String fileMask = arguments.getParameterStringValue(PARAM_FILEMASK, DEFAULT_FILEMASK);

    IJavaSourceConverter converter = getConverter(converterType);
    if (converter == null) {
      System.err.println("ERROR: Unknown converter type: " + converterType);
      printUsage();
      System.exit(1);
    }

    JavaSourceConversionOptions options = JavaSourceConversionOptions.getDefault();
    applyStyle(styleName, options);
    options.setTabSize(tabSize);

    return new Java2HtmlDirectoryConversion(new File(srcDirectoryName), converter, targetDirectoryName == null
        ? null
        : new File(targetDirectoryName), fileMask, copyUnprocessedFiles, options);
  }

  /**
   * Prints the valid usage of this tool.
   *
   */
  public static void printUsage() {
    System.out.println("\nValid usage: \n");
    System.out.println("1. To convert a directory including subdirectories: \n");

    System.out.println("\tjava -jar java2html.jar -"
        + PARAM_SRCDIR
        + " d:/src [-"
        + PARAM_TARGETDIR
        + " d:/src/output] [-"
        + PARAM_FILEMASK
        + " *.java] [-"
        + PARAM_CONVERTER
        + " "
        + getValidConverterNames()
        + "] [-"
        + PARAM_TABS
        + " 4] [-"
        + FLAG_COPYTOTARGETUNPROCESSEDFILES
        + "] -"
        + PARAM_STYLE
        + " [ "
        + getValidStylesAsString()
        + " ]\n");

    System.out.println("2. To convert a single file: \n");
    System.out.println("\tjava -jar java2html.jar -"
        + PARAM_SRCFILE
        + " d:/src/test.java [-"
        + PARAM_TARGETFILE
        + " d:/src/output/target.java]  [-"
        + PARAM_CONVERTER
        + " "
        + getValidConverterNames()
        + "] [-"
        + PARAM_TABS
        + " 8]"
        + " -"
        + PARAM_STYLE
        + " [ "
        + getValidStylesAsString()
        + " ]\n");
  }

  /**
   * set the style of generation, If the specified style is not a valid style then use the first defined style.
   * @param options
   * @param styleName
   */
  private static void applyStyle(String styleName, JavaSourceConversionOptions options)
      throws IllegalCommandlineParametersException {
    JavaSourceStyleTable styleTable = JavaSourceStyleTable.getPredefinedTable(styleName);
    if (styleTable == null) {
      throw new IllegalCommandlineParametersException("Unknown style '" + styleName + "'");
    }
    options.setStyleTable(styleTable);
  }

  /**
   * Return a list of all defined styles as a string
   * 
   * @return String
   */
  private static String getValidStylesAsString() {
    return ConversionOptionsUtilities.getSeparatedString(ConversionOptionsUtilities
        .getPredefinedStyleTableNames(), " | ");
  }

  private static String getValidConverterNames() {
    return ConversionOptionsUtilities.getSeparatedString(
        JavaSourceConverterProvider.getAllConverterNames(),
        " | ");
  }

  /**
   * Return a converter instance for the specified converterType 
   *  or null if no matching converter was found.
   * 
   * @param converterType
   * @return JavaSourceConverter
   */
  private static IJavaSourceConverter getConverter(String converterType)
      throws IllegalCommandlineParametersException {
    IJavaSourceConverter converter = JavaSourceConverterProvider.getJavaSourceConverterByName(converterType);
    if (converter == null) {
      throw new IllegalCommandlineParametersException("Unsupported converter type '" + converterType + "'");
    }
    return converter;
  }
}