define(["leaflet/LoginPageManager"], function(){
	return{
		userName: null,
		password: null,
		submit: null,
		reset: null,
		
		addUserName: function(userName){
			this.userName = userName;
		},
		addPassword: function(password){
			this.password = password;
		}
	
		
	};
	
	
});