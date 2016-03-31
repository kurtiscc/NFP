var express = require('express');
var router = express.Router();
var MC = require('mongodb').MongoClient;
var ObjectID = require('mongodb').ObjectID;
var async = require('async');

//var MongoConnectionString = "mongodb://nfpuser:nfpAPPuser@40.78.57.211:27017/NFP";
//MongoConnectionString
// var MongoConnectionString = "mongodb://localhost:27017/NFP";
var MongoConnectionString = "mongodb://40.78.58.204:27017/NFP";

/* GET home page. */
router.get('/', function(req, res, next) {
  //res.render('index', { title: 'Express' });
  res.render('splash');
});

router.get('/map', function(req, res, next){
	res.render('map', {title:'Map'});
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


router.post('/postjson', function(req, res, next){
	console.log(req.body);
	res.write("" + JSON.stringify({'received': 'true'}));
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
	MC.connect(MongoConnectionString, function(err, db){
		//console log all errors
		if(err) console.log(err);
		var coll = db.collection(req.params.coll);
		coll.find(function(err, list){
			list.toArray(function(err, listArr){
				res.end(""+JSON.stringify(listArr));
			});
		});
	});
});

router.get('/getdoctors/:clinicname', function(req, res, next){
	MC.connect(MongoConnectionString, function(err, db){
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
	MC.connect(MongoConnectionString, function(err, db){
		if(err) throw err;
		var coll = db.collection('patient');
		coll.find({Doctorid:req.params.doctorid}, function(err, list){
			list.toArray(function(err, listArr){
				res.end(""+JSON.stringify(listArr));
			});
		});
	});
});


router.get('/getsamples/:Patientid', function(req,res,next){
	MC.connect(MongoConnectionString, function(err, db){
		if(err) throw err;
		var coll = db.collection('sample');
		coll.find({patientid:req.params.Patientid}, function(err, list){
			list.toArray(function(err, listArr){
				res.end(""+JSON.stringify(listArr));
			});
		});
	});
});

router.get('/getsample/:taguid', function(req,res,next){
	MC.connect(MongoConnectionString, function(err, db){
		if(err) throw err;
		var coll = db.collection('sample');
		coll.find({tagUID:req.params.taguid}, function(err, list){
			list.toArray(function(err, listArr){
				res.end(""+JSON.stringify(listArr));
			});
		});
	});
});

router.get('/getpatient/:Patientid', function(req,res,next){
	MC.connect(MongoConnectionString, function(err, db){
		if(err) throw err;
		var coll = db.collection('patient');
		var newId = ObjectID(req.params.Patientid);
		coll.find({_id:newId}, function(err, list){
			list.toArray(function(err, listArr){
				res.end(""+JSON.stringify(listArr));
			});
		});
	});
});




/*---------------------------------------------
					POST
----------------------------------------------*/
var sampleTemplate = {
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

	localsample.tagUID = req.body.tagUID;
	localsample.userid = req.body.userid;

	localtracking.lat = req.body.lat;
	localtracking.lon = req.body.lon;
	localtracking.addr = req.body.addr;
	localtracking.userid = req.body.userid;
	localtracking.purpose = req.body.purpose;
	localtracking.timestamp = req.body.timestamp;

	localsample.trackingSteps.push(localtracking);

	// MC.connect('mongodb://localhost/NFP', function(err, db){
	// 	if(err) throw err;
	// 	var coll = db.collection('sample');
	// 	coll.count({tagUID:req.body.tagUID}, function(err, count){
	// 		if(err) throw err;
	// 		if(!count){
	// 			coll.insert(localsample, function(err, record){
	// 				if(err) throw err;
	// 				res.sendStatus(200);
	// 			});
	// 		}else{
	// 			console.log('already there');
	// 		}
	// 	});
	// });

	MC.connect(MongoConnectionString, function(err, db){
		if(err) throw err;
		var coll = db.collection('sample');
		// coll.count({tagUID:req.body.tagUID}, function(err, count){
			// if(err) throw err;
			// if(!count){
				coll.insert(localsample, function(err, record){
					if(err) throw err;
					res.sendStatus(200);
					res.end(""+JSON.stringify(req.body));
				});
			// }else{
				// console.log('already there');
			// }
		// });
	});
});

router.post('/addtrackingstep/:tagUID', function(req, res, next){
	// console.log(JSON.stringify(req.body));
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

	MC.connect(MongoConnectionString, function(err, db){
		if(err) throw err;
		var coll = db.collection('sample');
		coll.update({tagUID:req.params.tagUID}, {$push:{trackingSteps: localtracking}}, function(err, list){
			if(err) throw err;
			res.sendStatus(200);
		});
	});
});

router.post('/signup', function(req, res, next){
	// console.log(req.body);
	// console.log(JSON.stringify(req.body));
	MC.connect(MongoConnectionString, function(err, db){
		if(err) throw err;
		var coll = db.collection('users');
		coll.insert({email:req.body.email, password:req.body.password, loggedon: true}, function(err, inserted){
			if (err) throw err;
			res.send({"userid":inserted[0]._id, "email":inserted[0].email});
		});
	});
});

router.post('/login', function(req,res, next){
	var local = false;
	MC.connect(MongoConnectionString, function(err, db){
		if(err) throw err;
		var coll = db.collection('users');
		coll.find({email:req.body.email}, function(err, list){
			if(list.length == 0){
				res.send({"not":"found"});
			}else{
				for(var i = 0; i < list.length; i++){
					if(list[i].password == req.body.password){
						local = true;
						coll.update({_id:list[i]._id}, {$push:{loggedon: true}}, function(err, list){
							if(err) throw err;
							res.send({"userid":inserted[0]._id, "email":inserted[0].email});
						});
						break;
					}
				}
				if(!local){
					res.send({"not":"found"});
				}
			}
		});
	});
});

router.post('/logout', function(req, res, next){
	MC.connect(MongoConnectionString, function(err, db){
		if(err) throw err;
		var newId = ObjectID(req.body.userid);
		var coll = db.collection('users');
		coll.update({_id:newId}, {$push:{loggedon:false}}, function(err, list){
			if(err) throw err;
			res.end("" + newId);
		});
	});
});

router.post("/search", function(req, res, next){
	//http://www.kdelemme.com/2014/07/28/read-multiple-collections-mongodb-avoid-callback-hell/
	//https://github.com/caolan/async
	// console.log(req.body);
	MC.connect(MongoConnectionString, function(err, db){
		async.parallel([
			function(callback){
				if(req.body.clinic){
					var coll = db.collection('clinic');
					var regex = new RegExp(".*" + req.body.text + ".*", 'i');
					var query = {"Name": regex};
					coll.find(query, function(err, list){
						list.toArray(function(err, listArr){
							// console.log('listarr', listArr);
							// console.log(JSON.stringify(listArr));
							callback(null, listArr);
						});
					});
				}else{
					callback(null,null);
				}
			},
			function(callback){
				if(req.body.doctor){
					var coll = db.collection('doctor');
					var regex = new RegExp(".*" + req.body.text + ".*", 'i');
					var query = {"Name": regex};
					coll.find(query, function(err, list){
						list.toArray(function(err, listArr){
							// console.log('listarr', listArr);
							// console.log(JSON.stringify(listArr));
							callback(null, listArr);
						});
					});
				}else{
					callback(null,null);
				}
			},
			function(callback){
				if(req.body.patient){
					var coll = db.collection('patient');
					var regex = new RegExp(".*" + req.body.text + ".*", 'i');
					var query = {"Name": regex};
					coll.find(query, function(err, list){
						list.toArray(function(err, listArr){
							// console.log('listarr', listArr);
							// console.log(JSON.stringify(listArr));
							callback(null, listArr);
						});
					});
				}else{
					callback(null,null);
				}
			}
		],
			function(err, results){
				if(err) throw err;
				var sendResults = {};
				sendResults.clinics = results[0] || [];
				sendResults.doctors = results[1] || [];
				sendResults.patients = results[2] || [];
				return res.send(JSON.stringify(sendResults));
			}
		);
	});
});

module.exports = router;
