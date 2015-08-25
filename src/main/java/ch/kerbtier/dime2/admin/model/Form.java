package ch.kerbtier.dime2.admin.model;

import java.util.ArrayList;
import java.util.List;

import ch.kerbtier.dime2.admin.model.form.FormEntity;
import ch.kerbtier.struwwel.MappedObservable;

public class Form extends NodeList {

  private FormEntity subject;

  private List<FormElement> elements = new ArrayList<>();
  
  @ADHS
  private boolean valid = true;

  public Form(FormEntity subject) {
    this.subject = subject;
  }

  public void register(FormElement element) {
    elements.add(element);

    element.setForm(this);

    element.initValue(subject);
  }

  public void save() {
    subject.commit();

    for (FormElement fe : elements) {
      fe.initValue(subject);
    }
  }

  public MappedObservable<String> getChange() {
    return subject.getChange();
  }

  public void update(String field, Object value) {
    subject.set(field, value);
    getChange().inform(field);
  }

  public Object get(String field) {
    return subject.get(field);
  }
  
  public FormEntity getBackendData() {
    return subject;
  }

  public void changeValid(FormElement formElement) {
    boolean val = true;
    for(FormElement fe: elements) {
      val = val && fe.isValid();
    }
    this.valid = val;
  }

  public boolean isValid() {
    return valid;
  }

  public List<FormEntity> getObjectList(String formList) {
    return subject.getObjectList(formList);
  }
  
  public void addObjectTo(String list) {
    subject.addObjectTo(list);
  }

  public void delete() {
    subject.delete();
  }
}
