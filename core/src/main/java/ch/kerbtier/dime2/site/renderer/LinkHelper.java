package ch.kerbtier.dime2.site.renderer;

import java.io.IOException;

import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HSlug;
import ch.kerbtier.webb.di.InjectSingleton;
import ch.kerbtier.webb.util.ContextInfo;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

@Inject
public class LinkHelper implements Helper<Object> {

  @InjectSingleton
  private ContextInfo contextInfo;
  
  @Override
  public CharSequence apply(Object p, Options arg) throws IOException {

    if (p instanceof HSlug) {
      return contextInfo.getPath() + "/" + ((HSlug) p).getValue();
    } else {
      return contextInfo.getPath() + "/" + p.toString();
    }
  }

}
