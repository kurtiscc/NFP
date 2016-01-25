var express = require('express');
var router = express.Router();
var MC = require('mongodb').MongoClient;

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});


// router.get('/getjson', function(req, res, next){
// 	var employees = [
// 		{"firstName":"John", "lastName":"Doe"},
// 		{"firstName":"Anna", "lastName":"Smith"},
// 		{"firstName":"Peter","lastName": "Jones"}
// 	];
// 	// res.writeHead(200, {"Content-Type": "application/json"});
// 	res.end("" + JSON.stringify(employees));
// });


// router.post('/postjson', function(req, res, next){
// 	console.log(req.body);
// 	res.write("" + {'received': 'true'});
// 	res.end();
// });

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

router.get('/getdoctors/:clinicname', function(req, res, next){
	MC.connect('mongodb://localhost/NFP', function(err, db){
		if(err) throw err;
		var coll = db.collection('doctor');
		coll.find({Clinic:req.params.clinicname}, function(err, list){
			list.toArray(function(err, listArr){
				res.end(""+JSON.stringify(listArr));
			})
		});
	});
});

router.get('/getpatients/:doctorid', function(req,res, next){
	MC.connect('mongodb://localhost/NFP', function(err, db){
		if(err) throw err;
		var coll = db.collection('patient');
		coll.find({Doctorid:req.params.doctorid}, function(err, list){
			list.toArray(function(err, listArr){
				res.end(""+JSON.stringify(listArr));
			});
		});
	});
});


router.get('/getsamples/:patientid', function(req,res,next){
	MC.connect('mongodb://localhost/NFP', function(err, db){
		if(err) throw err;
		var coll = db.collection('sample');
		coll.find({Patientid:req.params.patientid}, function(err, list){
			console.log("found");
			list.toArray(function(err, listArr){
				res.end(""+JSON.stringify(listArr));
			});
		});
	});
});




/*---------------------------------------------
					POST
----------------------------------------------*/
//https://docs.angularjs.org/api/ng/function/angular.copy
var sampleTemplate = {
	patientid:null,
	userid:null,
	tagUID:null,
	trackingSteps:[]
}
var trackingStepTemplate = {
	lat:null,
	lon:null,
	addr:null,
	userid:null,
	purpose:null,
	timestamp:null
}
router.post('/addsample', function(req, res, next){
	var localsample = {
		patientid:null,
		userid:null,
		tagUID:null,
		trackingSteps:[]
	}
	var localtracking = {
		lat:null,
		lon:null,
		addr:null,
		userid:null,
		purpose:null,
		timestamp:null
	}

	localsample.patientid = req.body.patientid;
	localsample.tagUID = req.body.tagUID;
	localsample.userid = req.body.userid;

	localtracking.lat = req.body.lat;
	localtracking.lon = req.body.lon;
	localtracking.addr = req.body.addr;
	localtracking.userid = req.body.userid;
	localtracking.purpose = req.body.purpose;
	localtracking.timestamp = req.body.timestamp;

	localsample.trackingSteps.push(localtracking);

	MC.connect('mongodb://localhost/NFP', function(err, db){
		if(err) throw err;
		var coll = db.collection('sample');
		coll.count({tagUID:req.body.tagUID}, function(err, count){
			if(err) throw err;
			if(!count){
				coll.insert(localsample, function(err, record){
					if(err) throw err;
					res.sendStatus(200);
				});
			}else{
				console.log('already there');
			}
		});
	});
});

router.post('/addtrackingstep/:tagUID', function(req, res, next){
	var localtracking = {
		lat:null,
		lon:null,
		addr:null,
		userid:null,
		purpose:null,
		timestamp:null
	}

	localtracking.lat = req.body.lat;
	localtracking.lon = req.body.lon;
	localtracking.addr = req.body.addr;
	localtracking.userid = req.body.userid;
	localtracking.purpose = req.body.purpose;
	localtracking.timestamp = req.body.timestamp;

	MC.connect('mongodb://localhost/NFP', function(err, db){
		if(err) throw err;
		var coll = db.collection('sample');
		coll.update({tagUID:req.params.tagUID}, {$push:{trackingSteps: localtracking}}, function(err, list){
			if(err) throw err;
			res.sendStatus(200);
		});
	});
});

module.exports = router;
