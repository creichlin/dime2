package ch.kerbtier.dime2.admin.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.kerbtier.helene.HObject;
import ch.kerbtier.helene.ModifiableNode;

public class DateInput extends FormElement {
  
  private static DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

  @ADHS
  private Date value;

  public DateInput(String field) {
    super(field);
  }

  public Date getValue() {
    return value;
  }

  public void setValue(String value) {
    try {
      setValue(format.parse(value));
    } catch (ParseException e) {
      this.value = null;
    }
  }

  public void setValue(Date value) {
    this.value = value;
    getForm().update(getField(), value);
  }

  @Override
  public void initValue(FormEntity subject) {
    value = subject.getDate(getField());
  }
}
