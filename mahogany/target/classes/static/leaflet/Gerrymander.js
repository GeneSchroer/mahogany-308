var GerrymanderController = L.Class.extend({
	_theMap: null,
	_stateBorders: null,
	_districtBorders: null,
	_measures: null,
	_stateLayerGroup: null,
	_stateControl: null,
	_instance: null,
	initialize: function(builder){
		this._theMap = builder._theMap;
		this._stateBorders = builder._stateBorders;
		this._districtBorders = builder._districtBorders;
		this._measures = builder._measures;
		this._stateLayerGroup = nationalFactory(this);
			
			/*L.geoJson(this._stateBorders,{
			style: this.stateStyle,
			onEachFeature: this._stateFeatures
		}).addTo(this._theMap);*/
		this._addStateDisplay(this._theMap);
		this._instance = this;
		console.log(this._stateLayerGroup);
		console.log(this);
		console.log(this._instance);
	},
	
	_addStateDisplay: function(map){
		
	},
	_setStateColor: function(d) {
	    return d > 1000 ? '#33241C' :
	           d > 500  ? '#814B19' :
	           d > 200  ? '#AD6418' :
	           d > 100  ? '#DB7C00' :
	           d > 50   ? '#FE8500' :
	           d > 20   ? '#FCAF6D' :
	           d > 10   ? '#FDCFAD' :
	                      '#F5E8DE';
	},
	_stateFeatures: function(feature, layer){
		console.log(this);
		layer.on({
			mouseover: function(e){
				//GerrymanderController.prototype.highlightFeature.call(this, e.target);
				layer=e.target;
				layer.setStyle({
					weight: 5,
					color: 'brown',
					dashArray: '3',
					fillOpacity: 0.7
					
				});
			},
			mouseout: function(e){ 
				console.log(this);
				console.log(this._stateLayerGroup);
				this._instance._stateLayerGroup.resetStyle(e.target);

				
				//.resetHighlight(this, e.target);
				//GerrymanderController.prototype.resetHighlight.call(this, e.target);
			},
			click: function(e){
				console.log(this);
				_instance._theMap.fitBounds(e.target.getBounds());
				//_instance.zoomInFeature(e.target);
			}
		});
	},
	highlightFeature: function(layer){
		layer.setStyle({
			weight: 5,
			color: 'brown',
			dashArray: '3',
			fillOpacity: 0.7
			
		});
	},
	resetHighlight: function(stl, layer){
		//console.log(_instance);
		stl.resetStyle(layer);
	},
	zoomInFeature: function(layer){
		_theMap.fitBounds(layer.getBounds());
	},
	stateStyle: function(feature){
		return{
			fillColor: this._setStateColor(feature.properties.density),
			weight: 2,
			opacity: 1,
			color: 'white',
			fillOpacity: 0.7
		};
	}

	

});

function _setStateColor(d){
    return d > 1000 ? '#33241C' :
           d > 500  ? '#814B19' :
           d > 200  ? '#AD6418' :
           d > 100  ? '#DB7C00' :
           d > 50   ? '#FE8500' :
           d > 20   ? '#FCAF6D' :
           d > 10   ? '#FDCFAD' :
                      '#F5E8DE';
}

GerrymanderController.GerrymanderControllerBuilder = {
	_theMap: null,
	_stateBorders: null,
	_districtBorders: null,
	_measures: null,
	
	addMap: function(map){
		this._theMap = map;
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
	build: function(){
		return new GerrymanderController(this);
	}
};
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

function nationalFactory(controlData){
	return L.geoJson(controlData._stateBorders,{
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
				mouseover: function(e){
					layer = e.target;
					layer.setStyle({
						weight: 5,
						color: 'brown',
						dataArray:'',
						fillOpacity: 0.7
					});
				},
				mouseout: function(e){
					controlData._stateLayerGroup.resetStyle(e.target);
				},
				click: function(e){
					controlData._theMap.fitBounds(e.target.getBounds());
				}
			});
		}
	}).addTo(controlData._theMap);
}