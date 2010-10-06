package de.java2html;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.java2html.gui.DirectTextConversionPanel;
import de.java2html.gui.GuiTools;
import de.java2html.gui.IStatisticsView;
import de.java2html.gui.Java2HtmlOptionsPanel;
import de.java2html.javasource.JavaSourceStatistic;

/**
 * Applet for the Java2Html converter.
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
 * Copyright (C) Markus Gebhard 2000-2003
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
public class Java2HtmlApplet extends JApplet {
  private static final String EMPTY_STATISTICS_TEXT = "<html>-<br>-<br>-</html>";

  private JLabel lStatistics;
  private Java2HtmlOptionsPanel optionsPanel;

  /**
   * Applet info.
   */
  public String getAppletInfo() {
    return Version.getJava2HtmlAppletTitle();
  }

  public void init() {
    try {
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          createGui();
        }
      });
    }
    catch (Exception e) {
      System.err.println("createGui didn't successfully complete"); //$NON-NLS-1$
    }
  }

  private void createGui() {
    GuiTools.setNativeLookAndFeel();
    optionsPanel = new Java2HtmlOptionsPanel();
    lStatistics = new JLabel(EMPTY_STATISTICS_TEXT);

    final DirectTextConversionPanel directTextConversionPanel = new DirectTextConversionPanel(
        optionsPanel,
        new IStatisticsView() {
          public void setStatistics(JavaSourceStatistic statistic) {
            lStatistics.setText(statistic == null ? EMPTY_STATISTICS_TEXT : "<html>"
                + statistic.getScreenString("<br>")
                + "</html>");
          }
        });

    JPanel statisticsPanel = GuiTools.createBorderedPanel("Statistics");
    statisticsPanel.add(lStatistics);
    JPanel optionsPanelComponent = GuiTools.createBorderedPanel("Options");
    optionsPanelComponent.add(optionsPanel.getContent());

    JPanel eastPanel = new JPanel(new GridBagLayout());
    final GridBagConstraints c1 = new GridBagConstraints();
    c1.fill = GridBagConstraints.HORIZONTAL;
    c1.gridx = 0;
    c1.anchor = GridBagConstraints.NORTHWEST;
    eastPanel.add(optionsPanelComponent, c1);
    eastPanel.add(statisticsPanel, c1);
    c1.fill = GridBagConstraints.BOTH;
    c1.weighty = 1.0;
    eastPanel.add(Box.createVerticalGlue(), c1);

    Container container = getContentPane();
    container.setLayout(new BorderLayout(4, 4));
    container.add(directTextConversionPanel.getContent(), BorderLayout.CENTER);
    container.add(eastPanel, BorderLayout.EAST);
    directTextConversionPanel.requestFocus();
  }

  public void start() {
    //nothing to do
  }

  public void stop() {
    //nothing to do
  }

  public static void main(String[] args) {
    //Create frame to run applet in
    JFrame appletFrame = new JFrame("Applet viewer frame");

    appletFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Setup layout manager
    appletFrame.setLayout(new GridLayout());

    //Create applet instanse (inset the name of your applet)
    Java2HtmlApplet myApplet = new Java2HtmlApplet();

    //Add the applet to the frame
    appletFrame.getContentPane().add(myApplet, BorderLayout.CENTER);

    //Set size of the frame (It can be resized using the mouse)
    appletFrame.setSize(700, 420);

    //Initialize the applet
    myApplet.init();

    //Start the applet
    myApplet.start();

    //Make the frame visible
    appletFrame.setVisible(true);

    appletFrame.setResizable(false);
  }
}