package ch.kerbtier.dime2;

import ch.kerbtier.webb.dispatcher.Action;

import static ch.kerbtier.dime2.ContainerFacade.*;

public class AdminDispatchers {
  @Action(pattern = "exit")
  public void exit() {
    if(getConfig().isDevelopment()) {
      Server.sr.stop();
    }
  }

}
