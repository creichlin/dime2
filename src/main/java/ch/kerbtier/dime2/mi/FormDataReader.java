package ch.kerbtier.dime2.mi;

import ch.kerbtier.dime2.admin.model.Form;

public class FormDataReader implements DataReader {
  
  private Form model;
  
  public FormDataReader(Form model) {
    this.model = model;
  }

  @Override
  public void onChange(String attrib, Runnable run) {
    // form subject has no subscribe mechanism
  }

  @Override
  public Object get(String attrib) {
    return model.getBackendData().get(attrib);
  }

}
