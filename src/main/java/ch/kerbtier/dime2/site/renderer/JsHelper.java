package ch.kerbtier.dime2.site.renderer;

import java.io.IOException;
import java.nio.file.Path;

import ch.kerbtier.dime2.Config;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectSingleton;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

@Inject
public class JsHelper implements Helper<String> {
  
  @InjectSingleton
  private Config config;

  @Override
  public CharSequence apply(String file, Options arg) throws IOException {
    Process p = Process.get();

    Path path = p.getPath().resolve(file);

    path = config.getModulesPath().relativize(path);

    p.addJs(path.toString());

    return new Handlebars.SafeString("<!-- JS " + file + " -->");
  }

}
