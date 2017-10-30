Relation = require('../models/relations');

module.exports.createRelation = function(user_id, party_id, relation, res) {
	var relation = new Relation({
	    user_id: user_id,
	    party_id: party_id,
	    party_user_relation: relation
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
				    user_id: user_id, party_id: party_id
			})
		}
	});
}
module.exports.createRelationInvite = function(user_id, party_id, res) {
	var relation = new Relation({
	    party_user_relation: user_id,
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
				    user_id: user_id, party_id: party_id
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
module.exports.findRelationByID = function(id, res) {
	var relation = new Relation();
	relation.find('all', {where: 'user_id=' + id}, function(err, rows, fields) {
		if (err) {
			console.log("error");
			res.json({
			    relations: "Error"
			})
		}
		else {
			if (rows.length == 0) {
				console.log("Relation not found.");
				res.json({
				    relations: "Error"
				})
			}
			else {
				res.contentType('application/json');
				res.send(JSON.stringify(rows));
			}
		}
	});
}
module.exports.deleteRelation = function(user_id, relation, res) {
    relation = new Relation();
    relation.set('user_id', user_id);
    relation.set('party_user_relation', relation);
    relation.remove(function(err) {
        if (err) {
            console.log("Tried to delete a null relation: ", id);
            res.json({
                relations: "Error"
            })
        } else {
            console.log('Deleted relation: ', user_id, relation);
            res.json({
                relations: "Success"
            })
        }
    });
}