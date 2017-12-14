define([
	"leaflet/gerrymander/utils/ColorMode",
	"leaflet/gerrymander/constants/MapColors"
	], function(ColorMode, MapColors){
	
	function setStyle(mapData){
		return function(feature){
			return{
				fillOpacity: 0.4,
				weight: 0.5,
				color: "black",
				fillColor: fillColor(mapData, feature.properties.id, ColorMode.DEFAULT_COLOR)

			};
		};
	}
	
	function setEvents(mapData, districtLayer, dataControl){
		return function (layer){
			layer.off();
			layer.on({
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						fillColor: fillColor( mapData,layer.feature.properties.id, ColorMode.HIGHLIGHT_COLOR),
						dataArray:' ',
						fillOpacity: 0.4
					});
					dataControl.update(setDisplay(layer, mapData));
				},
				mouseout:function(e){
					dataControl.update(" ");
					districtLayer.resetStyle(e.target);
				},
				click: function(e){
					//electionLayerPopup(mapData, e);
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
		
		displayString += "<h4>Votes:</h4>";
		
		var districtId = layer.feature.properties.id;
		var districtData = mapData.districtData[districtId];
		if(!districtData){
			return "Data Not Available";
		}
		
		var voteData=districtData.voteData;
		
		
		
		for(party in voteData){
			
			var data = voteData[party];
			displayString += "<b>" + party + "</b><br/>";
			displayString += "Votes: " + data.votes.toLocaleString() + "<br/>";
			
			var wastedVotesPercentage = (data.wastedVotes/data.votes).toFixed(3);
			if(wastedVotesPercentage != 1 && data.votes!=0){
				displayString += "Wasted Votes: " + data.wastedVotes.toLocaleString() + "<br/>";
				displayString += "Wasted Vote Percentage: " + wastedVotesPercentage + "<br/>";
			}
		}		
		return displayString;
	}
	
	
	function fillColor(mapData, id, colorMode){
		var districtId = id;
		var metricData = mapData;
//		console.log(districtId);
	//	console.log(metricData);
		var districtData = metricData.districtData[districtId];
		if(!districtData){
			return colorMode == ColorMode.HIGHLIGHT_COLOR ? "lightgray" : "gray";
		}
		var wastedVotePercent;
		var voteData;
		if(districtData.winningParty == "Democrat"){
				voteData=districtData.voteData.Democrat;
				wastedVotePercent = voteData.wastedVotes / voteData.votes;
				//console.log(wastedVotePercent);
				return colorMode == ColorMode.HIGHLIGHT_COLOR ? MapColors.COOL_GRAY_30 :
					wastedVotePercent < 0.10 ? MapColors.BLUE_20 : 
					wastedVotePercent < 0.20 ? MapColors.BLUE_30 : // blue 40
					wastedVotePercent < 0.30 ? MapColors.BLUE_40 :// blue 60
					wastedVotePercent < 0.40 ? MapColors.BLUE_50
					: MapColors.BLUE_60; // blue 80
		}
		else{
			voteData=districtData.voteData.Republican;
			wastedVotePercent = voteData.wastedVotes / voteData.votes;
			return colorMode == ColorMode.HIGHLIGHT_COLOR ? MapColors.WARM_GRAY_30:
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
		setStyle: function(mapData){
			return setStyle(mapData);
		}
	};
	
});