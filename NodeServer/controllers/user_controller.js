User = require('../models/users');

module.exports.createUser = function(f_name, l_name, username, password, email) {
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
		}
		else {
			console.log("Created new user");
		}
	});
}
module.exports.findAllUsers = function(res) {
	var user = new User();
	user.find('all', function(err, rows, fields) {
		if (err) {
			console.log("error");
		}
		else {
			if (rows.length == 0) {
				console.log("Users not found.");
			}
			else {
				console.log(rows);
				res.json({
				    users: rows
				})
			}
		}
	});
}
module.exports.findUserByID = function(id, res) {
	var user = new User();
	user.find('all', {where: 'id=' + id}, function(err, rows, fields) {
		if (err) {
			console.log("error");
		}
		else {
			if (rows.length == 0) {
				console.log("User not found.");
			}
			else {
				res.json({
				    user: rows
				})
			}
		}
	});
}
module.exports.deleteUser = function(id) {
	user = new User();
	user.set('id', id);
	user.remove(function(err) {
		if (err) {
			console.log("Tried to delete a null user: ", id);
		}
		else {
			console.log('Deleted user: ', id);
		}
	});
}
module.exports.updateUser = function(id, f_name, l_name, username, password, email) {
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
		}
	});
}