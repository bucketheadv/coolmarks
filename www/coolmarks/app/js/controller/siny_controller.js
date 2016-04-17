var siny = angular.module('siny', ['LocalStorageModule']);

siny.config(function (localStorageServiceProvider) {
  localStorageServiceProvider
  .setPrefix('coolmarks')
  .setStorageType('sessionStorage')
  .setNotify(true, true)
});

siny.controller('sinyCtrl', function($scope, $http, $window, sinyService, localStorageService) {
  var bookMarkName = $scope.bookMarkName;
  var bookMarkUrl = $scope.bookMarkUrl;

  $scope.user = { bookmark: [] };

  sinyService.getBookMarks(getBookMarksSuccess, getBookMarksFail)

  $scope.append = function (user, bookMarkName, bookMarkUrl, bookMarkTab) {
    var bm = {'url': bookMarkUrl, 'name': bookMarkName};
    var tab = angular.fromJson(bookMarkTab);
    sinyService.postBookMark(user, bookMarkName, bookMarkUrl, tab);
  };

  $scope.addTab = function (user, tabName) {
    sinyService.postTab(tabName, user);
  };

  $scope.removeMarkItem = function(user, tab, index) {
    var bookMarkId = sinyService.removeBookMark(user.bookmark, tab, index)
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
