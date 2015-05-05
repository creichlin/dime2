package ch.kerbtier.dime2.admin.model.builder;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Stack;

import ch.kerbtier.dime2.admin.model.Button;
import ch.kerbtier.dime2.admin.model.ConfirmDialog;
import ch.kerbtier.dime2.admin.model.DateInput;
import ch.kerbtier.dime2.admin.model.FileInput;
import ch.kerbtier.dime2.admin.model.Form;
import ch.kerbtier.dime2.admin.model.Grid;
import ch.kerbtier.dime2.admin.model.Label;
import ch.kerbtier.dime2.admin.model.MenuItem;
import ch.kerbtier.dime2.admin.model.Node;
import ch.kerbtier.dime2.admin.model.NodeList;
import ch.kerbtier.dime2.admin.model.Ruler;
import ch.kerbtier.dime2.admin.model.SlugInput;
import ch.kerbtier.dime2.admin.model.Table;
import ch.kerbtier.dime2.admin.model.Table.Row;
import ch.kerbtier.dime2.admin.model.TextArea;
import ch.kerbtier.dime2.admin.model.TextInput;
import ch.kerbtier.dime2.admin.ui.ElementNode;
import ch.kerbtier.dime2.mi.MiTemplate;
import ch.kerbtier.helene.HList;
import ch.kerbtier.helene.HNode;
import ch.kerbtier.helene.HObject;
import ch.kerbtier.helene.events.Listeners;
import static ch.kerbtier.dime2.ContainerFacade.*;

public class Builder {

  Stack<Form> forms = new Stack<>();

  public void buildMenu(NodeList menu) {
    ElementNode en = getViews().getMenu();

    for (ElementNode uin : en.getElements()) {
      menu.add(build(uin, null));
    }
  }

  public Node build(ElementNode en, HNode node) {
    String name = en.getName();

    try {
      Node created = (Node) getClass().getMethod(name, ElementNode.class, HNode.class).invoke(this, en,
          getModel(en, node));

      if (en.getParent() != null && en.getParent().getName().equals("grid")) {
        if (en.getAttribute("span") != null) {
          created.setSpan(Integer.parseInt(en.getAttribute("span")));
        } else {
          created.setSpan(1);
        }
      }

      if (en.getAttribute("style") != null) {
        created.setStyle(en.getAttribute("style"));
      }

      return created;
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
        | SecurityException e) {
      throw new RuntimeException(e);
    }
  }

  public Node entry(ElementNode en, HNode node) {
    MenuItem item = new MenuItem();
    item.setText(en.getText().trim());

    for (final ElementNode setViewNode : en.getElements("setView")) {
      item.getClick().onEvent(new Runnable() {
        @Override
        public void run() {
          String view = setViewNode.getAttribute("view");
          String model = setViewNode.getAttribute("model");
          Node newNode = new Builder().build(getViews().getWidget(view).firstElement(), getModels().get(model));
          getAdminRoot().getRoot().set(setViewNode.getAttribute("area"), newNode);
        }
      });
    }

    for (ElementNode uin : en.getElements("!setView")) {
      item.add(build(uin, node));
    }

    return item;
  }

  public Node label(ElementNode en, HNode node) {
    final Label label = new Label("");

    final MiTemplate template = new MiTemplate(en.getText(), node);

    label.setText(template.compile());

    template.change().onEvent(new Runnable() {
      @Override
      public void run() {
        label.setText(template.compile());
      }
    }).keepFor(label);

    return label;
  }

  public Node button(final ElementNode en, final HNode node) {
    Button button = new Button(en.getText().trim(), en.getAttribute("icon"));

    final Listeners buttonActions = new Listeners();

    if (en.getAttribute("confirm") != null) {
      button.getClick().onEvent(new Runnable() {
        @Override
        public void run() {
          ConfirmDialog dialog = new ConfirmDialog(en.getAttribute("confirm"));
          dialog.getConfirm().onEvent(new Runnable() {
            @Override
            public void run() {
              buttonActions.trigger();
            }
          });

          getAdminRoot().getRoot().setDialog(dialog);
        }

      });

    } else {
      button.getClick().onEvent(new Runnable() {
        @Override
        public void run() {
          buttonActions.trigger();
        }

      });
    }

    for (final ElementNode setView : en.getElements("setView")) {
      buttonActions.onEvent(new Runnable() {
        @Override
        public void run() {
          String view = setView.getAttribute("view");
          Node newNode = new Builder().build(getViews().getWidget(view).firstElement(), getModel(setView, node));
          getAdminRoot().getRoot().set(setView.getAttribute("area"), newNode);
        }
      });
    }

    for (ElementNode a : en.getElements("form")) {
      buttonActions.onEvent(new ButtonFormAction(forms.peek(), a.getAttribute("command"), a.getAttribute("message")));
    }

    for (ElementNode a : en.getElements("model")) {
      buttonActions.onEvent(new ButtonModelAction(getModel(en, node), a.getAttribute("command"), a
          .getAttribute("message")));
    }

    return button;
  }

