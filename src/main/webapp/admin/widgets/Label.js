d2.views.register('Label', function() {
  this.init = function() {
    this.$ = $('<span class="label"></span>');
  };

  this.setText = function(text) {
    this.$.text(text);
  };
});
