define(["dojo/_base/declare", "dojo/on", "dojo/topic", "dojo/dom-style", "dojo/request", "dojo/when", "leaflet/RedistrictUtils"], 
		function(declare, on, topic, domStyle, request, when, RedistrictUtils){
	
	function createSuperdistrictLayer(mapData){
		var superdistrictLayer = L.geoJson(null,{
			style: RedistrictUtils.superdistrictLayerStyle(mapData),
			onEachFeature: RedistrictUtils.superdistrictLayerEvents(mapData)
		});
		
		superdistrictLayer.on({
			mouseover: function(e){
				superdistrictLayer.eachLayer(function(layer){
					layer.setStyle({
						fillColor:'green',
						fillOpacity: 0.5
					});
				});
			},
			mouseout: function(e){
				superdistrictLayer.eachLayer(function(layer){
					superdistrictLayer.resetStyle(layer);
				});
			}
		});
		return superdistrictLayer;
		
	}
	
	
	var countryLayerStyle = function(x){
		return {
			weight: 1,
			color: "green",
			fillColor: "lime",
			fillOpacity: 0.1
		};
		
	}
	
	function setHighlightColor(feature, mapData){
		var party;
		//console.log(mapData.year);
		var congress = feature.properties.member[mapData.year];
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
		var congress = feature.properties.member[mapData.year];
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
					if(!feature.properties.selected)
						layer.setStyle(RedistrictUtils.districtLayerHighlightStyle(mapData));
				},
				mouseout:function(e){
					if(!feature.properties.selected)
						mapData.districtLayer.resetStyle(e.target);
				},
				click: function(e){
					if(!mapData.creationModeOn){
						
						mapData.creationModeOn = true;
						mapData.createSDBtn.enable();
						
					}
					if(!feature.superNum){
						feature.properties.selected = true;
						layer.setStyle(RedistrictUtils.createSuperdistrictStyle(mapData));
					}
					else{
						layer.setStyle(RedistrictUtils.districtLayerHighlightStyle(mapData));
						feature.properties.selected=null;
					}
					
				}
			});
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
				//	console.log(e.target.feature);
					mapData.state = e.target.feature.properties.name;
					mapData.map.fitBounds(e.target.getBounds());
					mapData.zoomOutBtn.enable();
					getDistrictsRequest(mapData);
				}
			});
		}
	}
	
	
	
	function getDistrictsRequest(mapData){
		
		var jsonStatefile = mapData.state + mapData.year + ".json"; 
		
		request(jsonStatefile, {
			handleAs: 'json'
		}).response.then(function(success){
			topic.publish("redistrict/getDistricts/success", success);
			var geoJsonData = success.data;
			var deepClone = JSON.parse(JSON.stringify(geoJsonData));
			mapData.districtData = geoJsonData;
			mapData.districtLayer.clearLayers();
			mapData.districtLayer.addData(deepClone);
			
			var i;
			for (i=0; i< mapData.superdistrictLayers.length; ++i){
				mapData.superdistrictLayers[i].addTo(mapData.map);
			}
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
	
	
	
	var RedistrictMapManager = L.Class.extend({
		_mapData: null,
		initialize: function(map){
			this._constructRedistrictMap(map);
		},
		
		_constructRedistrictMap:function(map){
			this._mapData = {};
			
			this._mapData.map = this._createMap(map);
			
			this._mapData.districtLayer = this._createDistrictLayer(this._mapData);
			this._mapData.districtLayer.addTo(this._mapData.map);
			
			this._mapData.stateLayer = this._createStateLayer(this._mapData);
			this._mapData.stateLayer.addTo(this._mapData.map);
			
					
			this._mapData.superdistrictLayers = [];
		
			
			
			this._mapData.zoomOutBtn = this._createZoomOutBtn(this._mapData);
			this._mapData.zoomOutBtn.addTo(this._mapData.map);
			
			this._mapData.createSDBtn = this._createCreateSuperdistrictBtn(this._mapData);
			this._mapData.createSDBtn.addTo(this._mapData.map);
	
			
			
			this._mapData.creationModeOn=false;
			this._mapData.mode = "State";
			
			
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
				style: RedistrictUtils.districtLayerStyle(mapData),
				onEachFeature: setDistrictLayerEvents(mapData) 
				});

			return districtLayer;
		},
		_createStateLayer: function(mapData){
			var stateLayer = L.geoJson(null, {
					style: countryLayerStyle(),
					onEachFeature: setStateLayerEvents(mapData) 
				});
			return stateLayer;
		},
		_createSuperdistrictLayer: function(mapData){
			/*var superdistrictLayer = L.geoJson(null,{
					style: RedistrictUtils.superdistrictLayerStyle(mapData),
					onEachFeature: RedistrictUtils.superdistrictLayerEvents(mapData)
				});
			
			superdistrictLayer.on('mouseover', function(e){
				this.eachLayer(function(layer){
					layer.setStyle(RedistrictUtils.superdistrictHighlightStyle(mapData));
				})
			}, mapData.superdistrictLayer);
			
			superdistrictLayer.on('mouseout', function(e){
				mapData.superdistrictLayer.mapData.stateLayer.res
			});*/
			//return superdistrictLayer;
			return null;
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
					mapData.createSDBtn.disable();
					zoomOutBtn.disable();
					mapData.creationModeOn=false;
					//mapData.tempSuperdistrictLayer.removeFrom(mapData.map);
					var i;
					for(i = 0; i< mapData.superdistrictLayers.length; ++i){
						mapData.superdistrictLayers[i].removeFrom(mapData.map);
					}
				});
			};
			return zoomOutBtn;
		},
		_createCreateSuperdistrictBtn: function(mapData){
			var createSDBtn = L.control();
			createSDBtn.onAdd = function(map){
				this._div=L.DomUtil.create('button', 'createSD');
				this._div.innerHTML = "Create";
				this.disable();
				return this._div;
			}
			createSDBtn.disable = function(){
				domStyle.set(this._div, "display", "none");
				L.DomEvent.off(createSDBtn);
			}
			createSDBtn.enable = function(){
				
				domStyle.set(createSDBtn._div, "display", "block");
				L.DomEvent.on(createSDBtn._div, "click", function(evt){
					var tempLayer = createSuperdistrictLayer(mapData);
						
					/*L.geoJson(null, {
						style: RedistrictUtils.superdistrictLayerStyle(mapData)
							
						,//RedistrictUtils.superdistrictLayerStyle(mapData),
						onEachFeature: RedistrictUtils.superdistrictLayerEvents(mapData)
					
					});*/
					var x = L.featureGroup();
					//mapData.tempSuperdistrictLayer.bringToFront();
					mapData.districtLayer.eachLayer(function(layer){
						if(layer.feature.properties.selected){
							x.addLayer(layer);
							console.log("added");
						}
					});
					tempLayer.addData(x.toGeoJSON());
					mapData.districtLayer.clearLayers();
					var deepClone = JSON.parse(JSON.stringify(mapData.districtData));
					mapData.districtLayer.addData(deepClone);
					mapData.districtLayer.bringToBack();
					mapData.stateLayer.bringToBack();
					//mapData.districtLayer.bringToBack();
					tempLayer.addTo(mapData.map);
					console.log(mapData.districtData);
					mapData.superdistrictLayers.push(tempLayer);
					
					
					//console.log(mapData.tempSuperdistrictLayer.toGeoJSON());
					createSDBtn.disable();
					mapData.creationModeOn=false;
				});
			}
			return createSDBtn;
		},
		_createSuperdistrictLayer(mapData){
				var superdistrictLayer = L.geoJson(null, {
					style: RedistrictUtils.superdistrictLayerStyle(mapData),
					onEachFeature: RedistrictUtils.superdistrictLayerEvents(mapData)
				
				});
			
			return superdistrictLayer;
		},
		setYear: function(year){
			this._mapData.year = year;
		},
		updateMap: function(){
			if(this._mapData.mode=="State"){
				getCountryMapRequest(this._mapData);
			}
			else{
				getDistrictsRequest(this._mapData);
			}
		},
		getMap: function(){
			return this._mapData.map;
		}
	
		
		
		
		
	});
	

	
	
	return {
		create: function(map){
			return new RedistrictMapManager(map);
		}
	};
});
