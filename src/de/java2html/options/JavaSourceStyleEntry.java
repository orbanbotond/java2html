package de.java2html.options;

import de.java2html.util.HtmlUtilities;
import de.java2html.util.RGB;

/**
 * Object defining color and other style options for output.
 * 
 * @author Markus Gebhard
 */
public class JavaSourceStyleEntry {
  private RGB color;
  private String htmlColor;
  private boolean bold;
  private boolean italic;

  public JavaSourceStyleEntry(RGB color) {
    this(color, false, false);
  }

  public JavaSourceStyleEntry(RGB color, boolean bold, boolean italic) {
    this.color = color;
    this.italic = italic;
    this.bold = bold;
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof JavaSourceStyleEntry)) {
      return false;
    }
    JavaSourceStyleEntry other = (JavaSourceStyleEntry) obj;
    return color.equals(other.color) && bold == other.bold && italic == other.italic;
  }
  
  public int hashCode() {
    return color.hashCode();
  }

  /**
   * @deprecated As of Dec 21, 2003 (Markus Gebhard): object is immutable and cloning not necessary.
   */
  public JavaSourceStyleEntry getClone() {
    return new JavaSourceStyleEntry(color, bold, italic);
  }

  public String getHtmlColor() {
    if (htmlColor==null) {
      htmlColor = HtmlUtilities.toHTML(getColor());
    }
    return htmlColor;
  }

  public RGB getColor() {
    return color;
  }

  public boolean isBold() {
    return bold;
  }

  public boolean isItalic() {
    return italic;
  }
}