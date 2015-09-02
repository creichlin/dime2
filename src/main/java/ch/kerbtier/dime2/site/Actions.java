package ch.kerbtier.dime2.site;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.lesscss.LessCompiler;

import ch.kerbtier.amarillo.Route;
import ch.kerbtier.dime2.Config;
import ch.kerbtier.dime2.Models;
import ch.kerbtier.dime2.Modules;
import ch.kerbtier.dime2.Response;
import ch.kerbtier.dime2.modules.ModuleInfo.Mapping;
import ch.kerbtier.dime2.modules.Page;
import ch.kerbtier.dime2.site.renderer.SiteRenderer;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.webb.di.InjectRequest;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class Actions {

  private static int CACHE_SECONDS = 60 * 60 * 24 * 356;

  @InjectSingleton
  private Modules modules;

  @InjectSingleton
  private Models models;

  @InjectRequest
  private HttpServletResponse httpResponse;

  @InjectRequest
  private Response response;

  @InjectSingleton
  private SiteRenderer siteRenderer;

  @InjectSingleton
  private Config config;

  @Route(pattern = "(.*)")
  public void site(String slug) {
    Mapping mapping = modules.getMapping(slug);

    if (mapping != null) {
      serveMapping(mapping);
    } else {
      serveHtml(slug, modules);
    }
  }

  private void serveMapping(Mapping mapping) {
    Page page = modules.getPage(mapping.getTemplate());

    try {

      HNode model = models.get(mapping.getModel());

      if (model == null) {
        httpResponse.setStatus(404);
        return;
      }

      httpResponse.setContentType(mapping.getMime() + ";charset=utf-8");
      PrintWriter pw = httpResponse.getWriter();
      siteRenderer.render(page, model, pw);
      pw.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void serveHtml(String slug, Modules m) {
    Page page = m.getRoot();

    httpResponse.setContentType("text/html;charset=utf-8");
    try {

      HNode model = models.get("#" + slug);

      if (model == null) {
        PrintWriter pw = httpResponse.getWriter();
        pw.print("<html><head></head><body>Page not found. Please go to the <a href=\"/\">main site</a>.</body>");
        pw.close();
        httpResponse.setStatus(404);
        return;
      }

      PrintWriter pw = httpResponse.getWriter();
      siteRenderer.render(page, model, pw);
      pw.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Route(pattern = "modules/(.*)\\.js")
  public void processJs(String path) {
    response.setFile(config.getModulesPath(path + ".js"));
    response.setContentType("text/javascript;charset=UTF-8");
  }

  @Route(pattern = "modules/(.*)\\.css")
  public void processLessCss(String path) {
    LessCompiler lcc = new LessCompiler();
    try {
      response.setContent(lcc.compile(config.getModulesPath(path + ".css").toFile()));
    } catch (Exception e) {
      e.printStackTrace();
      response.setFile(config.getModulesPath(path + ".css"));
    }
    response.setContentType("text/css;charset=UTF-8");
  }

  @Route(pattern = "ic/([A-Za-z0-9_/-]*)\\.png")
  public void processImage(String name) {
    response.setFile(config.getImageCache(name + ".png"));
    response.setContentType("image/png");

    long expiry = new Date().getTime() + CACHE_SECONDS * 1000;
    httpResponse.setDateHeader("Expires", expiry);
    httpResponse.setHeader("Cache-Control", "max-age=" + CACHE_SECONDS);
  }

  @Route(pattern = "ic/([A-Za-z0-9_/-]*)\\.jpeg")
  public void processImageJpeg(String name) {
    response.setFile(config.getImageCache(name + ".jpeg"));
    response.setContentType("image/jpeg");

    long expiry = new Date().getTime() + CACHE_SECONDS * 1000;
    httpResponse.setDateHeader("Expires", expiry);
    httpResponse.setHeader("Cache-Control", "max-age=" + CACHE_SECONDS);
  }

  @Route(pattern = "ic/([A-Za-z0-9_/-]*)\\.css")
  public void processCachedCss(String name) {
    response.setFile(config.getImageCache(name + ".css"));
    response.setContentType("text/css");

    long expiry = new Date().getTime() + CACHE_SECONDS * 1000;
    httpResponse.setDateHeader("Expires", expiry);
    httpResponse.setHeader("Cache-Control", "max-age=" + CACHE_SECONDS);
  }

  @Route(pattern = "modules/(.*?)\\.png")
  public void normalImage(String name) {
    response.setFile(config.getModulesPath(name + ".png"));
    response.setContentType("image/png");
  }
}
