package ch.kerbtier.dime2.admin.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ElementNode extends UiNode implements Iterable<UiNode> {
  private ElementNode parent;
  private String name;
  private List<UiNode> children = new ArrayList<>();
  private Map<String, String> attributes = new HashMap<>();

  public ElementNode(ElementNode parent, String name) {
    this.parent = parent;
    this.name = name;
  }

  public void add(UiNode node) {
    children.add(node);
  }

  public String getName() {
    return name;
  }

  public List<UiNode> getChildren() {
    return children;
  }
  
  public ElementNode getParent() {
    return parent;
  }

  public List<ElementNode> getElements(String ename) {
    List<ElementNode> l = new ArrayList<>();
    for(UiNode uin: this) {
      if(uin instanceof ElementNode) {
        if(ename.startsWith("!")) {
          if(!ename.substring(1).equals(((ElementNode)uin).getName())) {
            l.add((ElementNode)uin);
          }
        } else {
          if(ename.equals(((ElementNode)uin).getName())) {
            l.add((ElementNode)uin);
          }
        }
      }
    }
    return l;
  }

  public List<ElementNode> getElements() {
    List<ElementNode> l = new ArrayList<>();
    for (UiNode uin : this) {
      if (uin instanceof ElementNode) {
        l.add((ElementNode) uin);
      }
    }
    return l;
  }

  public void setAttribute(String name, String value) {
    attributes.put(name, value);
  }

  @Override
  public Iterator<UiNode> iterator() {
    return children.iterator();
  }

  public ElementNode firstElement() {
    List<ElementNode> ens = getElements();
    if (ens.size() > 0) {
      return ens.get(0);
    }
    throw new RuntimeException("Node has no child elements");
  }

  public ElementNode firstElement(String name) {
    List<ElementNode> ens = getElements(name);
    if (ens.size() > 0) {
      return ens.get(0);
    }
    throw new RuntimeException("Node has no child elements of type " + name);
  }

  public String getText() {
    StringBuilder sb = new StringBuilder();
    for (UiNode uin : this) {
      if (uin instanceof TextNode) {
        sb.append(((TextNode) uin).getText());
      }
    }
    return sb.toString();
  }

  public String getAttribute(String name) {
    return attributes.get(name);
  }

}
