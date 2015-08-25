d2.views.register('Button', function() {
  this.init = function() {
    this.$ = $('<div class="button"></div>');
    this.$.on('click', _.bind(this.click, this));
  };
  
  this.click = function() {
    this.send('click');
  }
  
  this.setText = function(text) {
    this.$.text(text);
  }
  
  this.setIcon = function(icon) {
    this.$icon = $('<span class="icon ' + icon + '"></span>');
    this.$.append(this.$icon);
  }
});
