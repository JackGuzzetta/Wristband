var mysqlModel = require('mysql-model');
var MyAppModel = require('./appModel');

var Comment = MyAppModel.extend({
    tableName: "comments",
});

module.exports = Comment;