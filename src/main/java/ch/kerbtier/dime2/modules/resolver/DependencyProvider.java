package ch.kerbtier.dime2.modules.resolver;

import java.util.Set;

public interface DependencyProvider<T> {
  Set<T> getFor(T element);
}
