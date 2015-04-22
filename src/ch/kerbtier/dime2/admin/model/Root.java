package ch.kerbtier.dime2.admin.model;

import static ch.kerbtier.dime2.ContainerFacade.*;

import java.nio.file.Path;

import ch.kerbtier.dime2.admin.model.builder.Builder;
import ch.kerbtier.dime2.admin.model.builder.UserAdmin;
import ch.kerbtier.dime2.admin.queue.UserEventQueue;

public class Root extends Node {

  @ADHS
  private String title;

  @ADHS
  private Node workspace = new Label("Here we go");
  @ADHS
  private Node list = new Label("Here we go");
  @ADHS
  private Node log = new Label("Here we go");
  
  @ADHS
  private Menu menu = new Menu();

  public Root() {
    menu.setStyle("horizontal");
    new Builder().buildMenu(menu);
    
    MenuItem mi = new MenuItem();
    mi.setText("Admin");
    mi.getClick().onEvent(new Runnable() {
      @Override
      public void run() {
        new UserAdmin().start();
      }
    });
    
    menu.add(mi);
    

    MenuItem save = new MenuItem();
    save.setText("Write backup");
    save.getClick().onEvent(new Runnable() {
      @Override
      public void run() {
        try {
          Path path = getModels().writeData();
            getAdminRoot().getLog().log("Wrote data to disk " + path);
          }catch(Exception e) {
            getAdminRoot().getLog().log("Failed to write data. " + e.getMessage());
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
}
