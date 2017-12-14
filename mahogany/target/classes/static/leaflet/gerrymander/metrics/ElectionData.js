define([
	"leaflet/gerrymander/utils/ColorMode",
	"leaflet/gerrymander/constants/MapColors"
	], function(ColorMode, MapColors){
	
	var mapData;
	var districtLayer;
	var displayControl;
	
	function setStyle(mapData){
		return function(feature){
			//console.log(mapData);
			return{
				weight: 0.5,
				color: "yellow",
				fillColor: setColor(feature, ColorMode.DEFAULT_COLOR),
				
				dataArray: ' ',
				fillOpacity: 0.6
			};
		};
	}
	
	function setEvents(mapData, districtLayer, displayControl){
		return function (layer){
			layer.off();
			layer.on({
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						weight: 0.5,
						fillColor: setColor(layer.feature, ColorMode.HIGHLIGHT_COLOR),
						dataArray:' '
					});
					displayControl.update(setDisplay(layer, mapData));
				},
				mouseout:function(e){
					districtLayer.resetStyle(e.target);
					displayControl.update(" ");
				},
				click: function(e){
					electionLayerPopup(mapData, e);
				}
			});
		};
	}
	
	function setDisplay(layer, mapData){
		if(!mapData){
			return "Data Not Available";
		}
		
		var displayString = "";
		
		var districtNumber = layer.feature.properties.districtNumber;
		if(districtNumber < 10){
			districtNumber = "0" + districtNumber;
		}
		displayString += "District: " + districtNumber + "<br/>";
		
		displayString += "<b>Votes:</b><br/>";
		
		var districtId = layer.feature.properties.id;
		var districtData = mapData.districtData[districtId];
		if(!districtData){
			return "Data Not Available";
		}
		var voteData=districtData.voteData;
		
		
		for(party in voteData){
			var data = voteData[party];
			displayString += party + ": " + data.votes.toLocaleString() + "<br/>";
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
	
	
	
	function setColor(feature, colorMode){
		
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
		mapData: null,
		districtLayer: null,
		displayControl: null,
		setEvents: function(mapData,districtLayer, dataControl){
			return setEvents(mapData, districtLayer, dataControl);
		},
		setStyle: function(){
			return setStyle();
		},
		setMapData: function(data){
			this.mapData = data;
			return this;
		},
		setDistrictLayer: function(layer){
			this.districtLayer = layer;
			return this;
		},
		setDisplayControl: function(control){
			this.displayControl = control;
			return this;
		},
		getEvents(){
			var events = setEvents(this.mapData, this.districtLayer, this.displayControl);
			return events;
		}
		
		
	};
	
});