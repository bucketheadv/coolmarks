angular.module('login')
.factory('loginService', ['$http', function($http){
  var userLogin = function(user) {
    return $http.post('/login.html', user);
  };

  var userRegist = function(user) {
    return $http.post('/register.html', user);
  };

  return {
    userLogin: userLogin,
    userRegist: userRegist
  }
}])
