var express = require('express');
var router = express.Router();
var MC = require('mongodb').MongoClient;

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
	res.end("" + JSON.stringify(employees));
});


router.post('/postjson', function(req, res, next){
	console.log(req.body);
	res.write("" + {'received': 'true'});
	res.end();
});

/*---------------------------------------------
					GET
----------------------------------------------*/

router.get('/dbtest', function(req, res, next){
	MC.connect('mongodb://localhost/webpageTest', function(err, db){
		if(err) throw err;

		var coll = db.collection('webpageTest');
		coll.insert({name: 'taco', tasty: true}, function(err, record){});

		// coll.find().toArray(function(err, asfd){
		// 	res.write("first");
		// 	res.end(""+asfd);
		// });

		coll.find(function(err, arr){
			arr.toArray(function(err, itemarr){
				res.write("Second");
				res.end(JSON.stringify(itemarr));
			})
		})
	});
});

router.get('/getlist/:coll', function(req, res, next){
	// console.log(req.params.coll);
	// res.end(req.params.coll)
	MC.connect('mongodb://localhost/NFP', function(err, db){
		if(err) throw err;

		var coll = db.collection(req.params.coll);
		coll.find(function(err, list){
			list.toArray(function(err, listArr){
				res.end(""+JSON.stringify(listArr));
			});
		});
	});
});




/*---------------------------------------------
					POST
----------------------------------------------*/
router.post('/addtag', function(req, res, next){
	console.log(req.body);
	MC.connect('mongodb://localhost/NFP', function(err, db){
		if(err) throw err;

		var coll = db.collection('tag');

	});
});

module.exports = router;
