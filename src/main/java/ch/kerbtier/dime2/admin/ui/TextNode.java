package ch.kerbtier.dime2.admin.ui;

public class TextNode extends UiNode {
  private String text;
  
  public TextNode(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
