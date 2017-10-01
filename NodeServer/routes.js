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
    app.post('/users', function(req, res) {
        User.createUser(req.headers.f_name, req.headers.l_name, req.headers.username, req.headers.password, req.headers.email, res)
    });
    app.put('/users/:id', function(req, res) {
        User.updateUser(req.params.id, req.headers.f_name, req.headers.l_name, req.headers.username, req.headers.password, req.headers.email, res)
    });

    app.delete('/users/:id', function(req, res) {
        User.deleteUser(req.params.id, res);
    });
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
