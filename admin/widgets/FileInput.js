d2.views.register('FileInput', function() {
  this.init = function() {
    this.$ = $('<div class="file-input"><input type="file"/><img src="" height="88"><div class="selectfile"><br>click to select file</div></div>');
    this.$input = this.$.children("input");
    this.$input.hide();
    this.$img = this.$.children("img");
    this.$input.on('change', _.bind(this.change, this));
    this.$.children(".selectfile").click(_.bind(this.triggerDialogue, this));
  };
  
  this.triggerDialogue = function() {
    this.$input.trigger('click');
  }

  this.change = function(e, data) {
    var send = _.bind(this.send, this);
    if(this.$input[0].files.length > 0) {
      var reader = new FileReader();
      reader.readAsDataURL(this.$input[0].files[0]);
      
      reader.onloadend = function () {
        send('value', reader.result);
      }
    }
  }
  
  this.setUrl = function(value) {
    this.$img.attr("src", value + "/$1.fit(130x88)/" + Math.random());
  }
});
