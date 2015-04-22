package ch.kerbtier.dime2.admin.model;

public class Table extends NodeList {
  @ADHS
  private Columns columns = new Columns();
  
  public Columns getColumns() {
    return columns;
  }
  
  public Row appendRow() {
    Row row = new Row();
    add(row);
    return row;
  }
  
  public class Columns extends NodeList {
    // just a list
  }
  
  public class Row extends NodeList {
    // just a list
  }
}
