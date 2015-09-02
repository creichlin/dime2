package ch.kerbtier.dime2;

import java.nio.file.Path;

import ch.kerbtier.dime2.admin.ui.ElementNode;
import ch.kerbtier.dime2.admin.ui.Ui;
import ch.kerbtier.dime2.modules.Module;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectSingleton;

@Inject
public class Views {
  private Ui ui = new Ui();
  
  @InjectSingleton
  private Modules modules;
  
  
  public Views() {
    for(Module m: modules) {
      for(Path p: m.getViews()) {
        ui.read(p);
      }
    }
  }

  public ElementNode getWidget(String name) {
    return ui.getWidget(name);
  }

  public ElementNode getMenu() {
    return ui.getMenu();
  }
}
