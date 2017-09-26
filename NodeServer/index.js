var express = require('express')
var mysql = require('mysql');

var app = express()


//connect to database
var con = mysql.createConnection({
    host: 'mysql.cs.iastate.edu',
    user: 'dbu309ytb4',
    password: 'z3ASzDvf',
    database: 'db309ytb4'
});

con.connect(function(err) {
    if (err) {
        console.log("error: ", err);
    } else {
        console.log("Connected!");

    }
});
//

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
    con.query('SELECT * FROM parties', req.params.uid, function(err, result) {
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
    con.query('SELECT * FROM parties WHERE id=?', req.params.uid, function(err, result) {
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
app.listen(3000)
//