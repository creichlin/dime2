package ch.kerbtier.dime2.admin.model;


public abstract class FormElement extends Node {

  private String field;
  private Form form;

  public FormElement(String field) {
    this.field = field;
  }

  public String getField() {
    return field;
  }
  
  public Form getForm() {
    return form;
  }

  public abstract void initValue(FormEntity subject);

  public void setForm(Form form) {
    this.form = form;
  }

}
