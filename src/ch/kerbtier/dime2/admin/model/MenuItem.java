package ch.kerbtier.dime2.admin.model;

import ch.kerbtier.helene.events.Listeners;


public class MenuItem extends NodeList {
  private Listeners click = new Listeners();
  
  @ADHS
  private String text;
  
  public Listeners getClick() {
    return click;
  }

  public void click() {
    click.trigger();
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
