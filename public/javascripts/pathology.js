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

	$scope.getClinicChildren = function(clinic){
		urls.getdoctors(clinic.Name).then(function(data){
			clinic.doctors = data;
		});
	}

	$scope.getDoctorChildren = function(doctor){
		urls.getpatients(doctor.Name).then(function(data){
			doctor.patients = data;
		});
	}
	
}]);

app.factory('urls', ['$http', '$q', function($http, $q){
	function getList(a){
		var prom = $q.defer();
		$http.get("/getlist/"+a)
		.success(function(data){prom.resolve(data);})
		.error(function(e){prom.resolve(e);})
		return prom.promise;
	}	

	function getdoctors(clinicname){
		var prom = $q.defer();
		$http.get("/getdoctors/"+clinicname)
		.success(function(data){prom.resolve(data);})
		.error(function(e){prom.resolve(e);})
		return prom.promise;
	}

	function getpatients(doctorname){
		var prom = $q.defer();
		$http.get("/getpatients/"+doctorname)
		.success(function(data){prom.resolve(data);})
		.error(function(e){prom.resolve(e);})
		return prom.promise;
	}

	return{
		getList:getList,
		getdoctors:getdoctors,
		getpatients:getpatients
	}
}]);

