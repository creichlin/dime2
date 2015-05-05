package ch.kerbtier.dime2.site;

import static ch.kerbtier.dime2.ContainerFacade.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.lesscss.LessCompiler;

import ch.kerbtier.dime2.Modules;
import ch.kerbtier.dime2.modules.ModuleInfo.Mapping;
import ch.kerbtier.dime2.modules.Page;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.webb.dispatcher.Action;

public class Actions {

  private static int CACHE_SECONDS = 60 * 60 * 24 * 356;

  @Action(pattern = "(.*)")
  public void site(String slug) {
    Modules m = getModules();

    Mapping mapping = m.getMapping(slug);

    if (mapping != null) {
      serveMapping(mapping);
    } else {
      serveHtml(slug, m);
    }
  }

  private void serveMapping(Mapping mapping) {
    Modules m = getModules();
    Page page = m.getPage(mapping.getTemplate());
    
    try {

      HNode model = getModels().get(mapping.getModel());

      if (model == null) {
        getHttpResponse().setStatus(404);
        return;
      }

      getHttpResponse().setContentType(mapping.getMime() + ";charset=utf-8");
      PrintWriter pw = getHttpResponse().getWriter();
      getSiteRenderer().render(page, model, pw);
      pw.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }  }

  private void serveHtml(String slug, Modules m) {
    Page page = m.getRoot();

    getHttpResponse().setContentType("text/html;charset=utf-8");
    try {

      HNode model = getModels().get("#" + slug);

      if (model == null) {
        PrintWriter pw = getHttpResponse().getWriter();
        pw.print("<html><head></head><body>Page not found. Please go to the <a href=\"/\">main site</a>.</body>");
        pw.close();
        getHttpResponse().setStatus(404);
        return;
      }

      PrintWriter pw = getHttpResponse().getWriter();
      getSiteRenderer().render(page, model, pw);
      pw.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Action(pattern = "modules/(.*)\\.js")
  public void processJs(String path) {
    getResponse().setFile(getConfig().getModulesPath(path + ".js"));
    getResponse().setContentType("text/javascript;charset=UTF-8");
  }

  @Action(pattern = "modules/(.*)\\.css")
  public void processLessCss(String path) {
    LessCompiler lcc = new LessCompiler();
    try {
      getResponse().setContent(lcc.compile(getConfig().getModulesPath(path + ".css").toFile()));
    } catch (Exception e) {
      e.printStackTrace();
      getResponse().setFile(getConfig().getModulesPath(path + ".css"));
    }
    getResponse().setContentType("text/css;charset=UTF-8");
  }

  @Action(pattern = "ic/([A-Za-z0-9_/-]*)\\.png")
  public void processImage(String name) {
    getResponse().setFile(getConfig().getImageCache(name + ".png"));
    getResponse().setContentType("image/png");

    long expiry = new Date().getTime() + CACHE_SECONDS * 1000;
    getHttpResponse().setDateHeader("Expires", expiry);
    getHttpResponse().setHeader("Cache-Control", "max-age=" + CACHE_SECONDS);
  }

  @Action(pattern = "ic/([A-Za-z0-9_/-]*)\\.jpeg")
  public void processImageJpeg(String name) {
    getResponse().setFile(getConfig().getImageCache(name + ".jpeg"));
    getResponse().setContentType("image/jpeg");

    long expiry = new Date().getTime() + CACHE_SECONDS * 1000;
    getHttpResponse().setDateHeader("Expires", expiry);
    getHttpResponse().setHeader("Cache-Control", "max-age=" + CACHE_SECONDS);
  }

  @Action(pattern = "ic/([A-Za-z0-9_/-]*)\\.css")
  public void processCachedCss(String name) {
    getResponse().setFile(getConfig().getImageCache(name + ".css"));
    getResponse().setContentType("text/css");

    long expiry = new Date().getTime() + CACHE_SECONDS * 1000;
    getHttpResponse().setDateHeader("Expires", expiry);
    getHttpResponse().setHeader("Cache-Control", "max-age=" + CACHE_SECONDS);
  }

  @Action(pattern = "modules/(.*?)\\.png")
  public void normalImage(String name) {
    getResponse().setFile(getConfig().getModulesPath(name + ".png"));
    getResponse().setContentType("image/png");
  }
}
