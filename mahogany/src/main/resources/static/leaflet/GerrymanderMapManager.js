define(["dojo/_base/declare", "dojo/on", "dojo/topic", "dojo/dom-style", "dojo/request", "dojo/when"], 
		function(declare, on, topic, domStyle, request, when){
	var countryLayerStyle = function(x){
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
	
	function setHighlightColor(feature, mapData){
		var party;
		//console.log(mapData.year);
		var congress = feature.properties.member[mapData.congress];
		for (member in congress){
			//console.log(member);
			party = congress[member].party;
			break;
		}                
		//console.log(party);
		if (party == "Republican")
			return "darkred";
		else if (party == "Democrat")
			return "navy";
		else
			return "lime";
	}
	
	function setColor(feature, mapData){
		var party;
		//console.log(mapData.year);
		var congress = feature.properties.member[mapData.congress];
		for (member in congress){
			//console.log(member);
			party = congress[member].party;
			break;
		}                
		//console.log(party);
		if (party == "Republican")
			return "red";
		else if (party == "Democrat")
			return "blue";
		else
			return "green";
	}
	
	function setDistrictLayerEvents(mapData){
		return function (feature, layer){
			layer.on({
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						weight: 0.5,
						//color: 'green',
						fillColor: setHighlightColor(layer.feature, mapData),
						dataArray:' ',
						fillOpacity: 0.8
					});
				},
				mouseout:function(e){
					mapData.districtLayer.resetStyle(e.target);
				},
				click: function(e){
					
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
				fillColor: setColor(feature, mapData),
				
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
		
		var jsonStatefile = mapData.state + mapData.congress + ".json"; 
		
		request(jsonStatefile, {
			handleAs: 'json'
		}).response.then(function(success){
			topic.publish("gerrymander/getDistricts/success", success);
			var districtData = success.data;
			mapData.districtLayer.clearLayers();
			mapData.districtLayer.addData(districtData);
			
		});
	}
	function getCountryMapRequest(mapData){
		request("stateData.json",{
			method: "GET",
			handleAs: "json"
		}).response.then(function(success){
			var stateData = success.data;
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
			
			this._mapData.districtLayer = this._createDistrictLayer(this._mapData);
			this._mapData.districtLayer.addTo(this._mapData.map);
			
			this._mapData.stateLayer = this._createStateLayer(this._mapData);
			this._mapData.stateLayer.addTo(this._mapData.map);
			
			this._mapData.mode = "State";
			
			this._mapData.zoomOutBtn = this._createZoomOutBtn(this._mapData);
			this._mapData.zoomOutBtn.addTo(this._mapData.map);
			
			getCountryMapRequest(this._mapData);
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
				onEachFeature: setDistrictLayerEvents(mapData) 
				}).bindPopup(setDistrictPopup(mapData));

			return districtLayer;
		},
		_createStateLayer: function(mapData){
			var stateLayer = L.geoJson.call(null, {
					style: countryLayerStyle(),
					onEachFeature: setStateLayerEvents(mapData) 
				}).bindPopup();
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
		}
	
		
		
		
		
	});
	

	
	
	return {
		create: function(map){
			return new GerrymanderMapManager(map);
		}
	};
});
