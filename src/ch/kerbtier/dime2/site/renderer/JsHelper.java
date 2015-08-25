package ch.kerbtier.dime2.site.renderer;

import java.io.IOException;
import java.nio.file.Path;

import static ch.kerbtier.dime2.ContainerFacade.*;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class JsHelper implements Helper<String> {

  @Override
  public CharSequence apply(String file, Options arg) throws IOException {
    Process p = Process.get();

    Path path = p.getPath().resolve(file);

    path = getConfig().getModulesPath().relativize(path);

    p.addJs(path.toString());

    return new Handlebars.SafeString("<!-- JS " + file + " -->");
  }

}
