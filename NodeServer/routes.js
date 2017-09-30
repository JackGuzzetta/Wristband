module.exports = function(app){
   
	var User = require('./controllers/user_controller');
	var Party = require('./controllers/party_controller');

	//----------User-------------
    app.get('/users', function(req, res) {
    	User.findAllUsers(res);
    });
    app.get('/users/:id', function(req, res) {
    	User.findUserByID(req.params.id, res);
    });

    //TODO
    //app.post('/users', add);
    // app.put('/users/:id', update);
    // app.delete('/users/:id', delete);

	//----------------------------



    //----------PARTY-------------
    app.get('/party', function(req, res) {
    	Party.findAllParties(res);
    });
    app.get('/party/:id', function(req, res) {
    	Party.findPartyByID(req.params.id, res);
    });

    //TODO
    //app.post('/party', add);
    //app.put('/party/:id', update);
    //app.delete('/party/:id', delete);


	//----------------------------
    


    
}
