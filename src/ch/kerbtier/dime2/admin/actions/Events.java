package ch.kerbtier.dime2.admin.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import ch.kerbtier.dime2.admin.model.Node;
import ch.kerbtier.dime2.admin.queue.UserEvent;
import ch.kerbtier.dime2.admin.queue.UserEventQueue;
import ch.kerbtier.dime2.auth.User;
import ch.kerbtier.webb.dispatcher.Action;
import ch.kerbtier.webb.util.HTTPMethod;
import static ch.kerbtier.dime2.ContainerFacade.*;

public class Events {
  @Action(pattern = "admin/evs", method = HTTPMethod.POST)
  public void events() {
    User user = getAdminRoot().getUser();
    if(user == null) {
      throw new RuntimeException("no user authenticated");
    }
    
    
    UserEventQueue ueq = getAdminRoot().getRoot().getQueue();

    try {
      BufferedReader br = getHttpRequest().getReader();

      Type listType = new TypeToken<ArrayList<Event>>() {
      }.getType();
      List<Event> events = new Gson().fromJson(br, listType);

      for (Event e : events) {
        Node subject = ueq.get(e.getId());
        if(e.getVal() == null) {
          subject.triggerAction(e.getProp());
        } else {
          subject.triggerSet(e.getProp(), e.getVal());
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    List<UserEvent> events = new ArrayList<>();

    try {
      while (true) {
        events.add(ueq.next());
      }

    } catch (EmptyStackException e) {
      // none
    }
    // System.out.println("events... " + events);
    getResponse().setJson(events);
  }

  class Event {
    private long id;
    private String prop;
    private Object val;

    public long getId() {
      return id;
    }

    public void setId(long id) {
      this.id = id;
    }

    public String getProp() {
      return prop;
    }

    public void setProp(String prop) {
      this.prop = prop;
    }

    public Object getVal() {
      return val;
    }

    public void setVal(Object val) {
      this.val = val;
    }
    
    @Override
    public String toString() {
      return id + ":" + prop + ":" + val;
    }
  }
}
