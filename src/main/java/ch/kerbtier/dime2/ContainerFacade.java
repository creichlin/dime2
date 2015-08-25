package ch.kerbtier.dime2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.picocontainer.DefaultPicoContainer;

import ch.kerbtier.amarillo.Router;
import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.auth.Authentication;
import ch.kerbtier.dime2.site.renderer.SiteRenderer;
import ch.kerbtier.webb.transform2d.ImageTransformer;
import ch.kerbtier.webb.util.ContextInfo;
import ch.kerbtier.webb.util.HttpInfo;

public class ContainerFacade {
  private static ThreadLocal<DefaultPicoContainer> instance = new ThreadLocal<>();

  public static void set(DefaultPicoContainer pc) {
    instance.set(pc);
  }

  public static HttpInfo getHttpInfo() {
    return instance.get().getComponent(HttpInfo.class);
  }

  public static ContextInfo getContextInfo() {
    return instance.get().getComponent(ContextInfo.class);
  }

  public static HttpServletResponse getHttpResponse() {
    return instance.get().getComponent(HttpServletResponse.class);
  }

  public static Config getConfig() {
    return instance.get().getComponent(Config.class);
  }

  public static Router getDispatchers() {
    return instance.get().getComponent(Router.class);
  }

  public static Response getResponse() {
    return instance.get().getComponent(Response.class);
  }

  public static AdminRoot getAdminRoot() {
    return instance.get().getComponent(AdminRoot.class);
  }
  
  public static HttpServletRequest getHttpRequest() {
    return instance.get().getComponent(HttpServletRequest.class);
  }
  
  public static Models getModels() {
    return instance.get().getComponent(Models.class);
  }

  public static Views getViews() {
    return instance.get().getComponent(Views.class);
  }

  public static Modules getModules() {
    return instance.get().getComponent(Modules.class);
  }
  
  public static SiteRenderer getSiteRenderer() {
    return instance.get().getComponent(SiteRenderer.class);
  }
  
  public static ImageTransformer getImageTransformer() {
    return instance.get().getComponent(ImageTransformer.class);
  }

  public static Authentication getAuthentication() {
    return instance.get().getComponent(Authentication.class);
  }
}
