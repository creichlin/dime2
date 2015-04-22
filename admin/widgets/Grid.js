d2.views.register('Grid', function() {
  this.init = function() {
    this.$ = $('<div class="grid section group"></div>');
  };
  
  this.setElement = function(index, child) {
    var $tr = this.$.find('#e' +  this.id + '-' + index);
    
    if($tr.length > 0) {
      $tr.replaceWith(child.$);
    } else {
      this.$.append(child.$);
    }
    
    child.$.attr('id', 'e' + this.id + '-' + index);
  };
});
