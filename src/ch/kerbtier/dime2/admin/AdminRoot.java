package ch.kerbtier.dime2.admin;

import ch.kerbtier.dime2.admin.model.Log;
import ch.kerbtier.dime2.admin.model.Root;
import ch.kerbtier.dime2.auth.User;

public class AdminRoot {
  private Root root;
  private User user;
  private Log log;
  
  public Root getRoot() {
    return root;
  }

  public void setRoot(Root root) {
    this.root = root;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Log getLog() {
    return log;
  }

  public void setLog(Log log) {
    this.log = log;
  }

}
