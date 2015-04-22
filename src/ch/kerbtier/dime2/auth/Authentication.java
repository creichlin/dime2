package ch.kerbtier.dime2.auth;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

public class Authentication {
  private Map<String, User> users = new HashMap<>();
  
  private Path path;
  
  public Authentication(@Nonnull Path path) {
    this.path = path;
    this.read();
    
    if(users.size() == 0) {
      try {
        create("admin", "password");
      } catch (UsernameExists e) {
        assert(false);
      }
    }
  }
  
  private void read() {
    Properties prop = new Properties();
    
    try {
      Reader reader= Files.newBufferedReader(path, Charset.forName("UTF-8"));
      prop.load(reader);
      reader.close();
    }catch(NoSuchFileException e) {
      Logger.getLogger(Authentication.class.getCanonicalName()).warning("no user database, will have to create a new one");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    for(String key: prop.stringPropertyNames()) {
      String value = prop.getProperty(key);
      String[] parts = value.split(",");
      users.put(key, new User(this, key, parts[0], parts[1]));
    }
    
    
  }

  public synchronized User get(@Nonnull String username, @Nonnull String password) throws InvalidCredentials {
    if (users.containsKey(username)) {
      if (users.get(username).authenticates(password)) {
        return users.get(username);
      }
    }
    throw new InvalidCredentials();
  }

  public boolean exists(@Nonnull String username) {
    return users.containsKey(username);
  }

  public synchronized User create(@Nonnull String username, @Nonnull String password) throws UsernameExists {
    if (users.containsKey(username)) {
      throw new UsernameExists();
    }
    User nu = new User(this, username, password);
    users.put(username, nu);
    save();
    return nu;
  }

  public List<String> getUsernames() {
    List<String> uns = new ArrayList<>();
    uns.addAll(users.keySet());
    Collections.sort(uns);
    return uns;
  }
  
  public User get(@Nonnull String username) {
    return users.get(username);
  }
  
  synchronized void save() {
    
    Properties prop = new Properties();
    for(User user: users.values()) {
      prop.setProperty(user.getUsername(), user.getPassword() + "," + user.getSalt());
    }

    try {
      Writer writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
      prop.store(writer, "");
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
