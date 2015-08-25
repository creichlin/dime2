package ch.kerbtier.dime2;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public class Response {

  private Path file;
  private String content;
  private String contentType;
  private Object json;
  private BufferedImage image;

  public void setFile(Path file) {
    this.file = file;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  Path getFile() {
    return file;
  }

  String getContentType() {
    return contentType;
  }

  public void setJson(Object json) {
    this.json = json;
  }

  public Object getJson() {
    return json;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public void setImage(BufferedImage image) {
    this.image = image;
  }

  public BufferedImage getImage() {
    return image;
  }
}
