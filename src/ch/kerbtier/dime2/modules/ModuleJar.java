package ch.kerbtier.dime2.modules;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModuleJar {
  private Module module;
  private List<ModuleInit> initInstances = new ArrayList<>();
  
  
  public ModuleJar(Path jar, Module module) {
    this.module = module;
    try {
      URLClassLoader child = new URLClassLoader(new URL[] { jar.toUri().toURL() }, this.getClass().getClassLoader());

      for (String cn : module.getInfo().getInitClasses()) {
        Class<ModuleInit> classToLoad = (Class<ModuleInit>) Class.forName(cn, true, child);
        initInstances.add(classToLoad.newInstance());
      }
    } catch (Exception e) {
      throw new RuntimeException("failed to load module jar", e);
    }
  }

  public void init() {
    for (ModuleInit ii : initInstances) {
      ii.init(module.getFolder());
    }
  }

}
