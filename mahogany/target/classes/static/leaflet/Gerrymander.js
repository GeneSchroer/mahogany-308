var GerrymanderMapController = L.Class.extend({
	_stateMap: null,
	_stateBorders: null,
	_districtBorders: null,
	_measures: null,
	_stateLayer: null,
	_stateControl: null,
	_instance: null,
	_zoomOutBtn: null,
	initialize: function(builder){
		this._stateMap = builder._stateMap;
		this._stateBorders = builder._stateBorders;
		this._districtBorders = builder._districtBorders;
		this._measures = builder._measures;
		this._stateLayer = builder._stateLayer; //stateBorderFactory(this);
		this._addStateDisplay(this._theMap);
		this._zoomOutBtn = builder._zoomOutBtn;
		
	},
	
	_addStateDisplay: function(map){
		
	}
	

});


GerrymanderMapController.GerrymanderBuilder = {
	_stateBorders: null,
	_districtBorders: null,
	_measures: null,
	_stateMap: null,
	_stateLayer: null,
	_zoomOutBtn: null,
	addMap: function(map){
		this._stateMap = map;
		return this;
	},
	addStateBorders: function(borders){
		this._stateBorders = borders;
		return this;
	},
	addDistrictBorders: function(borders){
		this._districtBorders = borders;
		return this;
	},
	addMeasures: function(measures){
		this._measures = measures;
		return this;
	},
	_createZoomOutBtn: function(map){
		var zoomBtn = L.control();
		zoomBtn.onAdd = function(map){
			this._div=L.DomUtil.create('button', "info");
			
			
			this.disabled();
			this._div.innerHTML= "Zoom Out";
			return this._div;
		};
		zoomBtn.disabled = function(){
			this._div.style.display="none";
			map.setView([38, -88], 4, true);
			L.DomEvent.off(zoomBtn);
		}
		zoomBtn.enabled= function(){
			this._div.style.display="block";
			L.DomEvent.on(this._div, 'click', function(e){
				zoomBtn.disabled();
				
			});
		}
		zoomBtn.update=function(props){
			this._div.innerHTML = '<h4>US Population Density</h4>' + (props ? 
					"<b>" + props.name + "</b><br />" + props.density + " people / mi<sup>2</sup>"
					: "Hover over a state");
		}
		zoomBtn.addTo(map);
		return zoomBtn;
	},
	_createStateLayer: function(map, borders, button){
		var geoJson =  L.geoJson(borders,{
			style: function(feature){
				return{
					fillColor: setColor(feature.properties.density),
					weight: 2,
					opacity: 1,
					color: 'white',
					fillOpacity: 0.7
				};
			},
			onEachFeature: function(feature, layer){
				layer.on({
					mouseover:function(e){
						layer = e.target;
						layer.setStyle({
							weight: 5,
							color: 'brown',
							dataArray:'',
							fillOpacity: 0.7
						});
					},
					mouseout:function(e){
						geoJson.resetStyle(e.target);
					},
					click: function(e){
						map.fitBounds(e.target.getBounds());
						button.enabled();
					}
				});
				
			}
		}).addTo(map);
		return geoJson;
	},
	_createMap: function(map){
		return L.map(map, {
				zoomControl: false,
				dragging: false,
				scrollWheelZoom: false
				}).setView([32, -80], 4);
	},
	build: function(){
		
		
		this._stateMap = this._createMap(this._stateMap);
		this._zoomOutBtn = this._createZoomOutBtn(this._stateMap);
		this._stateLayer = this._createStateLayer(this._stateMap, this._stateBorders, this._zoomOutBtn);

		return new GerrymanderMapController(this);
	}
};

function stateBorderFactory(data){
	var geoJson = L.geoJson(data._stateBorders,{
			style: function(feature){
				return{
					fillColor: setColor(feature.properties.density),
					weight: 2,
					opacity: 1,
					color: 'white',
					fillOpacity: 0.7
				};
			},
			onEachFeature: function(feature, layer){
				layer.on({
					mouseover:function(e){
						layer = e.target;
						layer.setStyle({
							weight: 5,
							color: 'brown',
							dataArray:'',
							fillOpacity: 0.7
						});
					},
					mouseout:function(e){
						geoJson.resetStyle(e.target);
					},
					click: function(e){
						data._stateMap.fitBounds(e.target.getBounds());
						data._zoomOutBtn.enabled();
					}
				});
				
			}
		}).addTo(data._stateMap);
		
	return geoJson;
}
function setColor(d){
	return d > 1000 ? '#33241C' :
           d > 500  ? '#814B19' :
           d > 200  ? '#AD6418' :
           d > 100  ? '#DB7C00' :
           d > 50   ? '#FE8500' :
           d > 20   ? '#FCAF6D' :
           d > 10   ? '#FDCFAD' :
                      '#F5E8DE';
}
