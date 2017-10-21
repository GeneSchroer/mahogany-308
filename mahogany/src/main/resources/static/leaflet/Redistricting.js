var RedistrictingMapController = L.Class.extend({
	
	_map: null,
	_stateBorders: null,
	_districts: null,
	_stateLayerGroup: null,
	initialize: function(map, states){
		this._mapData = map;
		this._stateData = states;
		_stateLayerGroup = L.geoJson(_stateData,{
			style: zoomedOutStyle,
			onEachFeature: zoomedOutFeature
		}).addTo(_mapData);
	}
	
	
	
});
