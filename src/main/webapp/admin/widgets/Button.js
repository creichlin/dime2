d2.views.register('Button', function() {
  this.init = function() {
    this.$ = $('<div class="button btn btn-default"></div>');
    this.$.on('click', _.bind(this.click, this));
  };
  
  this.click = function() {
    this.send('click');
  }
  
  this.setText = function(text) {
    this.$.text(text);
  }
  
  this.setIcon = function(icon) {
    
    if(icon == 'delete') {
      icon = 'trash';
    }
    
    if(icon == 'up' || icon == 'down') {
      icon = 'arrow-' + icon;
    }
    
    
    
    this.$.addClass('btn-xs glyphicon glyphicon-' + icon);
  }
});
