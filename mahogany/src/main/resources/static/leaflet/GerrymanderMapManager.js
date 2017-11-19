define(["dojo/_base/declare", "dojo/on", "dojo/topic", "dojo/dom-style", "dojo/request", "dojo/when"], 
		function(declare, on, topic, domStyle, request, when){
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
					var popup = L.popup()
					.setLatLng(e.latlng)
					.setContent('popup')
					.openOn(mapData.map);
				}
			});
		};
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
					getDistrictsRequest(mapData);
				}
			});
		}
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
		_map: null,
		_stateLayer: null,
		_districtLayer: null,
		_zoomOutBtn: null,
		_congress: null,
		_state: null, 
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
		setMetric: function(metric){
			this._mapData.metric = metric;
			
		//	metricDataRequest(this._mapData);
			if(metric == "efficiencyGap"){
				efficiencyGapRequest(this._mapData);
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
			mapData.metricData = success.data;
			setEfficiencyGapLayer(mapData);
		});

	}
	

	function efficiencyGapStyle(mapData){
		return function(feature){
			return{
				fillOpacity: 0.8,
				weight: 1,
				color: "yellow",
				fillColor: x(mapData, feature.properties.id)

			};
		};
	}
	function x(mapData, id){
		var districtId = id;
		var metricData = mapData.metricData;
		var districtData = metricData.districtData[districtId];
		if(districtData.winningParty == "Democrat"){
			return "blue";
		}
		else{
			return "red";
		}
	}
	function setEfficiencyGapLayer(mapData){
		
			mapData.districtLayer.setStyle(efficiencyGapStyle(mapData));
		
		
			L.Util.setOptions(mapData.districtLayer, { style: efficiencyGapStyle(mapData)	});
	}
	
	function metricDataRequest(mapData){
		
		request("/metricDataRequest",{
			method: "GET",
			query: {
				state: mapData.state,
				congress: mapData.congress,
				metric: mapData.metric
			},
			handleAs: "json"
		}).response.then(function(success){
			
		});
		
		
		
	}
	
	
	return {
		create: function(map){
			return new GerrymanderMapManager(map);
		}
	};
});
