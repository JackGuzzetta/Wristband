var mysqlModel = require('mysql-model');
var MyAppModel = require('./appModel');

var Relation = MyAppModel.extend({
    tableName: "party_relation",
});

module.exports = Relation;