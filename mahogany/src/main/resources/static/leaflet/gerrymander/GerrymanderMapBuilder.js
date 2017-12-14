define([
	"dojo/_base/declare",
	"dojo/on",
	"dojo/topic",
	"dojo/dom-style",
	"dojo/request",
	"leaflet/gerrymander/metrics/ElectionData",
	"leaflet/gerrymander/metrics/EfficiencyGap",
	"leaflet/gerrymander/constants/DataType",
	"leaflet/gerrymander/constants/MapColors",
	"leaflet/gerrymander/constants/TopicEvents"
	
	],function(declare, on, topic, domStyle, request, ElectionData, EfficiencyGap, DataType, MapColors, TopicEvents){
	
	var MAP_MODE_STATE = "state";
	var MAP_MODE_DISTRICT = "district";
	
	var map;				
	var districtLayer; // geoJson layer for districts boundaries
	var stateLayer;    // geoJson layer for the state boundaries
	var mapMode;			// used to determine whether we are in state or district mode
	var dataMode;			// used to determine which type of data we are viewing.

	
	var stateMapControls = {};
	var stateNameDisplay;
	
	var mapControls = {}; // object that holds out map controls/displays/legends.
	var zoomOutBtn;				// used to zoom out to the state view.
	var memberDisplay;		// used to see the members of congress for a district
	var dataDisplay; 			// used to view the data for a district.
	var legendControl;    // legend for what the colors of each layer represent.
	
	
	var memberData;			// holds the json data for members of congress
	var mapData;				// holds district data, such as election results or the efficiency gap
	
	var currentState;		// state the map is focused on.
	var year;						// the year of congress we are focused on.
	
	
	function buildGerrymanderMap(mapBlock){
		
		map = createMap(mapBlock);
		
		console.log("Create district layer")
		districtLayer = L.geoJson();
		districtLayer.addTo(map);
		
		console.log("Create state layer")
		stateLayer = L.geoJson();
		stateLayer.addTo(map);
		
		//Create map controls
		zoomOutBtn = createZoomOutBtn();
		zoomOutBtn.addTo(map);
		L.DomEvent.on(zoomOutBtn._div, 'click', function(evt){
			map.setView([38, -88], 4, true);

			zoomOutMode();
		});
		mapControls.zoomOutBtn = zoomOutBtn;
		
		memberDisplay = createDataDisplay();
		memberDisplay.addTo(map);
		mapControls.memberDisplayer = memberDisplay;
		
		dataDisplay = createDataDisplay();
		dataDisplay.addTo(map);
		mapControls.dataDisplay = dataDisplay;
		
		legendControl = createLegendControl();
		legendControl.addTo(map);
		mapControls.legendControl = legendControl;
		
		stateNameDisplay = createStateNameDisplay();
		stateNameDisplay.addTo(map);
		stateMapControls.stateNameDisplay = stateNameDisplay;
		
		
		mapMode = MAP_MODE_STATE;
		dataMode = DataType.ELECTION_DATA;
		
		//initializeDistrictLayer();
		initializeStateLayer();
		
		loadStateBoundariesRequest(stateLayer);
		return map;
	}
	
	function createMap(map){
		// the map needs to take up the whole block it's contained in,
		// so it's styled here just in case it wasn't already.
		domStyle.set(map, "width", "100%");
		domStyle.set(map, "height", "100%");
		domStyle.set(map, "position", "absolute");
		var mapDisplay = L.map(map, {
			//zoomControl: false,
			/*dragging: false,*/
			//scrollWheelZoom: false,
		}).setView([32, -80], 4);
		
		mapDisplay.on('zoomend', function(evt){
			console.log(mapDisplay.getZoom());
			if(mapDisplay.getZoom() <= 5){
				zoomOutMode();
			}
		});
		
		return mapDisplay;
		
	}

	function createZoomOutBtn(){
		var zoomOutBtn = L.control({position:'topleft'});	

		// onAdd() triggers when the control is first added to the map
		zoomOutBtn.onAdd = function(map){
			this._div=L.DomUtil.create('button', 'zoomOut');
			this._div.innerHTML = "ZoomOut";
			this.disable();
			return this._div;
		};
		zoomOutBtn.disable = function(){
			domStyle.set(this._div, "display", "none");
			
		};
		zoomOutBtn.enable = function(){
			domStyle.set(this._div, "display", "block");
			
		};
		return zoomOutBtn;
	
	}
	
	function createDataDisplay(){
		var dataDisplay = L.control({position:'topright'});
		
	// onAdd() triggers when the control is first added to the map
		dataDisplay.onAdd = function(map){
			this._div = L.DomUtil.create('div', 'dataDisplay');
			this._div.innerHTML = "";
			this.disable();
			return this._div;
		};
		dataDisplay.update = function (string) {
		    this._div.innerHTML = string ? string : "Data Not Available";
		};
		dataDisplay.disable = function(){
			domStyle.set(this._div, "display", "none");
		};
		dataDisplay.enable = function(){
			domStyle.set(this._div, "display", "block");
		};
		
		return dataDisplay;
	}
	
	function createLegendControl(){
		var legendControl = L.control({position:'bottomright'});

		// onAdd() triggers when the control is first added to the map
		legendControl.onAdd = function(map){
			this._div = L.DomUtil.create('div', 'dataDisplay legendControl');
			this._div.innerHTML = electionDataLegend();
			this.disable();
			return this._div;
		};
		legendControl.update = function (string) {
		    this._div.innerHTML = string ? string : "Data Not Available";
		};
		legendControl.disable = function(){
			domStyle.set(this._div, "display", "none");
		};
		legendControl.enable = function(){
			domStyle.set(this._div, "display", "block");
		};
		
		
		return legendControl;
	}
	
	function createStateNameDisplay(){
		var stateNameDisplay = L.control({position:'topright'});

		// onAdd() triggers when the control is first added to the map
		stateNameDisplay.onAdd = function(map){
			this._div = L.DomUtil.create('div', 'dataDisplay legendControl');
			this._div.innerHTML = " ";
			return this._div;
		};
		stateNameDisplay.update = function (string) {
		    this._div.innerHTML = string ? string : "Data Not Available";
		};
		stateNameDisplay.disable = function(){
			domStyle.set(this._div, "display", "none");
		};
		stateNameDisplay.enable = function(){
			domStyle.set(this._div, "display", "block");
		};
		
		
		return stateNameDisplay;
	
	}
	
	function initializeStateLayer(){
		L.Util.setOptions(stateLayer, {style: stateLayerStyle()});
		L.Util.setOptions(stateLayer, {onEachFeature:
			function(feature, layer){
			
				layer.on("mouseover", function(e){
					var layer = e.target;
					layer.setStyle({
						weight: 2,
						dataArray: ' ',
						fillColor: stateFillColor(layer.feature.properties.color),
						fillOpacity: 0.7
					});
					stateNameDisplay.update(setStateNameDisplay(layer, stateNameDisplay));
				}, this);
				layer.on("mouseout", function(e){
					stateLayer.resetStyle(layer);
					stateNameDisplay.update(" ");
				}, this);
				layer.on("click", function(e){
					zoomInMode(layer);
				}, this);
			}
		});
	}
	
	function loadStateBoundariesRequest(stateLayer){
		request("stateData.json",{
			method: "GET",
			handleAs: "json"
		}).response.then(function(success){
			var stateData = success.data;
			console.log("Add states to the layer");
			stateLayer.addData(stateData);
		});
	}
	
	function loadDistrictBoundariesRequest(){
		loadDistrictData();
		request("/districtBoundariesRequest", {
			method: "POST",
			data: {
				state: currentState,
				year: year
			},
			handleAs: 'json'
		}).response.then(function(success){
			console.log(success.data);
			var districtData = success.data;
			districtLayer.addData(districtData);
			
		}, function(error){
			console.log(error.response.status);
			topic.publish(TopicEvents.DATA_SIDE_PANEL, DataType.NONE);
		});
		
	}
	
	function memberDataRequest(){
		request("/getMemberData",{
			method: "GET",
			query:{
				state: currentState,
				year: year
			}, 
			handleAs: "json"
		}).response.then(function(success){
			console.log(success.data);
			memberData = success.data;
			
		});
	}
	
	function electionDataRequest(){
		mapData = null;
		request("/electionDataRequest",{
			method: "GET",
			query:{
				state: currentState,
				year: year
			}, 
			handleAs: "json"
		}).response.then(function(success){
			topic.publish(TopicEvents.DATA_SIDE_PANEL, DataType.ELECTION_DATA, success.data);
			console.log(success.data);
			mapData = success.data;
			setElectionDataLayer();
		}, function(error){
			console.log(error.response.status);
			topic.publish(TopicEvents.DATA_SIDE_PANEL, DataType.NONE);
			mapData = null;
			setElectionDataLayer();
			
		});
	}
	
	function efficiencyGapRequest(){
		mapData = null;
		request("/efficiencyGapRequest",{
			method: "GET",
			query: {
				state: currentState,
				year: year
			},
			handleAs: "json"
		}).response.then(function(success){
			topic.publish(TopicEvents.DATA_SIDE_PANEL, DataType.EFFICIENCY_GAP, success.data);
			console.log(success.data);
			mapData = success.data;
			setEfficiencyGapLayer();
		}, function(error){
			console.log(error.response.status);
			topic.publish(TopicEvents.DATA_SIDE_PANEL, DataType.NONE);
			mapData = null;
			setEfficiencyGapLayer();
		});

	}
	
	function setEfficiencyGapLayer(){
		legendControl.update(efficiencyGapLegend());
		districtLayer.setStyle(null);
		L.Util.setOptions(districtLayer, {style:null});
		L.Util.setOptions(districtLayer, { style: EfficiencyGap.setStyle(mapData)	});
		districtLayer.setStyle(EfficiencyGap.setStyle(mapData));
		districtLayer.eachLayer(EfficiencyGap.setEvents(mapData, districtLayer, mapControls.dataDisplay));
		
	}
	
	function setElectionDataLayer(){
		legendControl.update(electionDataLegend());
		districtLayer.setStyle(null);
		L.Util.setOptions(districtLayer, {style:null});
		L.Util.setOptions(districtLayer, {style: ElectionData.setStyle()});
		districtLayer.setStyle(ElectionData.setStyle());
		districtLayer.eachLayer(ElectionData.setEvents(mapData, districtLayer, mapControls.dataDisplay));
		districtLayer.eachLayer(function(layer){
			layer.on('mouseover', function(e){
				memberDisplay.update(setMemberDisplay(layer));
			});
			layer.on('mouseout', function(e){
				memberDisplay.update(" ");
			});
		});
	}
	
	function stateLayerStyle(){
		return function(feature, layer){
			return{
				weight: 1,
				color: "black",
				fillColor: stateFillColor(feature.properties.color),
				fillOpacity: 0.2
			}
		};
	}
	
	function stateLayerStyle2(){
		return{
			weight: 1,
			color: "black",
			fillColor: "gray",
			fillOpacity: 0.2
		}
	}
	
	function stateFillColor(color){
		if(!color){
			return "gray";
		}
		else{
			return color == 1 ? "blue" :
							color == 2 ? "red" :
								color == 3 ? "green" :
									"yellow";
		}
	}
	
	function setStateNameDisplay(layer, display){
		var displayString = "";
		var stateName = layer.feature.properties.name;
		var stateCode = layer.feature.id;
		displayString += "<b>" + stateName + "</b><br/>";
		displayString += "<b>State Code: " + stateCode + "</b>"; 
		return displayString;
		
	}
	function setDistrictLayer(){
		if(dataMode == DataType.ELECTION_DATA){
			setElectionDataLayer();
		}
		else if (dataMode == DataType.EFFICIENCY_GAP){
			setEfficiencyGapLayer();
		}
	}
	
	function loadDistrictData(){
		
		memberDataRequest();
		
		if(dataMode == DataType.ELECTION_DATA){
			electionDataRequest();
		}
		else if(dataMode == DataType.EFFICIENCY_GAP){
			efficiencyGapRequest();
		}
	}
	
	function setMemberDisplay(layer){
		var displayString = "";
		var districtId = layer.feature.properties.id
		
		if(!memberData || !memberData.districtData[districtId]){
			return "Data Not Available";
		}
		
		var districtData = memberData.districtData[districtId];
		var memberList = districtData.memberData;
		var electionDistrictData = null;
		if(mapData && mapData.districtData[districtId]){
			electionDistrictData = mapData.districtData[districtId];
		}
		for(m in memberList){
			var member = memberList[m];
			if(electionDistrictData && electionDistrictData.winningParty == member.party){
		//		displayString += "<b>" + member.party + ": " + member.name + "</b><br/>";
				displayString += "<b>" + member.name + ": " + member.party + "</b><br/>";
			}
			/*else{
				displayString += member.party + ": " + member.name + "<br/>";
				
			}*/
		}
		return displayString;
	}
	
	function updateMap(){
		if(mapMode == MAP_MODE_DISTRICT){
			clearDistrictLayer();
			loadDistrictBoundariesRequest();
		}
	}
	
	function zoomInMode(layer){
		// clear the current layers from the map.
		clearDistrictLayer();
		
		mapMode = MAP_MODE_DISTRICT;
		currentState = layer.feature.properties.name;
		map.fitBounds(layer.getBounds());
		
		for(control in mapControls){
			mapControls[control].enable();
		}
		for(control in stateMapControls){
			stateMapControls[control].disable();
			if(stateMapControls[control].update){
				stateMapControls[control].update(" ");
			}
		}
		
		L.Util.setOptions(stateLayer, {style:stateLayerStyle2()});
		stateLayer.setStyle(stateLayerStyle2());
		loadDistrictBoundariesRequest();
	}

	function zoomOutMode(){
		clearDistrictLayer();
		mapMode = MAP_MODE_STATE;
		topic.publish(TopicEvents.DATA_SIDE_PANEL, DataType.NONE, " ");
	
		for(control in mapControls){
			mapControls[control].disable();
			if(mapControls[control].update){
				mapControls[control].update(" ");
			}
		}
		for(control in stateMapControls){
			stateMapControls[control].enable();
		}
		
		L.Util.setOptions(stateLayer, {style:stateLayerStyle()});
		stateLayer.setStyle(stateLayerStyle());
	}
	
	function clearDistrictLayer(){
		districtLayer.clearLayers();
		L.Util.setOptions(districtLayer, {style:null});
		metricData = {};
	}
	
	function electionDataLegend(){
		var legendDisplay = "";
		legendDisplay += "<b>Parties:</b><br/>";
		legendDisplay += "Democrat " + '<i style="background:' + MapColors.BLUE_60 + ';"></i><br/>';
		legendDisplay += "Republican " + '<i style="background:' + MapColors.RED_60 + ';"></i><br/>';
		legendDisplay += "Green " + '<i style="background:' + "Green" + ';"></i><br/>';
		legendDisplay += "Libertarian " + '<i style="background:' + "Gold" + ';"></i><br/>';
		legendDisplay += "Other " + '<i style="background:' + "Gray" + ';"></i><br/>';
		return legendDisplay;
	}
	
	function efficiencyGapLegend(){
		var legendDisplay = "";
		legendDisplay += "<b>Wasted Vote Percentage:</b><br/>";
		legendDisplay += "0-10 " + '<i style="background:' + MapColors.BLUE_20 + ';"></i><i style="background:' + MapColors.RED_20 + '"></i><br/>';
		legendDisplay += "10-20 " + '<i style="background:' + MapColors.BLUE_30 + ';"></i><i style="background:' + MapColors.RED_30 + '"></i><br/>';
		legendDisplay += "20-30 " + '<i style="background:' + MapColors.BLUE_40 + ';"></i><i style="background:' + MapColors.RED_40 + '"></i><br/>';
		legendDisplay += "30-40 " + '<i style="background:' + MapColors.BLUE_50 + ';"></i><i style="background:' + MapColors.RED_50 + '"></i><br/>';
		legendDisplay += "40-50 " + '<i style="background:' + MapColors.BLUE_60 + ';"></i><i style="background:' + MapColors.RED_60 + '"></i><br/>';
		return legendDisplay;
	}
	return {
		create: function(map){
			map = buildGerrymanderMap(map);
			return map;
		},
		
		setYear: function(yearValue){
			year = yearValue;
			updateMap();
		},
		setDataMode: function(dataType){
			dataMode = dataType;
			if(dataMode == DataType.EFFICIENCY_GAP){
				efficiencyGapRequest();
			}
			else if(dataMode == DataType.ELECTION_DATA){
				electionDataRequest();
			}
		
		}
		
	};
});
