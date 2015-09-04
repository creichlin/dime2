package ch.kerbtier.dime2.admin.model.form;

import java.util.List;

import ch.kerbtier.struwwel.MappedObservable;

public interface FormEntity {

  void commit();

  Object get(String field);

  List<FormEntity> getObjectList(String field);

  void addObjectTo(String list);

  void set(String field, Object value);

  MappedObservable<String> getChange();

  void delete();
}