/*
 * jQuery treeTable Plugin VERSION
 * http://ludo.cubicphuse.nl/jquery-plugins/treeTable/doc/
 *
 * Copyright 2011, Ludo van den Boom
 * Dual licensed under the MIT or GPL Version 2 licenses.
 */
(function($) {
  // Helps to make options available to all functions
  // TODO: This gives problems when there are both expandable and non-expandable
  // trees on a page. The options shouldn't be global to all these instances!
  var options;
  var defaultPaddingLeft;

  $.fn.treeTable = function(opts) {
    options = $.extend({}, $.fn.treeTable.defaults, opts);

    return this.each(function() {
      $(this).addClass("treeTable").find("tbody tr").each(function() {
        // Skip initialized nodes.
        if (!$(this).hasClass('initialized')) {
          var isRootNode = ($(this)[0].className.search(options.childPrefix) == -1);

          // To optimize performance of indentation, I retrieve the padding-left
          // value of the first root node. This way I only have to call +css+
          // once.
          if (isRootNode && isNaN(defaultPaddingLeft)) {
            defaultPaddingLeft = parseInt($($(this).children("td")[options.treeColumn]).css('padding-left'), 10);
          }

          // Set child nodes to initial state if we're in expandable mode.
          if(!isRootNode && options.expandable && options.initialState == "collapsed") {
            $(this).addClass('ui-helper-hidden');
          }

          // If we're not in expandable mode, initialize all nodes.
          // If we're in expandable mode, only initialize root nodes.
          if(!options.expandable || isRootNode) {
            initialize($(this));
          }
        }
      });
      var trs = $(this).find("tbody tr").filter(":visible");
      var tr = trs[trs.length-1];
      $(tr).find(".expander").removeClass("expander").addClass("expanderlast");
      $("#"+tr.id+" + tr").find(".nodeIconBlankI").removeClass("nodeIconBlankI").addClass("nodeIconBlank");
    });
   
  };

  $.fn.treeTable.defaults = {
    childPrefix: "child-of-",
    clickableNodeNames: false,
    expandable: true,
    indent: 19,
    initialState: "collapsed",
    onNodeShow: null,
    onNodeHide: null,
    treeColumn: 0,
    persist: false,
    persistCookiePrefix: 'treeTable_',
    persistCookieOptions: {},
    stringExpand: "Expand",
    stringCollapse: "Collapse"
  };

  // Recursively hide all node's children in a tree
  $.fn.collapse = function() {
    $(this).addClass("collapsed");

    childrenOf($(this)).each(function() {
      if(!$(this).hasClass("collapsed")) {
        $(this).collapse();
      }

      $(this).addClass('ui-helper-hidden');

      if($.isFunction(options.onNodeHide)) {
        options.onNodeHide.call(this);
      }

    });

    return this;
  };

  // Recursively show all node's children in a tree
  $.fn.expand = function() {
    $(this).removeClass("collapsed").addClass("expanded");

    childrenOf($(this)).each(function() {
      initialize($(this));
      if($(this).is(".expanded.parent")) {
        $(this).expand();
      }
      $(this).removeClass('ui-helper-hidden');
      if($.isFunction(options.onNodeShow)) {
        options.onNodeShow.call(this);
      }
    });

    return this;
  };

  // Reveal a node by expanding all ancestors
  $.fn.reveal = function() {
    $(ancestorsOf($(this)).reverse()).each(function() {
      initialize($(this));
      $(this).expand().show();
    });

    return this;
  };

  // Add an entire branch to +destination+
  $.fn.appendBranchTo = function(destination) {
    var node = $(this);
    var parent = parentOf(node);

    var ancestorNames = $.map(ancestorsOf($(destination)), function(a) { return a.id; });

    // Conditions:
    // 1: +node+ should not be inserted in a location in a branch if this would
    //    result in +node+ being an ancestor of itself.
    // 2: +node+ should not have a parent OR the destination should not be the
    //    same as +node+'s current parent (this last condition prevents +node+
    //    from being moved to the same location where it already is).
    // 3: +node+ should not be inserted as a child of +node+ itself.
    if($.inArray(node[0].id, ancestorNames) == -1 && (!parent || (destination.id != parent[0].id)) && destination.id != node[0].id) {
      indent(node, ancestorsOf(node).length * options.indent * -1); // Remove indentation

      if(parent) { node.removeClass(options.childPrefix + parent[0].id); }

      node.addClass(options.childPrefix + destination.id);
      move(node, destination); // Recursively move nodes to new location
      indent(node, ancestorsOf(node).length * options.indent);
    }

    return this;
  };

  // Add reverse() function from JS Arrays
  $.fn.reverse = function() {
    return this.pushStack(this.get().reverse(), arguments);
  };

  // Toggle an entire branch
  $.fn.toggleBranch = function() {
    if($(this).hasClass("collapsed")) {
      $(this).expand();
    } else {
      $(this).removeClass("expanded").collapse();
    }

    if (options.persist) {
      // Store cookie if this node is expanded, otherwise delete cookie.
      var cookieName = options.persistCookiePrefix + $(this).attr('id');
      $.cookie(cookieName, $(this).hasClass('expanded') ? 'true' : null, options.persistCookieOptions);
    }

    return this;
  };

  // === Private functions

  function ancestorsOf(node) {
    var ancestors = [];
    while(node = parentOf(node)) {
      ancestors[ancestors.length] = node[0];
    }
    return ancestors;
  };

  function childrenOf(node) {
    return $(node).siblings("tr." + options.childPrefix + node[0].id);
  };

  function getPaddingLeft(node) {
    var paddingLeft = parseInt(node[0].style.paddingLeft, 10);
    return (isNaN(paddingLeft)) ? defaultPaddingLeft : paddingLeft;
  }

  function indent(node, value) {
    var cell = $(node.children("td")[options.treeColumn]);
    cell[0].style.paddingLeft = getPaddingLeft(cell) + value + "px";

    childrenOf(node).each(function() {
      indent($(this), value);
    });
  };

  function initialize(node) {
    if(!node.hasClass("initialized")) {
      node.addClass("initialized");
      var childNodes = childrenOf(node);
      if(!node.hasClass("parent") && childNodes.length > 0) {
        node.addClass("parent");
      }
      
      if(node.hasClass("parent")) {
        var cell = $(node.children("td")[options.treeColumn]);
        var padding = getPaddingLeft(cell) + options.indent;

        cell.prepend();
        if(options.expandable) {
        	var classexpander = "expander";
        	if(node.hasClass("parentnode")){
        		classexpander = "expandertop";
        	}
        	cell.prepend('<SPAN class="'+classexpander+'" vAlign="middle" style="display: block;float:left;background-repeat:repeat-y;width:19px;height:20px"></SPAN>');
          $(cell[0].firstChild).click(function() { node.toggleBranch(); return false; });
          $(cell[0].firstChild).keydown(function(e) { if(e.keyCode == 13) {node.toggleBranch(); return false; }});

          if(options.clickableNodeNames) {
            cell[0].style.cursor = "pointer";
            $(cell).click(function(e) {
              // Don't double-toggle if the click is on the existing expander icon
              if (e.target.className != 'expander') {
                node.toggleBranch();
              }
            });
          }

          if (options.persist) {
            var cookieName = options.persistCookiePrefix + node.attr('id');
            if ($.cookie(cookieName) == 'true') {
              node.addClass('expanded');
            }
          }
          // Check for a class set explicitly by the user, otherwise set the default class
          if(!(node.hasClass("expanded") || node.hasClass("collapsed"))) {
            node.addClass(options.initialState);
          }

          if(node.hasClass("expanded")) {
            node.expand();
          }
        }
        var cell = $(node.children("td")[options.treeColumn]);
        if($(node).attr("level") && $(node).attr("level") >= 3){
			  cell.prepend('<SPAN class="nodeIconBlankI" vAlign="middle" style="background-repeat:repeat-y;display: block;float:left;"></SPAN>');  
		  }
        if(!node.hasClass("parentnode")){
        	cell.children(".nodeIconBlank").remove();
            cell.prepend('<span class="nodeIconBlank" vAlign="middle" style="display: block;float:left;"></span>');
        }
        
        childNodes.each(function(k) {
        	$($(this).children("td")[options.treeColumn]).children(".nodeIconBlank").remove();
            $($(this).children("td")[options.treeColumn]).prepend('<span class="nodeIconBlank" vAlign="middle" style="display: block;float:left;"></span>');
          });
      }else{
    	  var cell = $(node.children("td")[options.treeColumn]);
    	  var blankspan = cell.find(".nodeIconBlank");
    	  blankspan.remove();
    	  var nexttr = $("#"+$(node).attr("id")+" + tr");
    	  if(nexttr[0] && nexttr[0].id){
    		  var level = $(node).attr("level");
    		  var nextlevel = nexttr.attr("level");
    		  if($(nexttr).hasClass("parent") || level > nextlevel){
    			  //console.log("last=="+nexttr[0].id);
    			  cell.prepend('<SPAN class="nodeIconLastChild" vAlign="middle" style="display: block;float:left;"></SPAN>');
    		  }else{
    			  cell.prepend('<SPAN class="nodeIconMoreChild" vAlign="middle" style="background-repeat:repeat-y ;display: block;float:left;"></SPAN>');
    		  }
    		  if($(node).attr("level") && $(node).attr("level") >= 3){
    			  
    			  for(i=0;i<(level-2);i++){
    				  cell.prepend('<SPAN class="nodeIconBlankI" vAlign="middle" style="background-repeat:repeat-y;display: block;float:left;"></SPAN>');  
    			  }
    			    
    		  }
    	  }else{
    		  cell.prepend('<SPAN class="nodeIconLastChild" vAlign="middle" style="display: block;float:left;"></SPAN>');
    		  //cell.prepend('<SPAN class="nodeIconBlank" vAlign="middle" style="background-repeat:repeat-y;display: block;float:left;"></SPAN>');
    	  }
    	  node.children(".nodeIconBlank").remove();
    	  cell.prepend('<span class=nodeIconBlank vAlign="middle" style="display: block;float:left;"></span>');
    	  //cell.find(".nodeIconBlank").remove();
      }
    }
  };

  function move(node, destination) {
    node.insertAfter(destination);
    childrenOf(node).reverse().each(function() { move($(this), node[0]); });
  };

  function parentOf(node) {
    var classNames = node[0].className.split(' ');

    for(var key=0; key<classNames.length; key++) {
      if(classNames[key].match(options.childPrefix)) {
        return $(node).siblings("#" + classNames[key].substring(options.childPrefix.length));
      }
    }

    return null;
  };
})(jQuery);
