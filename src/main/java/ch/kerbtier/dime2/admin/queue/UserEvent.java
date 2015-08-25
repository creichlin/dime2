package ch.kerbtier.dime2.admin.queue;

public class UserEvent {

  private long id;
  private String field;
  
  public UserEvent(long id, String field) {
    this.id = id;
    this.field = field;
  }

  public long getId() {
    return id;
  }

  public String getField() {
    return field;
  }
}
