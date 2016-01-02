d2.views.register('ConfirmDialog', function() {
  this.init = function() {
    this.$ = $('<div class="confirm-dialog"><div class="window"><h1></h1><div class="content"></div></div></div>');
    
    this.$ = $('<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
          '<div class="modal-header">' +
            '<h4 class="modal-title" id="myModalLabel"></h4>' +
          '</div>' +
          '<div class="modal-body">' +
          '</div>' +
          '<div class="modal-footer">' +
          '</div>' +
        '</div>' +
      '</div>' +
    '</div>');
    
    $('body').append(this.$);
    this.$.modal();
  };
  
  this.setTitle = function(value) {
    this.$.find(".modal-header > h4").text(value);
  };
  
  this.setElement = function(index, child) {
    var $sn = this.$.find("#e" + this.id + "-" + index);
    if($sn.length == 0) {
      $sn = $('<div class="element" id="e' + this.id + "-" + index + '"></div>');
      this.$.find('.modal-body').append($sn);
    }
    $sn.empty();
    $sn.append(child.$);
  };
  
  this.setFooter = function(child) {
    this.$.find('.modal-footer').empty();
    this.$.find('.modal-footer').append(child.$);
  }
  
});
