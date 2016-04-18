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

  $scope.removeMarkItem = function(user, tab, bookmark) {
    var bookMarkId = sinyService.removeBookMark(user.bookmark, tab, bookmark)
    sinyService.deleteBookMark(bookMarkId);
  }

  function getBookMarksSuccess(data) {
    $scope.user.bookmark = data;
    $scope.user.bookmark.push({"name": "My Indeed", id: "myIndeed"});
  }
  function getBookMarksFail() {
    $window.location.href = '/login.html';
  }
});
