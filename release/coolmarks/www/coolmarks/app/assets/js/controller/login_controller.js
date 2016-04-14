var loginForm = angular.module('login', ["ngRoute"]);

loginForm.config(["$routeProvider",function($routeProvider){
  $routeProvider.when("/regist.html", {
    templateUrl: "/regist_form.html",
    controller: 'loginCtrl'
  }).otherwise({
    templateUrl: "/login_form.html",
    controller: 'loginCtrl'
  });
}]);

loginForm.controller('loginCtrl', function($scope, loginService, $http, $window) {
  var user = {name: "", email: "", password: ""};
  $scope.user = user;

  $http.get("/bookmark").success(function(data, status, headers, config) {
    $window.location.href = '/';
  }).error(function(data, status, headers, config) {
  });

  $scope.userLogin = function() {
    loginService.userLogin($scope.user).success(function(data, status, headers, config) {
      $window.location.href = "/index.html";
    }).error(function(data, status, headers, config) {
      alert(data);
    });
  };
  $scope.userRegist = function() {
    if($scope.user.password != $scope.user.password_confirm) {
      alert("password must equals with password_confirm");
      return false;
    }
    loginService.userRegist($scope.user).success(function(data, status, headers, config) {
      $window.location.href = "/index.html";
    }).error(function(data, status, headers, config) {
      alert(data);
    });
  };
});
