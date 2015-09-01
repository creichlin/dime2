package ch.kerbtier.dime2.admin.model;

import ch.kerbtier.dime2.Models;
import ch.kerbtier.dime2.admin.model.form.FormEntity;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HSlug;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class SlugInput extends FormElement {

  @InjectSingleton
  private Models models;
  
  @ADHS
  private String value;
  
  private String currentSlug;

  public SlugInput(String field) {
    super(field);
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
    HSlug slug = value != null? new HSlug(value) : null;
    
    boolean validSlug = currentSlug != null && currentSlug.equals(value);
    validSlug = validSlug || models.get().isAvailable(slug);
    
    this.setValid(validSlug);
    getForm().update(getField(), slug);
  }

  @Override
  public void initValue(FormEntity subject) {
    HSlug slug = (HSlug)subject.get(getField());
    if(slug != null) {
      value = slug.getValue();
    } else {
      value = null;
    }
    currentSlug = value;
  }
}