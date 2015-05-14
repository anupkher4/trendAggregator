// Mongoose setup
var mongoose = require('mongoose');
mongoose.connect('mongodb://anup:mongo@ds047911.mongolab.com:47911/cmpe273');

// Schema
var Schema = mongoose.Schema;

var kafkaSchema = new Schema({
	email: String,
	dateAdded: { type: Date, default: Date.now }
});

// Create model
var Kafka = mongoose.model('Kafka', kafkaSchema);

// Get the email from index page
var userEmail = document.getElementById("email").value;

var newEmail = new Kafka({
	email: userEmail
});

// Save to database
newEmail.save(function(err) {
	if (err) throw err;

	console.log('Email saved');
});
