package ch.kerbtier.dime2.site.renderer.queries;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ch.kerbtier.helene.HObject;

public class Published implements Query {

  @SuppressWarnings("unchecked")
  @Override
  public void transform(List<Object> input) {
    List<HObject> hos = (List) input;
    for (Iterator<HObject> i = hos.iterator(); i.hasNext();) {
      if (!valid(i.next())) {
        i.remove();
      }
    }
    Collections.sort(hos, new Comparator<HObject>() {
      @Override
      public int compare(HObject o1, HObject o2) {
        Date d1 = o1.getDate("date");
        Date d2 = o2.getDate("date");
        
        if(d1 == null && d2 == null) {
          return 0;
        } else if(d1 == null) {
          return 1;
        } else if(d2 == null) {
          return -1;
        }
        
        return d2.compareTo(d1);
      }
    });
  }

  @Override
  public boolean valid(HObject object) {
    Date d = object.getDate("date");
    return d != null && d.before(new Date());
  }
}
