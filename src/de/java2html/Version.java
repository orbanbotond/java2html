package de.java2html;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Version {
  private static final String BUNDLE_NAME = "de.java2html.version"; //$NON-NLS-1$
  private static ResourceBundle resourceBundle;

  private static String getString(String key, String fallback) {
    try {
      return getResourceBundle().getString(key);
    }
    catch (MissingResourceException e) {
      return fallback;
    }
  }

  private static ResourceBundle getResourceBundle() {
    if (resourceBundle == null) {
      resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
    }
    return resourceBundle;
  }

  public static String getFullVersionNumber() {
    return getString("Version.version", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static String getBuildDate() {
    return getString("Version.buildDate", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public final static String getJava2HtmlConverterTitle() {
    return "Java2Html Converter " + getFullVersionNumber();
  }

  public final static String getJava2HtmlAppletTitle() {
    return "Java2Html Applet " + getFullVersionNumber();
  }
}