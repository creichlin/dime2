package ch.kerbtier.dime2.site.renderer;

import static ch.kerbtier.dime2.ContainerFacade.getModules;

import java.io.IOException;

import ch.kerbtier.dime2.modules.Page;
import ch.kerbtier.helene.HObject;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.google.common.io.Files;

public class RenderHelper implements Helper<HObject> {
  
  

  @Override
  public CharSequence apply(HObject model, Options options) {
    
    Process p = Process.get();
    
    if(model == null) {
      throw new RuntimeException("model must not be null");
    }
    
    String style = options.param(0) == null ? "default" : (String)options.param(0);
    String ext = p.getExtension();
    
    Page page = getModules().getPage(model.getName() + "." + style + "." + ext);
    if (page == null) {
      throw new RuntimeException("found no template for " + model.getName() + "." + style + "." + ext);
    }
    try {
      Context context = Context.newBuilder(model).resolver(HNodeResolver.INSTANCE).build();
      return new Handlebars.SafeString(p.compile(page).apply(context));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
