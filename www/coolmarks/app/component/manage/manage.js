(function(angular) {
  'use strict';
function ManageController($scope, $element, $attrs, sinyService) {
  $scope.user = this.user;
  $scope.append = function (user, bookMarkName, bookMarkUrl, bookMarkTab) {
    var bm = {'url': bookMarkUrl, 'name': bookMarkName};
    var tab = angular.fromJson(bookMarkTab);
    sinyService.postBookMark(user, bookMarkName, bookMarkUrl, tab);
  };

  $scope.addTab = function (user, tabName) {
    debugger;
    sinyService.postTab(tabName, user);
  };
}

angular.module('siny').component('manage', {
  templateUrl: '/component/manage/manage.html',
  controller: ManageController,
  bindings: {
    user: '='
  }
});
})(window.angular);