package ch.kerbtier.dime2;

import ch.kerbtier.dime2.admin.ui.ElementNode;
import ch.kerbtier.dime2.admin.ui.Ui;

public class Views {
  private Ui ui = new Ui();
  
  public Views(Config config) {
    ui.read(config.getModulesPath("blog", "blog.view"));
  }

  public ElementNode getWidget(String name) {
    return ui.getWidget(name);
  }

  public ElementNode getMenu() {
    return ui.getMenu();
  }
}
