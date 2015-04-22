package ch.kerbtier.dime2.site.renderer;

import java.io.IOException;

import ch.kerbtier.helene.HSlug;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class LinkHelper implements Helper<Object> {

  @Override
  public CharSequence apply(Object p, Options arg) throws IOException {

    if (p instanceof HSlug) {
      return "/" + ((HSlug) p).getValue();
    } else {
      return "/" + p.toString();
    }
  }

}
