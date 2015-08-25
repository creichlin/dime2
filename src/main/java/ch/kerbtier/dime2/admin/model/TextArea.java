package ch.kerbtier.dime2.admin.model;

public class TextArea extends TextInput {

  @ADHS
  private int lines;
  
  public TextArea(String field) {
    super(field);
  }

  public void setLines(int lines) {
    this.lines = lines;
  }

}
