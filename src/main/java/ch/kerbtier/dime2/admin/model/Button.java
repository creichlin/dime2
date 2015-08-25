package ch.kerbtier.dime2.admin.model;

import ch.kerbtier.struwwel.Observable;


public class Button extends Node {
  @ADHS
  private String text;
  
  @ADHS
  private String icon;
  
  private Observable click = new Observable();
  
  public Button(String text) {
    this.text = text;
  }
  
  public Button(String text, String icon) {
    this.text = text;
    this.icon = icon;
  }

  public void click() {
    click.inform();
  }

  public Observable getClick() {
    return click;
  }
}
