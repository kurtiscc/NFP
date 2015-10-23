app = angular.module('pathology', []);

app.controller('MainCtrl', ['$scope', 'urls', function($scope, urls){
	$scope.test = "This is just a test";
}]);

app.factory('urls', ['$http', '$q', function($http, $q){
	return{}
}]);

