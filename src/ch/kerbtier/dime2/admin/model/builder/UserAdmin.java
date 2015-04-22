package ch.kerbtier.dime2.admin.model.builder;

import static ch.kerbtier.dime2.ContainerFacade.*;

import java.util.Date;

import ch.kerbtier.dime2.admin.model.Button;
import ch.kerbtier.dime2.admin.model.Form;
import ch.kerbtier.dime2.admin.model.FormEntity;
import ch.kerbtier.dime2.admin.model.Grid;
import ch.kerbtier.dime2.admin.model.Label;
import ch.kerbtier.dime2.admin.model.NodeList;
import ch.kerbtier.dime2.admin.model.Ruler;
import ch.kerbtier.dime2.admin.model.Table;
import ch.kerbtier.dime2.admin.model.TextInput;
import ch.kerbtier.dime2.auth.User;
import ch.kerbtier.helene.HSlug;

public class UserAdmin {

  public void start() {
    NodeList nl = new NodeList();
    
    Button add = new Button("Add user");
    nl.add(add);
    nl.add(new Ruler());
    
    Table userList = new Table();
    nl.add(userList);
    userList.getColumns().add(new Label("Username"));
    userList.getColumns().add(new Label("Actions"));
    for(final String username: getAuthentication().getUsernames()) {
      Table.Row row = userList.appendRow();
      row.add(new Label(username));
      Button editButton = new Button(null, "edit");
      editButton.getClick().onEvent(new Runnable() {
        @Override
        public void run() {
          showEdit(username);
        }
      });
      Button deleteButton = new Button(null, "delete");
      editButton.getClick().onEvent(new Runnable() {
        @Override
        public void run() {
          deleteUser(username);
        }
      });
      
      row.add(editButton);
      row.add(deleteButton);
    }
    
    
    getAdminRoot().getRoot().set("list", nl);
  }

  protected void deleteUser(String username) {
    
  }

  protected void showEdit(String username) {
    NodeList nl = new NodeList();
    User user = getAuthentication().get(username);
    Form form = new Form(new UserAdminEntitySubject(user));
    
    Grid l1 = new Grid();
    Label pwl = new Label("Password");
    // TODO pwl.setSize(3);
    
    TextInput password = new TextInput("password");
    // TODO password.setSize(9);
    l1.add(pwl);
    l1.add(password);
    
    form.register(password);
    
    form.add(l1);
    
    Button save = new Button("Save user");
    save.getClick().onEvent(new ButtonFormAction(form, "save", "saved user {{username}}"));
    form.add(save);
    nl.add(form);
    
    getAdminRoot().getRoot().set("workspace", nl);
  }
  
  class UserAdminEntitySubject implements FormEntity {

    private User user;
    
    private String lastPassword = "";

    public UserAdminEntitySubject(User user) {
      this.user = user;
    }

    @Override
    public void commit(Form form) {
      if(user == null) {
        // create user, other form is needed...
        // showEdit();
      } else {
        Object pw = form.get("password");
        if(pw instanceof String && !((String) pw).trim().matches("\\**") && !pw.equals(lastPassword)) {
          user.setPassword((String)pw);
          this.lastPassword = (String)pw;
        }
      }
    }

    @Override
    public Date getDate(String field) {
      return null;
    }

    @Override
    public HSlug getSlug(String field) {
      return null;
    }

    @Override
    public String getString(String field) {
      if(field.equals("password")) {
        return "********";
      } else if(field.equals("username")) {
        return user.getUsername();
      }
      return null;
    }

    @Override
    public Object get(String field) {
      return getString(field);
    }
  }
}
