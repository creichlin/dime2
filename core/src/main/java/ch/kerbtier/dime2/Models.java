package ch.kerbtier.dime2;

import java.nio.charset.Charset;
import java.nio.file.Path;

import com.google.common.io.Files;

import ch.kerbtier.dime2.modules.Module;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.helene.Parse;
import ch.kerbtier.helene.Store;
import ch.kerbtier.helene.entities.EntityMap;
import ch.kerbtier.helene.impl.ImpEntityMap;
import ch.kerbtier.helene.store.sql.JsonWriter;
import ch.kerbtier.helene.store.sql.SqlStore;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class Models {
  private EntityMap definition;
  private SqlStore store;
  
  @InjectSingleton
  private Config config;
  
  @InjectSingleton
  private Modules modules;
  
  public Models() {
    definition = new ImpEntityMap();

    for (Module m : modules) {
      for (Path model : m.getModels()) {
        Parse.extend(definition, model);
      }
    }

    try {
      String url = "jdbc:h2:file:" + config.getDbPath("h23c") + ";USER=test;PASSWORD=test;AUTO_SERVER=TRUE";

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
    
    JsonWriter sql2Json = new JsonWriter(store);
    
    Path p = config.getBackupPath("latest.json");
    try {
      Files.write(sql2Json.write(), p.toFile(), Charset.forName("UTF-8"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
    return store.writeBackupTo(config.getBackupPath());
  }
  
  public void close() {
    store.getDb().destroy();
  }
}
