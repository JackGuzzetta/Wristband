var express = require('express')
var mysql = require('mysql');
var jwt = require('jwt-simple');
var moment = require('moment');

var config = require('./config');
var app = express()

//set token to be used for authentication
app.set('jwtTokenSecret', config.crypt);

//connect to database
var con = mysql.createConnection(config.mysql);

con.connect(function(err) {
    if (err) {
        console.log("error: ", err);
    } else {
        console.log("Connected to MySQL database: " + config.mysql.host);

    }
});
//

//user auth
//https://www.sitepoint.com/using-json-web-tokens-node-js/
var fake_user = {};

var res = {};
fake_user.id = 12345;

var expires = moment().add('days', 7).valueOf();

var token = jwt.encode({
  iss: fake_user.id,
  exp: expires
}, app.get('jwtTokenSecret'));

console.log(token);
// res.json({
//   token : token,
//   expires: expires,
//   user: fake_user
// });



//end user auth








//endpoints
app.get('/get_all_users', function(req, res) {
    //get data from mysql database
    console.log(req.params.uid);
    con.query('SELECT * FROM users', req.params.uid, function(err, result) {
        if (err) {
            res.json({
                err
            })
            console.log("error: ", err);
        } else {
            res.json({all: result})
            console.log(result);
        }
    });
});

app.get('/get_user/:uid', function(req, res) {
    //get data from mysql database
    console.log(req.params.uid);
    con.query('SELECT * FROM users WHERE id=?', req.params.uid, function(err, result) {
        if (err) {
            res.json({
                err
            })
            console.log("error: ", err);
        } else {
            res.json({user: result})
            console.log(result);
        }
    });
});

app.get('/get_all_parties', function(req, res) {
    //get data from mysql database
    console.log(req.params.uid);
    con.query('SELECT * FROM party', req.params.uid, function(err, result) {
        if (err) {
            res.json({
                err
            })
            console.log("error: ", err);
        } else {
            res.json({all: result})
            console.log(result);
        }
    });
});

app.get('/get_party/:uid', function(req, res) {
    //get data from mysql database
    console.log(req.params.uid);
    con.query('SELECT * FROM party WHERE id=?', req.params.uid, function(err, result) {
        if (err) {
            res.json({
                err
            })
            console.log("error: ", err);
        } else {
            res.json({user: result})
            console.log(result);
        }
    });
});
//

//port to listen on
app.listen(config.port)
console.log('Sever listening on port: ' + config.port);
//