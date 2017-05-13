/**
 * Created by Adam Zieliński on 2017-05-13.
 */

app.filter('removeAllSpaces', function () {
    return function(str) {
        return str.replace(/\s/g, '');
    };
});