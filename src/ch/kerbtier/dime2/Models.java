package ch.kerbtier.dime2;

import java.nio.file.Path;

import ch.kerbtier.dime2.modules.Module;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.helene.HObject;
import ch.kerbtier.helene.Parse;
import ch.kerbtier.helene.Store;
import ch.kerbtier.helene.entities.EntityMap;
import ch.kerbtier.helene.impl.ImpEntityMap;
import ch.kerbtier.helene.store.sql.SqlStore;

public class Models {
  private EntityMap definition;
  private SqlStore store;
  private Config config;

  public Models(Config config, Modules modules) {
    this.config = config;
    definition = new ImpEntityMap();

    for (Module m : modules) {
      for (Path model : m.getModels()) {
        System.out.println("parse model: " + model);
        Parse.extend(definition, model);
      }
    }

    try {
      String url = "jdbc:h2:file:" + config.getDbPath("h23c") + ";USER=test;PASSWORD=test";

      store = new SqlStore(definition, "org.h2.Driver", url, config.getDbPath("bin"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public HNode get(String model) {
    return (HNode)store.get(model);
  }

  public Store get() {
    return store;
  }

  public Path writeData() {
    return store.writeBackupTo(config.getBackupPath());
  }
  
  public void close() {
    store.getDb().destroy();
  }
}
