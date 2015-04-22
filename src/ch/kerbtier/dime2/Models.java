package ch.kerbtier.dime2;

import java.nio.file.Path;

import ch.kerbtier.helene.HObject;
import ch.kerbtier.helene.Parse;
import ch.kerbtier.helene.entities.EntityMap;
import ch.kerbtier.helene.impl.ImpEntityMap;
import ch.kerbtier.helene.store.sql.SqlStore;

public class Models {
  private EntityMap definition;
  private SqlStore store;
  private Config config;

  public Models(Config config) {
    this.config = config;
    definition = new ImpEntityMap();
    Parse.extend(definition, config.getModulesPath("blog", "blog.model"));

    try {
      String url = "jdbc:h2:file:" + config.getDbPath("h23c") + ";USER=test;PASSWORD=test";

      store = new SqlStore(definition, "org.h2.Driver", url,  config.getDbPath("bin"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public HObject get(String model) {
    return store.getObject(model);
  }
  
  public HObject get() {
    return store;
  }
  
  public Path writeData() {
    return store.writeBackupTo(config.getBackupPath());
  }
}
