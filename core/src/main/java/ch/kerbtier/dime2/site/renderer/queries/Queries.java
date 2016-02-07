package ch.kerbtier.dime2.site.renderer.queries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Queries {
  private final static Map<String, Query> QUERIES = Collections.synchronizedMap(new HashMap<String, Query>());

  public static final void add(String name, Query query) {
    QUERIES.put(name, query);
  }
  
  public static final Query get(String name) {
    return QUERIES.get(name);
  }

  public static boolean contains(String name) {
    return QUERIES.containsKey(name);
  }
}
