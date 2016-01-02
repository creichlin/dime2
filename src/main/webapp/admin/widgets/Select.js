d2.views.register('Select', function() {
  this.init = function() {
    this.$ = $('<div class="select"><select class="form-control"/></div>');
    this.$value = '';
    this.$input = this.$.children("select");
    this.$input.on('change', _.bind(this.change, this));
  };

  this.change = function(e, data) {
    this.value = this.$input.val();
    this.send('value', this.value);
  }
  
  this.setValue = function(value) {
    this.value = value;
    this.update();
  }
  
  this.update = function() {
    this.$input.val(this.value);
  }
  
  this.setElement = function(index, child) {
    child.select = this;
    this.$input.append(child.$);
  }

});

d2.views.register('SelectOption', function() {
  
  this.init = function() {
    this.$ = $('<option id=#"' + this.id + '"></option>');
  }
  
  this.setLabel = function(label) {
    this.$.text(label);
  }
  
  this.setValue = function(value) {
    this.$.attr('value', value);
    this.select.update();
  }
  
});
