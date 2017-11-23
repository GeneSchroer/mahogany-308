define([
	"leaflet/gerrymander/utils/ColorMode"
], function(ColorMode){
	

	
	function setEvents(mapData){
		return function (feature, layer){
			layer.on({
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						weight: 0.5,
						fillColor: setColor(layer.feature, mapData, ColorMode.HIGHLIGHT_COLOR),
						dataArray:' ',
						fillOpacity: 1
					});
					
				},
				mouseout:function(e){
					mapData.districtLayer.resetStyle(e.target);
				},
				click: function(e){
					electionLayerPopup(mapData, e);
				}
			});
		};
	}
	
	function electionLayerPopup(mapData, layer){
		var districtNumber = layer.target.feature.properties.districtNumber;
		var winningParty = layer.target.feature.properties.winningParty;
		if(districtNumber < 10){
			districtNumber = "0" + districtNumber;
		}
		var electionData = "District: " + districtNumber + "<br/>"
							+ "Party: " + winningParty + "<br/>";
							
		var popup = L.popup()
		.setLatLng(layer.latlng)
		.setContent(electionData)
		.openOn(mapData.map);
	}
	
	function setStyle(mapData){
		return function(feature){
			//console.log(mapData);
			return{
				weight: 0.5,
				color: "yellow",
				fillColor: setColor(feature, mapData, ColorMode.DEFAULT_COLOR),
				
				dataArray: ' ',
				fillOpacity: 0.8
			};
		};
	}
	
	function setColor(feature, mapData, colorMode){
		
		var winningParty = feature.properties.winningParty;
		
		if(winningParty){
			if(winningParty == "Democrat"){
				return colorMode == ColorMode.DEFAULT_COLOR ? "blue" : "cyan";
			}
			else if(winningParty == "Republican"){
				return colorMode == ColorMode.DEFAULT_COLOR ? "red": "ruby";
			}
			else{
				return colorMode == ColorMode.DEFAULT_COLOR ? "green" : "lime";
			}
		}
		else{
			return colorMode == ColorMode.DEFAULT_COLOR ? "gray" : "lightgray";
		}
	}
	
	
	return{
		setEvents: function(mapData){
			return setEvents(mapData);
		},
		
		setStyle: function(mapData){
			return setStyle(mapData);
		}
	};
	
});