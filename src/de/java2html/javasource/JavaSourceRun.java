package de.java2html.javasource;

/** A connected piece of Java source code having the same type
 * ({@link de.java2html.javasource.JavaSourceType}).
 * JavaSourceRun objects are created by {@link de.java2html.javasource.JavaSourceIterator} provided
 * from a {@link de.java2html.javasource.JavaSource} object.
 */
public class JavaSourceRun {
  private final JavaSource javaSource;
  private final int startIndex;
  private final int endIndex;

  public JavaSourceRun(
    JavaSource javaSource,
    int startIndex,
    int endIndex) {
    this.javaSource = javaSource; 
    this.startIndex = startIndex; 
    this.endIndex = endIndex;
  }
  
  public int getEndIndex() {
    return endIndex;
  }

  public boolean isAtEndOfLine() {
    return endIndex==javaSource.getCode().length() || javaSource.getCode().charAt(endIndex)=='\r';
  }

  public boolean isAtStartOfLine() {
    return (startIndex==0 || javaSource.getCode().charAt(startIndex-1)=='\n');
  }

  public JavaSource getJavaSource() {
    return javaSource;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public JavaSourceType getType(){
    return javaSource.getClassification()[startIndex];
  }

  public String getCode(){
    return javaSource.getCode().substring(startIndex, endIndex);
  }

  public void dump() {
    System.out.print(isAtStartOfLine() ? "[" : "(");
    System.out.print(startIndex+".."+endIndex);
    System.out.print(isAtEndOfLine() ? "]" : ")");
    System.out.println(" '"+getCode()+"'");
  }
}