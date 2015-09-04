package ch.kerbtier.dime2.site.renderer.queries;

import java.util.List;

import ch.kerbtier.helene.HObject;

public interface Query {
  void transform(List<Object> input);
  boolean valid(HObject object);
}
