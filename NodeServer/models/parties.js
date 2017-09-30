var mysqlModel = require('mysql-model');
var MyAppModel = require('./AppModel');

var Party = MyAppModel.extend({
    tableName: "parties",
});

module.exports = Party;