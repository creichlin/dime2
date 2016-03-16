package ch.kerbtier.dime2.admin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import ch.kerbtier.dime2.admin.model.Node;


public class ViewModelActions {
  private Map<String, Class<?>> actions = new HashMap<>();

  public void add(Class<?> type) {
    if(actions.containsKey(type.getCanonicalName())) {
      throw new RuntimeException("cannot override a viewModelAction " + type);
    }
    
    actions.put(type.getCanonicalName(), type);
  }
  
  public void run(String name) {
  }

  public void run(String name, Node used) {
    if(actions.containsKey(name)) {
      try {
        Object instance = actions.get(name).newInstance();
        Method m = instance.getClass().getMethod("action", new Class<?>[]{Node.class});
        m.invoke(instance, new Object[]{used});
      } catch (Exception e) {
        throw new RuntimeException("failed to call view model action", e);
      }
      
    } else {
      throw new RuntimeException("viewModelAction " + name + " not found");
    }
  }
}
