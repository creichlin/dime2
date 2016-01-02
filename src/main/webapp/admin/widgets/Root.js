
d2.views.register('Root', function() {
  this.init = function() {
    this.$ = $('<div class="container-fluid root"><div class="row"></div></div>');
    
    this.$list = $('<div class="col-sm-5 col-md-4 sidebar"></div>');
    this.$workspace = $('<div class="col-sm-7 col-sm-offset-5 col-md-8 col-md-offset-4 main workspace"></div>');

    this.$dialog = $('<div class="modal-dialog"></div>');

    this.$log = $('<div class="container-fluid root"><div class="row">' +
        '<div class="col-sm-7 col-sm-offset-5 col-md-8 col-md-offset-4 log-container"></div>' +
        '</div></div>');

    this.$.find('> .row').append(this.$list);
    this.$.find('> .row').append(this.$workspace);

    $("body").append(this.$);
    $("body").append(this.$log);
    $("body").append(this.$dialog);
    
  };
  
  this.setWorkspace = function(child) {
    this.$workspace.empty();
    this.$workspace.append(child.$);
  }
  
  this.setList = function(child) {
    this.$list.empty();
    this.$list.append(child.$);
  }
  
  this.setLog = function(child) {
    this.$log.find('.log-container').append(child.$);
  }
  
  this.setMenu = function(menu) {
    $('body').prepend(menu.$);
  }

  this.setDialog = function(dialog) {
    this.$dialog.empty();
    if(dialog) {
      this.$dialog.append(dialog.$);
    }
  }
});

