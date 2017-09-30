Party = require('../models/parties');

module.exports.createParty = function(party_name, date, time, privacy, max_people, alerts, host) {
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
		}
		else {
			console.log("Created new party: ", party_name);
		}
	});
}
module.exports.findAllParties = function(res) {
	var party = new Party();
	party.find('all', function(err, rows, fields) {
		if (err) {
			console.log("error");
		}
		else {
			if (rows.length == 0) {
				console.log("No parties not found.");
			}
			else {
				console.log(rows);
				res.json({
				    parties: rows
				})
			}
		}
	});
}
module.exports.findPartyByID = function(id, res) {
	var party = new Party();
	party.find('all', {where: 'id=' + id}, function(err, rows, fields) {
		if (err) {
			console.log("error");
		}
		else {
			if (rows.length == 0) {
				console.log("Party not found.");
			}
			else {
				res.json({
				    party: rows
				})
				console.log(party);
			}
		}
	});
}
module.exports.deleteParty = function(id) {
	party = new Party();
	party.set('id', id);
	party.remove(function(err) {
		if (err) {
			console.log("Tried to delete a null party: ", id);
		}
		else {
			console.log('Deleted party: ', id);
		}
	});
}
module.exports.updateParty = function(id, party_name, date, time, privacy, alerts, host) {
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
		}
		else {
			console.log('Updated party: ', id);
		}
	});
}