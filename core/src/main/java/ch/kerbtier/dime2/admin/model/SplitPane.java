package ch.kerbtier.dime2.admin.model;

public class SplitPane extends Node {
  @ADHS
  private Node left;
  @ADHS
  private Node right;
  @ADHS
  private float position;
  
  public SplitPane(float position) {
    this.position = position;
  }

  public Node getLeft() {
    return left;
  }

  public void setLeft(Node left) {
    this.left = left;
  }

  public Node getRight() {
    return right;
  }

  public void setRight(Node right) {
    this.right = right;
  }
}
