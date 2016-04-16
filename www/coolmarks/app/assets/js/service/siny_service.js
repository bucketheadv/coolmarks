angular.module('siny')
.factory('sinyService', ['$http', function($http){
  var getBookMarks = function(afterSuccess, afterFail) {
    $http.get("/bookmark").success(function(data, status, headers, config) {
      afterSuccess(data);
    }).error(function(data, status, headers, config) {
      afterFail();
    });
  };

  var statistic = function (query) {
    $http.post('/statistic', {"query": query}).success(function(data, status, headers, config) {
      alert(data)
    })
    .error(function(data, status, headers, config) {
      alert(data);
    });
  };

  var postBookMark = function (user, bookMarkName, bookMarkUrl, tab) {
    $http.post('/bookmark', {'name': bookMarkName, 'url': bookMarkUrl, 'tabId': tab.id}).success(function(data, status, headers, config) {
      appendBookMark(user.bookmark, tab.name, data);
      alert("create " + bookMarkName + " success");
    })
    .error(function(data, status, headers, config) {
      alert(data);
    });
  };

  var deleteBookMark = function (bookMarkId) {
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

  var removeBookMark = function (bookmarks, tabName, index) {
    for (var i in bookmarks) {
      if (bookmarks[i].name === tabName) {
        var bookMarkId = bookmarks[i].bookmark[index].id
        bookmarks[i].bookmark.splice(index, 1);
        return bookMarkId;
      }
    }
    return -1;
  };


  var postTab = function (tabName, user) {
    $http.post('/tab', {'name': tabName}).success(function(data, status, headers, config) {
      data["bookmark"] = [];
      user.bookmark.push(data);
      alert("Add " + tabName + " Success");
    }).error(function(data, status, headers, config) {
      alert(data);
    });
  }

  return {
    getBookMarks: getBookMarks,
    statistic: statistic,
    postBookMark: postBookMark,
    deleteBookMark: deleteBookMark,
    removeBookMark: removeBookMark,
    postTab: postTab
  }
}])
