package ch.kerbtier.dime2;

import java.nio.file.Path;

import com.mchange.util.AssertException;

import ch.kerbtier.achaia.Parse;
import ch.kerbtier.achaia.schema.MapEntity;
import ch.kerbtier.achaia.schema.implementation.ImpMapEntity;
import ch.kerbtier.dime2.modules.Module;
import ch.kerbtier.epirus.Epirus;
import ch.kerbtier.epirus.EpirusObject;
import ch.kerbtier.epirus.implementation.EpirusImplementation;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.pogo.PogoFactory;
import ch.kerbtier.pogo.hops.HopsPogoFactory;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class Models {
  private MapEntity definition;
  
  @InjectSingleton
  private Config config;
  
  @InjectSingleton
  private Modules modules;

  private PogoFactory factory;

  
  public Models() {
    definition = new ImpMapEntity();

    for (Module m : modules) {
      for (Path model : m.getModels()) {
        Parse.extend(definition, model);
      }
    }

    try {
      String url = "jdbc:h2:file:" + config.getDbPath("h23epi") + ";USER=test;PASSWORD=test;AUTO_SERVER=TRUE";
      factory = new HopsPogoFactory("org.h2.Driver", url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Epirus get() {
    return new EpirusImplementation(definition, factory.create());
  }

  public EpirusObject get(String name) {
    throw new AssertException();
  }

}
