package ch.kerbtier.dime2.admin.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import ch.kerbtier.amarillo.Route;
import ch.kerbtier.amarillo.Verb;
import ch.kerbtier.dime2.Response;
import ch.kerbtier.dime2.admin.AdminRoot;
import ch.kerbtier.dime2.admin.model.Node;
import ch.kerbtier.dime2.admin.queue.UserEvent;
import ch.kerbtier.dime2.admin.queue.UserEventQueue;
import ch.kerbtier.dime2.auth.User;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.webb.di.InjectRequest;
import ch.kerbtier.webb.di.InjectSession;

@Inject
public class Events {
  
  @InjectSession
  private AdminRoot adminRoot;
  
  @InjectRequest
  private HttpServletRequest httpRequest;
  
  @InjectRequest
  private Response response;
  
  @Route(pattern = "admin/evs", verb = Verb.POST)
  public void events() {
    User user = adminRoot.getUser();
    if(user == null) {
      throw new RuntimeException("no user authenticated");
    }
    
    
    UserEventQueue ueq = adminRoot.getRoot().getQueue();

    try {
      BufferedReader br = httpRequest.getReader();

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
    response.setJson(events);
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
