d2.views.register('Label', function() {
  this.init = function() {
    this.$ = $('<span class="simple_label"></span>');
  };

  this.setText = function(text) {
    this.$.text(text);
  };
});
