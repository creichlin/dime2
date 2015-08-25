d2.views.register('MenuItem', function() {
  this.init = function() {
    this.$ = $('<div class="menu-item"><div class="label">MenuItem</div></div>');
    
    
    this.$.children('.label').on('click', _.bind(this.click, this));
    
  };
  
  this.click = function() {
    this.send('click');
  }
  
  this.setElement = function(index, child) {
    if(!this.$elements) {
      this.$elements = $('<div class="elements"></div>');
      this.$.append(this.$elements);
    }
    var $sn = this.$.find("#e" + this.id + "-" + index);
    if($sn.length == 0) {
      $sn = $('<div class="element" id="e' + this.id + "-" + index + '"></div>');
      this.$elements.append($sn);
    }
    $sn.empty();
    $sn.append(child.$);
  }
  
  this.setText = function(text) {
    this.$.children('.label').text(text);
  }
});
