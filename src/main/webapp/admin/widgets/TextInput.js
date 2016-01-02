d2.views.register('TextInput', function() {
  this.init = function() {
    this.$ = $('<div class="text-input"><input type="text" class="form-control"/></div>');
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
