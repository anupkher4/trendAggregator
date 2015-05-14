var express = require('express');
var router = express.Router();

// Mongoose setup
var mongoose = require('mongoose');
mongoose.connect('mongodb://anup:mongo@ds047911.mongolab.com:47911/cmpe273');

var TwitterTrend = mongoose.model('TwitterTrend', {
	_class: String,
	name: String,
	url: String,
	created_at: Date,
	as_of: Date,
	state: String,
	count: Number
}, 'twitterTrend');

var InstaTrend = mongoose.model('InstaTrend', {
	_class: String,
	url: String,
	created_time: Date,
  likesCount: Number,
  commentCount: Number
}, 'instagramTrend');

var FoursquareTrend = mongoose.model('FoursquareTrend', {
  _class: String,
  name: String,
  category: String,
  checkinsCounts: Number,
  usersCount: Number,
  address: String,
  crossStreet: String,
  city: String,
  state: String,
  fullAddress: String,
  zipCode: String,
  insertedTime: Date
}, 'fourSquareTrend');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Welcome to Trend Aggregator' });
});

/* GET twitter data. */
router.get('/twitter', function(req, res, next) {
  TwitterTrend.find({}).sort({as_of: -1}).exec(function(err, result) {
    if (err) throw err;
    console.log(result);
    res.render('twitter', { title: 'Twitter Trending Topics', tweets: result });
  });
});

/* GET instagram data. */
router.get('/insta', function(req, res, next) {
  InstaTrend.find({}).sort({created_time: -1}).exec(function(err, result) {
    if (err) throw err;
    console.log(result);
    res.render('insta', { title: 'Instagram Trending Pics', instas: result });
  });
});

/* GET foursquare data. */
router.get('/foursquare', function(req, res, next) {
  FoursquareTrend.find({}).sort({insertedTime: -1}).exec(function(err, result) {
    if (err) throw err;
    console.log(result);
    res.render('foursquare', { title: 'FOursquare Recent Checkins', checkins: result });
  });
});



module.exports = router;
