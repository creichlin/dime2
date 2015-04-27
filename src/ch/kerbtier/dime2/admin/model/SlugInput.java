package ch.kerbtier.dime2.admin.model;

import ch.kerbtier.dime2.ContainerFacade;
import ch.kerbtier.helene.HSlug;

public class SlugInput extends FormElement {

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
    validSlug = validSlug || ContainerFacade.getModels().get().isAvailable(slug);
    
    this.setValid(validSlug);
    getForm().update(getField(), slug);
  }

  @Override
  public void initValue(FormEntity subject) {
    HSlug slug = subject.getSlug(getField());
    if(slug != null) {
      value = slug.getValue();
    } else {
      value = null;
    }
    currentSlug = value;
  }
}