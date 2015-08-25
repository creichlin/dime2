package ch.kerbtier.dime2.admin.model;

import ch.kerbtier.struwwel.Observable;


public class MenuItem extends NodeList {
  private Observable click = new Observable();
  
  @ADHS
  private String text;
  
  public Observable getClick() {
    return click;
  }

  public void click() {
    click.inform();
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
