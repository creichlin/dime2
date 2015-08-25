package ch.kerbtier.dime2.admin.model.form;

import java.util.List;

import ch.kerbtier.helene.events.MappedListeners;

public interface FormEntity {

  void commit();

  Object get(String field);

  List<FormEntity> getObjectList(String field);

  void addObjectTo(String list);

  void set(String field, Object value);

  MappedListeners<String> getChange();

  void delete();
}