package ch.kerbtier.dime2;

import java.nio.file.Path;
import java.nio.file.Paths;

import ch.kerbtier.webb.util.Configuration;
import ch.kerbtier.webb.util.ContextInfo;

public class Config {
  private Configuration config;
  private ContextInfo contextInfo;

  public Config(Configuration config, ContextInfo contextInfo) {
    this.config = config;
    this.contextInfo = contextInfo;
  }

  public boolean isDevelopment() {
    return config.get("development", false);
  }
  
  private Path getPath(String key, String def, String[] parts) {
    Path p = Paths.get(config.get(key, def));
    p = contextInfo.getLocalPath().resolve(p);
    Path cage = p.normalize();
    
    for(String part: parts) {
      p = p.resolve(part);
    }
    
    p = p.normalize();
    
    if(!p.startsWith(cage)) {
      throw new RuntimeException("trying to access outside path");
    }
    
    return p;
  }

  public Path getModulesPath(String... paths) {
    return getPath("modules-path", "modules", paths);
  }

  public Path getDbPath(String... paths) {
    return getPath("db-path", "db", paths);
  }

  public Path getImageCache(String... paths) {
    return getPath("image-cache", "ic", paths);
  }

  public Path getUserRealm() {
    return getPath("user-realm", "users.properties", new String[0]);
  }

  public Path getBackupPath(String... paths) {
    return getPath("backup", getDbPath("backup").toString(), paths);
  }
}
