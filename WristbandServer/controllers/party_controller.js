Party = require('../models/parties');

module.exports.createParty = function(party_name, date, time, privacy, max_people, alerts, host, location, res) {
	var party1 = new Party();
	party = new Party({
	    party_name: party_name,
	    date: date,
	    time: time,
	    privacy: privacy,
	    max_people: max_people,
	    alerts: alerts,
	    host: host,
	    location: location
	});
	party1.find('all', {where: 'party_name=' + '\'' + party_name+ '\''}, function(err, rows, fields) {
		if (err) {
			console.log("error");
			res.json({
			    parties: "Error"
			})
		}
		else {
			if (rows.length == 0) {
				party.save(function(err) {
					if (err) {
						console.log("Unable to create party");
						res.json({
						    parties: "Unable to create party, err"
						})
					}
					else {
						console.log("Created new party: ", party_name);
						res.json({
							parties: "Created new party",
							party_name: party_name
						})
					}
				});
			}
			else {
				res.json({
					parties: "Party already exists"
				})
			}
		}
	});
}
module.exports.findAllParties = function(res) {
	var party = new Party();
	party.find('all', function(err, rows, fields) {
		if (err) {
			console.log("error");
			res.json({
			    parties: "Error"
			})
		}
		else {
			if (rows.length == 0) {
				console.log("No parties not found.");
				res.json({
				    parties: "Error"
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
			    parties: "Error"
			})
		}
		else {
			if (rows.length == 0) {
				console.log("Party not found.");
				res.json({
				    parties: "Error"
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
module.exports.findPartyByName = function(name, res) {
	var party = new Party();
	party.find('all', {where: 'party_name=' + '\'' + name+ '\''}, function(err, rows, fields) {
		if (err) {
			console.log("error");
			res.json({
			    parties: "Error"
			})
		}
		else {
			if (rows.length == 0) {
				console.log("Party not found.");
				res.json({
				    parties: "Error"
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
			    parties: "Error"
			})
		}
		else {
			console.log('Deleted party: ', id);
			res.json({
				    parties: "Success"
			})
		}
	});
}
module.exports.updateParty = function(id, party_name, date, time, privacy, max_people, alerts, host, location, res) {
	party = new Party({
		id: id,
	    party_name: party_name,
	    date: date,
	    time: time,
	    host: host,
	    privacy: privacy,
	    max_people: max_people,
	    alerts: alerts,
	    location: location
	});
	party.save(function(err) {
		if (err) {
			console.log("Unable to create party", err);
			res.json({
			    parties: "Error"
			})
		}
		else {
			console.log("Updated new party: ", party_name);
			res.json({
				parties: "Success",
				party_name: party_name
			})
		}
	});
}
module.exports.updateParty = function(id, party_name, date, time, privacy, max_people, alerts, host, location, res) {
	party = new Party({
		id: id,
	    party_name: party_name,
	    date: date,
	    time: time,
	    host: host,
	    privacy: privacy,
	    max_people: max_people,
	    alerts: alerts,
	    location: location
	});
	party.save(function(err) {
		if (err) {
			console.log("Unable to create party", err);
			res.json({
			    parties: "Error"
			})
		}
		else {
			console.log("Updated new party: ", party_name);
			res.json({
				parties: "Success",
				party_name: party_name
			})
		}
	});
}
    module.exports.joinByPartyId = function(id, res) {
        var user = new User();
        user.query("SELECT users.id, users.f_name, users.l_name, party_relation.party_id, party_relation.user_id, party_relation.party_user_relation FROM users join party_relation ON users.id=party_relation.user_id join parties ON parties.id=party_relation.party_id WHERE parties.id=\"" + id  + "\";", function(err, rows, fields) {
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