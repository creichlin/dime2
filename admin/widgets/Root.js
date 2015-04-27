
d2.views.register('Root', function() {
  this.init = function() {
    this.$ = $('<div class="root"></div>'); 
    this.$menu = $('<div class="menu-area box-element"></div>');
    this.$list = $('<div class="list box-element"></div>');
    this.$workspace = $('<div class="workspace box-element"></div>');
    this.$log = $('<div class="log box-element"></div>');
    this.$dialog = $('<div class="modal-dialog"></div>');
    
    var $ct = $('<div class="ct"></div>');
    var $ctr = $('<div class="ctr"></div>');
    $ct.append(this.$list);
    $ct.append($ctr);
    $ctr.append(this.$workspace);
    $ctr.append(this.$log);
    $("body").append(this.$);
    this.$.append(this.$menu);
    this.$.append($ct);
    this.$.append(this.$dialog);
    
    this.resize();
    
    $(window).resize(_.bind(this.resize, this));
  };
  
  this.resize = function() {
    var height = Math.floor($(window).height() / 22 - 0.5);
    if(height < 15) {
      height = 15;
    }
    this.$.css('height', 22 * height);
  }
  
  this.setWorkspace = function(child) {
    this.$workspace.empty();
    this.$workspace.append(child.$);
  }
  
  this.setList = function(child) {
    this.$list.empty();
    this.$list.append(child.$);
  }
  
  this.setLog = function(child) {
    this.$log.empty();
    this.$log.append(child.$);
  }
  
  this.setMenu = function(menu) {
    this.$menu.empty();
    this.$menu.append(menu.$);
  }

  this.setDialog = function(dialog) {
    this.$dialog.empty();
    if(dialog) {
      this.$dialog.append(dialog.$);
    }
  }
});

