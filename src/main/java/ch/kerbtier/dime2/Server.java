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

import com.google.gson.Gson;

import ch.kerbtier.amarillo.Call;
import ch.kerbtier.amarillo.Router;
import ch.kerbtier.amarillo.Verb;
import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.admin.actions.Events;
import ch.kerbtier.dime2.admin.actions.Main;
import ch.kerbtier.dime2.admin.actions.Static;
import ch.kerbtier.dime2.auth.Authentication;
import ch.kerbtier.dime2.server.Request;
import ch.kerbtier.dime2.server.StartContext;
import ch.kerbtier.dime2.server.StartRequest;
import ch.kerbtier.dime2.server.StartSession;
import ch.kerbtier.dime2.server.StopContext;
import ch.kerbtier.dime2.server.StopRequest;
import ch.kerbtier.dime2.server.StopSession;
import ch.kerbtier.dime2.site.Actions;
import ch.kerbtier.dime2.site.renderer.SiteRenderer;
import ch.kerbtier.webb.Livecycles;
import ch.kerbtier.webb.transform2d.ImageTransformer;
import ch.kerbtier.webb.util.Configuration;
import ch.kerbtier.webb.util.ContextInfo;
import ch.kerbtier.webb.util.HttpInfo;

public class Server implements Livecycles {



  @Override
  public void request() {
    new Request().run();
  }

  @Override
  public void startContext() {
    new StartContext().run();
  }

  @Override
  public void startRequest() {
    new StartRequest().run();
  }

  @Override
  public void startSession() {
    new StartSession().run();
  }

  @Override
  public void stopContext() {
    new StopContext().run();
  }

  @Override
  public void stopRequest() {
    new StopRequest().run();
  }

  @Override
  public void stopSession() {
    new StopSession().run();
  };

}
