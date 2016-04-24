package ch.kerbtier.dime2.server;

import ch.kerbtier.dime2.Models;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class StopContext {
  
  @InjectSingleton
  private Models models;

  public void run() {
    models.writeBackup();
    models.close();
    System.out.println("shutdown");
  }

}
