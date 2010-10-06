package de.java2html;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.java2html.gui.DirectTextConversionPanel;
import de.java2html.gui.FileConversionPanel;
import de.java2html.gui.GuiTools;
import de.java2html.gui.IStatisticsView;
import de.java2html.gui.Java2HtmlOptionsPanel;
import de.java2html.javasource.JavaSourceStatistic;

/**
 * Main application for the Java2Html converter.
 * 
 * For questions, suggestions, bug-reports, enhancement-requests etc. I may be
 * contacted at: <a href="mailto:markus@jave.de">markus@jave.de</a>
 * 
 * The Java2html home page is located at: <a href="http://www.java2html.de">
 * http://www.java2html.de</a>
 * 
 * @author <a href="mailto:markus@jave.de">Markus Gebhard</a>
 * @version 2.1, 06/30/02
 * 
 * Copyright (C) Markus Gebhard 2000-2002
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
public class Java2HtmlApplication {

  private final JFrame frame;

  private final JButton bExit;
  private final Java2HtmlOptionsPanel optionsPanel = new Java2HtmlOptionsPanel();

  public Java2HtmlApplication() {
    final JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("File Conversion", new FileConversionPanel(optionsPanel).getContent());
    tabbedPane.addTab("Direct Text Conversion", new DirectTextConversionPanel(
        optionsPanel,
        new IStatisticsView() {
          public void setStatistics(JavaSourceStatistic statistic) {
            //nothing to do
          }
        }).getContent());

    bExit = new JButton("Exit");
    bExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        System.exit(0);
      }
    });

    final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    southPanel.add(bExit);

    final JPanel pOptions = GuiTools.createBorderedPanel("Options");
    pOptions.setLayout(new BorderLayout());
    pOptions.add(optionsPanel.getContent(), BorderLayout.CENTER);

    JPanel p = new JPanel(new BorderLayout());
    p.add(pOptions, BorderLayout.NORTH);
    p.add(Box.createVerticalGlue(), BorderLayout.CENTER);

    frame = new JFrame(Version.getJava2HtmlConverterTitle());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout(4, 4));
    frame.getContentPane().add(p, BorderLayout.EAST);
    frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
  }

  private void show() {
    frame.pack();
    GuiTools.centerOnScreen(frame);
    frame.setVisible(true);
  }

  public static void main(String args[]) {
    if (args != null && args.length > 0) {
      Java2Html.main(args);
      return;
    }
    GuiTools.setNativeLookAndFeel();
    Java2HtmlApplication application = new Java2HtmlApplication();
    application.show();
  }
}