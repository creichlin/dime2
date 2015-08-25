package ch.kerbtier.dime2.admin.model;

import ch.kerbtier.dime2.admin.model.form.FormEntity;

public class TextInput extends FormElement {

  @ADHS
  private String value;

  public TextInput(String field) {
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
    value = (String)subject.get(getField());
  }
}
