package ch.kerbtier.dime2.admin.queue;

public class SetStringEvent extends UserEvent {

  private String value;
  
  public SetStringEvent(long id, String field, String value) {
    super(id, field);
    this.value = value;
  }

}
