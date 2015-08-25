package ch.kerbtier.dime2.admin.actions;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.file.Path;

import org.lesscss.LessCompiler;

import ch.kerbtier.amarillo.Route;
import ch.kerbtier.dime2.admin.model.FileInput;
import static ch.kerbtier.dime2.ContainerFacade.*;

public class Static {
  @Route(pattern = "admin/(.*\\.js)")
  public void processJs(String path) {
    Path jsPath = getContextInfo().getLocalPath().resolve("admin").resolve(path);
    getResponse().setFile(jsPath);
    getResponse().setContentType("text/javascript;charset=UTF-8");
  }

  @Route(pattern = "admin/(.*\\.css)")
  public void processLessCss(String path) {
    LessCompiler lcc = new LessCompiler();
    Path cssPath = getContextInfo().getLocalPath().resolve("admin").resolve(path);
    try {
      getResponse().setContent(lcc.compile(cssPath.toFile()));
    } catch (Exception e) {
      System.out.println("Error processing " + path);
      e.printStackTrace();
      getResponse().setFile(cssPath);
    }
    getResponse().setContentType("text/css;charset=UTF-8");
  }
  
  @Route(pattern = "admin/(.*\\.png)")
  public void processPng(String path) {
    Path pngPath = getContextInfo().getLocalPath().resolve("admin").resolve(path);
    getResponse().setFile(pngPath);
    getResponse().setContentType("image/png");
  }
  
  @Route(pattern="admin/image/(-?[0-9]+)/(.*?)/.*")
  public void serveModelImage(long mid, String code) {
    FileInput i = (FileInput)getAdminRoot().getRoot().getQueue().get(mid);
    ByteBuffer data = i.getData();
    BufferedImage bi = getImageTransformer().transform(data, code);
    getResponse().setImage(bi);
  }

}
