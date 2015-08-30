angular.module('siny')
       .factory('sinyService', ['$http', function($http){
            var getBookMarks = function() {
                return $http.get("/bookmark");
            };

            return {
                getBookMarks: getBookMarks
            }
       }])