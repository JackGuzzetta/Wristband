//https://www.npmjs.com/package/mysql-model
var express = require('express');
var mysqlModel = require('mysql-model');
var app = express();
var config = require('./config');
var jwt = require('jwt-simple');
var moment = require('moment');
var User = require('./controllers/user_controller')(app);
var Party = require('./controllers/party_controller');
var serveIndex = require('serve-index');

require('./routes')(app);
app.set('jwtTokenSecret', config.crypt);
app.use(express.static(__dirname + "/"))
app.use('/images', serveIndex(__dirname + '/images'));

//EXAMPLE CRUD (CREATE/READ/UPDATE/DELETE) Operations

//----------PARTY-------------
//Party.createParty("test", "2017-12-25", "14:00", 0, 100 , 0, "mvanbosc");
//console.log(Party.findPartyByID(2));
//Party.deleteParty(1);
//Party.updateParty(2,"die", "2017-12-25", "14:00", 0, 100 , 0, "mvanbosc");
//----------------------------

//----------User-------------
//User.createUser("Mike", "Van Bosch", "mvanbosc", "test", "mvanbosc@iastate.edu");
//console.log(User.findUserByID(10));
//User.deleteUser(7);
//User.updateUser(13, "asd", "Test", "johnny", "tests", "test@iastate.edu");
//----------------------------


app.listen(config.port);
console.log('Listening on port:', config.port);