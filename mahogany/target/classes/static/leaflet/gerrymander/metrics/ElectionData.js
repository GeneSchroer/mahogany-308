define([], function(){
	function setEvents(mapData){
		return function (feature, layer){
			layer.on({
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						weight: 0.5,
						fillColor: setHighlightColor(layer.feature, mapData),
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
				fillColor: setColor(feature, mapData),
				
				dataArray: ' ',
				fillOpacity: 0.8
			};
		};
	}
	
	function setColor(feature, mapData){
		
		var winningParty = feature.properties.winningParty;
		
		if(winningParty){
			if(winningParty == "Democrat"){
				return "blue";
			}
			else if(winningParty == "Republican"){
				return "red";
			}
			else{
				return "green";
			}
		}
		else{
			return "gray";
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