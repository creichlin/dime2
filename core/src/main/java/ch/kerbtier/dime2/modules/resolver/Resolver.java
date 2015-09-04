package ch.kerbtier.dime2.modules.resolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Resolver<T> implements Iterable<T> {

  private DependencyProvider<T> dependencyProvider;
  private List<T> elements = new ArrayList<>();
  private boolean ordered = false;

  public Resolver(DependencyProvider<T> dependencyProvider) {
    this.dependencyProvider = dependencyProvider;
  }

  public void add(T element) {
    elements.add(element);
    ordered = false;
  }

  private void orderByDependencies() {
    List<T> all = new ArrayList<>(elements);
    List<T> resolved = new ArrayList<>();
    while(!all.isEmpty()) {
      List<T> batch = new ArrayList<>();
      
      for(T e: all) {
        if(resolved.containsAll(dependencyProvider.getFor(e))) {
          batch.add(e);
        } 
      }
      if(batch.isEmpty()) {
        break;
      }
      resolved.addAll(batch);
      all.removeAll(batch);
    }
    if(!all.isEmpty()) {
      System.out.println("unresolved dependencies");
    }
    elements = resolved;
    ordered = true;
  }
  
  public List<T> getResolved() {
    if(!ordered) {
      orderByDependencies();
    }
    return elements;
  }

  @Override
  public Iterator<T> iterator() {
    if(!ordered) {
      orderByDependencies();
    }
    return elements.iterator();
  }
}
