define([
	"leaflet/gerrymander/utils/ColorMode",
	"leaflet/gerrymander/constants/MapColors"
	], function(ColorMode, MapColors){
	
	function setEvents(mapData, districtLayer, dataControl){
		return function (layer){
			layer.off();
			layer.on({
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						weight: 0.5,
						fillColor: fillColor( mapData,layer.feature.properties.id, ColorMode.HIGHLIGHT_COLOR),
						dataArray:' ',
						fillOpacity: 1
					});
					dataControl.update(setDisplay(layer, mapData));
				},
				mouseout:function(e){
					districtLayer.resetStyle(e.target);
				},
				click: function(e){
					//electionLayerPopup(mapData, e);
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
		var districtData = mapData.districtData[districtId];
		var voteData=districtData.voteData;
		
		
		for(party in voteData){
			
			var data = voteData[party];
			displayString += "<h4>" + party + "</h4>";
			displayString += "Votes: " + data.votes + "<br/>";
			displayString += "Wasted Votes: " + data.wastedVotes + "<br/>";
			var wastedVotes = (data.wastedVotes/data.votes).toFixed(3);
			if(wastedVotes != 1){
				displayString += "Wasted Vote Percentage: " + wastedVotes + "<br/>";
			}
		}		
		return displayString;
	}
	
	function setStyle(mapData){
		return function(feature){
			return{
				fillOpacity: 1,
				weight: 0.5,
				color: "yellow",
				fillColor: fillColor(mapData, feature.properties.id, ColorMode.DEFAULT_COLOR)

			};
		};
	}
	function fillColor(mapData, id, colorMode){
		var districtId = id;
		var metricData = mapData;
		var districtData = metricData.districtData[districtId];
		var wastedVotePercent;
		var voteData;
		if(districtData.winningParty == "Democrat"){
				voteData=districtData.voteData.Democrat;
				wastedVotePercent = voteData.wastedVotes / voteData.votes;
				//console.log(wastedVotePercent);
				return colorMode == ColorMode.HIGHLIGHT_COLOR ? MapColors.ULTRAMARINE_80 :
					wastedVotePercent < 0.10 ? MapColors.BLUE_20 : 
					wastedVotePercent < 0.20 ? MapColors.BLUE_30 : // blue 40
					wastedVotePercent < 0.30 ? MapColors.BLUE_40 :// blue 60
					wastedVotePercent < 0.40 ? MapColors.BLUE_50
					: MapColors.BLUE_60; // blue 80
		}
		else{
			voteData=districtData.voteData.Republican;
			wastedVotePercent = voteData.wastedVotes / voteData.votes;
			return colorMode == ColorMode.HIGHLIGHT_COLOR ? MapColors.PEACH_80:
				wastedVotePercent < 0.10 ? MapColors.RED_20: 
			 	wastedVotePercent < 0.20 ? MapColors.RED_30 : // blue 40
			 		wastedVotePercent < 0.30 ? MapColors.RED_40 :// blue 60
			 			wastedVotePercent < 0.40 ? MapColors.RED_50
			 					: MapColors.RED_60;
		}
	}
	
	
	return{
		setEvents: function(mapData, districtLayer, dataControl){
			return setEvents(mapData, districtLayer, dataControl);
		},
		setEvents2: function(mapData){
			return setEvents2(mapData);
		},
		
		setStyle: function(mapData){
			return setStyle(mapData);
		}
	};
	
});