package ch.kerbtier.dime2.admin.model;

import ch.kerbtier.helene.events.Listeners;


public class Button extends Node {
  @ADHS
  private String text;
  
  @ADHS
  private String icon;
  
  private Listeners click = new Listeners();
  
  public Button(String text) {
    this.text = text;
  }
  
  public Button(String text, String icon) {
    this.text = text;
    this.icon = icon;
  }

  public void click() {
    click.trigger();
  }

  public Listeners getClick() {
    return click;
  }
}
