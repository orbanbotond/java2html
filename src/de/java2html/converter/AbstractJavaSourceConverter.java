package de.java2html.converter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import de.java2html.javasource.JavaSource;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.util.Ensure;

/**
 * Abstract superclass for all converters for converting a {@link de.java2html.javasource.JavaSource}
 * object to anything else.
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
public abstract class AbstractJavaSourceConverter implements IJavaSourceConverter {

  private final ConverterMetaData metaData;

  public AbstractJavaSourceConverter(ConverterMetaData metaData) {
    Ensure.ensureArgumentNotNull(metaData);
    this.metaData = metaData;
  }

  /**
   * Is called to convert the object 'source' to the destination
   * format. The result is stored in 'result' and can be retrieved
   * by calling getResult().
   */
  public final void convert(JavaSource source, JavaSourceConversionOptions options, Writer writer)
      throws IOException {
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(writer);
      convert(source, options, bw);
      bw.flush();
    }
    catch (IOException e) {
      throw e;
    }
  }

  public abstract void convert(JavaSource source, JavaSourceConversionOptions options, BufferedWriter writer)
      throws IOException;

  /**
   * Returns a header for the result document.
   * This one will be placed before the first block of converted
   * code.
   * Subclasses can return an empty String (&quot;&quot;) if there is none neccessary.
   * @param title 
   */
  public abstract String getDocumentHeader(JavaSourceConversionOptions options, String title);

  /**
   * Returns a footer for the result document.
   * This one will be placed behind the last block of converted
   * code.
   * Subclasses can return an empty String (&quot;&quot;) if there is none neccessary.
   */
  public abstract String getDocumentFooter(JavaSourceConversionOptions options);

  /**
   * Returns the code that has to be placed between two blocks
   * of converted code.
   * Subclasses can return an empty String (&quot;&quot;) if there is none neccessary.
   */
  public abstract String getBlockSeparator(JavaSourceConversionOptions options);

  public void writeDocumentHeader(Writer writer, JavaSourceConversionOptions options, String title)
      throws IOException {
    writer.write(getDocumentHeader(options, title));
  }

  public void writeDocumentFooter(Writer writer, JavaSourceConversionOptions options) throws IOException {
    writer.write(getDocumentFooter(options));
  }

  public void writeBlockSeparator(Writer writer, JavaSourceConversionOptions options) throws IOException {
    writer.write(getBlockSeparator(options));
  }

  public final String getDefaultFileExtension() {
    return metaData.getDefaultFileExtension();
  }
  
  public final ConverterMetaData getMetaData() {
    return metaData;
  }
}