  public Node list(ElementNode en, HNode node) {
    NodeList list = new NodeList();
    for (ElementNode uin : en.getElements()) {
      list.add(build(uin, node));
    }
    return list;
  }

  public Node each(final ElementNode en, HNode node) {
    final NodeList list = new NodeList();

    final HList<HObject> modelList = (HList<HObject>) node;

    for (HObject rowNode : modelList) {
      for (ElementNode se : en.getElements()) {
        list.add(build(se, rowNode));
      }
    }

    modelList.onChange(new Runnable() {
      @Override
      public void run() {
        list.clear();
        for (HObject rowNode : modelList) {
          for (ElementNode se : en.getElements()) {
            list.add(build(se, rowNode));
          }
        }
      }
    }).keepFor(list);

    return list;
  }

  public Node dataTable(final ElementNode en, HNode node) {
    final Table table = new Table();

    for (ElementNode column : en.firstElement("columns").getElements()) {
      table.getColumns().add(build(column, getModel(en.firstElement("columns"), node)));
    }

    final HList<HObject> modelList = (HList<HObject>) getModel(en.firstElement("rows"), node);

    for (HObject rowNode : modelList) {
      Row row = table.appendRow();
      for (ElementNode column : en.firstElement("rows").getElements()) {
        row.add(build(column, rowNode));
      }
    }

    modelList.onChange(new Runnable() {
      @Override
      public void run() {
        table.clear();
        for (HObject rowNode : modelList) {
          Row row = table.appendRow();
          for (ElementNode column : en.firstElement("rows").getElements()) {
            row.add(build(column, rowNode));
          }
        }
      }

    }).keepFor(table);

    return table;
  }

  public Node grid(ElementNode en, HNode node) {
    NodeList nl = new NodeList();
    nl.setStyle("grid");

    Grid grid = new Grid();
    nl.add(grid);
    int total = 0;
    for (ElementNode uin : en.getElements()) {
      int width = 1;
      if (uin.getAttribute("span") != null) {
        width = Integer.parseInt(uin.getAttribute("span"));
      }

      if (total + width > 12) {
        grid = new Grid();
        nl.add(grid);
        total = 0;
      }

      total += width;

      grid.add(build(uin, node));
    }

    return nl;
  }

  public Node ruler(ElementNode en, HNode node) {
    return new Ruler();
  }

  public Node form(ElementNode en, HNode node) {
    Form form = new Form(node);
    forms.push(form);

    for (ElementNode uin : en.getElements()) {
      form.add(build(uin, node));
    }

    forms.pop();
    return form;
  }

  public Node textInput(ElementNode en, HNode node) {
    TextInput ti = new TextInput(en.getAttribute("field"));

    forms.peek().register(ti);
    return ti;
  }

  public Node slugInput(ElementNode en, HNode node) {
    SlugInput si = new SlugInput(en.getAttribute("field"));

    forms.peek().register(si);
    return si;
  }

  public Node fileInput(final ElementNode en, HNode node) {
    final String field = en.getAttribute("field");
    final FileInput si = new FileInput(field);
    final Form form = forms.peek();

    form.getChange().on(en.getAttribute("field"), new Runnable() {
      @Override
      public void run() {
        try {
          si.setData((ByteBuffer) form.get(en.getAttribute("field")));
        } catch (NullPointerException e) {
          // there is no image
        }
      }
    });

    forms.peek().register(si);
    return si;
  }

  public Node dateInput(ElementNode en, HNode node) {
    DateInput di = new DateInput(en.getAttribute("field"));

    forms.peek().register(di);
    return di;
  }

  public Node textArea(ElementNode en, HNode node) {
    TextArea ta = new TextArea(en.getAttribute("field"));

    try {
      ta.setLines(Integer.parseInt(en.getAttribute("lines")));
    } catch (NumberFormatException e) {
      ta.setLines(5);
    }

    forms.peek().register(ta);
    return ta;
  }

  private HNode getModel(ElementNode element, HNode node) {
    if (element.getAttribute("model") != null) {
      if (node == null) {
        throw new RuntimeException("null model found");
      }
      HNode nn = (HNode) ((HObject) node).get(element.getAttribute("model"));
      if (nn == null) {
        throw new RuntimeException("found null model in " + node + " " + element.getAttribute("model"));
      }
      return nn;
    }
    return node;
  }
}
