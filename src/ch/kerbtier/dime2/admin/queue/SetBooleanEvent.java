package ch.kerbtier.dime2.admin.queue;

public class SetBooleanEvent extends UserEvent {

  private boolean value;
  
  public SetBooleanEvent(long id, String field, boolean value) {
    super(id, field);
    this.value = value;
  }

}
