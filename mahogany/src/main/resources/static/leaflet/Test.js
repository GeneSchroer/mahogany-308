define(["dojo/_base/declare", "dojo/dom"], function(declare, dom){

	
	
	var LeafletClass = L.Class.extend({
		number: null,
		_stateMap: null,
		
		initialize: function(num){
			//this._stateMap = L.map(map).setView([32, -88], 4);
			this.number = num;
		},
		
		testConsole: function(){
			console.log("Test from module " + this.number + " is successful");
		}
	
		
		
	});
	
	return declare(null, {
		map: null,
		constructor: function(num){
			this.map =  new LeafletClass(num);
			this.map.testConsole();
		}
	});
});