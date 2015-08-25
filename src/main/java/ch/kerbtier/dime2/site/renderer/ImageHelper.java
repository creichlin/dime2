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

import ch.kerbtier.dime2.ContainerFacade;
import ch.kerbtier.helene.HBlob;
import ch.kerbtier.lanthanum.Vec2i;
import ch.kerbtier.webb.pictomizer.Pictomizer;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class ImageHelper implements Helper<Object> {
  private static Map<String, Vec2i> sizes = new HashMap<>();
  private static Map<String, String> extensions = new HashMap<>();

  @Override
  public CharSequence apply(Object data, Options arg) throws IOException {
    HBlob blob = null;

    if (data == null) {
      return "noimg";
    } else if(data instanceof HBlob) {
      blob = ((HBlob) data);
    } else if(data instanceof String) {
      Path localImage = Process.get().getPath().resolve((String)data);
      blob = new FileBlob(localImage);
    }

    if (blob.getHash() == null) {
      return "noimgdata";
    }

    try {
      MessageDigest md = MessageDigest.getInstance("md5");
      md.update(arg.param(0).toString().getBytes());
      md.update(blob.getHash().getBytes());
      String key = DatatypeConverter.printHexBinary(md.digest());

      
      Vec2i size = sizes.get(key);
      String ext = extensions.get(key);
      Path p = ContainerFacade.getConfig().getImageCache(key + "." + ext);

      if (ext != null && size != null && Files.exists(p)) {
        // file is calculated already, we don't need to do anything
      } else {
        BufferedImage bi = ContainerFacade.getImageTransformer().transform(blob.asBuffer(), (String) arg.param(0));
        Pictomizer picto = new Pictomizer(bi);
        ext = picto.getFormat();
        extensions.put(key, ext);
        p = ContainerFacade.getConfig().getImageCache(key + "." + ext);
        
        if (!Files.exists(p.getParent())) {
          Files.createDirectories(p.getParent());
        }
        ImageIO.write(picto.getTarget(), picto.getFormat(), p.toFile());
        size = new Vec2i(bi.getWidth(), bi.getHeight());
        sizes.put(key, size);
      }
      
      String alt = arg.param(1, "");
      
      if(alt.equals("url")) {
        return new Handlebars.SafeString("/ic/" + key + "." + ext);
      } else {
        return new Handlebars.SafeString("<img src=\"/ic/" + key + "." + ext + "\" width=\"" + size.getX() + "\" height=\"" + size.getY() + "\" alt=\"" + alt + "\">");
      }
      
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
