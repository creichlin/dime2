package ch.kerbtier.dime2.site.renderer.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ch.kerbtier.dime2.Models;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HList;
import ch.kerbtier.helene.HObject;
import ch.kerbtier.helene.HSlug;
import ch.kerbtier.helene.exceptions.UndefinedFieldException;
import ch.kerbtier.webb.di.InjectSingleton;

import com.github.jknack.handlebars.ValueResolver;

@Inject
public class HNodeResolver implements ValueResolver {
  public static final HNodeResolver INSTANCE = new HNodeResolver();

  @InjectSingleton
  private Models models;

  
  public HNodeResolver() {
    Queries.add("published", new Published());
  }
  
  @Override
  public Object resolve(final Object context, final String name) {
    // in all templates, $ resolves to root model node
    if (name.equals("$")) {
      return models.get();
    }

    // if we have [LIST].foo we want to apply foo transformation to the list
    if (context instanceof HList) {
      
      if(Queries.contains(name)) {
        List<Object> elements = new ArrayList<>();
        for (Object x : ((HList) context)) {
          elements.add(x);
        }
        Queries.get(name).transform(elements);
        return elements;
      } else {
        System.out.println("no list query named " + name + " found");
        return Collections.EMPTY_LIST;
      }
    }

    if (context instanceof HObject) {
      try {
        // we have [OBJECT].foo, try to resolve foo attribute
        return transform(((HObject) context).get(name));
      } catch (UndefinedFieldException e) {
        // no foo attribute, check if we have foo transformation and if it allows the current object.
        if (Queries.get(name) != null && Queries.get(name).valid((HObject) context)) {
          return context;
        } else {
          return null; // null so no more resolvers can have a chance to display it
        }
      }
    }
    return UNRESOLVED;
  }

  private Object transform(Object object) {
    if (object instanceof HSlug) {
      return ((HSlug) object).getValue();
    }
    return object;
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
    if (context instanceof HObject) {
      HObject asHObject = (HObject) context;
      Map<String, Object> values = new HashMap<>();
      for (String key : asHObject.getProperties()) {
        values.put(key, transform(asHObject.get(key)));
      }
      return values.entrySet();
    }

    return Collections.emptySet();
  }

}
