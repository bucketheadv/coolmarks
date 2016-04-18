var siny = angular.module('siny', ['LocalStorageModule']);

siny.config(function (localStorageServiceProvider) {
  localStorageServiceProvider
  .setPrefix('coolmarks')
  .setStorageType('sessionStorage')
  .setNotify(true, true)
});

siny.controller('sinyCtrl', function($scope, $http, $window, sinyService, localStorageService) {
  $scope.user = { bookmark: [] };
  sinyService.getBookMarks(getBookMarksSuccess, getBookMarksFail)
  function getBookMarksSuccess(data) {
    $scope.user.bookmark = data;
    $scope.user.bookmark.push({"name": "My Indeed", id: "myIndeed"});
  }
  function getBookMarksFail() {
    $window.location.href = '/login.html';
  }
});
