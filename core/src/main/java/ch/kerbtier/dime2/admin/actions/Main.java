package ch.kerbtier.dime2.admin.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.kerbtier.amarillo.Route;
import ch.kerbtier.amarillo.Verb;
import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.admin.model.Log;
import ch.kerbtier.dime2.admin.model.Root;
import ch.kerbtier.dime2.auth.Authentication;
import ch.kerbtier.dime2.auth.InvalidCredentials;
import ch.kerbtier.dime2.auth.User;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectRequest;
import ch.kerbtier.webb.di.InjectSession;
import ch.kerbtier.webb.di.InjectSingleton;
import ch.kerbtier.webb.util.ContextInfo;

@Inject
public class Main {
  
  @InjectRequest
  private HttpServletRequest httpRequest;
  
  @InjectRequest
  private HttpServletResponse httpResponse;
  
  @InjectSingleton
  private Authentication authentication;
  
  @InjectSingleton
  private ContextInfo contextInfo;
  
  @InjectSession
  private AdminRoot adminRoot;

  @Route(pattern = "admin/", verb = Verb.POST)
  public void authenticate() {
    String username = httpRequest.getParameter("username");
    String password = httpRequest.getParameter("password");

    try {
      User user = authentication.get(username, password);
      adminRoot.setUser(user);
    } catch (InvalidCredentials e) {
      Logger.getLogger(Main.class.getName()).info("invalid login attempt");
    }
    try {
      httpResponse.sendRedirect("");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Route(pattern = "admin/", verb = Verb.GET)
  public void main() {
    
    try {
      PrintWriter pw = httpResponse.getWriter();

      if (adminRoot.getUser() == null) {
        pw.print("<html><head><title>login</title></head></body>");

        pw.print("<form method=\"post\">");

        pw.print("<input type=\"text\" name=\"username\"/></br>");
        pw.print("<input type=\"password\" name=\"password\"/></br>");

        pw.print("<input type=\"submit\"/>");

        pw.print("</form>");

        pw.print("</body></html>");
      } else {
        if (adminRoot.getRoot() == null) {
          adminRoot.setRoot(new Root());
          adminRoot.setLog(new Log());
          adminRoot.getRoot().set("log", adminRoot.getLog());
        } else {
          adminRoot.getRoot().triggerAllEvents();
        }

        pw.print("<html><head>");
        
        pw.println("<script src=\"//code.jquery.com/jquery-2.1.4.min.js\"></script>");

        for (String js : "libs/underscore,bootstrap,events/ajaxevents".split(",")) {
          pw.print("<script type=\"text/javascript\" src=\"" + js + ".js\"></script>");
        }

        pw.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">");
        pw.println("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>");        

        for (String css : "default".split(",")) {
          pw.print("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + css + ".css\">");
        }

        for (String type : "NodeList,Root,Button,Label,Menu,MenuItem,Table,Grid,TextInput,TextArea,DateInput,Form,SlugInput,FileInput,Ruler,Log,ConfirmDialog,Markdown,Select,ButtonGroup"
            .split(",")) {
          pw.print("<script type=\"text/javascript\" src=\"widgets/" + type + ".js\"></script>");
          if (Files.exists(contextInfo.getLocalPath().resolve("admin/widgets/" + type + ".css"))) {
            pw.print("<link rel=\"stylesheet\" type=\"text/css\" href=\"widgets/" + type + ".css\">");
          }
        }

        pw.print("</head><body></body></html>");
      }
      pw.flush();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
