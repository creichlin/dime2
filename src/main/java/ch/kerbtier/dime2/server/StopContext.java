package ch.kerbtier.dime2.server;

import ch.kerbtier.dime2.Models;
import ch.kerbtier.esdi.Esdi;
import ch.kerbtier.webb.di.InjectSingleton;

public class StopContext {

  public void run() {
    Models models = Esdi.get(Models.class, InjectSingleton.class);
    models.writeData();
    models.close();
    System.out.println("shutdown");
  }

}
