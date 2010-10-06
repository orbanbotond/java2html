package de.java2html.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.java2html.JavaSourceConversionSettings;
import de.java2html.converter.IJavaSourceConverter;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.util.Ensure;

public class DirectTextConversionPanel {

  private final JComponent content;
  private final static Font FONT = new Font("Monospaced", Font.PLAIN, 11);
  private final JTextArea taInput;
  private final JTextArea taOutput;
  private final JTextField tfTitle;
  private final Java2HtmlOptionsPanel optionsPanel;
  private final IStatisticsView statisticsView;

  public DirectTextConversionPanel(Java2HtmlOptionsPanel optionsPanel, IStatisticsView statisticsView) {
    Ensure.ensureArgumentNotNull(optionsPanel);
    Ensure.ensureArgumentNotNull(statisticsView);
    this.statisticsView = statisticsView;
    this.optionsPanel = optionsPanel;
    //Create UI:
    JLabel l1 = new JLabel("Title (optional):");
    tfTitle = new JTextField(30);
    final DocumentListener documentListener = new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        convert();
      }

      public void removeUpdate(DocumentEvent e) {
        convert();
      }

      public void insertUpdate(DocumentEvent e) {
        convert();
      }
    };
    tfTitle.getDocument().addDocumentListener(documentListener);

    taInput = new JTextArea(10, 80);
    taInput.getDocument().addDocumentListener(documentListener);
    taInput.setFont(FONT);
    taOutput = new JTextArea(10, 80);
    taOutput.setEditable(false);
    taOutput.setFont(FONT);

    final JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(new EmptyBorder(5, 6, 5, 6));

    final GridBagConstraints twoColumnLineConstraints = new GridBagConstraints();
    twoColumnLineConstraints.anchor = GridBagConstraints.WEST;
    twoColumnLineConstraints.gridx = 0;
    twoColumnLineConstraints.gridwidth = 2;
    twoColumnLineConstraints.fill = GridBagConstraints.HORIZONTAL;
    twoColumnLineConstraints.weightx = 1.0;
    twoColumnLineConstraints.insets = new Insets(6, 2, 2, 2);

    final GridBagConstraints twoColumnBlockConstraints = new GridBagConstraints();
    twoColumnBlockConstraints.anchor = GridBagConstraints.WEST;
    twoColumnBlockConstraints.gridx = 0;
    twoColumnBlockConstraints.gridwidth = 2;
    twoColumnBlockConstraints.fill = GridBagConstraints.BOTH;
    twoColumnBlockConstraints.weightx = 1.0;
    twoColumnBlockConstraints.weighty = 1.0;
    twoColumnBlockConstraints.insets = new Insets(2, 2, 2, 2);

    final GridBagConstraints leftColumnConstraints = new GridBagConstraints();
    leftColumnConstraints.insets = new Insets(2, 2, 2, 2);
    leftColumnConstraints.gridx = 0;
    leftColumnConstraints.anchor = GridBagConstraints.EAST;

    final GridBagConstraints rightColumnConstraints = new GridBagConstraints();
    rightColumnConstraints.insets = new Insets(2, 2, 2, 2);
    rightColumnConstraints.gridx = 1;
    rightColumnConstraints.fill = GridBagConstraints.HORIZONTAL;
    rightColumnConstraints.anchor = GridBagConstraints.WEST;

    panel.add(l1, leftColumnConstraints);
    panel.add(tfTitle, rightColumnConstraints);
    panel.add(new JLabel("Java source (paste your source code here):"), twoColumnLineConstraints);
    panel.add(new JScrollPane(taInput), twoColumnBlockConstraints);
    panel.add(
        new JLabel("Converted source (copy and paste this to where you want it):"),
        twoColumnLineConstraints);
    panel.add(new JScrollPane(taOutput), twoColumnBlockConstraints);

    optionsPanel.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        convert();
      }
    });

    this.content = panel;
  }

  public JComponent getContent() {
    return content;
  }

  private void convert() {
    String title = tfTitle.getText();
    if (title.equals("")) {
      title = null;
    }

    String input = taInput.getText();
    if (input.trim().equals("")) {
      taOutput.setText("");
      statisticsView.setStatistics(null);
      return;
    }
    JavaSourceConversionSettings settings = optionsPanel.getConversionSettings();
    settings.getConversionOptions().setShowJava2HtmlLink(true);
    JavaSourceParser parser = new JavaSourceParser(settings.getConversionOptions());
    JavaSource source = parser.parse(input);
    source.setFileName(title);

    //Create the converter
    IJavaSourceConverter converter = settings.createConverter();

    StringWriter writer = new StringWriter();
    try {
      converter.writeDocumentHeader(writer, settings.getConversionOptions(), title);
      converter.convert(source, settings.getConversionOptions(), writer);
      converter.writeDocumentFooter(writer, settings.getConversionOptions());
    }
    catch (IOException e) {
      throw new RuntimeException(e); //should never happen
    }

    taOutput.setText(writer.toString());
    statisticsView.setStatistics(source.getStatistic());
    taOutput.selectAll();
  }

  public void requestFocus() {
    taInput.requestFocus();
  }
}