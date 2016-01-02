package ch.kerbtier.dime2.admin.model;

public class Log extends NodeList<Label> {
  public void log(String message) {

    if (size() > 6) {
      for (int cnt = 0; cnt < size() - 1; cnt++) {
        set(cnt, new Label(get(cnt + 1).getText()));
      }
      set(size() - 1, new Label(message));
    } else {
      add(new Label(message));
    }
  }
}
