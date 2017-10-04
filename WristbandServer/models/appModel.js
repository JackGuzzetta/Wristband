var config = require('../config');
var mysqlModel = require('mysql-model');
module.exports = mysqlModel.createConnection({
  host     : config.mysql.host,
  user     : config.mysql.user,
  password : config.mysql.password,
  database : config.mysql.database
});