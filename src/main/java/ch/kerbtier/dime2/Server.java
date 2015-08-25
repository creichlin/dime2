package ch.kerbtier.dime2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;

import com.google.gson.Gson;

import ch.kerbtier.amarillo.Call;
import ch.kerbtier.amarillo.Router;
import ch.kerbtier.amarillo.Verb;
import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.admin.actions.Events;
import ch.kerbtier.dime2.admin.actions.Main;
import ch.kerbtier.dime2.admin.actions.Static;
import ch.kerbtier.dime2.auth.Authentication;
import ch.kerbtier.dime2.site.Actions;
import ch.kerbtier.dime2.site.renderer.SiteRenderer;
import ch.kerbtier.webb.Livecycles;
import ch.kerbtier.webb.embeded.ServerRunner;
import ch.kerbtier.webb.transform2d.ImageTransformer;
import ch.kerbtier.webb.util.Configuration;
import ch.kerbtier.webb.util.ContextInfo;
import ch.kerbtier.webb.util.HttpInfo;
import static ch.kerbtier.dime2.ContainerFacade.*;

public class Server implements Livecycles {

  public static ServerRunner sr;

  public static void main(String[] args) throws UnknownHostException {
    
    String name = "dime2";
    int port = 8004;
    
    if(args.length > 0) {
      name = args[0];
    }
    if(args.length > 1) {
      port = Integer.parseInt(args[1]);
    }
    
    
    
    try {
      URLConnection con = new URL("http://localhost:" + port + "/exit").openConnection();
      con.getInputStream();

    } catch (ConnectException e) {
      // no server running or server shutdown and connection refused
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    sr = new ServerRunner(name, "ch.kerbtier.dime2.Server", "0.0.0.0", port);
    sr.start();
  }

  @Override
  public void request(DefaultPicoContainer pc) {
    List<Call> calls = getDispatchers().findAll(getHttpInfo().getPath(), Verb.valueOf(getHttpInfo().getMethod().toString()));

    getHttpResponse().setHeader("Server", "Apache");
    
    if (calls.size() > 0) {
      try {
        calls.get(0).execute();
      } catch (Exception e) {
        System.out.println("error ocured in action");
        e.printStackTrace();
        try {
          getHttpResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        return;
      }
    } else {
      System.out.println("no action found");
    }

    if (getResponse().getFile() != null) {
      Path file = getResponse().getFile().normalize();
      // if it's relative, make it absolute to context path
      file = getContextInfo().getLocalPath().resolve(file);

      if (Files.exists(file)) {
        getHttpResponse().setContentType(getResponse().getContentType());
        try {
          FileUtils.copyFile(file.toFile(), getHttpResponse().getOutputStream());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } else {
        Logger.getLogger(Server.class.getName()).warning("static file " + file + " not found");
        try {
          getHttpResponse().sendError(404);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } else if (getResponse().getImage() != null) {
      getHttpResponse().setContentType("image/png");
      try {
        ImageIO.write(getResponse().getImage(), "png", getHttpResponse().getOutputStream());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else if (getResponse().getContent() != null) {
      getHttpResponse().setContentType(getResponse().getContentType());
      try {
        PrintWriter os = getHttpResponse().getWriter();
        os.print(getResponse().getContent());
        os.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (getResponse().getJson() != null) {
      Gson gson = new Gson();
      try {
        getHttpResponse().setContentType("application/json");
        PrintWriter os = getHttpResponse().getWriter();
        gson.toJson(getResponse().getJson(), os);
        os.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void startContext(DefaultPicoContainer pc) {
    pc.as(Characteristics.CACHE).addComponent(Router.class);
    pc.as(Characteristics.CACHE).addComponent(Configuration.class);
    pc.as(Characteristics.CACHE).addComponent(Config.class);

    pc.as(Characteristics.CACHE).addComponent(new Authentication(pc.getComponent(Config.class).getUserRealm()));

    pc.as(Characteristics.CACHE).addComponent(Modules.class);
    pc.as(Characteristics.CACHE).addComponent(Models.class);
    pc.as(Characteristics.CACHE).addComponent(Views.class);
    pc.as(Characteristics.CACHE).addComponent(new SiteRenderer(pc.getComponent(Config.class).isDevelopment()));
    pc.as(Characteristics.CACHE).addComponent(new ImageTransformer("."));

    pc.getComponent(Router.class).register(AdminDispatchers.class);
    pc.getComponent(Router.class).register(Main.class);
    pc.getComponent(Router.class).register(Events.class);
    pc.getComponent(Router.class).register(Static.class);
    pc.getComponent(Router.class).register(Actions.class);
    
    System.out.println("Servlet version: " + pc.getComponent(ContextInfo.class).getVersion());
  }

  @Override
  public void startRequest(DefaultPicoContainer pc) {
    ContainerFacade.set(pc);
    pc.as(Characteristics.CACHE).addComponent(Response.class);
    pc.as(Characteristics.CACHE).addComponent(HttpInfo.class);
  }

  @Override
  public void startSession(DefaultPicoContainer pc) {
    pc.as(Characteristics.CACHE).addComponent(AdminRoot.class);
  }

  @Override
  public void stopContext(DefaultPicoContainer pc) {
    pc.getComponent(Models.class).writeData();
    pc.getComponent(Models.class).close();
    System.out.println("shutdown");
  }

  @Override
  public void stopRequest(DefaultPicoContainer pc) {

  }

  @Override
  public void stopSession(DefaultPicoContainer pc) {

  };

}
