package de.java2html.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import de.java2html.JavaSourceConversionSettings;
import de.java2html.converter.IJavaSourceConverter;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.util.Ensure;

public class FileConversionPanel implements ActionListener {

  private final DefaultListModel fileListModel = new DefaultListModel();
  private final JList list;
  private final JButton bAdd;
  private final JButton bRemove;
  private final JButton bConvert;
  private final JButton bClear;
  private File currentDirectory;
  private final JComponent content;
  private final Java2HtmlOptionsPanel optionsPanel;

  public FileConversionPanel(Java2HtmlOptionsPanel optionsPanel) {
    Ensure.ensureArgumentNotNull(optionsPanel);
    this.optionsPanel = optionsPanel;
    list = new JList(fileListModel);

    bAdd = new JButton("Add...");
    bAdd.addActionListener(this);

    bRemove = new JButton("Remove");
    bRemove.addActionListener(this);
    bRemove.setEnabled(false);

    bClear = new JButton("Clear");
    bClear.addActionListener(this);
    bClear.setEnabled(false);

    bConvert = new JButton("Convert");
    bConvert.addActionListener(this);
    bConvert.setEnabled(false);

    final JPanel fileButtonPanel = new JPanel(new GridLayout(0, 1, 6, 6));
    fileButtonPanel.add(bAdd);
    fileButtonPanel.add(bRemove);
    fileButtonPanel.add(bClear);

    final JPanel buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.add(fileButtonPanel, BorderLayout.NORTH);

    final JPanel filesToConvertPanel = new JPanel(new BorderLayout(4, 4));
    filesToConvertPanel.add(new JLabel("Files to convert:"), BorderLayout.NORTH);
    filesToConvertPanel.add(new JScrollPane(list), BorderLayout.CENTER);
    filesToConvertPanel.add(buttonPanel, BorderLayout.EAST);

    final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    southPanel.add(bConvert);

    final JPanel panel = new JPanel(new BorderLayout(4, 4));
    panel.setBorder(new EmptyBorder(5, 6, 5, 6));
    panel.add(filesToConvertPanel, BorderLayout.CENTER);
    panel.add(southPanel, BorderLayout.SOUTH);
    this.content = panel;

    list.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        updateButtonsEnabled();
      }
    });
    fileListModel.addListDataListener(new ListDataListener() {
      public void contentsChanged(ListDataEvent e) {
        updateButtonsEnabled();
      }

      public void intervalRemoved(ListDataEvent e) {
        updateButtonsEnabled();
      }

      public void intervalAdded(ListDataEvent e) {
        updateButtonsEnabled();
      }
    });
    updateButtonsEnabled();
  }

  private void updateButtonsEnabled() {
    bRemove.setEnabled(!list.isSelectionEmpty());
    bClear.setEnabled(!fileListModel.isEmpty());
    bConvert.setEnabled(!fileListModel.isEmpty());
  }

  private void add() {
    JFileChooser chooser = new JFileChooser(currentDirectory);
    chooser.setDialogTitle("Open Java Source");
    chooser.setFileFilter(new FileFilter() {
      public String getDescription() {
        return "*.java";
      }

      public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".java");
      }
    });
    int result = chooser.showOpenDialog(content);
    if (result != JFileChooser.APPROVE_OPTION) {
      return;
    }
    File selectedFile = chooser.getSelectedFile();
    currentDirectory = selectedFile.getParentFile();
    fileListModel.addElement(selectedFile);
  }

  private void remove() {
    Object[] files = list.getSelectedValues();
    for (int i = 0; i < files.length; ++i) {
      fileListModel.removeElement(files[i]);
    }
  }

  private void clear() {
    fileListModel.clear();
  }

  private void convert() {
    JavaSourceConversionSettings settings = optionsPanel.getConversionSettings();
    settings.getConversionOptions().setShowJava2HtmlLink(true);

    //Collect statistical information
    final StringBuffer report = new StringBuffer();

    //Collect conversion-results
    StringWriter writer = new StringWriter();

    //Create the converter
    IJavaSourceConverter converter = settings.createConverter();

    Object[] files = fileListModel.toArray();
    try {
      converter.writeDocumentHeader(writer, settings.getConversionOptions(), "");

      for (int count = 0; count < files.length; ++count) {
        final File file = (File) files[count];
        report.append("<li>File " + (count + 1) + ": " + file.getName() + "<blockquote>");

        try {
          if (count > 0) {
            converter.writeBlockSeparator(writer, settings.getConversionOptions());
          }
          JavaSourceParser parser = new JavaSourceParser(settings.getConversionOptions());
          JavaSource source = parser.parse(file);
          converter.convert(source, settings.getConversionOptions(), writer);

          writer.write('\n');

          report.append(source.getStatistic().getScreenString("<br>") + "</blockquote></li>");
        }
        catch (IOException e) {
          //TODO Sep 13, 2004 (Markus Gebhard): Error handling!
          System.err.println("Error converting " + file + ": " + e);
        }
      }

      converter.writeDocumentFooter(writer, settings.getConversionOptions());
    }
    catch (IOException e) {
      //    TODO Sep 13, 2004 (Markus Gebhard): Error handling!
      System.err.println("Error converting files " + e);
    }

    //Copy result to system clipboard
    StringSelection sel = new StringSelection(writer.getBuffer().toString());
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(sel, sel);

    String plural = "";
    if (files.length > 1) {
      plural = "s";
    }

    //Show success-dialog

    final String title = "File" + plural + " converted";
    final String text = "<html><b>"
        + files.length
        + " File"
        + plural
        + " successfully converted.</b>"
        + "<ul>"
        + report.toString()
        + "</ul>"
        + "<b>The converted source code has been copied the system clipboard</b>"
        + "</html>";
    JOptionPane.showMessageDialog(content, text, title, JOptionPane.INFORMATION_MESSAGE);
  }

  public JComponent getContent() {
    return content;
  }

  public void actionPerformed(ActionEvent evt) {
    Object source = evt.getSource();
    if (source == bAdd) {
      add();
    }
    else if (source == bRemove) {
      remove();
    }
    else if (source == bClear) {
      clear();
    }
    else {
      convert();
    }
  }
}