package de.java2html.gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public abstract class AbstractSimpleListCellRenderer extends DefaultListCellRenderer {

  public Component getListCellRendererComponent(
      JList list,
      Object value,
      int index,
      boolean isSelected,
      boolean cellHasFocus) {
    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    setText(getLabel(value));
    return this;
  }

  protected abstract String getLabel(Object value);

}