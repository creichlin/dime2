package ch.kerbtier.dime2.admin.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import ch.kerbtier.dime2.admin.queue.UserEventQueue;

public class Node {
  private static SecureRandom random = new SecureRandom();

  private UserEventQueue eventQueue;
  private long id;

  @ADHS
  private String style;
  
  @ADHS
  private Integer span = null;

  public Node() {
    id = random.nextInt() >> 1 << 1;
  }

  public long getId() {
    return id;
  }

  public String getType() {
    String name = getClass().getCanonicalName();
    name = name.substring(name.lastIndexOf(".") + 1);
    return name;
  }

  UserEventQueue getEventQueue() {
    return eventQueue;
  }

  void setEventQueue(UserEventQueue eventQueue) {
    if (this.eventQueue != null) {
      throw new RuntimeException("cannot assign model node to different eventqueue");
    }

    this.eventQueue = eventQueue;
    eventQueue.register(this);

    triggerOwnEvents();

    for (Node node : getADHSNodes()) {
      node.setEventQueue(eventQueue);
    }
  }

  public void triggerAllEvents() {
    triggerOwnEvents();

    for (Node node : getADHSNodes()) {
      node.triggerAllEvents();
    }
  }

  void triggerOwnEvents() {
    for (Field field : getADHSFields()) {
      try {
        field.setAccessible(true);
        Object n = field.get(this);
        if (n != null) {
          getEventQueue().set(this, field.getName(), n);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  List<Node> getADHSNodes() {
    List<Node> nodes = new ArrayList<>();
    for (Field field : getADHSFields()) {
      if (Node.class.isAssignableFrom(field.getType())) {
        try {
          field.setAccessible(true);
          Node n = (Node) field.get(this);
          if (n != null) {
            nodes.add(n);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return nodes;
  }

  private List<Field> getADHSFields() {
    List<Field> fields = new ArrayList<>();

    List<Class<?>> classes = new ArrayList<>();
    Class<?> cl = getClass();

    while (cl.getSuperclass() != null) {
      classes.add(0, cl);
      cl = cl.getSuperclass();
    }

    for (Class<?> c : classes) {
      for (Field f : c.getDeclaredFields()) {
        if (f.isAnnotationPresent(ADHS.class)) {
          fields.add(f);
        }
      }
    }

    return fields;
  }

  public String getStyle() {
    return style;
  }

  public void setStyle(String style) {
    this.style = style;
  }
  
  public int getSpan() {
    return span;
  }

  public void setSpan(int span) {
    this.span = span;
  }

  public void triggerAction(String prop) {
    try {
      Method m = this.getClass().getMethod(prop);
      m.invoke(this);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void triggerSet(String prop, Object val) {
    try {
      String name = "set" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
      Method m = this.getClass().getMethod(name, val.getClass());
      m.invoke(this, val);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  

}
