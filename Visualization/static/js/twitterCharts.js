queue()
    .defer(d3.json, "/trendaggregator/twitter")
    .defer(d3.json, "/static/geojson/us-states.json")
    .await(makeGraphs);

function makeGraphs(error, projectsJson, statesJson) {
	
	//Clean projectsJson data
	var donorschooseProjects = projectsJson;
	var dateFormat = d3.time.format("%Y-%m-%d %H:%M:%S");
	donorschooseProjects.forEach(function(d) {
		d["created_at"] = dateFormat.parse(d["created_at"]);
		//d["created_at"].setDate(13);
		//d["name"] = +d["name"];
	});

	//Create a Crossfilter instance
	var ndx = crossfilter(donorschooseProjects);

	//Define Dimensions
	var dateDim = ndx.dimension(function(d) { return d["created_at"]; });
	var resourceTypeDim = ndx.dimension(function(d) { return d["name"]; });



	//Calculate metrics
	var numProjectsByDate = dateDim.group(); 
	var numProjectsByResourceType = resourceTypeDim.group();


	var all = ndx.groupAll();
	
 var min = d3.time.day.offset(d3.min(donorschooseProjects, function(d) { return d["created_at"];} ),-1);
 var max = d3.time.day.offset(d3.max(donorschooseProjects, function(d) { return d["created_at"];} ), 1);
    //Charts
	var timeChart = dc.lineChart("#time-chart");
	var resourceTypeChart = dc.rowChart("#resource-type-row-chart");
	//var usChart = dc.geoChoroplethChart("#us-chart");
	var numberProjectsND = dc.numberDisplay("#number-projects-nd");
	//var totalDonationsND = dc.numberDisplay("#total-donations-nd");


	numberProjectsND
		.formatNumber(d3.format("d"))
		.valueAccessor(function(d){return d; })
		.group(all);

	/* totalDonationsND
		.formatNumber(d3.format("d"))
		.valueAccessor(function(d){return d; })
		.group(totalDonations)
		.formatNumber(d3.format(".3s")); */

	timeChart
	.width(500)
                    .height(500)
                    .dimension(dateDim)
                    .group(numProjectsByDate)
                    .brushOn(true)
                    .x(d3.time.scale().domain([min, max]))
                    .round(d3.time.day(new Date()).round)
                    .elasticY(true)
                    .xUnits(d3.time.days)
		
	resourceTypeChart
        .width(6000)
        .height(6000)
        .dimension(resourceTypeDim)
        .group(numProjectsByResourceType)
        .xAxis().ticks(10);
		
		




	/* usChart.width(1000)
		.height(330)
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
					+ "Total Donations: " + Math.round(p["value"]) + " $";
		}) */

    dc.renderAll();

};