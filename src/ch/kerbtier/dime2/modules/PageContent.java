package ch.kerbtier.dime2.modules;

import java.io.IOException;
import java.nio.file.Files;

import com.github.jknack.handlebars.io.TemplateSource;

public class PageContent implements TemplateSource {
  
  private Page page;
  private String content;
  
  public PageContent(Page page, String content) {
    this.page = page;
    this.content = content;
  }
  
  @Override
  public String content() throws IOException {
    return content;
  }

  @Override
  public String filename() {
    return page.getFile().toString();
  }

  @Override
  public long lastModified() {
    try {
      return Files.getLastModifiedTime(page.getFile()).toMillis();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return 0;
  }

  @Override
  public int hashCode() {
    return content.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof PageContent) {
      return content.equals(((PageContent)obj).content);
    }
    return false;
  }

}
