package ch.kerbtier.dime2.admin.model;

public class Dialog extends NodeList {
  @ADHS
  private String title;
  
  @ADHS
  private Integer width = 300;
  
  @ADHS
  private Integer height = null;
  
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public int getWidth() {
    return width;
  }
  public void setWidth(int width) {
    this.width = width;
  }
  public int getHeight() {
    return height;
  }
  public void setHeight(int height) {
    this.height = height;
  }
  
  
  
}
