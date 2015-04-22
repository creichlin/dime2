package ch.kerbtier.dime2.site.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ch.kerbtier.dime2.ContainerFacade;
import ch.kerbtier.dime2.site.renderer.queries.Published;
import ch.kerbtier.dime2.site.renderer.queries.Query;
import ch.kerbtier.helene.HList;
import ch.kerbtier.helene.HObject;
import ch.kerbtier.helene.exceptions.UndefinedFieldException;

import com.github.jknack.handlebars.ValueResolver;

public class HNodeResolver implements ValueResolver {
  public static HNodeResolver INSTANCE = new HNodeResolver();

  private static Map<String, Query> QUERIES = new HashMap<>();
  static {
    QUERIES.put("published", new Published());
  }
  
  @Override
  public Object resolve(final Object context, final String name) {
    
    if(name.equals("$")) {
      return ContainerFacade.getModels().get();
    }
      
    
    if(context instanceof HList) {
      List<Object> elements = new ArrayList<>();
      for(Object x: ((HList)context)) {
        elements.add(x);
      }
      QUERIES.get(name).transform(elements);
      return elements;
    }
    
    
    
    if (context instanceof HObject) {
      try {
        return ((HObject)context).get(name);
      }catch(UndefinedFieldException e) {
        if(QUERIES.get(name) != null && QUERIES.get(name).valid((HObject)context)) {
          return context;
        } else {
          return null;
        }
      }
    }
    return UNRESOLVED;
  }

  @Override
  public Object resolve(final Object context) {
    if (context instanceof HObject) {
      return context;
    }
    return UNRESOLVED;
  }

  @Override
  public Set<Entry<String, Object>> propertySet(final Object context) {
    if(context instanceof HObject) {
      HObject asHObject = (HObject)context;
      Map<String, Object> values = new HashMap<>();
      for(String key: asHObject.getProperties()) {
        values.put(key, asHObject.get(key));
      }
      return values.entrySet();
    }
    
    return Collections.emptySet();
  }

}
