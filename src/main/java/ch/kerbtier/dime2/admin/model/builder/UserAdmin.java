package ch.kerbtier.dime2.admin.model.builder;

import static ch.kerbtier.dime2.ContainerFacade.*;

import java.util.List;

import ch.kerbtier.dime2.admin.model.Button;
import ch.kerbtier.dime2.admin.model.Form;
import ch.kerbtier.dime2.admin.model.Grid;
import ch.kerbtier.dime2.admin.model.Label;
import ch.kerbtier.dime2.admin.model.NodeList;
import ch.kerbtier.dime2.admin.model.Ruler;
import ch.kerbtier.dime2.admin.model.Table;
import ch.kerbtier.dime2.admin.model.TextInput;
import ch.kerbtier.dime2.admin.model.form.FormEntity;
import ch.kerbtier.dime2.auth.User;
import ch.kerbtier.struwwel.MappedObservable;

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
      editButton.getClick().register(new Runnable() {
        @Override
        public void run() {
          showEdit(username);
        }
      });
      Button deleteButton = new Button(null, "delete");
      editButton.getClick().register(new Runnable() {
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
    save.getClick().register(new ButtonFormAction(form, "save", "saved user {{username}}", null));
    form.add(save);
    nl.add(form);
    
    getAdminRoot().getRoot().set("workspace", nl);
  }
  
  class UserAdminEntitySubject implements FormEntity {

    private User user;
    
    private String lastPassword = "";
    
    private String password = null;
    private String username = null;
    
    private MappedObservable<String> onChange = new MappedObservable<>();

    public UserAdminEntitySubject(User user) {
      this.user = user;
    }

    @Override
    public void commit() {
      if(user == null) {
        // create user, other form is needed...
        // showEdit();
      } else {
        if(password instanceof String && !((String) password).trim().matches("\\**") && !password.equals(lastPassword)) {
          user.setPassword((String)password);
          this.lastPassword = (String)password;
        }
      }
    }

    @Override
    public String get(String field) {
      if(field.equals("password")) {
        return "********";
      } else if(field.equals("username")) {
        return username != null ? username: user.getUsername();
      }
      return null;
    }

    @Override
    public List<FormEntity> getObjectList(String field) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addObjectTo(String list) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void set(String field, Object value) {
      if(field.equals("password")) {
        password = (String)value;
      } else if(field.equals("username")) {
        username = (String)value;
      }
    }

    @Override
    public MappedObservable<String> getChange() {
      return onChange;
    }

    @Override
    public void delete() {
      throw new UnsupportedOperationException();
    }
  }
}
