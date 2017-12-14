define([
	"dojo/dom",
	"dojo/_base/declare", 
	"dojo/on", 
	"dojo/dom-construct", 
	"dojo/dom-style", 
	"dojo/request", 
	"dojo/topic", 
	"leaflet/gerrymander/GerrymanderMapBuilder",
	"leaflet/gerrymander/constants/DataType",
	"leaflet/gerrymander/constants/TopicEvents",
	"dijit/registry"
	], function(dom,declare, on, domConstruct, domStyle, request, topic, GerrymanderMapBuilder, DataType, TopicEvents, registry){
	
	var map;
	
	function buildGerrymanderPage(builder){
		map = GerrymanderMapBuilder.create(builder.map);
		initializeYearSelector(builder.yearSelector);
		initializeElectionDataButton(builder.electionDataBtn);
		initializeEfficiencyGapButton(builder.efficiencyGapBtn);
		initializeDataPanel(builder.dataPanel);
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
				if(yearList[0]){
					GerrymanderMapBuilder.setYear(yearList[0]);
				}
				else{
					yearSelector.addOption(domConstruct.create("option", {label:"No Years Available", value:0}));
					yearSelector.disabled =true;
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
	function initializeDataPanel(panel){
		if (panel){
			topic.subscribe(TopicEvents.DATA_SIDE_PANEL, function(dataType, metricData){
				
				if(dataType==DataType.NONE){
					if(!metricData){
						panel.innerHTML = "Data Not Available";
					}
					else{
						panel.innerHTML = " ";
					}
				}
				else if(dataType == DataType.ELECTION_DATA){
					var displayString="";
					
					displayString += "<h4>State Election Data</h4>";
					displayString += "<b>Seats:</b> <br/>";
					displayString += "Republican: " + metricData.totalRepublicanSeats + "<br/>";
					displayString += "Democrat: " + metricData.totalDemocratSeats + "<br/>";
					displayString += "<br/>";
					displayString += "<b>Votes:</b> <br/>";
					displayString += "Republican: " + metricData.totalRepublicanVotes.toLocaleString() + "<br/>";
					displayString += "Democrat: " + metricData.totalDemocratVotes.toLocaleString() + "<br/>";
					
					
					panel.innerHTML = displayString;
				}
				else if(dataType == DataType.EFFICIENCY_GAP){
					var displayString="";
					displayString += "<h4>Efficiency Gap:</h4>";
					displayString += "Democrat: " + metricData.democratGap.toFixed(2);
					displayString += "<br/>";
					displayString += "Republican: " + metricData.republicanGap.toFixed(2);
					
					displayString += "<h4>Seats:</h4>";
					displayString += "Democrat: " + metricData.totalDemocratSeats + "<br/>";
					displayString += "Republican: " + metricData.totalRepublicanSeats + "<br/>";
					panel.innerHTML = displayString;
				}
			});
			
		}
		
		
	}
	
	
	return {
		builder: {},
		addMap: function(map){
			this.builder.map = dom.byId(map);
			return this;
		},
		addYearSelector:function(select){
			this.builder.yearSelector = registry.byId(select);
			return this;
		},
		addRepTable: function(table){
			this.builder.repTable = dom.byId(table);
			return this;
		},
		addPartyCheckbox: function(checkbox){
			this.buidler.partyCheckbox = dom.byId(checkbox);
			return this;
		},
		addMetricSelectForm: function(form){
			this.builder.metricSelectForm = dom.byId(form);
			return this;
		},
		addDataPanel: function(panel){
			this.builder.dataPanel = dom.byId(panel);
			return this;
		},
		addDefaultModeRadioButton: function(button){
			this.builder.electionDataBtn = dom.byId(button);
			return this;
		},
		addEfficiencyGapRadioButton: function(button){
			this.builder.efficiencyGapBtn = dom.byId(button);
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