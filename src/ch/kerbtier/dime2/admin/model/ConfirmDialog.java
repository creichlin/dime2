package ch.kerbtier.dime2.admin.model;

import ch.kerbtier.dime2.ContainerFacade;
import ch.kerbtier.helene.events.Listeners;

public class ConfirmDialog extends Dialog {

  private Label message = new Label("");
  
  private final Listeners confirm = new Listeners();
  
  public ConfirmDialog(String message) {
    setTitle("Please confirm... or not");
    
    this.message.setText(message);
    add(this.message);
    
    NodeList buttons = new NodeList();
    
    Button no = new Button("No");
    Button yes = new Button("Yes");
    
    no.getClick().onEvent(new Runnable() {
      @Override
      public void run() {
        ContainerFacade.getAdminRoot().getRoot().setDialog(null);
      }
    });

    yes.getClick().onEvent(new Runnable() {
      @Override
      public void run() {
        getConfirm().trigger();
        ContainerFacade.getAdminRoot().getRoot().setDialog(null);
      }
    });
    
    buttons.add(no);
    buttons.add(yes);
    add(buttons);
  }
  
  public Listeners getConfirm() {
    return confirm;
  }
}
