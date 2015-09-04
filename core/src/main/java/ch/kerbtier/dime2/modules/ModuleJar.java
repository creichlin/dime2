package ch.kerbtier.dime2.modules;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModuleJar {
  private Module module;
  private List<ModuleInit> initInstances = new ArrayList<>();
  
  
  public ModuleJar(List<Path> jars, Module module) {
    this.module = module;
    
    
    try {
      URL[] urls = new URL[jars.size()];
      
      for(int cnt = 0; cnt < urls.length; cnt++) {
        urls[cnt] = jars.get(cnt).toUri().toURL();
      }

      System.out.println("create classloader for: " + jars);
      
      URLClassLoader child = new URLClassLoader(urls, this.getClass().getClassLoader());

      for (String cn : module.getInfo().getInitClasses()) {
        Class<ModuleInit> classToLoad = (Class<ModuleInit>) Class.forName(cn, true, child);
        initInstances.add(classToLoad.newInstance());
      }
    } catch (Exception e) {
      throw new RuntimeException("failed to load module jars", e);
    }
  }

  public void init() {
    for (ModuleInit ii : initInstances) {
      ii.init(module.getFolder());
    }
  }

}
