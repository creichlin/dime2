package ch.kerbtier.dime2.mi;

import ch.kerbtier.helene.HNode;
import ch.kerbtier.helene.HObject;

public class HNodeDataReader implements DataReader {
  
  private HNode subject;
  
  public HNodeDataReader(HNode subject) {
    this.subject = subject;
  }
  
  
  @Override
  public void onChange(String attrib, Runnable run) {
    if(subject instanceof HObject) {
      ((HObject)subject).onChange(attrib,run);
    }
  }

  @Override
  public Object get(String attrib) {
    if(subject instanceof HObject) {
      return ((HObject)subject).get(attrib);
    }
    return null;
  }

}
