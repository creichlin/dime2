package ch.kerbtier.dime2.modules;

import java.nio.file.Files;
import java.nio.file.Path;

public class Module {
  private Path folder;
  private Site site;

  public Module(Path folder) {
    this.folder = folder;

    if (!Files.exists(folder) || !Files.isDirectory(folder)) {
      throw new RuntimeException("invalid module path " + folder);
    }

    if (Files.exists(folder.resolve("site"))) {
      site = new Site(this, folder.resolve("site"));
    }
  }

  public Path getFolder() {
    return folder;
  }
  
  public boolean hasSite() {
    return site != null;
  }

  public Site getSite() {
    return site;
  }

  public String getName() {
    return folder.getFileName().toString();
  }

}
