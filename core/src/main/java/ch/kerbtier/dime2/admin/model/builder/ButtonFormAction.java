package ch.kerbtier.dime2.admin.model.builder;

import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.admin.model.Form;
import ch.kerbtier.dime2.mi.MiTemplate;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectSession;

@Inject
public class ButtonFormAction implements Runnable {
  
  @InjectSession
  private AdminRoot adminRoot;

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
          adminRoot.getLog().log(mi.compile());
        }
      } else {
        if(message != null) {
          MiTemplate mi = new MiTemplate("Cannot save, form has invalid values.", form);
          adminRoot.getLog().log(mi.compile());
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
