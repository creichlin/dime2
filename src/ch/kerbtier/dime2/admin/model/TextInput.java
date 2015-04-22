package ch.kerbtier.dime2.admin.model;

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
    value = subject.getString(getField());
  }
}
