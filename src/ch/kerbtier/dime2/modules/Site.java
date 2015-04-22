package ch.kerbtier.dime2.modules;

import java.nio.file.Files;
import java.nio.file.Path;

public class Site {

  private Module parent;
  private Path path;
  
  public Site(Module parent, Path path) {
    this.parent = parent;
    this.path = path;
  }
  
  public boolean hasRoot() {
    return Files.exists(path.resolve("index.html"));
  }
  
  public Page getRoot() {
    if(!hasRoot()) {
      throw new RuntimeException("no root page");
    }
    return new Page(this, path.resolve("index.html"));
  }
  
  public Page getPage(String model, String style) {
    if(Files.exists(path.resolve(model + "." + style + ".html"))) {
      return new Page(this, path.resolve(model + "." + style + ".html"));
    }
    return null;
  }
}
