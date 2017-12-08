		define(["dojo/_base/declare", "dojo/dom", "dojo/dom-style","dojo/_base/fx"],
    		function(declare, dom, domStyle, fx){
			
    			
    			
    			return declare(null, {
    				//loadingScreenNode: null,
    				constructor: function(node){
    				//save a reference to the overlay
    					console.log("This worked");
    					this.loadingScreenNode = dom.byId(node);
    				},
    				
    				startLoading: function(){
    					fx.fadeIn({
    						node: this.loadingScreenNode,
    						onEnd: function(node){
    							domStyle.set(node, 'display', 'block');
    						}
    					}).play();
    				},
    				
    				endLoading: function(){
    				// fade the overlay gracefully
    					fx.fadeOut({
    						node: this.loadingScreenNode,
    						onEnd: function(node){
    							domStyle.set(node, 'display', 'none');
    						}
    					}).play();	
    				}
    			});
			
			/*return declare(null, {
			    constructor: function(name, age, residence){
			      this.name = name;
			      this.age = age;
			      this.residence = residence;
			    }
			  });*/
    		// layout is ready, hide the loading overlay
    		});