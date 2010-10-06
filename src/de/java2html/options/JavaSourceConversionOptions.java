package de.java2html.options;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.java2html.properties.ConversionOptionsPropertiesReader;
import de.java2html.util.Ensure;
import de.java2html.util.IllegalConfigurationException;
import de.java2html.util.IoUtilities;

/**
 * Conversion options for customizing the output result. You can adjust the
 * output style of a {@link de.java2html.converter.AbstractJavaSourceConverter}by
 * changing the attributes of this object. The color and font style are defined
 * by the {@link de.java2html.options.JavaSourceStyleTable} associated with
 * this options.
 * 
 * @see #setStyleTable(JavaSourceStyleTable)
 * @see #getStyleTable()
 * @see de.java2html.converter.AbstractJavaSourceConverter
 * 
 * @author <a href="mailto:markus@jave.de">Markus Gebhard</a>
 * 
 * <code>Copyright (C) Markus Gebhard 2000-2003
 * 
 * This program is free software; you can redistribute it and/or
 *  * modify it under the terms of the GNU General Public License
 *  * as published by the Free Software Foundation; either version 2
 *  * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 *  * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.</code>
 */
public class JavaSourceConversionOptions {
  //Attribute names for persistence (e.g. in the eclipse plugin

  private static final String PROPERTIES_FILE_NAME = "java2html.properties"; //$NON-NLS-1$
  /**
   * @deprecated As of Dec 21, 2003 (Markus Gebhard), replaced by
   *             {@link IConversionOptionsConstants#TAB_SIZE}
   */
  public final static String TAB_SIZE = IConversionOptionsConstants.TAB_SIZE;

  /**
   * @deprecated As of Dec 21, 2003 (Markus Gebhard), replaced by
   *             {@link IConversionOptionsConstants#SHOW_LINE_NUMBERS}
   */
  public final static String SHOW_LINE_NUMBERS = IConversionOptionsConstants.SHOW_LINE_NUMBERS;

  /**
   * @deprecated As of Dec 21, 2003 (Markus Gebhard), replaced by
   *             {@link IConversionOptionsConstants#SHOW_FILE_NAME}
   */
  public final static String SHOW_FILE_NAME = IConversionOptionsConstants.SHOW_FILE_NAME;

  /**
   * @deprecated As of Dec 21, 2003 (Markus Gebhard), replaced by
   *             {@link IConversionOptionsConstants#SHOW_TABLE_BORDER}
   */
  public final static String SHOW_TABLE_BORDER = IConversionOptionsConstants.SHOW_TABLE_BORDER;

  private static JavaSourceConversionOptions defaultOptions;

  public static JavaSourceConversionOptions getRawDefault() {
    return new JavaSourceConversionOptions();
  }

  public static JavaSourceConversionOptions getDefault() throws IllegalConfigurationException {
    if (defaultOptions == null) {
      defaultOptions = createDefaultOptions();
    }
    return defaultOptions.getClone();
  }

  private static JavaSourceConversionOptions createDefaultOptions() throws IllegalConfigurationException {
    InputStream inputStream = JavaSourceConversionOptions.class.getClassLoader().getResourceAsStream(
        PROPERTIES_FILE_NAME);
    if (inputStream == null) {
      return new JavaSourceConversionOptions();
    }

    /* TODO: ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE_NAME)
     instead of
     Java2HtmlConversionOptions.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
     Will have to find out how I can make this compatible when using Java WebStart.
     As far as I know using ClassLoader.getSystemResourceAsStream() will propbably not find 
     resources that are defined in the JNLP file. Maybe I should use 
     ClassLoader.getSystemResourceAsStream as fallback if there is no file found using the other
     classloader... */

    Properties properties = new Properties();
    try {
      properties.load(inputStream);
      return new ConversionOptionsPropertiesReader().read(properties);
    }
    catch (IOException exception) {
      throw new IllegalConfigurationException("Error loading configuration file '" //$NON-NLS-1$
          + PROPERTIES_FILE_NAME + "' from classpath", exception); //$NON-NLS-1$
    }
    catch (IllegalArgumentException exception) {
      throw new IllegalConfigurationException("Error loading configuration file '" //$NON-NLS-1$
          + PROPERTIES_FILE_NAME + "' from classpath", exception); //$NON-NLS-1$
    }
    finally {
      IoUtilities.close(inputStream);
    }
  }

