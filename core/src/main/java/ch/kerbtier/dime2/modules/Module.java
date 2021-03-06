package ch.kerbtier.dime2.modules;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import ch.kerbtier.dime2.Modules;
import ch.kerbtier.dime2.modules.ModuleInfo.Mapping;

public class Module {
  private Path folder;
  private Site site;
  private Modules parent;
  private String description;
  private ModuleInfo moduleInfo;
  private ModuleJar jar = null;

  public Module(Modules parent, Path folder) {
    this.parent = parent;
    this.folder = folder;

    if (!Files.exists(folder) || !Files.isDirectory(folder)) {
      throw new RuntimeException("invalid module path " + folder);
    }

    if (Files.exists(folder.resolve("site"))) {
      site = new Site(this, folder.resolve("site"));
    }

    try {
      description = FileUtils.readFileToString(folder.resolve("module.md").toFile());
    } catch (IOException e) {
      description = "";
    }

    moduleInfo = new ModuleInfo(description);

    // List all jars in the modules lib folder and store them in a list
    List<Path> jars = new ArrayList<>();
    Path dir = folder.resolve("lib");

    // only if lib folder exists...
    if (Files.exists(dir)) {
      DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(Path file) throws IOException {
          return file.getFileName().toString().endsWith(".jar");
        }
      };
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
        for (Path path : stream) {
          jars.add(path);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      // if one or more jars found, add them as a ModuleJar and load/init them
      if (jars.size() > 0) {
        jar = new ModuleJar(jars, this);
        jar.init();
      }
    }

  }

  public Path getFolder() {
    return folder;
  }

  public boolean hasSite() {
    return site != null;
  }

  public Site getSite() {
    return site;
  }

  public String getName() {
    return folder.getFileName().toString();
  }

  public Set<Module> getDependencies() {
    Set<Module> mods = new HashSet<>();

    for (String modName : moduleInfo.getDependencies()) {
      Module dm = parent.getModule(modName);
      if (dm != null) {
        mods.add(dm);
      } else {
        System.out.println("module " + modName + " doesnt exist");
      }
    }

    return mods;
  }

  public List<Path> getModels() {
    List<Path> result = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.model")) {
      for (Path entry : stream) {
        result.add(entry);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public List<Path> getViews() {
    List<Path> result = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.view")) {
      for (Path entry : stream) {
        result.add(entry);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public Mapping getMapping(String slug) {
    return moduleInfo.getMappings().get(slug);
  }

  public ModuleInfo getInfo() {
    return moduleInfo;
  }

}
