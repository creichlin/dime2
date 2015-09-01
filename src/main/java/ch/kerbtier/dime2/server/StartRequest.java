package ch.kerbtier.dime2.server;

import ch.kerbtier.dime2.Response;
import ch.kerbtier.esdi.Esdi;
import ch.kerbtier.webb.di.InjectRequest;
import ch.kerbtier.webb.util.HttpInfo;

public class StartRequest {

  public void run() {

    Esdi.onRequestFor(Response.class).with(InjectRequest.class).deliver(Response.class);
    Esdi.onRequestFor(HttpInfo.class).with(InjectRequest.class).deliver(HttpInfo.class);

  }

}
