package ch.kerbtier.dime2.admin.model.builder;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.mi.MiTemplate;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.webb.di.InjectSession;

@Inject
public class ButtonModelAction implements Runnable {
  
  @InjectSession
  private AdminRoot adminRoot;

  private String commandName;
  private Method command;
  
  private HNode model;
  private String message;
  
  public ButtonModelAction(HNode model, String command, String message) {
    this.model = model;
    this.commandName = command;
    try {
      this.command = model.getClass().getMethod(command, (Class<?>[])null);
    } catch (NoSuchMethodException e) {
      // no such methods, leave command null
    }
    this.message = message;
  }
  
  
  @Override
  public void run() {
    
    if(command == null) {
      adminRoot.getLog().log("invalid action " + commandName);
      Logger logger = Logger.getLogger(ButtonModelAction.class.getName());
      logger.log(Level.WARNING, "invalid action " + commandName + " on model " + model);
      return;
    }
    
    try {
      command.invoke(model);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    
    if(message != null) {
      MiTemplate mi = new MiTemplate(message, model);
      adminRoot.getLog().log(mi.compile());
    }
  }
}
