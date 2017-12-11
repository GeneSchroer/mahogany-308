package mahogany.utils;

public enum StateNamesEnum {
	Alabama{
		public int getStateCode() {
			return 1;
		}
	},
	Alaska{
		public int getStateCode() {
			return 2;
		}
	},
	Arizona{
		public int getStateCode() {
			return 3;
		}
	},
	Arkansas{
		public int getStateCode() {
			return 5;
		}
	},
	California{
		public int getStateCode() {
			return 6;
		}
	},
	Colorado{
		public int getStateCode() {
			return 8;
		}
	},
	Connecticut{
		public int getStateCode() {
			return 9;
		}
	},
	Delaware{
		public int getStateCode() {
			return 10;
		}
	},
	District_Of_Columbia,
	Florida,
	Georgia,
	Hawaii,
	Idaho,
	Illinois,
	Indiana,
	Iowa,
	Kansas,
	Kentucky,
	Louisiana,
	Maine,
	Maryland,
	Massachusetts,
	Michigan,
	Minnesota,
	Mississippi,
	Missouri,
	Montana,
	Nebraska,
	Nevada,
	New_Hampshire,
	New_Jersey,
	New_Mexico,
	New_York,
	North_Carolina,
	North_Dakota,
	Ohio,
	Oklahoma,
	Oregon,
	Pennsylvania,
	Rhode_Island,
	South_Carolina,
	South_Dakota,
	Tennessee,
	Texas,
	Utah,
	Vermont,
	Virginia,
	Washington,
	West_Virginia,
	Wisconsin,
	Wyoming,
	Puerto_Rico;
	
	public int getStateCode() {
		return this.getStateCode();
	}
	public String getName() {
		switch(this) {
			case District_Of_Columbia:
				return "District Of Columbia";
			case New_Hampshire:
				return "New Hampshire";
			case New_Jersey:
				return "New Jersey";
			case New_Mexico:
				return "New Mexico";
			case New_York:
				return "New York";
			case North_Carolina:
				return "North Carolina";
			case North_Dakota:
				return "North Dakota";
			case Rhode_Island:
				return "Rhode Island";
			case South_Carolina:
				return "South Carolina";
			case South_Dakota:
				return "South Dakota";
			case West_Virginia:
				return "West Virginia";
			case Puerto_Rico:
				return "Puerto Rico";
			default:
				return this.toString();
		}
	}

	public static StateNamesEnum getByStateCode(Integer stateCode) {
		switch(stateCode) {
		case 1:
			return Alabama;
		case 2:
			return Alaska;
		case 4:
			return Arizona;
		case 5:
			return Arkansas;
		case 6: 
			return California;
		case 8:
			return Colorado;
		case 9: 
			return Connecticut;
		case 10:
			return Delaware;
		case 11:
			return District_Of_Columbia;
		case 12:
			return Florida;
		case 13:
			return Georgia;
		case 15: 
			return Hawaii;
		case 16:
			return Idaho;
		case 17:
			return Illinois;
		case 18:
			return Indiana;
		case 19:
			return Iowa;
		case 20:
			return Kansas;
		case 21:
			return Kentucky;
		case 22:
			return Louisiana;
		case 23:
			return Maine;
		case 24:
			return Maryland;
		case 25:
			return Massachusetts;
		case 26:
			return Michigan;
		case 27:
			return Minnesota;
		case 28:
			return Mississippi;
		case 29:
			return Missouri;
		case 30:
			return Montana;
		case 31:
			return Nebraska;
		case 32:
			return Nevada;
		case 33:
			return New_Hampshire;
		case 34:
			return New_Jersey;
		case 35:
			return New_Mexico;
		case 36:
			return New_York;
		case 37:
			return North_Carolina;
		case 38:
			return North_Dakota;
		case 39:
			return Ohio;
		case 40:
			return Oklahoma;
		case 41: 
			return Oregon;
		case 42:
			return Pennsylvania;
		case 44:
			return Rhode_Island;
		case 45:
			return South_Carolina;
		case 46:
			return South_Dakota;
		case 47:
			return Tennessee;
		case 48:
			return Texas;
		case 49:
			return Utah;
		case 50:
			return Vermont;
		case 51:
			return Virginia;
		case 53:
			return Washington;
		case 54: 
			return West_Virginia;
		case 55:
			return Wisconsin;
		case 56:
			return Wyoming;
		case 72:
			return Puerto_Rico;
		default:
			return null;
		}
		
	}
	
	public Integer getCode() {
		
		switch(this) {
			case Alabama:
				return 1;
			case Alaska:
				return 2;
			case Arizona:
				return 4;
			case Arkansas:
				return 5;
			case California:
				return 6;
			case Colorado:
				return 8;
			case Connecticut:
				return 9;
			case Delaware:
				return 10;
			case District_Of_Columbia:
				return 11;
			case Florida: 
				return 12;
			case Georgia:
				return 13;
			case Hawaii:
				return 15;
			case Idaho:
				return 16;
			case Illinois:
				return 17;
			case Indiana:
				return 18;
			case Iowa:
				return 19;
			case Kansas: 
				return 20;
			case Kentucky:
				return 21;
			case Louisiana:
				return 22;
			case Maine:
				return 23;
			case Maryland:
				return 24;
			case Massachusetts:
				return 25;
			case Michigan:
				return 26;
			case Minnesota:
				return 27;
			case Mississippi:
				return 28;
			case Missouri:
				return 29;
			case Montana:
				return 30;
			case Nebraska:
				return 31;
			case Nevada:
				return 32;
			case New_Hampshire:
				return 33;
			case New_Jersey:
				return 34;
			case New_Mexico:
				return 35;
			case New_York:
				return 36;
			case North_Carolina:
				return 37;
			case North_Dakota:
				return 38;
			case Ohio:
				return 39;
			case Oklahoma:
				return 40;
			case Oregon:
				return 41;
			case Pennsylvania:
				return 42;
			case Rhode_Island:
				return 44;
			case South_Carolina:
				return 45;
			case South_Dakota:
				return 46;
			case Tennessee:
				return 47;
			case Texas:
				return 48;
			case Utah:
				return 49;
			case Vermont:
				return 50;
			case Virginia:
				return 51;
			case Washington:
				return 53;
			case West_Virginia:
				return 54;
			case Wisconsin:
				return 55;
			case Wyoming:
				return 56;
			case Puerto_Rico:
				return 72;
			default:
				return null;
		}
	}
	
}
