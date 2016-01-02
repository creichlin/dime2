d2.views.register('ButtonGroup', function() {
  this.init = function() {
    this.$ = $('<div class="btn-group" role="group"></div>');
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
