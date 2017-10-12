module.exports = function(app) {
    var jwt = require('jwt-simple');
    var moment = require('moment');
    var config = require('../config');

    User = require('../models/users');

    function createToken(user_id, expires) {
        var token = jwt.encode({
            iss: user_id,
            exp: expires
        }, app.get('jwtTokenSecret'));
        return token;
    }

    function decodeToken(token) {
        if (token) {
            try {
                var decoded = jwt.decode(token, app.get('jwtTokenSecret'));
                // handle token here
                console.log(decoded);
                return true;
            } catch (err) {
                return false;
            }
        } else {
            return false;
        }
    }
    module.exports.login = function(username, password, res) {
        var expires = moment().add(7, 'days').valueOf();
        var token;
        var user = new User();
        var id;
        user.find('all', {
            where: 'username=' + '\'' + username + '\''
        }, function(err, rows, fields) {
            if (err) {
                console.log("error", err);
                res.json({
                    error: "db"
                })
            } else {
                if (rows.length == 0) {
                    res.json({
                        error: "username"
                    })
                } else {
                    id = rows[0].id;
                    if (rows[0].password == password) {
                        token = createToken(username, expires);
                        console.log(token);
                        res.json({
                            token: token,
                            id: id,
                            user: username
                        })
                    }
                    else {
                        res.json({
                            error: "password"
                        })
                    }
                }
            }
        });
    }


    module.exports.createUser = function(f_name, l_name, username, password, email, res) {
        user = new User({
            f_name: f_name,
            l_name: l_name,
            username: username,
            password: password,
            email: email,
        });
        user.save(function(err) {
            if (err) {
                console.log("Unable to create user");
                res.json({
                    users: "Error"
                })
            } else {
                console.log("Created new user: ", username);
                res.json({
                    users: username
                })
            }
        });
    }
    module.exports.findAllUsers = function(res) {
        var user = new User();
        user.find('all', function(err, rows, fields) {
            if (err) {
                console.log("error");
                res.json({
                    users: "Error"
                })
            } else {
                if (rows.length == 0) {
                    console.log("Users not found.");
                    res.json({
                        users: "Error"
                    })
                } else {
                    console.log(rows);
                    res.contentType('application/json');
                    res.send(JSON.stringify(rows));
                }
            }
        });
    }
    module.exports.findUserByID = function(id, res) {
        var user = new User();
        user.find('all', {
            where: 'id=' + id
        }, function(err, rows, fields) {
            if (err) {
                console.log("error");
                res.json({
                    users: "Error"
                })
            } else {
                if (rows.length == 0) {
                    console.log("User not found.");
                    res.json({
                        users: "Error"
                    })
                } else {
                    res.contentType('application/json');
                    res.send(JSON.stringify(rows));
                }
            }
        });
    }
        module.exports.findUserByUsername = function(username, res) {
        var user = new User();
        user.find('all', {
            where: 'username=' + '\'' + username + '\''
        }, function(err, rows, fields) {
            if (err) {
                console.log("error");
                res.json({
                    users: "Error"
                })
            } else {
                if (rows.length == 0) {
                    console.log("User not found.");
                    res.json({
                        users: "Error"
                    })
                } else {
                    res.contentType('application/json');
                    res.send(JSON.stringify(rows));
                }
            }
        });
    }
    module.exports.deleteUser = function(id, res) {
        user = new User();
        user.set('id', id);
        user.remove(function(err) {
            if (err) {
                console.log("Tried to delete a null user: ", id);
                res.json({
                    users: "Error"
                })
            } else {
                console.log('Deleted user: ', id);
                res.json({
                    users: "Success"
                })
            }
        });
    }
    module.exports.updateUser = function(id, f_name, l_name, username, password, email, res) {
        user = new User({
            id: id,
            f_name: f_name,
            l_name: l_name,
            username: username,
            password: password,
            email: email,
        });
        user.save(function(err) {
            if (err) {
                console.log("Unable to update user");
                res.json({
                    users: "Error"
                })
            } else {
                res.json({
                    users: "Success"
                })
            }
        });
    }
}