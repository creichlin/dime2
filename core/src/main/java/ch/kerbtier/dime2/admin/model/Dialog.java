package ch.kerbtier.dime2.admin.model;

public class Dialog extends NodeList {
  @ADHS
  private String title;
  
  @ADHS
  private Node footer;
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }

  public Node getFooter() {
    return footer;
  }

  public void setFooter(Node footer) {
    this.footer = footer;
  }
}
