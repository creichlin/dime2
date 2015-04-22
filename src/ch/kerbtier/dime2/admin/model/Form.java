package ch.kerbtier.dime2.admin.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.kerbtier.helene.HList;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.helene.HObject;
import ch.kerbtier.helene.HSlug;
import ch.kerbtier.helene.ModifiableNode;
import ch.kerbtier.helene.events.MappedListeners;

public class Form extends NodeList {

  private FormEntity subject;

  private List<FormElement> elements = new ArrayList<>();
  private MappedListeners<String> onChange = new MappedListeners<>();
  protected Map<String, Object> values = new HashMap<>();

  public Form(HNode subject) {
    this.subject = new HNodeFormEntity(subject);
  }

  public Form(FormEntity subject) {
    this.subject = subject;
  }

  public void register(FormElement element) {
    elements.add(element);

    element.setForm(this);

    element.initValue(subject);
  }

  public void save() {
    subject.commit(this);

    for (FormElement fe : elements) {
      fe.initValue(subject);
    }
  }

  public MappedListeners<String> getChange() {
    return onChange;
  }

  public void update(String field, Object value) {
    values.put(field, value);
    getChange().trigger(field);
  }

  public Object get(String field) {
    return values.get(field);
  }
  
  public FormEntity getBackendData() {
    return subject;
  }

  class HNodeFormEntity implements FormEntity {

    private HNode subject;

    public HNodeFormEntity(HNode subject) {
      this.subject = subject;
    }

    @Override
    public void commit(Form form) {
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
    }

    @Override
    public Date getDate(String field) {
      if (subject instanceof HObject) {
        return subject.getDate(field);
      }
      return null;
    }

    @Override
    public HSlug getSlug(String field) {
      if (subject instanceof HObject) {
        return subject.get(field, HSlug.class);
      }
      return null;
    }

    @Override
    public String getString(String field) {
      if (subject instanceof HObject) {
        return subject.getString(field);
      }
      return null;
    }

    @Override
    public Object get(String field) {
      if (subject instanceof HObject) {
        return subject.get(field);
      }
      return null;
    }

  }
}
