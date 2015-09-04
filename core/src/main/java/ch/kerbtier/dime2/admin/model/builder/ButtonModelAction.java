package ch.kerbtier.dime2.admin.model.builder;

import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.mi.MiTemplate;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.webb.di.InjectSession;

@Inject
public class ButtonModelAction implements Runnable {
  
  @InjectSession
  private AdminRoot adminRoot;

  private HNode model;
  private Command command;
  private String message;
  
  public ButtonModelAction(HNode model, String command, String message) {
    this.model = model;
    this.command = Command.valueOf(command);
    this.message = message;
  }
  
  
  @Override
  public void run() {
    if(command == Command.delete) {
      model.delete();
    }
    if(command == Command.up) {
      model.up();
    }
    if(command == Command.down) {
      model.down();
    }
    
    if(message != null) {
      MiTemplate mi = new MiTemplate(message, model);
      adminRoot.getLog().log(mi.compile());
    }
  }
}
