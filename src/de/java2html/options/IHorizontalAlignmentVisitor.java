package de.java2html.options;

/**
 * @author Markus Gebhard
 */
public interface IHorizontalAlignmentVisitor {
  public void visitLeftAlignment(HorizontalAlignment horizontalAlignment);
  public void visitRightAlignment(HorizontalAlignment horizontalAlignment);
  public void visitCenterAlignment(HorizontalAlignment horizontalAlignment);
}
