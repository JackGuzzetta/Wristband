var mysqlModel = require('mysql-model');
var MyAppModel = require('./appModel');

var User = MyAppModel.extend({
    tableName: "users",
});

module.exports = User;