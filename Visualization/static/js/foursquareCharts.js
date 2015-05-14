queue()
    .defer(d3.json, "/trendaggregator/foursquare")
    .defer(d3.json, "/static/geojson/us-states.json")
    .await(makeGraphs);

function makeGraphs(error, projectsJson, statesJson) {

	//Clean projectsJson data
	var trendsJson = projectsJson;
	var dateFormat = d3.time.format("%Y-%m-%d %H:%M:%S");
	trendsJson.forEach(function(d) {
		d["insertedTime"] = dateFormat.parse(d["insertedTime"]);
		//d["insertedTime"].setDate(1);
		d["usersCount"] = +d["usersCount"];
	});

	//Create a Crossfilter instance
	var ndx = crossfilter(trendsJson);

	//Define Dimensions
	var dateDim = ndx.dimension(function(d) { return d["insertedTime"]; });
	var resourceTypeDim = ndx.dimension(function(d) { return d["name"]; });
	var povertyLevelDim = ndx.dimension(function(d) { return d["category"]; });
	var stateDim = ndx.dimension(function(d) { return d["state"]; });
	var totalDonationsDim  = ndx.dimension(function(d) { return d["usersCount"]; });


	//Calculate metrics
	var numProjectsByDate = dateDim.group();
	var numProjectsByResourceType = resourceTypeDim.group();
	var numProjectsByPovertyLevel = povertyLevelDim.group();
	var totalDonationsByState = stateDim.group().reduceSum(function(d) {
		return d["state"];
	});

	var all = ndx.groupAll();
	var totalDonations = ndx.groupAll().reduceSum(function(d) {return d["usersCount"];});

	var max_state = totalDonationsByState.top(1)[0].value;

	//Define values (to be used in charts)
	//var minDate = dateDim.bottom(1)[0]["insertedTime"];
	//var maxDate = dateDim.top(1)[0]["insertedTime"];
var min = d3.time.day.offset(d3.min(trendsJson, function(d) { return d["insertedTime"];} ),-1);
 var max = d3.time.day.offset(d3.max(trendsJson, function(d) { return d["insertedTime"];} ), 1);
    //Charts
    //Charts
	var timeChart = dc.lineChart("#time-chart");
	var resourceTypeChart = dc.rowChart("#resource-type-row-chart");
	var povertyLevelChart = dc.rowChart("#category-level-row-chart");
	var usChart = dc.geoChoroplethChart("#us-chart");
	var numberProjectsND = dc.numberDisplay("#number-projects-nd");
	var totalDonationsND = dc.numberDisplay("#total-users-nd");
	//var x = d3.time.scale().range([0, 600]);

	numberProjectsND
		.formatNumber(d3.format("d"))
		.valueAccessor(function(d){return d; })
		.group(all);

	totalDonationsND
		.formatNumber(d3.format("d"))
		.valueAccessor(function(d){return d; })
		.group(totalDonations)
		.formatNumber(d3.format(".3s"));

	timeChart
	.width(100)
                    .height(100)
                    .dimension(dateDim)
                    .group(numProjectsByDate)
                    .x(d3.time.scale().domain([min, max]))
                    .round(d3.time.minute.round)
                    .elasticY(true)
                    .xUnits(d3.time.minutes);

	resourceTypeChart
        .width(350)
        .height(1000)
        .dimension(resourceTypeDim)
        .group(numProjectsByResourceType)
        .xAxis().ticks(4);

	povertyLevelChart
		.width(350)
		.height(1000)
        .dimension(povertyLevelDim)
        .group(numProjectsByPovertyLevel)
        .xAxis().ticks(4);
		


	usChart.width(900)
		.height(530)
		.dimension(stateDim)
		.group(totalDonationsByState)
		.colors(["#E2F2FF", "#C4E4FF", "#9ED2FF", "#81C5FF", "#6BBAFF", "#51AEFF", "#36A2FF", "#1E96FF", "#0089FF", "#0061B5"])
		.colorDomain([0, max_state])
		.overlayGeoJson(statesJson["features"], "state", function (d) {
			return d.properties.name;
		})
		.projection(d3.geo.albersUsa()
    				.scale(600)
    				.translate([340, 150]))
		.title(function (p) {
			return "State: " + p["key"]
					+ "\n"
					+ "Total Number of Users: " + Math.round(p["value"]) + " $";
		})

    dc.renderAll();

};
