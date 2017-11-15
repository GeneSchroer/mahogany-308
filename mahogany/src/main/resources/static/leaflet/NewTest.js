define(["dojo/_base/declare", "dojo/dom-style", "dojo/request"], function(declare, domStyle, request){
	var GerrymanderMapManager = L.Class.extend({
		_map: null,
		_stateMapLayer: null,

		initialize: function(map){
			this._constructGerrymanderMap(map);
			this._getStateMapRequest();
		},
		_constructGerrymanderMap:function(map){
			this._map = this._createMap(map);
			this._zoomOutBtn = this._createZoomOutBtn;
			this._zoomOutBtn.addTo(this._map);
			this._createStateLayer();
		},
		_createMap: function(map){
			domStyle.set(map, "width", "100%");
			domStyle.set(map, "height", "100%");
			domStyle.set(map, "position", "absolute");
			return L.map(map, {
				zoomControl: false,
				dragging: false,
				scrollWheelZoom: false,
			}).setView([32, -80], 4);
		},