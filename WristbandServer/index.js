//https://www.npmjs.com/package/mysql-model
var express = require('express');
var mysqlModel = require('mysql-model');
var app = express();
var config = require('./config');
var User = require('./controllers/user_controller');
var Party = require('./controllers/party_controller');


require('./routes')(app);



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