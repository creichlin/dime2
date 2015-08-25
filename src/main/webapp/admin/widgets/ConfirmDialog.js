d2.views.register('ConfirmDialog', function() {
  this.init = function() {
    this.$ = $('<div class="confirm-dialog"><div class="window"><h1></h1><div class="content"></div></div></div>');
    this.$window = this.$.children(".window");
    this.$content = this.$window.children(".content");
  };
  
  this.setTitle = function(value) {
    this.$window.children("h1").text(value);
  };
  
  this.setElement = function(index, child) {
    var $sn = this.$.find("#e" + this.id + "-" + index);
    if($sn.length == 0) {
      $sn = $('<div class="element" id="e' + this.id + "-" + index + '"></div>');
      this.$content.append($sn);
    }
    $sn.empty();
    $sn.append(child.$);
  };
  
  this.setWidth = function(value) {
    this.$window.css({width: value, marginLeft: -value / 2});
  }

  this.setHeight = function(value) {
    this.$window.css({height: value});
  }
});
