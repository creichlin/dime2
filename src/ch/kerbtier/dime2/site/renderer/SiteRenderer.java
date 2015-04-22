package ch.kerbtier.dime2.site.renderer;

import java.io.IOException;
import java.io.PrintWriter;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import ch.kerbtier.dime2.modules.Page;
import ch.kerbtier.dime2.modules.PageContent;
import ch.kerbtier.helene.HNode;

public class SiteRenderer {

  private boolean development;
  final Handlebars handlebars = new Handlebars();

  public SiteRenderer(boolean development) {
    this.development = development;
    handlebars.registerHelper("render", new RenderHelper());
    handlebars.registerHelper("css", new CssHelper());
    handlebars.registerHelper("format", new FormatHelper());
    handlebars.registerHelper("link", new LinkHelper());
    handlebars.registerHelper("markdown", new MarkdownHelper());
    handlebars.registerHelper("image", new ImageHelper());
  }

  public void render(Page page, HNode model, PrintWriter writer) {
    Process process = new Process(this, page, model);
    writer.print(process.render());
  }

  public Template compile(PageContent content) throws IOException {
    return handlebars.compile(content);
  }
  
  public boolean isDevelopment() {
    return development;
  }
}
