package ch.kerbtier.dime2.admin.model;

import ch.kerbtier.helene.HSlug;

public class SlugInput extends FormElement {

  @ADHS
  private String value;

  public SlugInput(String field) {
    super(field);
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
    getForm().update(getField(), value != null? new HSlug(value) : null);
  }

  @Override
  public void initValue(FormEntity subject) {
    HSlug slug = subject.getSlug(getField());
    if(slug != null) {
      value = slug.getValue();
    } else {
      value = null;
    }
  }
}