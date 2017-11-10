Comment = require('../models/comments');
    

    module.exports.newComment = function(party_id, username, cmt, res) {
        var comment1 = new Comment();
        comment = new Comment({
	    party_id: party_id,
	    username: username,
	    cmt: cmt
	});
	comment.save(function(err) {
		if (err) {
			console.log("Unable to create comment");
			res.json({
			    comment: "Error"
			})
		}
		else {
			console.log("Created new party: ", cmt);
			res.json({
				    cmt: cmt
			})
		}
	});
    }
    
    module.exports.findAllComments = function(res) {
        var comment = new Comment();
        comment.find('all', function(err, rows, fields) {
            if (err) {
                console.log("error");
                res.json({
                    comment: "Error"
                })
            } else {
                if (rows.length == 0) {
                    console.log("Comments not found.");
                    res.json({
                        comment: "Error"
                    })
                } else {
                    console.log(rows);
                    res.contentType('application/json');
                    res.send(JSON.stringify(rows));
                }
            }
        });
    }
    module.exports.findCommentByID = function(id, res) {
        var comment = new Comment();
        comment.find('all', {
            where: 'id=' + id
        }, function(err, rows, fields) {
            if (err) {
                console.log("error");
                res.json({
                    comments: "Error"
                })
            } else {
                if (rows.length == 0) {
                    console.log("User not found.");
                    res.json({
                        comments: "Error"
                    })
                } else {
                    res.contentType('application/json');
                    res.send(JSON.stringify(rows));
                }
            }
        });
    }
    
    module.exports.deleteComment = function(id, res) {
        comment = new Comment();
        comment.set('id', id);
        comment.remove(function(err) {
            if (err) {
                console.log("Tried to delete a null comment: ", id);
                res.json({
                    comments: "Error"
                })
            } else {
                console.log('Deleted comment: ', id);
                res.json({
                    comments: "Success"
                })
            }
        });
    }
    module.exports.updateComment = function(id, username, text, res) {
        comment = new Comment({
            id: id,
            username: username,
            text: text
        });
        comment.save(function(err) {
            if (err) {
                console.log("Unable to update user");
                res.json({
                    comments: "Error"
                })
            } else {
                res.json({
                    comments: "Success"
                })
            }
        });
    }
module.exports.getAllCommentsByPartyId = function(id, res) {
        var comment = new Comment();
        comment.query('all', {where: 'party_id=' + id}, function(err, rows, fields) {
            if (err) {
                console.log("error");
                res.json({
                    comment: "Error"
                })
            } else {
                if (rows.length == 0) {
                    console.log("User not found.");
                    res.json({
                        comment: "Error"
                    })
                } else {
                    res.contentType('application/json');
                    res.send(JSON.stringify(rows));
                }
            }
        });
    }