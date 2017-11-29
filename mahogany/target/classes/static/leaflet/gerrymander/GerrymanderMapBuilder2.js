define([
	"dojo/_base/declare",
	"dojo/on",
	"dojo/topic",
	"dojo/dom-style",
	"dojo/request",
	"leaflet/gerrymander/metrics/ElectionData2",
	"leaflet/gerrymander/metrics/EfficiencyGap2",
	"leaflet/gerrymander/constants/DataType",
	"leaflet/gerrymander/constants/MapColors"
	
	],function(declare, on, topic, domStyle, request, ElectionData, EfficiencyGap, DataType, MapColors){
	
	var MAP_MODE_STATE = "state";
	var MAP_MODE_DISTRICT = "district";
	
	var map;
	var districtLayer;
	var stateLayer;
	var mapMode;
	var dataMode;

	var mapControls = {}; // object that holds out map controls/displays/legends.
	var zoomOutBtn;
	
	var memberData;
	var mapData;
	
	var currentState;
	var year;
	
	function buildGerrymanderMap(mapBlock){
		
		map = createMap(mapBlock);
		
		
		console.log("Create district layer")
		districtLayer = L.geoJson();
		districtLayer.addTo(map);
		
		console.log("Create state layer")
		stateLayer = L.geoJson();
		stateLayer.addTo(map);
		

		mapMode = MAP_MODE_STATE;
		
		//Add map controls
		zoomOutBtn = createZoomOutBtn();
		zoomOutBtn.addTo(map);
		L.DomEvent.on(zoomOutBtn._div, 'click', function(evt){
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
		
		dataMode = DataType.ELECTION_DATA;
		
		initializeDistrictLayer();
		initializeStateLayer();
		
		loadStateBoundariesRequest(stateLayer);
		return map;
	}
	function createMap(map){
		domStyle.set(map, "width", "100%");
		domStyle.set(map, "height", "100%");
		domStyle.set(map, "position", "absolute");
		return L.map(map, {
			//zoomControl: false,
			/*dragging: false,*/
			//scrollWheelZoom: false,
		}).setView([32, -80], 4);
	}

	function createZoomOutBtn(){
		var zoomOutBtn = L.control({position:'topleft'});	
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
	function zoomInMode(layer){
		districtLayer.clearLayers();
		mapMode = MAP_MODE_DISTRICT;
		currentState = layer.feature.properties.name;
		map.fitBounds(layer.getBounds());
		for(control in mapControls){
			mapControls[control].enable();
		}
		loadDistrictBoundariesRequest();
	}
	function zoomOutMode(){
		districtLayer.clearLayers();
		L.Util.setOptions(districtLayer, {style:null});
		metricData = {};
		mapMode = MAP_MODE_STATE;
		map.setView([38, -88], 4, true);
		for(control in mapControls){
			mapControls[control].disable();
		}
	}
	function createDataDisplay(){
		var dataDisplay = L.control({position:'topright'});
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
	function electionDataLegend(){
		var legendDisplay = "";
		legendDisplay += "<b>Parties:</b><br/>";
		legendDisplay += "Democrat " + '<i style="background:' + MapColors.BLUE_60 + ';"></i><br/>';
		legendDisplay += "Republican " + '<i style="background:' + MapColors.RED_60 + ';"></i><br/>';
		
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
	
	function initializeDistrictLayer(){
	
	}
	function initializeStateLayer(){
		L.Util.setOptions(stateLayer, {style: stateLayerStyle()});
		L.Util.setOptions(stateLayer, {onEachFeature:
			function(feature, layer){
			
				layer.on("mouseover", function(e){
					var layer = e.target;
					layer.setStyle({
						weight: 2,
						color: 'brown',
						dataArray: ' ',
						fillOpacity: 0.7
					});
				}, this);
				layer.on("mouseout", function(e){
					stateLayer.resetStyle(layer);
				}, this);
				layer.on("click", function(e){
					zoomInMode(layer);
				}, this);
			}
		});
	}
	function updateMap(){
		if(mapMode == MAP_MODE_DISTRICT){
			districtLayer.clearLayers();
			L.Util.setOptions(districtLayer, {style:null, onEachFeature:null});
			loadDistrictBoundariesRequest();
		}
	}
	
	function loadStateBoundariesRequest(stateLayer){
		request("stateBoundaries.json",{
			method: "GET",
			handleAs: "json"
		}).response.then(function(success){
			var stateData = success.data;
			console.log("Add states to the layer");
			stateLayer.addData(stateData);
		});
	}
	
	function stateLayerStyle(mapData){
		return {
			weight: 1,
			color: "green",
			fillColor: "lime",
			fillOpacity: 0.1
		};
	}
	function loadDistrictBoundariesRequest(){
		request("/districtBoundariesRequest", {
			method: "POST",
			data: {
				state: currentState,
				year: year
			},
			handleAs: 'json'
		}).response.then(function(success){
			console.log(success.data);
			//topic.publish("gerrymander/getDistricts/success", success);
			var districtData = success.data;
			//districtLayer.clearLayers();
			districtLayer.addData(districtData);
			loadDistrictData();
		});
		
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
			console.log(success.data);
			mapData = success.data;
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
			console.log(success.data);
			mapData = success.data;
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
