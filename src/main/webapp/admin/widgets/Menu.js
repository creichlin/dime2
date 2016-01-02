d2.views.register('Menu', function() {
  this.init = function() {
    
    
    this.$ = $('<nav class="navbar-default navbar-fixed-top"><div class="container-fluid">' +
        '<div class="navbar-header">' +
          '<button class="navbar-toggle collapsed" aria-controls="navbar" aria-expanded="false" data-target="#navbar" data-toggle="collapse" type="button">' +
            '<span class="sr-only">Toggle navigation</span>' +
            '<span class="icon-bar"></span>' +
            '<span class="icon-bar"></span>' +
            '<span class="icon-bar"></span>' +
          '</button>' +
          '<a class="navbar-brand" href="#">D2</a>' +
        '</div>' +
        
        
        '<div id="navbar" class="navbar-collapse collapse" aria-expanded="false" style="height: 1px;">' +
          '<ul class="nav navbar-nav">' +
          '</ul>' +
        '</div>' +
        
    '</div></div>');
    
    this.$list = this.$.find("#navbar > ul");
  };
  
  this.setElement = function(index, child) {
    var $element = $("<li>");
    $element.append(child.$);
    this.$list.append($element);
  }

});
