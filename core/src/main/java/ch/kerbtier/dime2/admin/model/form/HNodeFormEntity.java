package ch.kerbtier.dime2.admin.model.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HList;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.helene.HObject;
import ch.kerbtier.helene.ModifiableNode;
import ch.kerbtier.struwwel.MappedObservable;
import ch.kerbtier.webb.di.InjectSession;

@Inject
public class HNodeFormEntity implements FormEntity {
  
  private Logger logger = Logger.getLogger(HNodeFormEntity.class.getCanonicalName());

  protected Map<String, Object> values = new HashMap<>();

  private HNodeFormEntity parent;
  private HNode subject;
  
  private Map<String, List<FormEntity>> lists = new HashMap<>();
  private MappedObservable<String> onChange = new MappedObservable<>();
  private List<HObject> toDelete = new ArrayList<>();

  @InjectSession
  private AdminRoot adminRoot;
  
  public HNodeFormEntity(HNodeFormEntity parent, HNode subject) {
    this.parent = parent;
    this.subject = subject;
  }

  @Override
  public void commit() {
    ModifiableNode mod;
    if (subject instanceof HList) {
      mod = ((HList<?>) subject).add();
    } else {
      mod = ((HObject) subject).update();
    }

    for (String f : values.keySet()) {
      mod.set(f, values.get(f));
    }

    subject = mod.commit();
    
    for(List<FormEntity> fes: lists.values()) {
      for(FormEntity fe: fes) {
        System.out.println("save child: " + fe);
        fe.commit();
      }
    }

    for(HObject ho: toDelete) {
      System.out.println("delete " + ho);
      ho.delete();
    }
  }

  @Override
  public Object get(String field) {
    if(values.containsKey(field)) {
      return values.get(field);
    }
    if (subject instanceof HObject) {
      return subject.get(field);
    }
    return null;
  }

  @Override
  public List<FormEntity> getObjectList(String name) {
    if(!lists.containsKey(name)) {
      List<FormEntity> hnfes = new ArrayList<>();
      for(HObject obj: subject.getObjects(name)) {
        hnfes.add(new HNodeFormEntity(this, obj));
      }
      lists.put(name, hnfes);
    }
    return lists.get(name);
  }

  @Override
  public void addObjectTo(String list) {
    try {
      List<FormEntity> fel = getObjectList(list);
      fel.add(new HNodeFormEntity(this, subject.getObjects(list)));
      onChange.inform(list);
    }catch(UnsupportedOperationException e) {
      logger.warning("cannot add new element to list of unsaved element");
     adminRoot.getLog().log("cannot add new element to list of unsaved element");
    }
  }

  @Override
  public void set(String field, Object value) {
    values.put(field, value);
  }
  
  @Override
  public String toString() {
    return "HNodeFormEntity[" + subject.toString() + "]";
  }

  @Override
  public MappedObservable<String> getChange() {
    return onChange;
  }

  @Override
  public void delete() {
    if(parent == null) {
      throw new IllegalStateException("cannot delete parent form");
    }
    parent.delete(this);
  }

  private void delete(HNodeFormEntity child) {
    for(String field: lists.keySet()) {
      List<FormEntity> feList = lists.get(field);
      if(feList.remove(child)) {
        onChange.inform(field);
        if(child.subject instanceof HObject) {
          toDelete.add((HObject)child.subject);
        }
      }
    }
  }
}