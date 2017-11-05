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
                        login: "username error"
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
                    } else {
                        res.json({
                            login: "password error"
                        })
                    }
                }
            }
        });
    }

    module.exports.createUser = function(f_name, l_name, username, password, email, res) {
        var expires = moment().add(7, 'days').valueOf();
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
                            id = rows[0].id;
                            token = createToken(username, expires);
                            console.log(token);
                            res.json({
                                token: token,
                                id: id,
                                user: username
                            })
                        }
                    }
                });
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
        var expires = moment().add(7, 'days').valueOf();
        var user = new User();
        user.find('all', {
            where: 'username=' + '\'' + username + '\''
        }, function(err, rows, fields) {
            if (err) {
                console.log("error");
                res.json([{
                    users: "Error"
                }])
            } else {
                if (rows.length == 0) {
                    console.log("User not found.");
                    res.contentType('application/json');
                    res.json([{
                        users: "Not found",
                        username: username
                    }])
                } else {
                    var id = rows[0].id;
                    var f_name = rows[0].f_name;
                    var l_name = rows[0].l_name;
                    var email = rows[0].email;
                    var uname = rows[0].username;
                    console.log("found user: ", username);
                    console.log("found user: ", rows[0]);
                    token = createToken(username, expires);
                    res.contentType('application/json');
                    res.json([{
                                users: "exists",
                                token: token,
                                id: id,
                                f_name: f_name,
                                l_name: l_name,
                                username: uname,
                                email: email
                            }])
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
    module.exports.email = function(email, username, res) {
        var send = require('gmail-send')({
          user: 'wristbandparties@gmail.com',
          pass: 'wristband1',
          to:   email,
          subject: 'Wristband Party Invite',
          text:    "Here is your invite: ", username,         // Plain text 
        });
        var retVal = "success";

        var QRCode = require('qrcode')
        QRCode.toFile("images/" + username + ".png", username, {type:'png'}, function (err, string) {
            if (err) {
                retVal = "error"
            }
        })

        send({
            files: [                                    // Array of files to attach 
                {
                  path: "images/" + username + ".png",
                }
              ]
        }, function (err) {
          if (err) {
            retVal = "error"
          }

        });
        console.log("Sending email to: ", email)
        res.json({
            email: retVal
        })
    }
        module.exports.text = function(number, res) {
        var carrier;
        var send = require('gmail-send')({
          user: 'wristbandparties@gmail.com',
          pass: 'wristband1',
          to:   number,
          subject: 'Wristband Party Invite',
          text:    "Here is your invite: ", number,         // Plain text 
        });
        var retVal = "success";

        var QRCode = require('qrcode')
        QRCode.toFile("images/" + number + ".png", number, {type:'png'}, function (err, string) {
            if (err) {
                retVal = "error"
            }
        })
        for (a = 0; a < 4; a++) {
            switch(a) {
                case 0:
                    carrier = "@mms.att.net"
                    break;
                case 1:
                    carrier = "@vzwpix.com "
                    break;
                case 2:
                    carrier = "@messaging.sprintpcs.com"
                    break;
                case 3:
                    carrier = "@mms.uscc.net"
                    break;
                default:
                    carrier = null
                break;
            }
            send({
                to: number + carrier,
                files: [                                    // Array of files to attach 
                    {
                      path: "images/" + number + ".png",
                    }
                  ]
            }, function (err) {
              if (err) {
                retVal = "error"
              }

            });
        }
        res.json({
            email: retVal
        })
       console.log("Sending text to: ", number)
    }
}