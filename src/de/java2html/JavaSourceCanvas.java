package de.java2html;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

import javax.swing.JComponent;

import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.javasource.JavaSourceType;
import de.java2html.options.JavaSourceStyleTable;
import de.java2html.util.RGB;

/**
* Experimental: A <code>java.awt.Canvas</code> for displaying parsed java source code as one pixel
* per character. The code will be displayed in colored pixels with one square block of pixels for
* each character in the code. At the moment there is not really any use for this class yet, however
* it could be cool to write a plugin for IDEs to be able to navigate in a source code by clicking in
* this overview component... Just an idea ;-)
*
* For questions, suggestions, bug-reports, enhancement-requests etc.
* I may be contacted at:
*   <a href="mailto:markus@jave.de">markus@jave.de</a>
*
* The Java2html home page is located at:
*   <a href="http://www.java2html.de">http://www.java2html.de</a>
*
* @author  <a href="mailto:markus@jave.de">Markus Gebhard</a>
* @version 2.0, 05/07/02
*
* Copyright (C) Markus Gebhard 2000-2002
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
public class JavaSourceCanvas extends JComponent{
  protected JavaSource source;
  protected int scale=1; //number of pixels per character
  private JavaSourceStyleTable colorTable = JavaSourceStyleTable.getDefault(); 
  
  public JavaSourceCanvas(JavaSource source){
    this.source=source;
  }
  
  /**
  * This method was once used for drawing the source code
  * (a pixel for each character)
  * I will leave it here - maybe someone will use it some day 
  */
  public Dimension getPreferredSize(){
    return new Dimension(scale*source.getMaxLineLength(),
                         scale*source.getLineCount());
  }

  /**
  * This method was once used for drawing the source code
  * (a pixel for each character).
  * I will leave it here - maybe someone will use it some day 
  */
  public void paint(Graphics g){
    //White background where source code
    g.setColor(Color.white);
    Dimension d=getPreferredSize();
    g.fillRect(0,0,d.width,d.height);
  
    int y=0;
  
    int index=0;
    StringTokenizer st=new StringTokenizer(source.getCode(),"\n\r",true);
    while (st.hasMoreTokens()){
      String line=st.nextToken();
  
      if (line.charAt(0)=='\n' || line.charAt(0)=='\r'){
        ++index;
        ++y;
      }else{
        paint(g, y, index, index+line.length()-1);
        index+=line.length();
      }
    }
  }

  /**
  *
  */
  protected void paint(Graphics g, int y, int start, int end){
    int x=0;
    int index1=start;
    int index2=start;
  
    JavaSourceType[] sourceTypes=source.getClassification();

    while(index2<=end){
      while(index2<end && sourceTypes[index2+1]==sourceTypes[index1]){
        ++index2;
      }
      
      if (sourceTypes[index1]!=JavaSourceType.BACKGROUND){
        g.setColor(getAwtColor(colorTable.get(sourceTypes[index1]).getColor()));
        
        if (scale==1)
          g.drawLine(x,                 y,
                     (x+index2-index1), y);
        else
        if (scale==2){
          g.drawLine(2*x,                   2*y,
                     2*(x+index2-index1)+1, 2*y);
          g.drawLine(2*x,                   2*y+1,
                     2*(x+index2-index1)+1, 2*y+1);
        }
        else
          System.err.println("scale>2 not implemented yet!");
      }
      
      x+=index2-index1+1;
      
      index1=index2+1;
      index2=index1;
    }
  }

  private Color getAwtColor(RGB rgb) {
    return new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
  }

  public static void main(String args[]) throws Exception{
    JavaSourceParser parser = new JavaSourceParser();
    JavaSource j = parser.parse(new java.io.File("f:\\eclipse\\de\\java2html\\JavaSource2TeXConverter.java"));
    JavaSourceCanvas canny=new JavaSourceCanvas(j);
    Frame f=new Frame();
    f.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        System.exit(0);
      }
    });
  
    f.setLayout(new BorderLayout());
    f.add(canny, BorderLayout.CENTER);
    f.pack();
    f.setVisible(true);
  }
}