package ch.kerbtier.dime2.util;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackuper {
  private Path folder;
  private String name;
  
  public FileBackuper(Path folder, String name) {
    this.folder = folder;
    this.name = name;
  }

  public synchronized void write(Path source) {
    try {
      if (!Files.exists(folder)) {
        Files.createDirectories(folder);
      }
      
      int cnt = 0;
      
      while(Files.exists(folder.resolve(name + String.format("%05d", cnt)))) {
        cnt++;
      }
      
      Path target = folder.resolve(name + String.format("%05d", cnt));
      
      Files.copy(source, target);
    } catch (Exception e) {
      throw new RuntimeException("failed to write file", e);
    }
  }
}
