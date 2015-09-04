package ch.kerbtier.dime2;


import ch.kerbtier.amarillo.Route;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class AdminDispatchers {
  
  @InjectSingleton
  private Config config;
  
  @Route(pattern = "exit")
  public void exit() {
    if(config.isDevelopment()) {
      System.exit(0);
    }
  }

}
