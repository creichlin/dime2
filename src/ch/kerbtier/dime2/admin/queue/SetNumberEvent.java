package ch.kerbtier.dime2.admin.queue;

import java.math.BigDecimal;

public class SetNumberEvent extends UserEvent {

  private BigDecimal value;
  
  public SetNumberEvent(long id, String field, BigDecimal value) {
    super(id, field);
    this.value = value;
  }

  public SetNumberEvent(long id, String field, int value) {
    super(id, field);
    this.value = new BigDecimal(value);
  }

  public SetNumberEvent(long id, String field, float value) {
    super(id, field);
    this.value = new BigDecimal(value);
  }

}
