package ch.kerbtier.dime2.admin.ui;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {

  private Ui ui;

  private Stack<ElementNode> current = new Stack<>();

  public SaxHandler(Ui ui) {
    this.ui = ui;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if ("widget".equals(qName)) {
      ElementNode en = ui.getWidget(attributes.getValue("name"));
      current.push(en);
    } else if ("menu".equals(qName)) {
      ElementNode en = ui.getMenu();
      current.push(en);
    } else if (current.size() > 0) {
      ElementNode en = new ElementNode(current.peek(), qName);
      current.peek().add(en);
      current.push(en);
    }
    if(current.size() > 0) {
      for(int attributeIndex = 0; attributeIndex < attributes.getLength(); attributeIndex++) {
        current.peek().setAttribute(attributes.getQName(attributeIndex), attributes.getValue(attributeIndex));
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (current.size() > 0) {
      current.pop();
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (current.size() > 0) {
      current.peek().add(new TextNode(new String(ch, start, length)));
    }
  }

}
