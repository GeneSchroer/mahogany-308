define([
	"dojo/_base/declare", 
	"dojo/on", 
	"dojo/dom-construct", 
	"dojo/dom-style", 
	"dojo/request", 
	"dojo/topic", 
	"leaflet/gerrymander/GerrymanderMapBuilder2",
	"leaflet/gerrymander/constants/DataType",
	"dijit/registry"
	], function(declare, on, domConstruct, domStyle, request, topic, GerrymanderMapBuilder, DataType, registry){
	
	
	var GerrymanderPageManager = declare(null, {
		_pageElements: null,
		
		constructor: function(builder){
			this._pageElements = builder;
			
			
			this._pageElements.mapManager = GerrymanderMapBuilder.create(this._pageElements.map);
			this._initializeYearSelector(this._pageElements);
			this._initializeRepTable(this._pageElements);
			this._initializePartyCheckbox(this._pageElements);
			this._initializeDefaultModeButton(this._pageElements);
			this._initializeEfficiencyGapButton(this._pageElements);
		},
		
		_initializeYearSelector: function(pageElements){
			var yearSelector = pageElements.yearSelector;
			GerrymanderMapBuilder.setYear(pageElements.yearSelector.value);
			on(registry.byId(pageElements.yearSelector), "change", function(){
				GerrymanderMapBuilder.setYear(pageElements.yearSelector.value);
			});
			
			request("/getYears",{
					method:"GET",
					handleAs: "json"
				}).response.then(function(success){
					var yearList = success.data;
					console.log(yearList);
					
					//yearList.sort();
					
					for(year in yearList){
						var option = domConstruct.create("option", {label:yearList[year], value: yearList[year]});
						yearSelector.addOption(option);
					}
					if(year[0]){
						GerrymanderMapBuilder.setYear(yearList[0]);
					}
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
					var member = districts[count].properties.member[pageElements.yearSelector.value];
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
		_initializeDefaultModeButton: function(pageElements){
			var defaultModeRadioBtn = pageElements.defaultModeRadioBtn;
			on(defaultModeRadioBtn, "click", function(e){
				GerrymanderMapBuilder.setDataMode(DataType.ELECTION_DATA);
			});
		},
		_initializeEfficiencyGapButton: function(pageElements){
			var efficiencyGapRadioBtn = pageElements.efficiencyGapRadioBtn;
			on(efficiencyGapRadioBtn, "click", function(e){
				GerrymanderMapBuilder.setDataMode(DataType.EFFICIENCY_GAP);
			});
		}
		
		
		
	});
	
	
	return {
		builder: {},
		addMap: function(map){
			this.builder.map = map;
			return this;
		},
		addYearSelector:function(select){
			this.builder.yearSelector = select;
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