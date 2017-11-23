define([
	"leaflet/gerrymander/utils/ColorMode",
	"leaflet/gerrymander/constants/MapColors"
	], function(ColorMode, MapColors){
	
	function setEvents(mapData){
		return function (layer){
			layer.on({
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						weight: 0.5,
						fillColor: fillColor( mapData,layer.feature.properties.id, ColorMode.HIGHLIGHT_COLOR),
						dataArray:' ',
						fillOpacity: 1
					});
					
				},
				mouseout:function(e){
					mapData.districtLayer.resetStyle(e.target);
				},
				click: function(e){
					//electionLayerPopup(mapData, e);
				}
			});
		};
	}
	
	
	function setStyle(mapData){
		return function(feature){
			return{
				fillOpacity: 0.8,
				weight: 1,
				color: "yellow",
				fillColor: fillColor(mapData, feature.properties.id, ColorMode.DEFAULT_COLOR)

			};
		};
	}
	function fillColor(mapData, id, colorMode){
		var districtId = id;
		var metricData = mapData.metricData.efficiencyGap;
		var districtData = metricData.districtData[districtId];
		var wastedVotePercent;
		var voteData;
		if(districtData.winningParty == "Democrat"){
				voteData=districtData.voteData.Democrat;
				wastedVotePercent = voteData.wastedVotes / voteData.votes;
				//console.log(wastedVotePercent);
				return colorMode == ColorMode.HIGHLIGHT_COLOR ? MapColors.BLUE_100 :
					wastedVotePercent < 0.10 ? MapColors.BLUE_10 : 
					 	wastedVotePercent < 0.20 ? MapColors.BLUE_30 : // blue 40
					 		wastedVotePercent < 0.30 ? MapColors.BLUE_50 :// blue 60
					 			wastedVotePercent < 0.40 ? MapColors.BLUE_70
					 					: MapColors.BLUE_90; // blue 80
		}
		else{
			voteData=districtData.voteData.Republican;
			wastedVotePercent = voteData.wastedVotes / voteData.votes;
			return colorMode == ColorMode.HIGHLIGHT_COLOR ? MapColors.RED_100:
				wastedVotePercent < 0.10 ? MapColors.RED_10: 
			 	wastedVotePercent < 0.20 ? MapColors.RED_30 : // blue 40
			 		wastedVotePercent < 0.30 ? MapColors.RED_50 :// blue 60
			 			wastedVotePercent < 0.40 ? MapColors.RED_70
			 					: MapColors.RED_90;
		}
	}
	
	
	return{
		setEvents: function(mapData){
			return setEvents(mapData);
		},
		setEvents2: function(mapData){
			return setEvents2(mapData);
		},
		
		setStyle: function(mapData){
			return setStyle(mapData);
		}
	};
	
});