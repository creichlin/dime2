package ch.kerbtier.dime2.admin.model;


public abstract class FormElement extends Node {

  private String field;
  private Form form;
  
  @ADHS
  private boolean valid = true;

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

  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    if(this.valid != valid) {
      this.valid = valid;
      form.changeValid(this);
    }
  }
}
