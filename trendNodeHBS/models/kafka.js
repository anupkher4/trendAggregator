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

// Make this available to other files
module.exports = Kafka; 