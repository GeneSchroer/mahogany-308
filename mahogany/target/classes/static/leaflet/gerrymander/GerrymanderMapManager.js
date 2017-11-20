define(["dojo/_base/declare", "dojo/on", "dojo/topic", "dojo/dom-style", "dojo/request", "dojo/when"], 
		function(declare, on, topic, domStyle, request, when){
	var METRIC_DEFAULT_MODE = "defaultMode";
	var METRIC_EFFICIENCY_GAP = "efficiency gap";
	
	var MAP_MODE_STATE = "state";
	var MAP_MODE_DISTRICT = "district";
	
	
	function stateLayerStyle(mapData){
		return {
			weight: 1,
			color: "green",
			fillColor: "lime",
			fillOpacity: 0.1
		};
	}
	
	function setDistrictPopup(mapData){
		return function(layer){
			return layer.feature.properties.state;
		};
	}
	
	function setDefaultHighlightColor(feature, mapData){
		var winningParty = feature.properties.winningParty;
		
		if(winningParty){
			if(winningParty == "Democrat"){
				return "navy";
			}
			else if(winningParty == "Republican"){
				return "darkred";
			}
			else{
				return "lime";
			}
		}
		else{
			return "lightgray";
		}
	}
	
	function setDefaultColor(feature, mapData){
		
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
	
	function setDefaultDistrictLayerEvents(mapData){
		return function (feature, layer){
			layer.on({
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						weight: 0.5,
						fillColor: setDefaultHighlightColor(layer.feature, mapData),
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
	
	function setDistrictLayerStyle(mapData){
		return function(feature){
			//console.log(mapData);
			return{
				weight: 0.5,
				color: "yellow",
				fillColor: setDefaultColor(feature, mapData),
				
				dataArray: ' ',
				fillOpacity: 0.8
			};
		};
	}
	
	function setStateLayerEvents(mapData){
		return function(feature, layer){
	
			layer.on({
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						weight: 2,
						color: 'brown',
						dataArray:'',
						fillOpacity: 0.7
					});
				},
				mouseout:function(e){
					mapData.stateLayer.resetStyle(e.target);
				},
				click: function(e){
					mapData.districtLayer.clearLayers();
					mapData.mode = "District";
					console.log(e.target.feature);
					mapData.state = e.target.feature.properties.name;
					mapData.map.fitBounds(e.target.getBounds());
					mapData.zoomOutBtn.enable();
					loadDistrictData(mapData);
				}
			});
		}
	}
	
	function loadDistrictData(mapData){
		getDistrictsRequest(mapData);
		if(mapData.metricMode == METRIC_DEFAULT_MODE){
			getElectionDataRequest(mapData);
		}
		else if(mapData.metricMode == METRIC_EFFICIENCY_GAP){
			efficiencyGapRequest(mapData);
		}
	}
	function getElectionDataRequest(mapData){
		request("electionDataRequest",{
			method: "GET",
			query:{
				state: mapData.state,
				congress: mapData.congress
			}, 
			handleAs: "json"
		}).response.then(function(success){
			console.log(success.data);
			mapData.metricData.defaultMode = success.data;
			setElectionDataLayer(mapData);
		});
	}
	
	function getDistrictsRequest(mapData){
		
		
		
		request("districtBoundariesRequest", {
			method: "POST",
			data: {
				state: mapData.state,
				congress: mapData.congress
			},
			handleAs: 'json'
		}).response.then(function(success){
			console.log(success.data);
			//topic.publish("gerrymander/getDistricts/success", success);
			var districtData = success.data;
			mapData.districtLayer.clearLayers();
			mapData.districtLayer.addData(districtData);
			
		});
	}
	function setStateLayersRequest(mapData){
		console.log("Requesting states");
		request("stateData.json",{
			method: "GET",
			handleAs: "json"
		}).response.then(function(success){
			var stateData = success.data;
			console.log("Add states to the layer");
			mapData.stateLayer.addData(stateData);
			
		});
	}
	
	
	
	var GerrymanderMapManager = L.Class.extend({
		
		_mapData: null,
		initialize: function(map){
			this._constructGerrymanderMap(map);
			this._mapData.mode = "State";
		},
		
		_constructGerrymanderMap:function(map){
			this._mapData = {};
			
			this._mapData.map = this._createMap(map);
			
			console.log("Create district layer")
			this._mapData.districtLayer = this._createDistrictLayer(this._mapData);
			this._mapData.districtLayer.addTo(this._mapData.map);
			
			console.log("Create state layer")
			this._mapData.stateLayer = this._createStateLayer(this._mapData);
			this._mapData.stateLayer.addTo(this._mapData.map);
			
			this._mapData.mode = "State";
			
			console.log("Create zoom out button")
			this._mapData.zoomOutBtn = this._createZoomOutBtn(this._mapData);
			this._mapData.zoomOutBtn.addTo(this._mapData.map);
			
			
			this._mapData.metricMode=METRIC_DEFAULT_MODE;
			this._mapData.metricData = {};
			
			setStateLayersRequest(this._mapData);
		},	
		_createMap: function(map){
			domStyle.set(map, "width", "100%");
			domStyle.set(map, "height", "100%");
			domStyle.set(map, "position", "absolute");
			return L.map(map, {
				zoomControl: false,
				/*dragging: false,*/
				scrollWheelZoom: false,
			}).setView([32, -80], 4);
		},
		_createDistrictLayer: function(mapData){
			var districtLayer = L.geoJson(null, {
				style: setDistrictLayerStyle(mapData),
				onEachFeature: setDefaultDistrictLayerEvents(mapData) 
				});

			return districtLayer;
		},
		_createStateLayer: function(mapData){
			var stateLayer = L.geoJson(null, {
					style: stateLayerStyle(),
					onEachFeature: setStateLayerEvents(mapData) 
				});
			return stateLayer;
		},
		_createZoomOutBtn:function(mapData){
			var zoomOutBtn = L.control();
			zoomOutBtn.onAdd = function(map){
				this._div=L.DomUtil.create('button', 'zoomOut');
				this._div.innerHTML = "ZoomOut";
				this.disable();
				return this._div;
			};
			zoomOutBtn.disable = function(){
				mapData.mode="State";
				domStyle.set(this._div, "display", "none");
				mapData.map.setView([38, -88], 4, true);
				L.DomEvent.off(zoomOutBtn);
			};
			zoomOutBtn.enable = function(){
				domStyle.set(this._div, "display", "");
				L.DomEvent.on(this._div, 'click', function(evt){
					mapData.districtLayer.clearLayers();
					L.Util.setOptions(mapData.districtLayer, {style:null});
					mapData.metricData = {};
					
					zoomOutBtn.disable();
				});
			};
			return zoomOutBtn;
		},
		setCongress: function(congress){
			this._mapData.congress = congress;
		},
		updateMap: function(){
			if(this._mapData.mode=="State"){
				getCountryMapRequest(this._mapData);
			}
			else{
				getDistrictsRequest(this._mapData);
			}
		},
		setMetricToEfficiencyGap: function(){
			this._mapData.metricMode = METRIC_EFFICIENCY_GAP;
			
			if(!this._mapData.metricData.efficiencyGap){
				efficiencyGapRequest(this._mapData);
			}
			else{
				setEfficiencyGapLayer(this._mapData);
			}
		},
		setMetricToDefaultMode: function(){
			this._mapData.metricMode = METRIC_DEFAULT_MODE;
			if(!this._mapData.metricData.defaultMode){
				getElectionDataRequest(this._mapData);
			}
			else{
				setElectionDataLayer(this._mapData);
			}
		}
	
		
		
		
		
	});
	
	function efficiencyGapRequest(mapData){

		request("/efficiencyGapRequest",{
			method: "GET",
			query: {
				state: mapData.state,
				congress: mapData.congress
			},
			handleAs: "json"
		}).response.then(function(success){
			mapData.metricData.efficiencyGap = success.data;
			setEfficiencyGapLayer(mapData);
		});

	}
	

	function efficiencyGapStyle(mapData){
		return function(feature){
			return{
				fillOpacity: 0.8,
				weight: 1,
				color: "yellow",
				fillColor: efficiencyGapFillColor(mapData, feature.properties.id)

			};
		};
	}
	function efficiencyGapFillColor(mapData, id){
		var districtId = id;
		var metricData = mapData.metricData.efficiencyGap;
		var districtData = metricData.districtData[districtId];
		var wastedVotePercent;
		var voteData;
		if(districtData.winningParty == "Democrat"){
			voteData=districtData.voteData.Democrat;
			wastedVotePercent = voteData.wastedVotes / voteData.votes;
			//console.long(wastedVotePercent);
			return wastedVotePercent < 0.05 ? "#a8c0f3" : // blue 20
				 	wastedVotePercent < 0.10 ? "#5392ff": // blue 40
				 		wastedVotePercent < 0.20 ? "#1f57a4" // blue 60
				 				: "#1d3458"; // blue 80
		}
		else{
			voteData=districtData.voteData.Republican;
			wastedVotePercent = voteData.wastedVotes / voteData.votes;
			//console.long(wastedVotePercent);
			return wastedVotePercent < 0.05 ? "#ffaa9d" : // red 20
				 	wastedVotePercent < 0.10 ? "#ff5c49": // red 40
				 		wastedVotePercent < 0.20 ? "#aa231f" // red 60
				 				: "#5c1f1b"; // red 80
		}
	}
	function setEfficiencyGapLayer(mapData){
		
			mapData.districtLayer.setStyle(efficiencyGapStyle(mapData));
		
		
			L.Util.setOptions(mapData.districtLayer, { style: efficiencyGapStyle(mapData)	});
	}
	
	function setElectionDataLayer(mapData){
		mapData.districtLayer.setStyle(setDistrictLayerStyle(mapData));
		L.Util.setOptions(mapData.districtLayer, {style: setDistrictLayerStyle(mapData)});
	}
	
		
	
	return {
		create: function(map){
			return new GerrymanderMapManager(map);
		}
	};
});
