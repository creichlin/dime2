package ch.kerbtier.dime2.admin.model;

public class Log extends NodeList {
  public void log(String message) {
    add(new Label(message));
  }
}
