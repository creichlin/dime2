package ch.kerbtier.dime2.mi;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.kerbtier.dime2.admin.model.Form;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.struwwel.Observable;

public class MiTemplate implements Runnable {
  private List<MiNode> nodes = new ArrayList<>();

  private Observable change = new Observable();

  public MiTemplate(String template, DataReader model) {
    Pattern patt = Pattern.compile("\\{\\{(.*?)(:(.*?))?}}");
    Matcher m = patt.matcher(template);
    int processed = 0;
    while (m.find()) {
      if (m.start() > processed) {
        nodes.add(new TextNode(template.substring(processed, m.start())));
      }
      nodes.add(new AttribNode(model, m.group(1), m.group(3)));
      processed = m.end();
    }
    if (processed < template.length()) {
      nodes.add(new TextNode(template.substring(processed)));
    }
  }

  public MiTemplate(String message, Form model) {
    this(message, new FormDataReader(model));
  }

  public MiTemplate(String message, HNode model) {
    this(message, new HNodeDataReader(model));
  }

  @Override
  public void run() {
    change.inform();
  }

  public String compile() {
    StringBuilder sb = new StringBuilder();
    for (MiNode node : nodes) {
      sb.append(node.compile());
    }
    return sb.toString();
  }

  public Observable change() {
    return change;
  }

  interface MiNode {
    String compile();
  }

  class TextNode implements MiNode {
    private String text;

    public TextNode(String text) {
      this.text = text;
    }

    @Override
    public String compile() {
      return text;
    }
  }

  class AttribNode implements MiNode {
    private String attrib;
    private String format;
    private DataReader model;

    public AttribNode(DataReader model, String attrib, String format) {
      this.model = model;
      this.attrib = attrib;
      this.format = format;

      model.onChange(attrib, MiTemplate.this);
    }

    @Override
    public String compile() {
      Object value = model.get(attrib);

      if (value == null) {
        return "---";
      } else if (value instanceof Date) {
        return formatDate((Date) value, format);
      } else {
        return formatString(value.toString(), format);
      }
    }
  }

  public String formatDate(Date value, String format) {
    // Hardcode UK locale for dateformats.
    // All other formats are just weird.
    if ("short".equals(format)) {
      return DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(value);
    } else if ("medium".equals(format)) {
      return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK).format(value);
    } else {
      return DateFormat.getDateInstance(DateFormat.LONG, Locale.UK).format(value);
    }
  }

  public String formatString(String value, String format) {
    // Hardcode UK locale for dateformats.
    // All other formats are just weird.
    if ("short".equals(format)) {
      if (value.length() > 24) {
        return value.substring(0, 20) + "...";
      }
    } else if ("medium".equals(format)) {
      if (value.length() > 44) {
        return value.substring(0, 40) + "...";
      }
    }
    return value;
  }
}
