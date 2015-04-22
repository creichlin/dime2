d2.views.register('SlugInput', function() {
  this.init = function() {
    this.$ = $('<div class="slug-input"><input type="text"/></div>');
    this.$input = this.$.children("input");
    
    
    this.$input.on('change', _.bind(this.change, this));
  };

  this.change = function(e, data) {
    this.send('value', this.$input.val());
  }
  
  this.setValue = function(value) {
    this.$input.val(value);
  }
});
