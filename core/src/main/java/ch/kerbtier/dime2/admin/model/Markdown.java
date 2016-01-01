package ch.kerbtier.dime2.admin.model;

public class Markdown extends Node {
  @ADHS
  private String text;
  
  public Markdown(String plain) {
    this.text = plain;
  }
  
  public void setText(String plain) {
    this.text = plain;
  }
}
