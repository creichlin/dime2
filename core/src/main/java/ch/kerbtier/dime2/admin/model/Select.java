package ch.kerbtier.dime2.admin.model;

import java.util.ArrayList;
import java.util.List;

import ch.kerbtier.dime2.Models;
import ch.kerbtier.dime2.admin.model.form.FormEntity;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class Select extends FormElement {

  @InjectSingleton
  private Models models;
  
  @ADHS
  private String value;
  
  
  private List<SelectOption> nodes = new ArrayList<>();

  
  public Select(String field) {
    super(field);
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
    getForm().update(getField(), value);
  }

  @Override
  public void initValue(FormEntity subject) {
    Object current = subject.get(getField());
    
    if(current != null) {
      setValue(current + "");
    }
  }
  
  
  
  // NodeList implemeted here
  
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



  public void add(String value, String label) {
    int index = nodes.size();
    SelectOption option = new SelectOption(value, label);
    nodes.add(option);
    if (getEventQueue() != null) {
      getEventQueue().set(this, "#" + index, option);
      option.setEventQueue(getEventQueue());
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
  
  
  static class SelectOption extends Node {
    
    @ADHS
    private String value;
    @ADHS
    private String label;
    
    public SelectOption(String value, String label) {
      this.value = value;
      this.label = label;
    }
  }
}