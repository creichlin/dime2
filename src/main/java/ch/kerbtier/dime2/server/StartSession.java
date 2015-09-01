package ch.kerbtier.dime2.server;

import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.esdi.Esdi;
import ch.kerbtier.webb.di.InjectSession;

public class StartSession {

  public void run() {
    Esdi.onRequestFor(AdminRoot.class).with(InjectSession.class).deliver(AdminRoot.class);
  }
}
