package ch.kerbtier.dime2;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.kerbtier.dime2.modules.Module;
import ch.kerbtier.dime2.modules.Page;

public class Modules {

  private List<Module> modules = new ArrayList<>();
  private Map<String, Module> byName = new HashMap<>();

  public Modules(Config config) {
    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(config.getModulesPath())) {
      for (Path path : directoryStream) {
        Module module = new Module(path);
        modules.add(module);
        byName.put(module.getName(), module);
        System.out.println("loaded module " + module.getName());
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public Page getPage(String name, String style) {
    for(Module m: modules) {
      if(m.hasSite()) {
        Page p = m.getSite().getPage(name, style);
        if(p != null) {
          return p;
        }
      }
    }
    return null;
  }
  
  public Page getRoot() {
    for(Module m: modules) {
      if(m.hasSite() && m.getSite().hasRoot()) {
        return m.getSite().getRoot();
      }
    }
    return null;
  }
}
