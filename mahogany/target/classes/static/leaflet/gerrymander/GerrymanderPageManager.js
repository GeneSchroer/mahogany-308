define(["dojo/_base/declare", "dojo/on", "dojo/dom-construct", "dojo/dom-style", "dojo/request", "dojo/topic", "leaflet/gerrymander/GerrymanderMapManager", "dijit/registry"], 
		function(declare, on, domConstruct, domStyle, request, topic, GerrymanderMapManager, registry){
	
	
	var GerrymanderPageManager = declare(null, {
		_pageElements: null,
		_mapManager: null,
		_yearSelector: null,
		
		constructor: function(builder){
			this._pageElements = builder;
			
			
			this._pageElements.mapManager = GerrymanderMapManager.create(this._pageElements.map);
			this._initializeCongressSelector(this._pageElements);
			this._initializeRepTable(this._pageElements);
			this._initializePartyCheckbox(this._pageElements);
			//this._initializeMetricSelectForm(this._pageElements);
			this._initializeDefaultModeButton(this._pageElements);
			this._initializeEfficiencyGapButton(this._pageElements);
		},
		
		_initializeCongressSelector: function(pageElements){
			pageElements.mapManager.setCongress(pageElements.congressSelector.value);
			on(registry.byId(pageElements.congressSelector), "change", function(){
				console.log("selected");
				pageElements.mapManager.setCongress(pageElements.congressSelector.value);
				pageElements.mapManager.updateMap();
			});
			
		},
		_initializeRepTable: function(pageElements){
			var table = pageElements.repTable;
			var header = domConstruct.create("tr", {}, table);
			var districts;
			var count;
			domConstruct.create("th", {innerHTML:"Name"}, header);
			domConstruct.create("th", {innerHTML:"District"}, header);
			domConstruct.create("th", {innerHTML:"Party"}, header);
			topic.subscribe("gerrymander/getDistricts/success", function(response){
				domConstruct.empty(table);
				header = domConstruct.create("tr", {}, table);
				domConstruct.create("th", {innerHTML:"Name"}, header);
				domConstruct.create("th", {innerHTML:"District"}, header);
				domConstruct.create("th", {innerHTML:"Party"}, header);
				districts = response.data.features;
				//console.log(districts);
				for(count = 0; count < districts.length; count = count + 1){
					var member = districts[count].properties.member[pageElements.congressSelector.value];
					//console.log(member);
					for (column in member){
						//console.log(column);
						var row = domConstruct.create("tr", {}, table);
						domConstruct.create("td", {innerHTML: member[column].name}, row);
						domConstruct.create("td", {innerHTML: member[column].district}, row);
						domConstruct.create("td", {innerHTML: member[column].party}, row);
					}
				}
			});
		},
		_initializePartyCheckbox: function(){
			
		},
		_initializeMetricSelectForm: function(pageElements){
			var metricRadioButtons = pageElements.metricSelectForm;
			on(metricRadioButtons, "click", function(e){
				pageElements.mapManager.setMetric(metricRadioButtons.metric.value);
			});
			
		},
		_initializeDefaultModeButton: function(pageElements){
			var defaultModeRadioBtn = pageElements.defaultModeRadioBtn;
			on(defaultModeRadioBtn, "click", function(e){
				pageElements.mapManager.setMetricToDefaultMode();
			});
		},
		_initializeEfficiencyGapButton: function(pageElements){
			var efficiencyGapRadioBtn = pageElements.efficiencyGapRadioBtn;
			on(efficiencyGapRadioBtn, "click", function(e){
				pageElements.mapManager.setMetricToEfficiencyGap();
			});
		}
		
		
		
	});
	
	
	return {
		builder: {},
		map: null,
		congressSelector: null,
		repTable: null,
		partyCheckbox: null,
		metricSelectForm: null,
		defaultModeRadioBtn: null,
		efficiencyGapRadioBtn: null,
		addMap: function(map){
			this.builder.map = map;
			return this;
		},
		addCongressSelector:function(select){
			this.builder.congressSelector = select;
			return this;
		},
		addRepTable: function(table){
			this.builder.repTable = table;
			return this;
		},
		addPartyCheckbox: function(checkbox){
			this.buidler.partyCheckbox = checkbox;
			return this;
		},
		addMetricSelectForm: function(form){
			this.builder.metricSelectForm = form;
			return this;
		},
		addDefaultModeRadioButton: function(button){
			this.builder.defaultModeRadioBtn = button;
			return this;
		},
		addEfficiencyGapRadioButton: function(button){
			this.builder.efficiencyGapRadioBtn = button;
			return this;
		},
		build: function(){
			//create copy of builder
			var builderCopy = this.builder;
			return new GerrymanderPageManager(builderCopy);
		}
	} 
});