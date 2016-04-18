(function(angular) {
  'use strict';
function BookMarkController($scope, $element, $attrs, sinyService) {
  $scope.user = this.user;
  $scope.removeMarkItem = function(user, tab, bookmark) {
    var bookMarkId = sinyService.removeBookMark(user.bookmark, tab, bookmark)
    sinyService.deleteBookMark(bookMarkId);
  }
}

angular.module('siny').component('bookmark', {
  templateUrl: '/component/bookmark/bookmark.html',
  controller: BookMarkController,
  bindings: {
    user: '='
  }
});
})(window.angular);
