d2.views.register('Markdown', function() {
  this.init = function() {
    this.$ = $('<div class="markdown"></div>');
  };

  this.setText = function(text) {
    this.$.html(text);
  };
});
