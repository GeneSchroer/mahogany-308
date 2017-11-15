package mahogany;

public enum StateNamesEnum {
	Alabama,
	Alaska,
	Arizona,
	Arkansas,
	California,
	Colorado,
	Connecticut,
	Delaware,
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
				return "Puerto_Rico";
			default:
				return this.name();
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
