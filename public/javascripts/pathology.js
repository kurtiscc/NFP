app = angular.module('pathology', []);

app.controller('MainCtrl', ['$scope', 'urls', function($scope, urls){
	$scope.searchdata = {
		cliniclist:[],
		doctorlist:[],
		patientlist:[],
	};

	urls.getList('clinic').then(function(data){
		$scope.searchdata.cliniclist = data;
	});
	urls.getList('doctor').then(function(data){
		$scope.searchdata.doctorlist = data;
	});
	urls.getList('patient').then(function(data){
		$scope.searchdata.patientlist = data;
	});
	
}]);

app.factory('urls', ['$http', '$q', function($http, $q){
	function getList(a){
		var prom = $q.defer();
		$http.get("/getlist/"+a)
		.success(function(data){prom.resolve(data);})
		.error(function(e){prom.resolve(e);})
		return prom.promise;
	}	


	return{
		getList:getList
	}
}]);

