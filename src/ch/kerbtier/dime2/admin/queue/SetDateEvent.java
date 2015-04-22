package ch.kerbtier.dime2.admin.queue;

import java.util.Date;

public class SetDateEvent extends UserEvent {

  private long value;
  
  public SetDateEvent(long id, String field, Date value) {
    super(id, field);
    this.value = value.getTime();
  }

}
