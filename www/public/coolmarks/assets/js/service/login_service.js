angular.module('login')
.factory('loginService', ['$http', function($http){
  var userLogin = function(user) {
    return $http.post('/login.html', user);
  };

  return {
    userLogin: userLogin
  }
}])