  private JavaSourceStyleTable styleTable = JavaSourceStyleTable.getDefault();
  private int tabSize = 2;
  private boolean showLineNumbers = false;
  private boolean showFileName = false;
  private boolean showTableBorder = false;

  /**
   * Flag indication whether html output contains a link to the
   * Java2Html-Homepage or not.
   */
  private boolean showJava2HtmlLink = false;
  private boolean addLineAnchors = false;
  private String lineAnchorPrefix = ""; //$NON-NLS-1$
  private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;

  private JavaSourceConversionOptions() {
    //nothing to do
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof JavaSourceConversionOptions)) {
      return false;
    }
    JavaSourceConversionOptions other = (JavaSourceConversionOptions) obj;
    return (other.tabSize == tabSize
        && other.styleTable.equals(styleTable)
        && other.showFileName == showFileName
        && other.showJava2HtmlLink == showJava2HtmlLink
        && other.showLineNumbers == showLineNumbers
        && other.showTableBorder == showTableBorder && other.horizontalAlignment == horizontalAlignment);
  }

  public int hashCode() {
    return styleTable.hashCode() + tabSize;
  }

  public JavaSourceConversionOptions getClone() {
    JavaSourceConversionOptions options = new JavaSourceConversionOptions();
    options.styleTable = styleTable.getClone();
    options.tabSize = tabSize;
    options.showLineNumbers = showLineNumbers;
    options.showFileName = showFileName;
    options.showJava2HtmlLink = showJava2HtmlLink;
    options.showTableBorder = showTableBorder;
    options.horizontalAlignment = horizontalAlignment;
    return options;
  }

  public void setStyleTable(JavaSourceStyleTable styleTable) {
    Ensure.ensureArgumentNotNull(styleTable);
    this.styleTable = styleTable;
  }

  public JavaSourceStyleTable getStyleTable() {
    return styleTable;
  }

  public int getTabSize() {
    return tabSize;
  }

  public void setTabSize(int tabSize) {
    this.tabSize = tabSize;
  }

  public boolean isShowLineNumbers() {
    return showLineNumbers;
  }

  public void setShowLineNumbers(boolean showLineNumbers) {
    this.showLineNumbers = showLineNumbers;
  }

  public boolean isShowFileName() {
    return showFileName;
  }

  public boolean isShowTableBorder() {
    return showTableBorder;
  }

  public void setShowFileName(boolean showFileName) {
    this.showFileName = showFileName;
  }

  public void setShowTableBorder(boolean showTableBorder) {
    this.showTableBorder = showTableBorder;
  }

  public boolean isAddLineAnchors() {
    return addLineAnchors;
  }

  public String getLineAnchorPrefix() {
    return lineAnchorPrefix;
  }

  public void setAddLineAnchors(boolean addLineAnchors) {
    this.addLineAnchors = addLineAnchors;
  }

  public void setLineAnchorPrefix(String lineAnchorPrefix) {
    this.lineAnchorPrefix = lineAnchorPrefix;
  }

  public HorizontalAlignment getHorizontalAlignment() {
    return horizontalAlignment;
  }

  public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
    Ensure.ensureArgumentNotNull(horizontalAlignment);
    this.horizontalAlignment = horizontalAlignment;
  }

  public boolean isShowJava2HtmlLink() {
    return showJava2HtmlLink;
  }

  public void setShowJava2HtmlLink(boolean isShowJava2HtmlLink) {
    this.showJava2HtmlLink = isShowJava2HtmlLink;
  }
}