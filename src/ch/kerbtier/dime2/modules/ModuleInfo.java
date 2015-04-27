package ch.kerbtier.dime2.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;

public class ModuleInfo {

  private String description;

  private List<String> dependencies = new ArrayList<>();

  public ModuleInfo(String description) {
    this.description = description;

    collectDependencies();

    System.out.println(infos());
  }

  public String infos() {
    return "dependencies: " + Joiner.on(", ").join(dependencies) + "\n";
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

  public List<String> getDependencies() {
    return dependencies;
  }
}
