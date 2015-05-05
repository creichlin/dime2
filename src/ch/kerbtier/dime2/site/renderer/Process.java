package ch.kerbtier.dime2.site.renderer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Template;

import static ch.kerbtier.dime2.ContainerFacade.*;
import ch.kerbtier.dime2.modules.Page;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.webb.cssopti.CssOpti;

public class Process {

  private static ThreadLocal<Process> local = new ThreadLocal<>();
  
  private Page page;
  private HNode model;
  private SiteRenderer sr;
  
  // current state
  private Page currentPage;
  
  private List<String> css = new ArrayList<>();

  public Process(SiteRenderer sr, Page page, HNode model) {
    this.sr = sr;
    this.page = page;
    this.model = model;
  }

  public String render() {
    local.set(this);
    Context context = Context.newBuilder(model).resolver(HNodeResolver.INSTANCE).build();

    try {
      Template template = compile(page);
      String code = template.apply(context);
      
      String cssc = "";
      
      
      if(sr.isDevelopment()) {
        for(String cssf: css) {
          cssc += "<link rel=\"stylesheet\" href=\"/modules/" + cssf + "\">\n";
        }
      } else {
        
        CssOpti opti = new CssOpti(getConfig().getImageCache());
        
        for(String cssf: css) {
          Path fp = getConfig().getModulesPath(cssf);
          opti.add(fp);
        }
        
        String key = opti.getId() + ".css";

        if(!Files.exists(getConfig().getImageCache(key))) {
          FileUtils.writeStringToFile(getConfig().getImageCache(key).toFile(), opti.process());
        }
        
        cssc = "<link rel=\"stylesheet\" href=\"/ic/" + key + "\">\n";
      }
      
      code = code.replace("[[CSS]]", cssc);
      
      return code;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      local.set(null);
    }
  }

  public static Process get() {
    return local.get();
  }

  public Template compile(Page page_) throws IOException {
    this.currentPage = page_;
    return sr.compile(page_.getContent());
  }

  /**
   * gets the currently processed pages folder
   * @return
   */
  public Path getPath() {
    return currentPage.getPath().getParent();
  }

  /**
   * gets the currently processed pages extension
   * @return
   */
  public String getExtension() {
    return com.google.common.io.Files.getFileExtension(currentPage.getPath().toString());
  }

  public void addCss(String path) {
    css.add(path);
  }
}
