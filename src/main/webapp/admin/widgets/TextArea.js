d2.views.register('TextArea', function() {
  this.init = function() {
    this.$ = $('<div class="text-area"><div class="sizer"></div><textarea class="form-control"></textarea></div>');
    this.$input = this.$.children("textarea");
    this.$sizer = this.$.children('.sizer');
    
    this.$input.on('change', _.bind(this.change, this));
    this.$input.on('keyup', _.bind(this.kp, this));
    this.kp();
  };
  
  this.kp = function() {
    this.$sizer.text(this.$input.val() + "."); // add the dot so trailing empty lines are rendered
    var lines = this.$sizer.height() / 22;
    this.$input.css('height', (lines + 1) * 22);
  }

  this.change = function(e, data) {
    this.send('value', this.$input.val());
  }

  this.setLines = function(lines) {
    this.$sizer.css('min-height', lines * 22);
    this.kp();
  }
  
  this.setValue = function(value) {
    this.$input.val(value);
    this.kp();
  }
});
