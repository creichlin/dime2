d2.AjaxEvents = function(target, url) {
  this.events = [];
  
  this.lastsendt = Date.now();
  
  this.idle = 1000;
  
  this.init = function() {
    this.send();
  }
  
  this.ready = function() {
    
    if(this.events.length > 0) {
      // send, there is data waiting on client
      this.idle = 300;
      return true;
    }
    
    var delta = Date.now() - this.lastsendt;
    
    
    
    if(delta > this.idle) {
      // send, but idle, maybe there is something on the server
      // increase idle time for next run
      if(this.idle < 2000) {
        this.idle += 100
      }
      return true;
    }
    return false;
  }
  
  
  this.send = function() {
    var lsend = _.bind(this.send, this);
    
    if(!this.ready()) {
      window.setTimeout(lsend, 100);
      return;
    }
    
    var data = this.events;
    this.events = [];
    
    $.ajax({
      type: 'POST',
      url: url,
      data: JSON.stringify(data),
      contentType: 'application/json',
      dataType: 'json',
      success: function(eq) {
        _.each(eq, function(e) {
          target.send(e);
        });
        window.setTimeout(lsend, 100);
      },
      error: function() {
        console.log("error...");
        //document.location.href = document.location.href;
      }
    });
    
    this.lastsendt = Date.now();
  }
  
  this.add = function(obj, prop, newValue) {
    this.events.push({id: obj, prop: prop, val: newValue});
  }
  
  
  this.init();
}