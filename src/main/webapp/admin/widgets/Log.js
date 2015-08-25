d2.views.register('Log', function() {
  this.init = function(parent) {
    this.$ = $('<div class="log"></div>');
  };
  
  this.setElement = function(index, child) {
    var id = "e" + this.id + '-' + index;
    
    var $sn = this.$.find("#" + id);
    if($sn.length == 0) {
      $sn = $('<div class="element" id="' + id + '"></div>');
      this.$.append($sn);
    }
    $sn.empty();
    $sn.append(child.$);
    
    _.defer(function() {
      document.location.href = "#" + id;
    });
  }
  
  this.setLevel = function(level) {
    this.$.addClass(level);
  }
});
