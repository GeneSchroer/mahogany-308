define([
	"leaflet/gerrymander/utils/ColorMode",
	"leaflet/gerrymander/constants/MapColors"
	], function(ColorMode, MapColors){
	
	
	function setDataBlock(properties, efficiencyGap){
		
	}
	
	function setEvents(mapData){
		return function (layer){
			layer.off();
			layer.on({
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						weight: 0.5,
						fillColor: setColor(layer.feature, mapData, ColorMode.HIGHLIGHT_COLOR),
						dataArray:' ',
						fillOpacity: 1
					});
					mapData.districtMapControls.dataDisplay.update(setDisplay(layer, mapData));
				},
				mouseout:function(e){
					mapData.districtLayer.resetStyle(e.target);
					mapData.districtMapControls.dataDisplay.update(" ");
				},
				click: function(e){
					electionLayerPopup(mapData, e);
				}
			});
		};
	}
	
	function setDisplay(layer, mapData){
		var displayString = "";
		
		var districtNumber = layer.feature.properties.districtNumber;
		if(districtNumber < 10){
			districtNumber = "0" + districtNumber;
		}
		displayString += "District: " + districtNumber + "<br/>";
		
		displayString += "<h4>Votes:</h4>";
		
		var districtId = layer.feature.properties.id;
		var districtData = mapData.metricData.defaultMode.districtData[districtId];
		var voteData=districtData.voteData;
		
		
		for(party in voteData){
			var data = voteData[party];
			displayString += party + ": " + data.votes + "<br/>";
		}
		
		return displayString;
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
				return colorMode == ColorMode.DEFAULT_COLOR ? MapColors.BLUE_60 : MapColors.BLUE_30;
			}
			else if(winningParty == "Republican"){
				return colorMode == ColorMode.DEFAULT_COLOR ? MapColors.RED_60: MapColors.RED_30;
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
		},
		setEvents2: function(mapData,layer){
			return setEvents2(mapData,layer);
		}
	};
	
});