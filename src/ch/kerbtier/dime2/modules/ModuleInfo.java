package ch.kerbtier.dime2.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;

public class ModuleInfo {

  private String description;

  private List<String> dependencies = new ArrayList<>();
  private Map<String, Mapping> mappings = new HashMap<>();

  public ModuleInfo(String description) {
    this.description = description;

    collectDependencies();
    collectMappings();

    System.out.println(infos());
  }

  public String infos() {
    String infos = "dependencies: " + Joiner.on(", ").join(dependencies) + "\n";
    
    for(Mapping m: mappings.values()) {
      infos += "mapping: " + m.getPath() + " -> " + m.getTemplate() + "/" + m.getModel() + " " + m.getMime();
    }
    return infos;
  }

  private void collectDependencies() {
    Pattern p = Pattern.compile("depends on ([a-z0-9-]+)(?:, ([a-z0-9-]+))*(?: and ([a-z0-9-]+))?");

    Matcher m = p.matcher(description);

    while (m.find()) {
      for (int cnt = 0; cnt < m.groupCount(); cnt++) {
        String mod = m.group(cnt + 1);
        if (mod != null) {
          if (!dependencies.contains(mod)) {
            dependencies.add(mod);
          }
        }
      }
    }
  }
  
  
  private void collectMappings() {
    Pattern p = Pattern.compile("path ([^ ]+) is mapped to the template ([^ ]+) using model ([^ ]+) and mime ([^ ]+)");
    
    
    Matcher m = p.matcher(description);

    while (m.find()) {
      mappings.put(m.group(1), new Mapping(m.group(1), m.group(2), m.group(3), m.group(4)));
    }
  }

  public List<String> getDependencies() {
    return dependencies;
  }

  public Map<String, Mapping> getMappings() {
    return mappings;
  }

  public class Mapping {
    private String path;
    private String template;
    private String model;
    private String mime;
    
    public Mapping(String path, String template, String model, String mime) {
      this.path = path;
      this.template = template;
      this.model = model;
      this.mime = mime;
    }
    
    public String getPath() {
      return path;
    }
    public String getTemplate() {
      return template;
    }
    public String getModel() {
      return model;
    }
    public String getMime() {
      return mime;
    }
    
    
  }
}

