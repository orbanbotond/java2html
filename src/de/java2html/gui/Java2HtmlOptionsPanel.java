package de.java2html.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.java2html.JavaSourceConversionSettings;
import de.java2html.converter.IJavaSourceConverter;
import de.java2html.converter.JavaSourceConverterProvider;
import de.java2html.options.HorizontalAlignment;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleTable;

public class Java2HtmlOptionsPanel {
  private final static JavaSourceStyleTable[] styles = JavaSourceStyleTable.getPredefinedTables();
  private final static HorizontalAlignment[] alignments = new HorizontalAlignment[]{
      HorizontalAlignment.LEFT,
      HorizontalAlignment.CENTER,
      HorizontalAlignment.RIGHT };

  private final SpinnerNumberModel tabModel = new SpinnerNumberModel(2, 0, 16, 1);

  private final JSpinner chTab;
  private final JComboBox chTarget;
  private final JComboBox chStyle;
  private final JComboBox chAlignment;
  private final JCheckBox cbShowLineNumbers;
  private final JCheckBox cbShowTableBorder;
  private final List/*<ChangeListener>*/listeners = new ArrayList();

  private final JComponent content;

  public Java2HtmlOptionsPanel() {
    chTab = new JSpinner(tabModel);
    ItemListener itemListener = new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        fireStateChanged();
      }
    };
    tabModel.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        fireStateChanged();
      }
    });

    chTarget = new JComboBox(JavaSourceConverterProvider.getAllConverters());
    chTarget.setRenderer(new AbstractSimpleListCellRenderer() {
      protected String getLabel(Object value) {
        IJavaSourceConverter converter = (IJavaSourceConverter) value;
        return converter.getMetaData().getPrintName();
      }
    });
    chTarget.addItemListener(itemListener);

    chStyle = new JComboBox(styles);
    chStyle.setRenderer(new AbstractSimpleListCellRenderer() {
      protected String getLabel(Object value) {
        JavaSourceStyleTable table = (JavaSourceStyleTable) value;
        return table.getName();
      }
    });
    chStyle.addItemListener(itemListener);

    chAlignment = new JComboBox(alignments);
    chAlignment.setRenderer(new AbstractSimpleListCellRenderer() {
      protected String getLabel(Object value) {
        HorizontalAlignment alignment = (HorizontalAlignment) value;
        return alignment.getName();
      }
    });
    chAlignment.addItemListener(itemListener);

    cbShowLineNumbers = new JCheckBox("Line numbers", true);
    cbShowLineNumbers.addItemListener(itemListener);

    cbShowTableBorder = new JCheckBox("Table border", true);
    cbShowTableBorder.addItemListener(itemListener);

    JPanel panel = new JPanel(new GridBagLayout());
    final GridBagConstraints leftColumnConstraints = new GridBagConstraints();
    leftColumnConstraints.insets = new Insets(2, 2, 2, 2);
    leftColumnConstraints.gridx = 0;
    leftColumnConstraints.anchor = GridBagConstraints.EAST;

    final GridBagConstraints rightColumnConstraints = new GridBagConstraints();
    rightColumnConstraints.insets = new Insets(2, 2, 2, 2);
    rightColumnConstraints.gridx = 1;
    rightColumnConstraints.fill = GridBagConstraints.NONE;
    rightColumnConstraints.anchor = GridBagConstraints.WEST;

    final GridBagConstraints twoColumnConstraints = new GridBagConstraints();
    twoColumnConstraints.insets = new Insets(2, 2, 2, 2);
    twoColumnConstraints.gridx = 0;
    twoColumnConstraints.gridwidth = 2;
    twoColumnConstraints.anchor = GridBagConstraints.WEST;

    panel.add(new JLabel("Target:"), leftColumnConstraints);
    panel.add(chTarget, rightColumnConstraints);
    panel.add(new JLabel("Style:"), leftColumnConstraints);
    panel.add(chStyle, rightColumnConstraints);
    panel.add(new JLabel("Tab-space:"), leftColumnConstraints);
    panel.add(chTab, rightColumnConstraints);
    panel.add(new JLabel("Alignment:"), leftColumnConstraints);
    panel.add(chAlignment, rightColumnConstraints);
    panel.add(cbShowLineNumbers, twoColumnConstraints);
    panel.add(cbShowTableBorder, twoColumnConstraints);

    this.content = panel;

    initDefaultOptions();
  }

  public JComponent getContent() {
    return content;
  }

  private void initDefaultOptions() {
    JavaSourceConversionOptions options = JavaSourceConversionOptions.getDefault();
    tabModel.setValue(new Integer(options.getTabSize()));

    chStyle.setSelectedItem(options.getStyleTable());
    cbShowLineNumbers.setSelected(options.isShowLineNumbers());
    cbShowTableBorder.setSelected(options.isShowTableBorder());
    chAlignment.setSelectedItem(options.getHorizontalAlignment());
  }

  public JavaSourceConversionSettings getConversionSettings() {
    final IJavaSourceConverter selectedConverter = JavaSourceConverterProvider.getAllConverters()[chTarget
        .getSelectedIndex()];
    String converterName = selectedConverter.getMetaData().getName();
    return new JavaSourceConversionSettings(getConversionOptions(), converterName);
  }

  private JavaSourceConversionOptions getConversionOptions() {
    JavaSourceConversionOptions options = JavaSourceConversionOptions.getDefault();
    int tabs = tabModel.getNumber().intValue();
    options.setTabSize(tabs);
    options.setStyleTable(styles[chStyle.getSelectedIndex()]);
    options.setShowLineNumbers(cbShowLineNumbers.isSelected());
    options.setShowTableBorder(cbShowTableBorder.isSelected());
    options.setHorizontalAlignment(alignments[chAlignment.getSelectedIndex()]);
    return options;
  }

  public synchronized void addChangeListener(ChangeListener listener) {
    listeners.add(listener);
  }

  public synchronized void removeChangeListener(ChangeListener listener) {
    listeners.remove(listener);
  }

  private synchronized void fireStateChanged() {
    ChangeEvent event = new ChangeEvent(this);
    for (Iterator iter = new ArrayList(listeners).iterator(); iter.hasNext();) {
      ChangeListener listener = (ChangeListener) iter.next();
      listener.stateChanged(event);
    }
  }
}