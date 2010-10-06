package de.java2html.javasource;

import java.util.ArrayList;
import java.util.List;

/**
 * Different types of source code for classifying characters in the raw text.
 * @author Markus Gebhard
 */
public class JavaSourceType {
  private static final List ALL = new ArrayList();

  private static int idCounter = 0;

  public final static JavaSourceType BACKGROUND = new JavaSourceType("Background", false);

  //Not really a Javasource type, but useful for conversion output options
  public final static JavaSourceType LINE_NUMBERS = new JavaSourceType("Line numbers", true);

  public final static JavaSourceType COMMENT_BLOCK = new JavaSourceType("Multi-line comments", true); //green 

  public final static JavaSourceType COMMENT_LINE = new JavaSourceType("Single-line comments", true); //green 

  public final static JavaSourceType KEYWORD = new JavaSourceType("Keywords", true);

  public final static JavaSourceType STRING = new JavaSourceType("Strings", true); //darker red

  public final static JavaSourceType CHAR_CONSTANT = new JavaSourceType("Character constants", true); //dark red

  public final static JavaSourceType NUM_CONSTANT = new JavaSourceType("Numeric constants", true); //dark red 

  public final static JavaSourceType PARENTHESIS = new JavaSourceType("Parenthesis", true);

  public final static JavaSourceType CODE_TYPE = new JavaSourceType("Primitive Types", true);

  public final static JavaSourceType CODE = new JavaSourceType("Others", true);

  public final static JavaSourceType JAVADOC_KEYWORD = new JavaSourceType("Javadoc keywords", true); //dark green

  public final static JavaSourceType JAVADOC_HTML_TAG = new JavaSourceType("Javadoc HTML tags", true);

  public final static JavaSourceType JAVADOC_LINKS = new JavaSourceType("Javadoc links", true);

  public final static JavaSourceType JAVADOC = new JavaSourceType("Javadoc others", true); //green 

  public final static JavaSourceType UNDEFINED = new JavaSourceType("Undefined", false);

  public static final JavaSourceType ANNOTATION = new JavaSourceType("Annotation", true);

  public static JavaSourceType[] getAll() {
    return (JavaSourceType[]) ALL.toArray(new JavaSourceType[ALL.size()]);
  }

  private String name;
  private int id;
  private boolean displayRelevant;

  /**
   * @param name The name of the type
   * @param displayRelevant false if this type does not really matter for
   * display (e.g. because type means empty or illegal code).
   */
  private JavaSourceType(String name, boolean displayRelevant) {
    this.id = idCounter++;
    this.name = name;
    this.displayRelevant = displayRelevant;
    ALL.add(this);
  }

  public String getName() {
    return toString();
  }

  public int getID() {
    return id;
  }

  public String toString() {
    return name;
  }

  public boolean isDisplayRelevant() {
    return displayRelevant;
  }
}