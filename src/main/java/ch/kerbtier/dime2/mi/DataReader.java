package ch.kerbtier.dime2.mi;

public interface DataReader {

  void onChange(String attrib, Runnable run);

  Object get(String attrib);

}
