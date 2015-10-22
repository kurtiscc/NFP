var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});


router.get('/test', function(req, res, next){
	res.end("You connected to test!");
});


router.get('/getjson', function(req, res, next){
	var employees = [
		{"firstName":"John", "lastName":"Doe"},
		{"firstName":"Anna", "lastName":"Smith"},
		{"firstName":"Peter","lastName": "Jones"}
	];
	// res.writeHead(200, {"Content-Type": "application/json"});
	res.end("" + employees);
});


router.post('/postjson', function(req, res, next){
	console.log(req.body);
	res.write("" + {'received': 'true'});
	res.end();
})

module.exports = router;
