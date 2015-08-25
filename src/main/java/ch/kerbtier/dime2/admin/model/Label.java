package ch.kerbtier.dime2.admin.model;

public class Label extends Node {
  @ADHS
  private String text;
  
  public Label(String text) {
    this.text = text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
}
