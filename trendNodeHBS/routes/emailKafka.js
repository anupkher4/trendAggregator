// Import the created model
var Kafka = require('./models/kafka');

// Get the email from index page
var userEmail = document.getElementById().value;

var newEmail = new Kafka({
	email: userEmail
});

// Save to database
newEmail.save(function(err) {
	if (err) throw err;

	console.log('Email saved');
});
