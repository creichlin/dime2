package ch.kerbtier.dime2.admin.queue;

public class RootEvent extends UserEvent {
  private long child;
  private String type = "Root";

  public RootEvent(long child) {
    super(0, "root");
    this.child = child;
  }

}
