d2.views.register('Form', function() {
  this.init = function(parent) {
    this.$ = $('<div class="form"></div>');
  };
  
  this.setElement = function(index, child) {
    var $sn = this.$.find("#e" + this.id + "-" + index);
    if($sn.length == 0) {
      $sn = $('<div class="element" id="e' + this.id + "-" + index + '"></div>');
      this.$.append($sn);
    }
    $sn.empty();
    $sn.append(child.$);
  }
});
