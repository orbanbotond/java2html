package de.java2html.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * @author Markus Gebhard
 */
public class IoUtilities {
  private IoUtilities() {
    //no instance available
  }

  public static byte[] readBytes(InputStream inputStream) throws IOException {
    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    copyStream(inputStream, byteOut);
    return byteOut.toByteArray();
  }

  public static void copyStream(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[4096];
    while (true) {
      int bytesRead = in.read(buffer);
      if (bytesRead == -1) {
        break;
      }
      out.write(buffer, 0, bytesRead);
    }
  }

  public static void close(OutputStream outputStream) {
    if (outputStream != null) {
      try {
        outputStream.close();
      }
      catch (IOException e) {
        //nothing to do
      }
    }
  }

  public static void close(InputStream inputStream) {
    if (inputStream != null) {
      try {
        inputStream.close();
      }
      catch (IOException e) {
        //nothing to do
      }
    }
  }

  public static void close(Writer writer) {
    if (writer != null) {
      try {
        writer.close();
      }
      catch (IOException e) {
        //nothing to do
      }
    }
  }

  public static void close(Reader reader) {
    if (reader != null) {
      try {
        reader.close();
      }
      catch (IOException e) {
        //nothing to do
      }
    }
  }

  public static void copy(File sourceFile, File destinationFile) throws IOException {
    if (!ensureFoldersExist(destinationFile.getParentFile())) {
      throw new IOException("Unable to create necessary output directory " //$NON-NLS-1$
          + destinationFile.getParentFile());
    }
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    try {
      bis = new BufferedInputStream(new FileInputStream(sourceFile));
      bos = new BufferedOutputStream(new FileOutputStream(destinationFile));
      copyStream(bis, bos);
    }
    finally {
      close(bis);
      close(bos);
    }
  }

  public static boolean ensureFoldersExist(File folder) {
    if (folder.exists()) {
      return true;
    }
    return folder.mkdirs();
  }

  public static File exchangeFileExtension(File file, String newFileExtension) {
    String fileName = file.getAbsolutePath();
    int index = fileName.lastIndexOf('.');
    if (index == -1) {
      throw new IllegalStateException("Unable to determine file extension from file name '" //$NON-NLS-1$
          + fileName
          + "'"); //$NON-NLS-1$
    }
    return new File(fileName.substring(0, index + 1) + newFileExtension);
  }
}