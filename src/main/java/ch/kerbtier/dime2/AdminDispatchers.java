package ch.kerbtier.dime2;


import static ch.kerbtier.dime2.ContainerFacade.*;
import ch.kerbtier.amarillo.Route;

public class AdminDispatchers {
  @Route(pattern = "exit")
  public void exit() {
    if(getConfig().isDevelopment()) {
      Server.sr.stop();
    }
  }

}
