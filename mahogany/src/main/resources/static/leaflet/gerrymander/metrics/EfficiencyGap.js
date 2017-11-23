require([
	"leaflet/gerrymander/utils/ColorMode"
	], function(ColorMode){
	
	function setStyle(mapData){
		return function(feature){
			return{
				fillOpacity: 0.8,
				weight: 1,
				color: "yellow",
				fillColor: fillColor(mapData, feature.properties.id)

			};
		};
	}
	function fillColor(mapData, id){
		var districtId = id;
		var metricData = mapData.metricData.efficiencyGap;
		var districtData = metricData.districtData[districtId];
		var wastedVotePercent;
		var voteData;
		if(districtData.winningParty == "Democrat"){
			voteData=districtData.voteData.Democrat;
			wastedVotePercent = voteData.wastedVotes / voteData.votes;
			console.log(wastedVotePercent);
			return wastedVotePercent < 0.05 ? "#a8c0f3" : // blue 20
				 	wastedVotePercent < 0.10 ? "#5392ff": // blue 40
				 		wastedVotePercent < 0.20 ? "#1f57a4" // blue 60
				 				: "#1d3458"; // blue 80
		}
		else{
			voteData=districtData.voteData.Republican;
			wastedVotePercent = voteData.wastedVotes / voteData.votes;
			return wastedVotePercent < 0.05 ? "#ffaa9d" : // red 20
				 	wastedVotePercent < 0.10 ? "#ff5c49": // red 40
				 		wastedVotePercent < 0.20 ? "#aa231f" // red 60
				 				: "#5c1f1b"; // red 80
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