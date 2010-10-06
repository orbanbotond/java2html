package de.java2html.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * A toolbox contaning useful tools for the graphical user interface.
 * 
 * (The open source version only contains one methode)
 * 
 * For questions, suggestions, bug-reports, enhancement-requests etc. I may be
 * contacted at: <a href="mailto:markus@jave.de">markus@jave.de</a>
 * 
 * The Java2html home page is located at: <a href="http://www.java2html.de">
 * http://www.java2html.de</a>
 * 
 * @author <a href="mailto:markus@jave.de">Markus Gebhard</a>
 * @version 2.0, 05/07/02
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
public class GuiTools {
  /** No instance available - just static methodes */
  private GuiTools() {
    //nothing to do
  }

  public final static void centerOnScreen(Window window) {
    Toolkit tk = Toolkit.getDefaultToolkit();

    Dimension dScreen = tk.getScreenSize();
    Dimension d = window.getSize();

    int x0 = (dScreen.width - d.width) / 2;
    int y0 = (dScreen.height - d.height) / 2;

    window.setLocation(x0, y0);
  }

  public final static JPanel createBorderedPanel(String title) {
    final JPanel panel = new JPanel();
    panel.setBorder(new CompoundBorder(new TitledBorder(title), new EmptyBorder(5, 6, 5, 6)));
    return panel;
  }

  public static void setNativeLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Error setting native LAF: " + e); //$NON-NLS-1$
    }
  }
}