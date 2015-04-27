package ch.kerbtier.dime2.admin.model.builder;

import ch.kerbtier.dime2.ContainerFacade;
import ch.kerbtier.dime2.admin.model.Form;
import ch.kerbtier.dime2.mi.MiTemplate;

public class ButtonFormAction implements Runnable {

  private Form form;
  private Command command;
  private String message;
  
  public ButtonFormAction(Form form, String command, String message) {
    this.form = form;
    this.command = Command.valueOf(command);
    this.message = message;
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
    }
    

  }

}
