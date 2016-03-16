package ch.kerbtier.dime2.admin.model.builder;

import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.admin.ViewModelActions;
import ch.kerbtier.dime2.admin.model.Node;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectSession;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class CustomAction implements Runnable {
  
  @InjectSingleton
  private ViewModelActions viewModelActions;
  
  @InjectSession
  private AdminRoot adminRoot;

  private String className;
  private String id;
  
  private Node model;
  private String message;
  
  public CustomAction(Node model, String className, String id, String message) {
    this.model = model;
    this.className = className;
    this.id = id;
    this.message = message;
  }
  
  
  @Override
  public void run() {
      Node used = model;
      
      try {
        used = model.getNodeById(Long.parseLong(id));
      }catch(Exception e) {
        //
      }
      
      viewModelActions.run(className, used);
    
    if(message != null) {
      adminRoot.getLog().log(message);
    }
  }
}
