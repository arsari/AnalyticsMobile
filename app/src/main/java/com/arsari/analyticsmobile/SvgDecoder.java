package com.arsari.analyticsmobile;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.caverock.androidsvg.PreserveAspectRatio;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import java.io.IOException;
import java.io.InputStream;

/** Decodes an SVG internal representation from an {@link InputStream}. */
public class SvgDecoder implements ResourceDecoder<InputStream, SVG> {

  /**
   * "This function returns true if the given InputStream can be decoded by this decoder."
   *
   * The @NonNull annotation is a hint to the compiler that the given parameter is not null. If you
   * pass null to this function, the compiler will complain
   *
   * @param source The InputStream that contains the image data
   * @param options The options that were passed to the decoder.
   * @return A boolean value.
   */
  @Override
  public boolean handles(@NonNull InputStream source, @NonNull Options options) {
    // TODO: Can we tell?
    return true;
  }

  /**
   * It takes an InputStream, and returns a Resource<SVG> object
   *
   * @param source The InputStream that contains the SVG data.
   * @param width The desired width in pixels, or SIZE_ORIGINAL to indicate that you want the image at
   * its
   * @param height The height of the SVG.
   * @param options Options for the SVG decoder.
   * @return A resource of type SVG.
   */
  public Resource<SVG> decode(
      @NonNull InputStream source, int width, int height, @NonNull Options options)
      throws IOException {
    try {
      SVG svg = SVG.getFromInputStream(source);
      if (width != SIZE_ORIGINAL) {
        svg.setDocumentWidth(width);
      }
      if (height != SIZE_ORIGINAL) {
        svg.setDocumentHeight(height);
      }
      return new SimpleResource<>(svg);
    } catch (SVGParseException ex) {
      throw new IOException("Cannot load SVG from stream", ex);
    }
  }
}
