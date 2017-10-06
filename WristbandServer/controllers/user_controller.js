User = require('../models/users');

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
		}
		else {
			console.log("Created new user: ", username);
			res.json({
				    users: "Success"
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
		}
		else {
			if (rows.length == 0) {
				console.log("Users not found.");
				res.json({
				    users: "Error"
				})
			}
			else {
				console.log(rows);
				res.contentType('application/json');
				res.send(JSON.stringify(rows));
			}
		}
	});
}
module.exports.findUserByID = function(id, res) {
	var user = new User();
	user.find('all', {where: 'id=' + id}, function(err, rows, fields) {
		if (err) {
			console.log("error");
			res.json({
			    users: "Error"
			})
		}
		else {
			if (rows.length == 0) {
				console.log("User not found.");
				res.json({
				    users: "Error"
				})
			}
			else {
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
		}
		else {
			console.log('Deleted user: ', id);
			res.contentType('application/json');
			res.send(JSON.stringify(id));
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
		}
		else {
			res.contentType('application/json');
			res.send(JSON.stringify(id));
		}
	});
}