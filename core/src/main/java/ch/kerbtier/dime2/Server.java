package ch.kerbtier.dime2;

import ch.kerbtier.dime2.server.Request;
import ch.kerbtier.dime2.server.StartContext;
import ch.kerbtier.dime2.server.StartRequest;
import ch.kerbtier.dime2.server.StartSession;
import ch.kerbtier.dime2.server.StopContext;
import ch.kerbtier.dime2.server.StopRequest;
import ch.kerbtier.dime2.server.StopSession;
import ch.kerbtier.webb.Livecycles;

public class Server implements Livecycles {



  @Override
  public void request() {
    new Request().run();
  }

  @Override
  public void startContext() {
    new StartContext().run();
  }

  @Override
  public void startRequest() {
    new StartRequest().run();
  }

  @Override
  public void startSession() {
    new StartSession().run();
  }

  @Override
  public void stopContext() {
    new StopContext().run();
  }

  @Override
  public void stopRequest() {
    new StopRequest().run();
  }

  @Override
  public void stopSession() {
    new StopSession().run();
  };

}
