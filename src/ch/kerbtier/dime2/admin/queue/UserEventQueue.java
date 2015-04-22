package ch.kerbtier.dime2.admin.queue;

import java.util.Date;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Map;

import com.google.common.collect.MapMaker;

import ch.kerbtier.dime2.admin.model.Node;

public class UserEventQueue {
  private LinkedList<UserEvent> queue = new LinkedList<>();
  
  private Map<Long, Node> nodes = new MapMaker().weakValues().makeMap();
  
  public void set(Node parent, String field, Object child) {
    
    if(parent == null) {
      queue.add(new RootEvent(((Node)child).getId()));
    } else if(child == null) {
      queue.add(new UnsetEvent(parent.getId(), field));
    } else if(child instanceof Node){
      queue.add(new SetNodeEvent(parent.getId(), field, ((Node)child).getId(), ((Node)child).getType()));
    } else if(child instanceof String){
      queue.add(new SetStringEvent(parent.getId(), field, (String)child));
    } else if(child instanceof Integer){
      queue.add(new SetNumberEvent(parent.getId(), field, (Integer)child));
    } else if(child instanceof Float){
      queue.add(new SetNumberEvent(parent.getId(), field, (Float)child));
    } else if(child instanceof Date){
      queue.add(new SetDateEvent(parent.getId(), field, (Date)child));
    } else {
      throw new RuntimeException("invalid value for " + parent + " " + field + " :  " + child);
    }
  }
  
  public UserEvent next() {
    if(queue.size() > 0) {
      return queue.removeFirst();
    }
    throw new EmptyStackException();
  }

  public Node get(long id) {
    return nodes.get(id);
  }

  public void register(Node node) {
    nodes.put(node.getId(), node);
  }
}
