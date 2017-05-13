/**
 * Created by Adam Zieliński on 2017-05-13.
 */
app.directive('myInput', function() {
    return {
        restrict: 'E',
        template: "<label>User:<input type='text' ng-model='username'></label><button ng-click='getGitHubUser(username)'>GO</button>"
    };
});