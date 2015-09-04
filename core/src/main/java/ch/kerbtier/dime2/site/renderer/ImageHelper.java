package ch.kerbtier.dime2.site.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import ch.kerbtier.dime2.Config;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HBlob;
import ch.kerbtier.lanthanum.Vec2i;
import ch.kerbtier.webb.di.InjectSingleton;
import ch.kerbtier.webb.pictomizer.Pictomizer;
import ch.kerbtier.webb.transform2d.ImageTransformer;
import ch.kerbtier.webb.util.ContextInfo;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

@Inject
public class ImageHelper implements Helper<Object> {
  private static Map<String, Vec2i> sizes = new HashMap<>();
  private static Map<String, String> extensions = new HashMap<>();

  @InjectSingleton
  private Config config;

  @InjectSingleton
  private ContextInfo contextInfo;

  @InjectSingleton
  private ImageTransformer imageTransformer;

  /**
   * gets url/imageData from first parameter second parameter is code that
   * applies transformations/filter/resizing to image if third parameter is
   * "url" return the url where the image is reachable otherwise use the third
   * parameter as alt content for the generated img tag
   */
  @Override
  public CharSequence apply(Object data, Options arg) throws IOException {
    HBlob blob = null;

    if (data == null) {
      return "noimg";
    } else if (data instanceof HBlob) {
      blob = ((HBlob) data);
    } else if (data instanceof String) {
      Path localImage = Process.get().getPath().resolve((String) data);
      blob = new FileBlob(localImage);
    }

    if (blob.getHash() == null) {
      return "noimgdata";
    }

    try {

      // create key of image using transformation code (which defines resulting
      // image size) and hash over image data
      MessageDigest md = MessageDigest.getInstance("md5");
      md.update(arg.param(0).toString().getBytes());
      md.update(blob.getHash().getBytes());
      String key = DatatypeConverter.printHexBinary(md.digest());

      // get resulting size and extension of image if already calculated
      Vec2i size = sizes.get(key);
      String ext = extensions.get(key);

      if (ext != null && size != null && Files.exists(config.getImageCache(key + "." + ext))) {
        // file is calculated already, don't do anything
      } else {
        // create image from blob and transform it
        BufferedImage bi = imageTransformer.transform(blob.asBuffer(), (String) arg.param(0));

        // identify image format
        Pictomizer picto = new Pictomizer(bi);
        ext = picto.getFormat();

        // cache image extension
        extensions.put(key, ext);

        // update size
        size = new Vec2i(bi.getWidth(), bi.getHeight());
        // cache image size
        sizes.put(key, size);

        // construct path of image in local cache
        Path localPath = config.getImageCache(key + "." + ext);

        // create parent directory of image path
        if (!Files.exists(localPath.getParent())) {
          Files.createDirectories(localPath.getParent());
        }

        // write image data to proper location with correct extension
        ImageIO.write(picto.getTarget(), picto.getFormat(), localPath.toFile());
      }

      // get alt attribute from parameter
      String alt = arg.param(1, "");

      // construct url
      String url = contextInfo.getPath() + "/ic/" + key + "." + ext;

      // if alt parameter equals "url" return just url
      if (alt.equals("url")) {
        return new Handlebars.SafeString(url);
      } else { // otherwise return whole image tag
        return new Handlebars.SafeString("<img src=\"" + url + "\" width=\"" + size.getX() + "\" height=\""
            + size.getY() + "\" alt=\"" + alt + "\">");
      }

    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
