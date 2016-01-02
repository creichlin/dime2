d2.views.register('Table', function() {
  this.init = function(parent) {
    this.$ = $('<table class="table table-striped table-condensed"><colgroup></colgroup><thead></thead><tbody></tbody></table>');
    this.$body = this.$.children("tbody");
    this.$cols = this.$.children("colgroup");
  };

  this.setElement = function(index, child) {
    var $tr = this.$body.find('#e' + this.id + '-' + index);

    if (!child) {
      if ($tr.length > 0) {
        $tr.remove();
      }
      return;
    } else {

      if ($tr.length > 0) {
        $tr.replaceWith(child.$);
      } else {
        this.$body.append(child.$);
      }
    }

    child.$.attr('id', 'e' + this.id + '-' + index);
  }

  this.setColumns = function(columns) {
    this.$.children("thead").empty();
    this.$.children("thead").append(columns.$);
  }
});

d2.views.register('Columns', function() {
  this.init = function(parent) {
    this.$ = $('<tr></tr>');
  };

  this.setElement = function(index, child) {
    var $sn = this.$.find("#e" + this.id + "-" + index);
    if ($sn.length == 0) {
      $sn = $('<th id="e' + this.id + "-" + index + '"></th>');
      this.$.append($sn);
    }
    $sn.empty();
    $sn.append(child.$);
  }
});

d2.views.register('Row', function() {
  this.init = function(parent) {
    this.$ = $('<tr></tr>');
  };

  this.setElement = function(index, child) {
    var $sn = this.$.find("#e" + this.id + "-" + index);
    if ($sn.length == 0) {
      $sn = $('<td id="e' + this.id + "-" + index + '"></td>');
      this.$.append($sn);
    }
    $sn.empty();
    $sn.append(child.$);
  }
});
