package ch.kerbtier.dime2;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ch.kerbtier.dime2.modules.Module;
import ch.kerbtier.dime2.modules.ModuleDependencyProvider;
import ch.kerbtier.dime2.modules.ModuleInfo.Mapping;
import ch.kerbtier.dime2.modules.Page;
import ch.kerbtier.dime2.modules.resolver.Resolver;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class Modules implements Iterable<Module> {

  private Resolver<Module> modules = new Resolver<>(new ModuleDependencyProvider());
  private Map<String, Module> byName = new HashMap<>();

  @InjectSingleton
  private Config config;
  
  public Modules() {
    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(config.getModulesPath())) {
      for (Path path : directoryStream) {
        Module module = new Module(this, path);
        modules.add(module);
        byName.put(module.getName(), module);
        System.out.println("loaded module " + module.getName());
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public Page getPage(String name) {
    for(Module m: modules) {
      if(m.hasSite()) {
        Page p = m.getSite().getPage(name);
        if(p != null) {
          return p;
        }
      }
    }
    return null;
  }
  
  // TODO: getPage should be used instead. does the same
  public Page getRoot() {
    for(Module m: modules) {
      if(m.hasSite() && m.getSite().hasRoot()) {
        return m.getSite().getRoot();
      }
    }
    return null;
  }

  @Override
  public Iterator<Module> iterator() {
    return modules.iterator();
  }

  public Module getModule(String name) {
    return byName.get(name);
  }

  public Mapping getMapping(String slug) {
    Mapping mapping = null;
    for(Module m: modules) {
      Mapping mm = m.getMapping(slug);
      if(mm != null) {
        mapping = mm;
      }
    }
    return mapping;
  }
}
