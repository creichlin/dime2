package ch.kerbtier.dime2.admin.model;

import java.util.ArrayList;
import java.util.List;

public class NodeList<ELEMENT_TYPE extends Node> extends Node {
  private List<ELEMENT_TYPE> nodes = new ArrayList<>();

  public int size() {
    return nodes.size();
  }
  
  

  @Override
  void triggerOwnEvents() {
    super.triggerOwnEvents();
    for (int cnt = 0; cnt < nodes.size(); cnt++) {
      getEventQueue().set(this, "#" + cnt, nodes.get(cnt));
    }
  }



  public void add(ELEMENT_TYPE e) {
    int index = nodes.size();
    nodes.add(e);
    if (getEventQueue() != null) {
      getEventQueue().set(this, "#" + index, e);
      e.setEventQueue(getEventQueue());
    }
  }

  public void remove(Object o) {
    int index = nodes.indexOf(o);
    if (index != -1) {
      remove(index);
    }
  }

  public void clear() {
    if (getEventQueue() != null) {
      for (int cnt = 0; cnt < nodes.size(); cnt++) {
        getEventQueue().set(this, "#" + cnt, null);
      }
    }
    nodes.clear();
  }

  public ELEMENT_TYPE get(int index) {
    return nodes.get(index);
  }

  public void set(int index, ELEMENT_TYPE element) {
    nodes.set(index, element);
    if (getEventQueue() != null) {
      getEventQueue().set(this, "#" + index, element);
      element.setEventQueue(getEventQueue());
    }
  }

  public void remove(int index) {
    nodes.remove(index);

    if (getEventQueue() != null) {
      getEventQueue().set(this, "#" + nodes.size(), null);

      for (int cnt = 0; cnt < nodes.size(); cnt++) {
        getEventQueue().set(this, "#" + cnt, nodes.get(cnt));
      }
    }
  }

  @Override
  List<Node> getADHSNodes() {
    List<Node> ns = super.getADHSNodes();

    for (Node n : nodes) {
      if (n != null) {
        ns.add(n);
      }
    }

    return ns;
  }

}
