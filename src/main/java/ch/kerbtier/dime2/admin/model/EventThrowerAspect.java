package ch.kerbtier.dime2.admin.model;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class EventThrowerAspect {

  @Before("set(@ch.kerbtier.dime2.admin.model.ADHS * ch.kerbtier.dime2.admin.model.Node+.*) && args(value) && target(parent)")
  public void throwSet(JoinPoint joinPoint, Node parent, Object value) {
    if (parent.getEventQueue() != null) {
      String field = joinPoint.getSignature().getName();

      parent.getEventQueue().set(parent, field, value);
      if (value != null && value instanceof Node) {
        Node childNode = ((Node) value);
        childNode.setEventQueue(parent.getEventQueue());
      }
    }
  }
}
