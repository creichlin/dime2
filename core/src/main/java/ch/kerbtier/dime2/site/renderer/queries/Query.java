package ch.kerbtier.dime2.site.renderer.queries;

import java.util.List;

import ch.kerbtier.helene.HObject;

public interface Query {
  /**
   * can modify, filter, whatever the input list
   */
  void transform(List<Object> input);
  
  /**
   * check if hobject should be displayed with this query
   */
  boolean valid(HObject object);
}
