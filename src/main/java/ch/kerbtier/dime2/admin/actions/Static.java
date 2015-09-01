package ch.kerbtier.dime2.admin.actions;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.file.Path;

import org.lesscss.LessCompiler;

import ch.kerbtier.amarillo.Route;
import ch.kerbtier.dime2.Response;
import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.admin.model.FileInput;
import ch.kerbtier.dime2.auth.Authentication;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectRequest;
import ch.kerbtier.webb.di.InjectSession;
import ch.kerbtier.webb.di.InjectSingleton;
import ch.kerbtier.webb.transform2d.ImageTransformer;
import ch.kerbtier.webb.util.ContextInfo;

@Inject
public class Static {
  
  
  @InjectSession
  private AdminRoot adminRoot;
  
  @InjectSingleton
  private ContextInfo contextInfo;
  
  @InjectSingleton
  private ImageTransformer imageTransformer;
  
  @InjectRequest
  private Response response;

  
  @Route(pattern = "admin/(.*\\.js)")
  public void processJs(String path) {
    Path jsPath = contextInfo.getLocalPath().resolve("admin").resolve(path);
    response.setFile(jsPath);
    response.setContentType("text/javascript;charset=UTF-8");
  }

  @Route(pattern = "admin/(.*\\.css)")
  public void processLessCss(String path) {
    LessCompiler lcc = new LessCompiler();
    Path cssPath = contextInfo.getLocalPath().resolve("admin").resolve(path);
    try {
      response.setContent(lcc.compile(cssPath.toFile()));
    } catch (Exception e) {
      System.out.println("Error processing " + path);
      e.printStackTrace();
      response.setFile(cssPath);
    }
    response.setContentType("text/css;charset=UTF-8");
  }
  
  @Route(pattern = "admin/(.*\\.png)")
  public void processPng(String path) {
    Path pngPath = contextInfo.getLocalPath().resolve("admin").resolve(path);
    response.setFile(pngPath);
    response.setContentType("image/png");
  }
  
  @Route(pattern="admin/image/(-?[0-9]+)/(.*?)/.*")
  public void serveModelImage(long mid, String code) {
    FileInput i = (FileInput)adminRoot.getRoot().getQueue().get(mid);
    ByteBuffer data = i.getData();
    BufferedImage bi = imageTransformer.transform(data, code);
    response.setImage(bi);
  }

}
