var mysqlModel = require('mysql-model');
var MyAppModel = require('./appModel');

var Party = MyAppModel.extend({
    tableName: "parties",
});

module.exports = Party;