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

  $http.get("/bookmark").success(function(data, status, headers, config) {
    $scope.user.bookmark = data;
    $scope.user.bookmark["My Indeed"] = { "id": "tracemarker_indeed_id"};
    localStorageService.set("chengpohi", $scope.user);
  }).error(function(data, status, headers, config) {
    $scope.user = getItem("chengpohi");
    $window.location.href = '/login.html';
  });

  $scope.append = function (user, bookMarkName, bookMarkUrl, bookMarkTab) {
    var bm = {'url': bookMarkUrl, 'name': bookMarkName};
    var bmt = angular.fromJson(bookMarkTab);
    user.bookmark[bmt.name].marks.push(bm);

    postBookMark(bookMarkName, bookMarkUrl, bmt.id);

    $scope.bookMarkName = '';
    $scope.bookMarkUrl = '';
    $scope.bookMarkTab = '';
  };

  $scope.addTab = function (user, tabName) {
    postTab(tabName, user);
  };

  $scope.removeMarkItem = function(user, tab, index) {
    var bookMarkId = user.bookmark[tab]["marks"][index].id;
    user.bookmark[tab]["marks"].splice(index, 1);
    deleteBookMark(bookMarkId);
  }

  function getItem(key) {
    return localStorageService.get(key);
  }

  function postTab(tabName, user) {
    $http.post('/tab', {'name': tabName}).success(function(data, status, headers, config) {
      user.bookmark[tabName] = {"marks": [], "id": data};
      alert("Add " + tabName + " Success");
    }).error(function(data, status, headers, config) {
      alert(data);
    });
  }

  function postBookMark(bookMarkName, bookMarkUrl, bookMarkTabId) {
    $http.post('/bookmark', {'name': bookMarkName, 'url': bookMarkUrl, 'tabId': bookMarkTabId}).success(function(data, status, headers, config) {
      alert("create " + bookMarkName + " success");
    })
    .error(function(data, status, headers, config) {
      alert(data);
    });
  }

  function deleteBookMark(bookMarkId) {
    $http.delete('/bookmark/' + bookMarkId).success(function(data, status, headers, config) {
      alert("delete success");
    })
    .error(function(data, status, headers, config) {
      alert(data);
    });
  }
});
