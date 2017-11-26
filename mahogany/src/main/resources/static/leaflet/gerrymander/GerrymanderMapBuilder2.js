define([
	"dojo/_base/declare",
	"dojo/on",
	"dojo/topic",
	"dojo/dom-style",
	"dojo/request",
	"leaflet/gerrymander/metrics/ElectionData2",
	"leaflet/gerrymander/metrics/EfficiencyGap2",
	"leaflet/gerrymander/constants/DataType"
	
	],function(declare, on, topic, domStyle, request, ElectionData, EfficiencyGap, DataType){
	
	var MAP_MODE_STATE = "state";
	var MAP_MODE_DISTRICT = "district";
	
	var map;
	var districtLayer;
	var stateLayer;
	var mapMode;
	var dataMode;

	var mapControls = {}; // object that holds out map controls/displays/legends.
	var zoomOutBtn;
	
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
		
		dataDisplay = createDataDisplay();
		dataDisplay.addTo(map);
		mapControls.dataDisplay = dataDisplay;
		
		dataMode = DataType.ELECTION_DATA;
		
		mapData = {};
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
		metricData={};
		loadDistrictBoundariesRequest();
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
		request("districtBoundariesRequest", {
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
			loadDistrictData(mapData);
		});
		
	}
	function loadDistrictData(){
		if(dataMode == DataType.ELECTION_DATA){
			electionDataRequest();
		}
		else if(dataMode == DataType.EFFICIENCY_GAP){
			
		}
	}
	
	function electionDataRequest(){
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
			setEfficiencyGapLayer(mapData);
		});

	}
	

	
	
	function setEfficiencyGapLayer(){
			L.Util.setOptions(districtLayer, {style:null});
			L.Util.setOptions(districtLayer, { style: EfficiencyGap.setStyle(mapData)	});
			districtLayer.eachLayer(EfficiencyGap.setEvents(mapData, districtLayer, mapControls.dataDisplay));
			districtLayer.setStyle(EfficiencyGap.setStyle(mapData));
	}
	
	function setElectionDataLayer(){
		
		districtLayer.setStyle(ElectionData.setStyle());
		L.Util.setOptions(districtLayer, {style:null});
		L.Util.setOptions(districtLayer, {style: ElectionData.setStyle()});
		
		districtLayer.eachLayer(ElectionData.setEvents(mapData, districtLayer, mapControls.dataDisplay));
		
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
