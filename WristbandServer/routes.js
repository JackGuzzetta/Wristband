module.exports = function(app) {
   
	var User = require('./controllers/user_controller');
	var Party = require('./controllers/party_controller');
    var Relation = require('./controllers/relation_contoller');



	//----------User-------------
    app.get('/users', function(req, res) {
    	User.findAllUsers(res);
    });
    app.get('/users/:id', function(req, res) {
    	User.findUserByID(req.params.id, res);
    });
    app.get('/user_name/:username', function(req, res) {
        User.findUserByUsername(req.params.username, res);
    });
    app.post('/users', function(req, res) {
        User.createUser(req.headers.f_name, req.headers.l_name, req.headers.username, req.headers.password, req.headers.email, res);
    });
    app.put('/users/:id', function(req, res) {
        User.updateUser(req.params.id, req.headers.f_name, req.headers.l_name, req.headers.username, req.headers.password, req.headers.email, res);
    });
    app.delete('/users/:id', function(req, res) {
        User.deleteUser(req.params.id, res);
    });
    app.get('/join/:id', function(req, res) {
        User.joinByUserId(req.params.id, res);
    });
	//----------------------------



    //----------PARTY-------------
    app.get('/parties', function(req, res) {
        Party.findAllParties(res);
    });
    app.get('/parties/:id', function(req, res) {
        Party.findPartyByID(req.params.id, res);
    });
    app.get('/party_name/:name', function(req, res) {
        Party.findPartyByName(req.params.name, res);
    });
    app.post('/parties', function(req, res) {
        Party.createParty(req.headers.party_name, req.headers.date, req.headers.time, req.headers.privacy, req.headers.max_people, req.headers.alerts, req.headers.host, req.headers.location, res);
    });
    app.put('/parties', function(req, res) {
        Party.updateParty(req.headers.id, req.headers.party_name, req.headers.date, req.headers.time, req.headers.privacy, req.headers.max_people, req.headers.alerts, req.headers.host, req.headers.location, res);
    });
    app.delete('/parties/:id', function(req, res) {
        Party.deleteParty(req.params.id, res);
    });
	//----------------------------

        //----------Relation-------------
    app.get('/relation', function(req, res) {
        Relation.findAllRelations(res);
    });
    app.get('/relation/:id', function(req, res) {
        Relation.findRelationByID(req.params.id, res);
    });
    app.post('/relation', function(req, res) {
        Relation.createRelation(req.headers.user_id, req.headers.party_id, req.headers.relation, res);
    });
    app.delete('/relation', function(req, res) {
        Relation.deleteRelation(req.headers.user_id, req.headers.relation_id, res);
    });

    app.post('/users/login', function(req, res) {
        User.login(req.headers.username, req.headers.password, res);
    }); 
    app.post('/email', function(req, res) {
        User.email(req.headers.email, req.headers.username, res);
    }); 
    app.post('/text', function(req, res) {
        User.text(req.headers.number, res);
    }); 
}
