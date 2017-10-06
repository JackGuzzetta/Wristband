Party = require('../models/parties');

module.exports.createParty = function(party_name, date, time, privacy, max_people, alerts, host, res) {
	party = new Party({
	    party_name: party_name,
	    date: date,
	    time: time,
	    privacy: privacy,
	    max_people: max_people,
	    alerts: alerts,
	    host: host
	});
	party.save(function(err) {
		if (err) {
			console.log("Unable to create party");
			res.json({
			    users: "Error"
			})
		}
		else {
			console.log("Created new party: ", party_name);
			res.contentType('application/json');
			res.send(JSON.stringify(party_name));
		}
	});
}
module.exports.findAllParties = function(res) {
	var party = new Party();
	party.find('all', function(err, rows, fields) {
		if (err) {
			console.log("error");
			res.json({
			    users: "Error"
			})
		}
		else {
			if (rows.length == 0) {
				console.log("No parties not found.");
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
module.exports.findPartyByID = function(id, res) {
	var party = new Party();
	party.find('all', {where: 'id=' + id}, function(err, rows, fields) {
		if (err) {
			console.log("error");
			res.json({
			    users: "Error"
			})
		}
		else {
			if (rows.length == 0) {
				console.log("Party not found.");
				res.json({
				    users: "Error"
				})
			}
			else {
				res.contentType('application/json');
				res.send(JSON.stringify(rows));
				console.log(party);
			}
		}
	});
}
module.exports.deleteParty = function(id, res) {
	party = new Party();
	party.set('id', id);
	party.remove(function(err) {
		if (err) {
			console.log("Tried to delete a null party: ", id);
			res.json({
			    users: "Error"
			})
		}
		else {
			console.log('Deleted party: ', id);
			res.contentType('application/json');
			res.send(JSON.stringify(id));
		}
	});
}
module.exports.updateParty = function(id, party_name, date, time, privacy, alerts, host, res) {
	party = new Party({
		id: id,
	    party_name: party_name,
	    date: date,
	    time: time,
	    privacy: privacy,
	    alerts: alerts,
	    host: host
	});
	party.save(function(err) {
		if (err) {
			console.log("Unable to update party");
			res.json({
			    users: "Error"
			})
		}
		else {
			console.log('Updated party: ', id);
			res.contentType('application/json');
			res.send(JSON.stringify(id));
		}
	});
}