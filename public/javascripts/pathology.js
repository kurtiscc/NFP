app = angular.module('pathology', []);

app.controller('MainCtrl', ['$scope', 'urls', function($scope, urls){
	$scope.searchdata = {
		clinic:false,
		doctor:false,
		patient:false,
		text:'',
		cliniclist:{},
		doctorlist:{},
		patientlist:{},
		showresults:false
	};

	urls.getList('clinic').then(function(data){
		$scope.browsedata = data;
	});
	// urls.getList('doctor').then(function(data){
	// 	$scope.searchdata.doctorlist = data;
	// });
	// urls.getList('patient').then(function(data){
	// 	$scope.searchdata.patientlist = data;
	// });
	// urls.getList('sample').then(function(data){
	// 	$scope.searchdata.samplelist = data;
	// });

	$scope.page = {
		curPage :'search',
		setPage : function(a){
			this.curPage = a;
		}
	}

	$scope.getMapTitle = function(a, b, c, d){
		$scope.mapTitle = (a.Name ? a.Name + " > " : "") + (b.Name ? b.Name + " > " : "") + (c.Name ? c.Name + " > " : "") + (d.tagUID ? d.tagUID : "");
	}

	$scope.getClinicChildren = function(clinic){
		urls.getdoctors(clinic.Name).then(function(data){
			clinic.doctors = data;
		});
	}

	$scope.getDoctorChildren = function(doctor){
		urls.getpatients(doctor._id).then(function(data){
			doctor.patients = data;
		});
	}

	$scope.getPatientSamples = function(patient){
		urls.getPatientSamples(patient._id).then(function(data){
			patient.samples = data;
		});
	}

	$scope.search = function(){
		$scope.searchdata.showresults = false;
		urls.search($scope.searchdata).then(function(data){
			$scope.searchdata.cliniclist = {};
			$scope.searchdata.doctorlist = {};
			$scope.searchdata.patientlist = {};
			$scope.searchdata.cliniclist = data.clinics;
			$scope.searchdata.doctorlist = data.doctors;
			$scope.searchdata.patientlist = data.patients;
			$scope.searchdata.showresults = true;
		});
	}

	// function initMap() {
	// 	var mapDiv = document.getElementById('map');
	// 		var map = new google.maps.Map(mapDiv, {
	// 		center: {lat: 40.276423, lng: -111.723700},
	// 		zoom: 12
	// 	});
	// }

	var mapOptions = {
		zoom:10,
		center: new google.maps.LatLng(40.2558333, -111.6379139),
	}

	$scope.startMap = function(){
		$scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);
	}

	$scope.markers = [];

	var createMarker = function(info){
		var marker = new google.maps.Marker({
			map: $scope.map,
			position: new google.maps.LatLng(info.lat, info.lon),
			title:info.addr
		});

		marker.content = '<div class="infoWindowContent">' + info.purpose + '</div>';
        
        google.maps.event.addListener(marker, 'click', function(){
            infoWindow.setContent('<h2>' + marker.title + '</h2>' + marker.content);
            infoWindow.open($scope.map, marker);
        });

		$scope.markers.push(marker);
	}

	$scope.showMarkers = function(input){
		for(var i = 0; i < input.trackingSteps.length; i++){
			createMarker(input.trackingSteps[i]);
		}
	}

	 $scope.openInfoWindow = function(e, selectedMarker){
        e.preventDefault();
        google.maps.event.trigger(selectedMarker, 'click');
    }
	//http://jsfiddle.net/pc7Uu/854/
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

	function getpatients(doctorid){
		var prom = $q.defer();
		$http.get("/getpatients/"+doctorid)
		.success(function(data){prom.resolve(data);})
		.error(function(e){prom.resolve(e);})
		return prom.promise;
	}

	function getPatientSamples(patientid){
		var prom = $q.defer();
		$http.get("/getsamples/"+patientid)
		.success(function(data){prom.resolve(data);})
		.error(function(e){prom.resolve(e);})
		return prom.promise;
	}

	function search(data){
		var prom = $q.defer();
		$http.post("/search", data)
		.success(function(data){prom.resolve(data);})
		.error(function(e){prom.resolve(e);})
		return prom.promise;
	}

	return{
		getList:getList,
		getdoctors:getdoctors,
		getpatients:getpatients,
		getPatientSamples,getPatientSamples,
		search:search
	}
}]);

