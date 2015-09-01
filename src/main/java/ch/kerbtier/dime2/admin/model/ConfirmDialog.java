package ch.kerbtier.dime2.admin.model;

import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.struwwel.Observable;
import ch.kerbtier.webb.di.InjectSession;

@Inject
public class ConfirmDialog extends Dialog {
  
  @InjectSession
  private AdminRoot adminRoot;

  private Label message = new Label("");
  
  private final Observable confirm = new Observable();
  
  public ConfirmDialog(String message) {
    setTitle("Please confirm... or not");
    
    this.message.setText(message);
    add(this.message);
    
    NodeList buttons = new NodeList();
    
    Button no = new Button("No");
    Button yes = new Button("Yes");
    
    no.getClick().register(new Runnable() {
      @Override
      public void run() {
        adminRoot.getRoot().setDialog(null);
      }
    });

    yes.getClick().register(new Runnable() {
      @Override
      public void run() {
        getConfirm().inform();
        adminRoot.getRoot().setDialog(null);
      }
    });
    
    buttons.add(no);
    buttons.add(yes);
    add(buttons);
  }
  
  public Observable getConfirm() {
    return confirm;
  }
}
