package ch.kerbtier.dime2.server;

import ch.kerbtier.amarillo.Router;
import ch.kerbtier.dime2.AdminDispatchers;
import ch.kerbtier.dime2.Config;
import ch.kerbtier.dime2.Models;
import ch.kerbtier.dime2.Modules;
import ch.kerbtier.dime2.Response;
import ch.kerbtier.dime2.Views;
import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.admin.actions.Events;
import ch.kerbtier.dime2.admin.actions.Main;
import ch.kerbtier.dime2.admin.actions.Static;
import ch.kerbtier.dime2.auth.Authentication;
import ch.kerbtier.dime2.site.Actions;
import ch.kerbtier.dime2.site.renderer.SiteRenderer;
import ch.kerbtier.esdi.Esdi;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectRequest;
import ch.kerbtier.webb.di.InjectSession;
import ch.kerbtier.webb.di.InjectSingleton;
import ch.kerbtier.webb.transform2d.ImageTransformer;
import ch.kerbtier.webb.util.Configuration;
import ch.kerbtier.webb.util.ContextInfo;
import ch.kerbtier.webb.util.HttpInfo;

@Inject
public class StartContext {
  
  @InjectSingleton
  private ContextInfo contextInfo;
  
  public StartContext() {
    System.out.println("construct context " + contextInfo);
  }

  public void run() {
    
    System.out.println("start context...");
    System.out.println("contextInfo: " + contextInfo);
    
    
    Esdi.onRequestFor(Router.class).with(InjectSingleton.class).deliver(Router.class);
    Esdi.onRequestFor(Configuration.class).with(InjectSingleton.class).deliver(Configuration.class);
    
    Esdi.onRequestFor(Config.class).with(InjectSingleton.class).deliver(Config.class);

    Esdi.onRequestFor(Authentication.class).with(InjectSingleton.class).deliver(Authentication.class);

    Esdi.onRequestFor(Modules.class).with(InjectSingleton.class).deliver(Modules.class);
    Esdi.onRequestFor(Models.class).with(InjectSingleton.class).deliver(Models.class);
    Esdi.onRequestFor(Views.class).with(InjectSingleton.class).deliver(Views.class);
    
    Esdi.onRequestFor(SiteRenderer.class).with(InjectSingleton.class).deliver(SiteRenderer.class);
    Esdi.onRequestFor(ImageTransformer.class).with(InjectSingleton.class).deliver(ImageTransformer.class);
    
    // SessionRegistration
    Esdi.onRequestFor(AdminRoot.class).with(InjectSession.class).deliver(AdminRoot.class);
    
    // Request registration
    Esdi.onRequestFor(Response.class).with(InjectRequest.class).deliver(Response.class);
    Esdi.onRequestFor(HttpInfo.class).with(InjectRequest.class).deliver(HttpInfo.class);

    
    
    Router router = Esdi.get(Router.class, InjectSingleton.class);
    
    router.register(AdminDispatchers.class);
    router.register(Main.class);
    router.register(Events.class);
    router.register(Static.class);
    router.register(Actions.class);
    
    System.out.println("Servlet version: " + Esdi.get(ContextInfo.class, InjectSingleton.class).getVersion());
  }

}
