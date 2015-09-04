package ch.kerbtier.dime2.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
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
  private Response response;
  
  @InjectSingleton
  private ContextInfo contextInfo;
  
  

  public void run() {
    List<Call> calls = router.findAll(httpInfo.getPath(), Verb.valueOf(httpInfo.getMethod().toString()));

    httpResponse.setHeader("Server", "Apache");
    
    if (calls.size() > 0) {
      try {
        calls.get(0).execute();
      } catch (Exception e) {
        System.out.println("error ocured in action");
        e.printStackTrace();
        try {
          httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        return;
      }
    } else {
      System.out.println("no action found");
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
        e.printStackTrace();
      }
    } else if (response.getJson() != null) {
      Gson gson = new Gson();
      try {
        httpResponse.setContentType("application/json");
        PrintWriter os = httpResponse.getWriter();
        gson.toJson(response.getJson(), os);
        os.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
