package ch.kerbtier.dime2.modules;

import java.util.Set;

import ch.kerbtier.dime2.modules.resolver.DependencyProvider;

public class ModuleDependencyProvider implements DependencyProvider<Module> {

  @Override
  public Set<Module> getFor(Module element) {
     return element.getDependencies();
  }

}
