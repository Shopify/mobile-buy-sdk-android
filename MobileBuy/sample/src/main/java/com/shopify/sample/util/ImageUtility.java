package com.shopify.sample.util;

import android.net.Uri;

import java.net.URI;

import timber.log.Timber;

public final class ImageUtility {

  /**
   * Return the URL of an appropriate file to display for the given product image. This method
   * examines the default URL for the image, and returns the URL for a resized version of that
   * image which has dimensions that are at least as large as the specified width and height.
   *
   * @param featuredImageUrl    URL of image src.
   * @param imageTargetWidthPx  Width of the view that will display the image, in pixels.
   * @param imageTargetHeightPx Height of the view that will display the image, in pixels.
   * @return A URL string, or null, if one could not be generated.
   */
  public static String getSizedImageUrl(final String featuredImageUrl, int imageTargetWidthPx, int imageTargetHeightPx) {
    if (featuredImageUrl == null) {
      return null;
    }

    // The usage of both Java's URI and Android's Uri classes is intentional. The former provides public
    // getters to retrieve the path component, but a verbose constructor to build a URI out of its
    // components. The latter is immutable but provides a Builder class with a cleaner way of
    // altering a single component.

    try {
      URI originalUri = new URI(featuredImageUrl);
      String path = originalUri.getPath();
      String suffixedPath;
      String sizeSuffix = getImageSuffixForDimensions(imageTargetWidthPx, imageTargetHeightPx);
      int extensionSeparatorPos = path.lastIndexOf('.');
      if (-1 == extensionSeparatorPos) {
        // Shopify should always store the images in the CDN with an extension, even if you upload
        // the file without one. But just in case.
        suffixedPath = path + sizeSuffix;
      } else {
        suffixedPath = path.substring(0, extensionSeparatorPos) + sizeSuffix + path.substring(extensionSeparatorPos);
      }

      return Uri.parse(featuredImageUrl).buildUpon().path(suffixedPath).toString();
    } catch (Exception e) {
      Timber.v(e, "Getting sized image URL");
      return null;
    }
  }

  public static String getImageSuffixForDimensions(int width, int height) {
    final int pixels = Math.max(width, height);
    if (pixels <= 16) {
      return "_pico";
    } else if (pixels <= 32) {
      return "_icon";
    } else if (pixels <= 50) {
      return "_thumb";
    } else if (pixels <= 100) {
      return "_small";
    } else if (pixels <= 160) {
      return "_compact";
    } else if (pixels <= 240) {
      return "_medium";
    } else if (pixels <= 480) {
      return "_large";
    } else if (pixels <= 600) {
      return "_grande";
    } else if (pixels <= 1024) {
      return "_1024x1024";
    } else {
      return "_2048x2048";
    }
  }

  private ImageUtility() {
  }
}
