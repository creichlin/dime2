package ch.kerbtier.dime2.site.renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import ch.kerbtier.dime2.Config;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HBlob;
import ch.kerbtier.lanthanum.Vec2i;
import ch.kerbtier.webb.di.InjectSingleton;
import ch.kerbtier.webb.transform2d.ImageTransformer;
import ch.kerbtier.webb.util.ContextInfo;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

@Inject
public class PdfHelper implements Helper<Object> {
  private static Map<String, Vec2i> sizes = Collections.synchronizedMap(new HashMap<String, Vec2i>());
  private static Map<String, String> extensions = Collections.synchronizedMap(new HashMap<String, String>());

  @InjectSingleton
  private Config config;

  @InjectSingleton
  private ContextInfo contextInfo;

  @InjectSingleton
  private ImageTransformer imageTransformer;

  /**
   * gets pdf from blob and returns link to it
   */
  @Override
  public CharSequence apply(Object data, Options arg) throws IOException {
    HBlob blob = null;

    if (data == null) {
      return "nopdf";
    } else if (data instanceof HBlob) {
      blob = ((HBlob) data);
    } else {
      return "invalidpdfdata";
    }

    if (blob.getHash() == null) {
      return "nopdfdata";
    }

    try {

      // create key of image using transformation code (which defines resulting
      // image size) and hash over image data
      MessageDigest md = MessageDigest.getInstance("md5");
      md.update(arg.param(0).toString().getBytes());
      md.update(blob.getHash().getBytes());
      String key = DatatypeConverter.printHexBinary(md.digest());

      if (Files.exists(config.getImageCache(key + ".pdf"))) {
        // file is calculated already, don't do anything
      } else {
        // construct path of pdf in local cache
        Path localPath = config.getImageCache(key + ".pdf");

        // create parent directory of image path
        if (!Files.exists(localPath.getParent())) {
          Files.createDirectories(localPath.getParent());
        }

        com.google.common.io.Files.write(blob.asArray(), localPath.toFile());
      }

      // get alt attribute from parameter
      String alt = arg.param(0, "document");

      // construct url
      String url = contextInfo.getPath() + "/ic/" + key + "/" + alt + ".pdf";
      return new Handlebars.SafeString(url);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
