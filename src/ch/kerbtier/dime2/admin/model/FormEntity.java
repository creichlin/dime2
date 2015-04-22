package ch.kerbtier.dime2.admin.model;

import java.util.Date;

import ch.kerbtier.helene.HSlug;

public interface FormEntity {

  void commit(Form form);

  Date getDate(String field);

  HSlug getSlug(String field);

  String getString(String field);
  
  Object get(String field);

}