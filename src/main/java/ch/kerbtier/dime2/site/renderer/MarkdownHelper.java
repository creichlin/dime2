package ch.kerbtier.dime2.site.renderer;

import java.io.IOException;

import org.pegdown.PegDownProcessor;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class MarkdownHelper implements Helper<String>{

  @Override
  public CharSequence apply(String source, Options arg) throws IOException {
    if(source != null) {
    return  new Handlebars.SafeString(new PegDownProcessor().markdownToHtml(source));
    }
    return new Handlebars.SafeString("");
  }

}
