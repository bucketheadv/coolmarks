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
    $scope.user.bookmark.push({"name": "My Indeed", id: "myIndeed"});
    localStorageService.set("chengpohi", $scope.user);
  }).error(function(data, status, headers, config) {
    $scope.user = getItem("chengpohi");
    $window.location.href = '/login.html';
  });

  $scope.append = function (user, bookMarkName, bookMarkUrl, bookMarkTab) {
    var bm = {'url': bookMarkUrl, 'name': bookMarkName};
    var tab = angular.fromJson(bookMarkTab);

    postBookMark(user, bookMarkName, bookMarkUrl, tab);

    $scope.bookMarkName = '';
    $scope.bookMarkUrl = '';
    $scope.bookMarkTab = '';
  };

  $scope.addTab = function (user, tabName) {
    postTab(tabName, user);
  };

  $scope.removeMarkItem = function(user, tab, index) {
    var bookMarkId = removeBookMark(user.bookmark, tab, index)
    deleteBookMark(bookMarkId);
  }

  function getItem(key) {
    return localStorageService.get(key);
  }

  function postTab(tabName, user) {
    $http.post('/tab', {'name': tabName}).success(function(data, status, headers, config) {
      data["bookmark"] = [];
      user.bookmark.push(data);
      alert("Add " + tabName + " Success");
    }).error(function(data, status, headers, config) {
      alert(data);
    });
  }

  function postBookMark(user, bookMarkName, bookMarkUrl, tab) {
    $http.post('/bookmark', {'name': bookMarkName, 'url': bookMarkUrl, 'tabId': tab.id}).success(function(data, status, headers, config) {
      appendBookMark(user.bookmark, tab.name, data);
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

  function appendBookMark(bookmarks, tabName, value) {
    for (var i in bookmarks) {
      if (bookmarks[i].name === tabName) {
        bookmarks[i].bookmark.push(value);
      }
    }
  }

  function removeBookMark(bookmarks, tabName, index) {
    for (var i in bookmarks) {
      if (bookmarks[i].name === tabName) {
        var bookMarkId = bookmarks[i].bookmark[index].id
        bookmarks[i].bookmark.splice(index, 1);
        return bookMarkId;
      }
    }
    return -1;
  }

});
