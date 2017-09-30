var mysqlModel = require('mysql-model');
var MyAppModel = require('./AppModel');

var User = MyAppModel.extend({
    tableName: "users",
});

module.exports = User;