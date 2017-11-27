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
	
	var map;
	
	function buildGerrymanderPage(builder){
		map = GerrymanderMapBuilder.create(builder.map);
		initializeYearSelector(builder.yearSelector);
		initializeElectionDataButton(builder.electionDataBtn);
		initializeEfficiencyGapButton(builder.efficiencyGapBtn);
		
	}
	function initializeYearSelector(yearSelector){
		//GerrymanderMapBuilder.setYear(yearSelector.value);
		on(registry.byId(yearSelector), "change", function(){
			GerrymanderMapBuilder.setYear(yearSelector.value);
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
	}
	function initializeEfficiencyGapButton(button){
		on(button, "click", function(e){
			GerrymanderMapBuilder.setDataMode(DataType.EFFICIENCY_GAP);
		});
	}
	function initializeElectionDataButton(button){
		on(button, "click", function(e){
			GerrymanderMapBuilder.setDataMode(DataType.ELECTION_DATA);
		});
	}
	
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
			this.builder.electionDataBtn = button;
			return this;
		},
		addEfficiencyGapRadioButton: function(button){
			this.builder.efficiencyGapRadioBtn = button;
			this.builder.efficiencyGapBtn = button;
			return this;
		},
		build: function(){
			//create copy of builder
			var builderCopy = this.builder;
			buildGerrymanderPage(builderCopy);
			//return new GerrymanderPageManager(builderCopy);
			return map;
		},
		getMap: function(){
			return map;
		}
	} 
});