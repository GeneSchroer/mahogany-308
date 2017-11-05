function changeColorOfFeatureTo(){}
function setToSelectedSuperdistrict(){}
function setToUnselectedSuperdistrict(){}

define([], function(){
	
	
	return {
		redistrictDistrictLayerStyle: function(mapData){
			return function(feature){
				//console.log(mapData);
				return{
					weight: 0.5,
					color: "white",
					fillColor: "#464646",
					dataArray: ' ',
					fillOpacity: 0.8
				};
			};
		},
		redistrictDistrictLayerHighlightStyle: function(mapData){
				//console.log(mapData);
				return{
					weight: 0.5,
					color: "white",
					fillColor: "#949394",
					dataArray: ' ',
					fillOpacity: 0.8
				};
			
		},
		redistrictCreateSuperdistrict: function(mapData){
			return{
				weight: 0.5,
				color: "white",
				fillColor: "#b4a1a1",
				dataArray: ' ',
				fillOpacity: 0.8
			};
		}
		
		
	};
	
});
