Relation = require('../models/relations');

module.exports.createRelation = function(user_id, party_id, res) {
	var relation = new Relation({
	    user_id: user_id,
	    party_id: party_id,
	});
	relation.save(function(err) {
		if (err) {
			console.log("Unable to create relation");
			res.json({
			    relations: "Error"
			})
		}
		else {
			console.log("Created new relation: ", user_id);
			res.json({
				    relations: user_id
			})
		}
	});
}
module.exports.findAllRelations = function(res) {
	var relation = new Relation();
	relation.find('all', function(err, rows, fields) {
		if (err) {
			console.log("error");
			res.json({
			    relations: "Error"
			})
		}
		else {
			if (rows.length == 0) {
				console.log("Relations not found.");
				res.json({
				    relations: "Error"
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
//TODO
// module.exports.findRelationByID = function(id, res) {
// 	var relation = new Relation();
// 	relation.find('all', {where: 'id=' + id}, function(err, rows, fields) {
// 		if (err) {
// 			console.log("error");
// 			res.json({
// 			    relations: "Error"
// 			})
// 		}
// 		else {
// 			if (rows.length == 0) {
// 				console.log("Relation not found.");
// 				res.json({
// 				    relations: "Error"
// 				})
// 			}
// 			else {
// 				res.contentType('application/json');
// 				res.send(JSON.stringify(rows));
// 			}
// 		}
// 	});
// }
// module.exports.deleteRelation = function(id, res) {
// 	relation = new Relation();
// 	relation.set('id', id);
// 	relation.remove(function(err) {
// 		if (err) {
// 			console.log("Tried to delete a null relation: ", id);
// 			res.json({
// 			    relations: "Error"
// 			})
// 		}
// 		else {
// 			console.log('Deleted relation: ', id);
// 			res.json({
// 				    relations: "Success"
// 			})
// 		}
// 	});
// }
// module.exports.updateRelation = function(id, f_name, l_name, username, password, email, res) {
// 	relation = new Relation({
// 		id: id,
// 	    f_name: f_name,
// 	    l_name: l_name,
// 	    username: username,
// 	    password: password,
// 	    email: email,
// 	});
// 	relation.save(function(err) {
// 		if (err) {
// 			console.log("Unable to update relation");
// 			res.json({
// 			    relations: "Error"
// 			})
// 		}
// 		else {
// 			res.json({
// 				    relations: "Success"
// 			})
// 		}
// 	});
// }