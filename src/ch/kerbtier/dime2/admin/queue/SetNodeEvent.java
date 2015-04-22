package ch.kerbtier.dime2.admin.queue;

public class SetNodeEvent extends UserEvent {
  
  private long child;
  private String type;
  
  public SetNodeEvent(long id, String field, long child, String type) {
    super(id, field);
    this.child = child;
    this.type = type;
  }

  public long getChild() {
    return child;
  }

  public String getType() {
    return type;
  }
}
