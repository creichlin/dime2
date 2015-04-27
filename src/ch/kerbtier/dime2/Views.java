package ch.kerbtier.dime2;

import java.nio.file.Path;

import ch.kerbtier.dime2.admin.ui.ElementNode;
import ch.kerbtier.dime2.admin.ui.Ui;
import ch.kerbtier.dime2.modules.Module;

public class Views {
  private Ui ui = new Ui();
  private Modules modules;
  
  public Views(Config config, Modules modules) {
    this.modules = modules;
    
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
