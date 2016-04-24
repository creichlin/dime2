package ch.kerbtier.dime2.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import ch.kerbtier.amarillo.Call;
import ch.kerbtier.amarillo.Router;
import ch.kerbtier.amarillo.Verb;
import ch.kerbtier.dime2.Response;
import ch.kerbtier.dime2.Server;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectRequest;
import ch.kerbtier.webb.di.InjectSingleton;
import ch.kerbtier.webb.util.ContextInfo;
import ch.kerbtier.webb.util.HttpInfo;

import com.google.gson.Gson;

@Inject
public class Request {
  
  @InjectSingleton
  private Router router;
  
  @InjectRequest
  private HttpInfo httpInfo;
  
  @InjectRequest
  private HttpServletResponse httpResponse;

  @InjectRequest
  private HttpServletRequest httpRequest;
  
  @InjectRequest
  private Response response;
  
  @InjectSingleton
  private ContextInfo contextInfo;
  
  private Logger logger = Logger.getLogger(Request.class.getName());

  public void run() {
    // use UTF-8 encoding as default if no other encoding is set by the request header
    if(httpRequest.getCharacterEncoding() == null ){
      try {
        httpRequest.setCharacterEncoding("UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw new AssertionError(e);
      }
    }
    
    Verb verb;
    try {
      // only post and get supported, rest is interpreted as get
      verb = Verb.valueOf(httpInfo.getMethod().toString());
    } catch(IllegalArgumentException e) {
      verb = Verb.GET;
    }
    
    List<Call> calls = router.findAll(httpInfo.getPath(), verb);

    httpResponse.setHeader("Server", "Apache");
    
    if (calls.size() > 0) {
      try {
        calls.get(0).execute();
      } catch (Exception e) {
        logger.log(Level.SEVERE, "error in action", e);
        try {
          httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e1) {
          logger.log(Level.SEVERE, "error sending error", e1);
        }
        return;
      }
    } else {
      logger.info("no action found for request " + httpInfo.getPath());
    }

    if (response.getFile() != null) {
      Path file = response.getFile().normalize();
      // if it's relative, make it absolute to context path
      file = contextInfo.getLocalPath().resolve(file);

      if (Files.exists(file)) {
        httpResponse.setContentType(response.getContentType());
        try {
          FileUtils.copyFile(file.toFile(), httpResponse.getOutputStream());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } else {
        Logger.getLogger(Server.class.getName()).warning("static file " + file + " not found");
        try {
          httpResponse.sendError(404);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } else if (response.getImage() != null) {
      httpResponse.setContentType("image/png");
      try {
        ImageIO.write(response.getImage(), "png", httpResponse.getOutputStream());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else if (response.getContent() != null) {
      httpResponse.setContentType(response.getContentType());
      try {
        PrintWriter os = httpResponse.getWriter();
        os.print(response.getContent());
        os.flush();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else if (response.getJson() != null) {
      Gson gson = new Gson();
      try {
        httpResponse.setContentType("application/json; charset=UTF-8");
        PrintWriter os = httpResponse.getWriter();
        gson.toJson(response.getJson(), os);
        os.flush();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}


