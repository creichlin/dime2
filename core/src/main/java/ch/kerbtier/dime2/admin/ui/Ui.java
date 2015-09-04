package ch.kerbtier.dime2.admin.ui;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Ui {

  private Map<String, ElementNode> views = new HashMap<>();
  private ElementNode menu = new ElementNode(null, "menu");
  
  public void read(Path path) {
    System.out.println("read " + path);
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      InputStream xmlInput = new FileInputStream(path.toFile());

      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(xmlInput, new SaxHandler(this));
    } catch (Throwable err) {
      err.printStackTrace();
    }
  }


  public ElementNode getWidget(String name) {
    if(!views.containsKey(name)) {
      views.put(name, new ElementNode(null, "widget"));
    }
    return views.get(name);
  }


  public ElementNode getMenu() {
    return menu;
  }

}
