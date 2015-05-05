d2 = {};

d2.views = {};

d2.views.instances = {};

d2.views.register = function(name, obj) {
  d2.views[name] = obj;
  obj.prototype.type = name;
  obj.prototype.registerView = function(id) {
    this.id = id;
    d2.views.instances[id] = this;
  }
  
  obj.prototype.send = function(prop, value) {
    d2.events.add(this.id, prop, value);
  }
  
  
  obj.prototype.setSpan = function(size) {
    this.$.addClass('col');
    this.$.addClass('span_' + size + "_of_12");
  }

  obj.prototype.setStyle = function(style) {
    this.$.addClass(style);
  }
}


$(function() {
  console.log("init...");
  
  var target = function() {
    this.send = function(e) {
      console.log(e);
      var seter = "set" + e.field.substr(0, 1).toUpperCase() + e.field.substr(1);
      if(e.type) {
        var constructor = d2.views[e.type];
        if(constructor) {
          var instance = new constructor();
        } else {
          console.warn("no view for", e.type);
          return;
        }

        var parent = d2.views.instances[e.id];
        instance.registerView(e.child);
        if(instance.init) {
          instance.init();
        }
        if(parent) {
          if(e.field.substr(0, 1) == '#') {
            if(parent.setElement) {
              parent.setElement(parseInt(e.field.substr(1)), instance);
            } else {
              console.warn(parent.type, 'has no setElement for object');
            }
          } else if(parent[seter]) {
            parent[seter].call(parent, instance);
          } else {
            console.warn(parent.type, 'has no setter for object', e.field);
          }
        }
      } else {
        var parent = d2.views.instances[e.id];
        if(parent) {
          if(e.field.substr(0, 1) == '#') {
            if(parent.setElement) {
              parent.setElement(parseInt(e.field.substr(1)), e.value);
            } else {
              console.warn(parent.type, 'has no setElement for field', e.value);
            }
          } else if(parent[seter]) {
            parent[seter].call(parent, e.value);
          } else {
            console.warn(parent.type, 'has no setter for field', e.field, e.value);
          }
        } else {
          console.warn("no parent for event ", e);
        }
      }
    }
  }
  
  d2.events = new d2.AjaxEvents(new target(), "evs");
});