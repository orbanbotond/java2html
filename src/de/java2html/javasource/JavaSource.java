package de.java2html.javasource;

/**
* This class represents java source code in a parsed, but still flat style.
* It contains the raw text along with an array of source type entries
* ({@link de.java2html.javasource.JavaSourceType}) for each character.
* <code>JavaSource</code> objects are created using the
* {@link de.java2html.javasource.JavaSourceParser}.
* 
* A <code>JavaSource</code> object can be pretty-printed to HTML by using the
* {@link de.java2html.converter.JavaSource2HTMLConverter}.
*
* For questions, suggestions, bug-reports, enhancement-requests etc. I may be contacted at:
*   <a href="mailto:markus@jave.de">markus@jave.de</a>
*
* The Java2html home page is located at:
*   <a href="http://www.java2html.de">http://www.java2html.de</a>
*
* @author  <a href="mailto:markus@jave.de">Markus Gebhard</a>
*
* <code>Copyright (C) Markus Gebhard 2000-2003
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
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.</code>
*
*/
public class JavaSource{
  /** The source code as raw text */
  private String source;
  
  /** Flags for every character in the source code telling
  the type. */
  private JavaSourceType[] types;
  
  private JavaSourceStatistic statistic;
 
  public JavaSource(String source){
    this.source   = source;
    statistic = new JavaSourceStatistic();
  }

  public JavaSourceType[] getClassification(){
    return types;
  }
  
  public void setClassification(JavaSourceType[] types){
    this.types=types;
  }
  
  public String getCode(){
    return source;
  }

  /**
  * Debug output of the code
  */
  public void print(){
    System.out.println("------------------------------");
    int start=0;
    int end=0;
    
    while(start<types.length){
      while(end<types.length-1 && types[end+1]==types[start]){
        ++end;
      }
       
      print(start,end);
      
      start=end+1;
      end=start;
    }
  }

  protected void print(int start, int end){
    System.out.print(types[start]+": ");
    System.out.println("@"+source.substring(start,end+1).replace('\n','#')+"@");
  }


  /**
  * Returns statistical information as String
  * @deprecated As of 26.02.2006 (Markus Gebhard), replaced by {@link #getStatistic()}
  */
  public String getStatisticsString(){
    /* output format (example):
     Lines total: 127  Code: 57  Comments: 16  Empty: 54
     3164 Characters, maximum line length: 95                */
    return statistic.getScreenString("\n");
  }
  
  public String getFileName(){
    return getStatistic().getFileName();
  }
  
  public void setFileName(String fileName){
    getStatistic().setFileName(fileName);
  }
 
  public int getLineCount(){
    return statistic.getLineCount();
  }

  public int getMaxLineLength(){
    return statistic.getMaxLineLength();
  }

  public JavaSourceStatistic getStatistic() {
    return statistic;
  }
  
  public JavaSourceIterator getIterator(){
    return new JavaSourceIterator(this);
  }
}