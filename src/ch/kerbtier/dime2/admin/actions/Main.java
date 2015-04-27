package ch.kerbtier.dime2.admin.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.logging.Logger;

import ch.kerbtier.dime2.admin.model.Log;
import ch.kerbtier.dime2.admin.model.Root;
import ch.kerbtier.dime2.auth.InvalidCredentials;
import ch.kerbtier.dime2.auth.User;
import ch.kerbtier.webb.dispatcher.Action;
import ch.kerbtier.webb.util.HTTPMethod;
import static ch.kerbtier.dime2.ContainerFacade.*;

public class Main {

  @Action(pattern = "admin/", method = HTTPMethod.POST)
  public void authenticate() {
    String username = getHttpRequest().getParameter("username");
    String password = getHttpRequest().getParameter("password");

    try {
      User user = getAuthentication().get(username, password);
      getAdminRoot().setUser(user);
    } catch (InvalidCredentials e) {
      Logger.getLogger(Main.class.getName()).info("invalid login attempt");
    }
    try {
      getHttpResponse().sendRedirect("");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Action(pattern = "admin/", method = HTTPMethod.GET)
  public void main() {
    try {
      PrintWriter pw = getHttpResponse().getWriter();

      if (getAdminRoot().getUser() == null) {
        pw.print("<html><head><title>login</title></head></body>");

        pw.print("<form method=\"post\">");

        pw.print("<input type=\"text\" name=\"username\"/></br>");
        pw.print("<input type=\"password\" name=\"password\"/></br>");

        pw.print("<input type=\"submit\"/>");

        pw.print("</form>");

        pw.print("</body></html>");
      } else {
        if (getAdminRoot().getRoot() == null) {
          getAdminRoot().setRoot(new Root());
          getAdminRoot().setLog(new Log());
          getAdminRoot().getRoot().set("log", getAdminRoot().getLog());
        } else {
          getAdminRoot().getRoot().triggerAllEvents();
        }

        pw.print("<html><head>");

        for (String js : "libs/underscore,libs/zepto,bootstrap,events/ajaxevents".split(",")) {
          pw.print("<script type=\"text/javascript\" src=\"" + js + ".js\"></script>");
        }

        for (String css : "default".split(",")) {
          pw.print("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + css + ".css\">");
        }

        for (String type : "NodeList,Root,Button,Label,Menu,MenuItem,Table,Grid,TextInput,TextArea,DateInput,Form,SlugInput,FileInput,Ruler,Log,ConfirmDialog"
            .split(",")) {
          pw.print("<script type=\"text/javascript\" src=\"widgets/" + type + ".js\"></script>");
          if (Files.exists(getContextInfo().getLocalPath().resolve("admin/widgets/" + type + ".css"))) {
            pw.print("<link rel=\"stylesheet\" type=\"text/css\" href=\"widgets/" + type + ".css\">");
          }
        }

        pw.print("</head><body></body></html>");

        getModels();
        getViews();
      }
      pw.flush();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
