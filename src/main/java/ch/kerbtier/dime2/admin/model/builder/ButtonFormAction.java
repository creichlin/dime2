package ch.kerbtier.dime2.admin.model.builder;

import java.util.logging.Logger;

import ch.kerbtier.dime2.ContainerFacade;
import ch.kerbtier.dime2.admin.model.Form;
import ch.kerbtier.dime2.mi.MiTemplate;

public class ButtonFormAction implements Runnable {

  private Logger logger= Logger.getLogger(ButtonFormAction.class.getCanonicalName());
  
  private Form form;
  private Command command;
  private String message;
  private String list;
  
  public ButtonFormAction(Form form, String command, String message, String list) {
    this.form = form;
    this.command = Command.valueOf(command);
    this.message = message;
    this.list = list;
  }
  
  
  @Override
  public void run() {
    if(command == Command.save) {
      if(form.isValid()) { 
        form.save();
        if(message != null) {
          MiTemplate mi = new MiTemplate(message, form);
          ContainerFacade.getAdminRoot().getLog().log(mi.compile());
        }
      } else {
        if(message != null) {
          MiTemplate mi = new MiTemplate("Cannot save, form has invalid values.", form);
          ContainerFacade.getAdminRoot().getLog().log(mi.compile());
        }
      }
    } else if(command == Command.add) {
      form.addObjectTo(list);
    } else if(command == Command.delete) {
      form.delete();
    } else {
      throw new RuntimeException();
    }
    

  }

}
