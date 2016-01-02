package ch.kerbtier.dime2.admin.model;


import java.nio.file.Path;

import ch.kerbtier.dime2.Models;
import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.admin.model.builder.Builder;
import ch.kerbtier.dime2.admin.model.builder.UserAdmin;
import ch.kerbtier.dime2.admin.queue.UserEventQueue;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectSession;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class Root extends Node {

  @InjectSingleton
  private Models models;
  
  @InjectSession
  private AdminRoot adminRoot;
  
  @ADHS
  private String title;

  @ADHS
  private Node workspace = new Label("");
  @ADHS
  private Node list = new Label("");
  @ADHS
  private Node log = new Label("");
  
  @ADHS
  private Menu menu = new Menu();

  @ADHS
  private Dialog dialog;

  public Root() {
    menu.setStyle("horizontal");
    new Builder().buildMenu(menu);
    
    MenuItem mi = new MenuItem();
    mi.setText("Admin");
    mi.getClick().register(new Runnable() {
      @Override
      public void run() {
        new UserAdmin().start();
      }
    });
    
    menu.add(mi);
    

    MenuItem save = new MenuItem();
    save.setText("Write backup");
    save.getClick().register(new Runnable() {
      @Override
      public void run() {
        try {
          Path path = models.writeData();
            adminRoot.getLog().log("Wrote data to disk " + path);
          }catch(Exception e) {
            adminRoot.getLog().log("Failed to write data. " + e.getMessage());
        }
      }
    });
    
    menu.add(save);
    
    
    UserEventQueue eq = new UserEventQueue();
    eq.set(null, "root", this);
    setEventQueue(eq);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public UserEventQueue getQueue() {
    return getEventQueue();
  }

  public Node getWorkspace() {
    return workspace;
  }
  
  public Dialog getDialog() {
    return dialog;
  }

  public void setDialog(Dialog dialog) {
    this.dialog = dialog;
  }

  public void set(String area, Node newNode) {
    if("workspace".equals(area)) {
      this.workspace = newNode;
    }else if("list".equals(area)) {
      this.list = newNode;
    }else if("log".equals(area)) {
      this.log = newNode;
    } else {
      throw new RuntimeException("invalid area: " + area);
    }
  }

  @Override
  public void triggerAllEvents() {
    // send root / bootstrap event
    getEventQueue().set(null, "root", this);
    super.triggerAllEvents();
  }
  
  
}
