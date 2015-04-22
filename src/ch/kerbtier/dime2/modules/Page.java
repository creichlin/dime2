package ch.kerbtier.dime2.modules;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

public class Page {

  private Site site;
  private Path file;
  private PageContent content;
  private long last = 0;

  public Page(Site site, Path file) {
    this.site = site;
    this.file = file;
  }

  public void reRead() {
    try {
      if(Files.getLastModifiedTime(file).toMillis() != last) {
        last = Files.getLastModifiedTime(file).toMillis();
        content = new PageContent(this, FileUtils.readFileToString(file.toFile(), Charset.forName("utf-8")));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Site getSite() {
    return site;
  }

  public Path getFile() {
    return file;
  }

  public PageContent getContent() {
    reRead();
    return content;
  }

  public Path getPath() {
    return file.getParent();
  }
}
