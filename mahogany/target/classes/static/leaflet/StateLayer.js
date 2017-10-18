var StateLayer = L.Class.extend({
	/*stateMap: null,
	statesData: null,
	
	setMap: function(map){
		stateMap = L.map(map).setView([38, -88], 4);
		return this;
	},

	setStatesData: function(geoJSON){
		this.statesData = geoJSON;
		return this;
	},
	
	build: function(){
		if(stateMap == null){
			
		}
		else if(statesData == null){
			
		}
		else{
			L.esri.basemapLayer('Topographic').addTo(stateMap);
			this.geoJson(statesData, {
			    style: style,
			    onEachFeature: this.onEachFeature
			}).addTo(stateMap);
		}
	},
	setColor: function(d) {
	    return d > 1000 ? '#33241C' :
	           d > 500  ? '#814B19' :
	           d > 200  ? '#AD6418' :
	           d > 100  ? '#DB7C00' :
	           d > 50   ? '#FE8500' :
	           d > 20   ? '#FCAF6D' :
	           d > 10   ? '#FDCFAD' :
	                      '#F5E8DE';
	},

	style: function(feature) {
	    return {
	        fillColor: setColor(feature.properties.density),
	        weight: 2,
	        opacity: 1,
	        color: 'white',
	        fillOpacity: 0.7
	    };
	},
	
	onEachFeature: function(feature, layer) {
	    layer.on({
	        mouseover: myHighlightFeature,
	        mouseout: function(e){
	        	statesData.resetStyle(e.target);
	        }
	        click: function(e){
	        	statemap.fitBounds(e.target.getBounds());
	        }
	    });
	}*/
	
});

function resetHighlight(e) {
    statesData.resetStyle(e.target);
}

function myZoomToFeature(e){
	stateMap.fitBounds(e.target.getBounds());
}

function myHighlightFeature(e){
    var layer = e.target;

    layer.setStyle({
        weight: 5,
        color: 'brown',
        dashArray: '',
        fillOpacity: 0.7
    });

    if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
        layer.bringToFront();
    }
}