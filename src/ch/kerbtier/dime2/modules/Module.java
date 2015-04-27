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

public class Module {
  private Path folder;
  private Site site;
  private Modules parent;
  private String description;
  private ModuleInfo moduleInfo;

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

}
