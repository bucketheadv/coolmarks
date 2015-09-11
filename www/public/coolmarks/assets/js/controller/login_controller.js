var loginForm = angular.module('login', []);

loginForm.controller('loginCtrl', function($scope, loginService, $http, $window) {
    var user = {name: "", email: "", password: ""};
    $scope.user = user;

    $scope.userLogin = function() {
        loginService.userLogin($scope.user).success(function(data, status, headers, config) {
            $window.location.href = "/index.html";
		}).error(function(data, status, headers, config) {
		    alert(data);
		});
    };
});
