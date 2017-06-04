/**
 * Created by Adam Zieliński on 2017-05-13.
 */

app.service("googlePlacesService", function ($http, $q) {

        var deferred = $q.defer();
        $http.get('resources/json/placeTypes.json').then(function (data) {
                deferred.resolve(data);
            }
        );

        this.getGoogleAvailablePlaces = function () {
            return deferred.promise;
        }
    }
);

app.service("busStopsService", function ($http, $q) {

        var deferred = $q.defer();
        $http.get('resources/json/stops_with_lines.json').then(function (data) {
                deferred.resolve(data);
            }
        );

        this.getBusStopPlaces = function () {
            return deferred.promise;
        }
    }
);