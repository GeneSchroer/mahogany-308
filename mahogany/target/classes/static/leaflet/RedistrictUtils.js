function changeColorOfFeatureTo(){}
function setToSelectedSuperdistrict(){}
function setToUnselectedSuperdistrict(){}

define([], function(){
	var superdistrictColor = 0;
	
	function getSuperdistrictColor(){
		superdistrictColor = (superdistrictColor++) % 4;
		
		return superdistrictColor == 0 ? "red" :
				superdistrictColor == 1 ? "blue" :
						superdistrictColor == 2 ? "green" :
								"yellow";
	}
	
	return {
		
		stateLayerEvents: function(mapData){
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
		},
		getDistrictsRequest: function(mapData){
			
			var jsonStatefile = mapData.state + mapData.year + ".json"; 
			
			request(jsonStatefile, {
				handleAs: 'json'
			}).response.then(function(success){
				topic.publish("redistrict/getDistricts/success", success);
				mapData.districtData = success.data;
				mapData.districtLayer.clearLayers();
				mapData.districtLayer.addData(mapData.districtData);
				
			});
		},
		districtLayerStyle: function(mapData){
			return function(feature){
				//console.log(mapData);
				return{
					weight: 0.5,
					color: "white",
					fillColor: "#464646",
					dataArray: ' ',
					fillOpacity: 0.8
				};
			};
		},
		districtLayerHighlightStyle: function(mapData){
				//console.log(mapData);
				return{
					weight: 0.5,
					color: "white",
					fillColor: "#949394",
					dataArray: ' ',
					fillOpacity: 0.8
				};
			
		},
		createSuperdistrictStyle: function(mapData){
			return{
				weight: 0.5,
				color: "white",
				fillColor: "#b4a1a1",
				dataArray: ' ',
				fillOpacity: 0.8
			};
		},
		
		superdistrictLayerStyle: function(mapData){
			return function(feature){
				return{
					weight: 0.5,
					fillColor: getSuperdistrictColor(),
					dataArray: ' ',
					fillOpacity: 1,
					color: "white"
				};
			};
		},
		superdistrictHighlightStyle: function(mapData){
			return{
				weight: 0.5,
				fillColor: "lime",
				dataArray: ' ',
				fillOpacity: 0.8,
				color: "white"
			}
		},
		superdistrictLayerEvents: function(mapData){
			return function(feature, layer){
				layer.on({
					click:function(e){
					
						console.log("called as superdistrictEvent")
					},
					mouseover: function(e){
						console.log("mouseover");
						layer.setStyle({
						//	fillColor:"blue",
							dataArray: ' '
						})
					}
				});
				
			};
		},
		
		
		
		
		
		
	};
	
});
