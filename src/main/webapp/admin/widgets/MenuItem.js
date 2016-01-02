d2.views.register('MenuItem', function() {
  this.init = function() {
    this.$ = $('<div class="btn-group navbar-btn menu-main-button" role="group">' +
        '<button type="button" class="btn btn-default main">Action</button>' +
      '</div>');    
    
    this.$.children('button.main').on('click', _.bind(this.click, this));
    
  };
  
  this.click = function() {
    this.send('click');
  }
  
  this.setElement = function(index, child) {
    if(!this.$elements) {
      // if there is no dropdown menu already, add it
      var $dropdown = $('<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">' +
                        '<span class="caret"> </span>' +
                        '<span class="sr-only">Toggle Dropdown</span>' +
                        '</button>');
      
      this.$elements = $('<ul class="dropdown-menu"></ul>');
      this.$.append($dropdown);
      this.$.append(this.$elements);
    }
    
    var $sn = this.$.find("#e" + this.id + "-" + index);
    if($sn.length == 0) {
      $sn = $('<li id="e' + this.id + "-" + index + '"></li>');
      this.$elements.append($sn);
    }
    $sn.empty();
    $sn.append(child.$);
  }
  
  this.setText = function(text) {
    this.$.children('button.main').text(text);
  }
});


d2.views.register('MenuSubItem', function() {
  this.init = function() {
    this.$ = $('<a  href="#"></a>');    
    
    this.$.on('click', _.bind(this.click, this));
    
  };
  
  this.click = function() {
    this.send('click');
  }
  
  this.setText = function(text) {
    this.$.text(text);
  }
});