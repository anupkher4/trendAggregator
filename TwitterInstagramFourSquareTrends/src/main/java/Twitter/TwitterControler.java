package Twitter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class TwitterControler {

    	@Autowired
 	    private TwitterTrendRepository tRepo;
    	
    	@Autowired
    	private InstagramTrendRepository iRepo;
    	
    	@Autowired
    	private FourSquareTrendRepository fRepo;
    	
    	@Autowired
    	private TwitterAvailableTrendRepository taRepo;
    	
    	@Scheduled(fixedRate = 900000)
 	    public void getTrend() throws IOException, ParseException {
    		
    	System.out.println("Twitter Trends");
    	System.out.println("----------------------------");
    		
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	
    	System.out.println("Bearer Token: " + TwitterAppAuth.requestBearerToken("https://api.twitter.com/oauth2/token"));
    	headers.add("Authorization", "Bearer " + TwitterAppAuth.requestBearerToken("https://api.twitter.com/oauth2/token"));
    	headers.add("Content-Type", "application/json");

    	HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        
    	String t = restTemplate.exchange("https://api.twitter.com/1.1/trends/place.json?id=1", HttpMethod.GET, entity, String.class).getBody();
    	
    	
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(t);
        
        JSONArray array = (JSONArray) obj;
        
        System.out.println("Second element of array " + array.get(0));
        System.out.println();

        
        JSONObject obj2 = (JSONObject) array.get(0);
        System.out.println("Trends ");
        System.out.println(obj2.get("trends"));
        
        String createdDate = (String) obj2.get("created_at");
        String asOf = (String) obj2.get("as_of");
        
        //TwitterTrend tt = new TwitterTrend();
        
        
        System.out.println("Created at : " + createdDate);
        System.out.println("As of : " + asOf);
        
        
         
        JSONArray array2 = (JSONArray) obj2.get("trends");
        
        for(int i=0; i<array2.size();i++)
        {
        	JSONObject obj3 = (JSONObject) array2.get(i);
        	
        	String name = (String) obj3.get("name");
        	if(name.charAt(0) == '#')
        	{
        		name = name.substring(1);
        	}
        	String url = (String) obj3.get("url");
        	
        	TwitterTrend tt = new TwitterTrend();
        	
        	tt.setCreated_at(createdDate);
            tt.setAs_of(asOf);
        	
        	tt.setName(name);
        	tt.setUrl(url);
        	tRepo.save(tt);
        	
        	System.out.println("name of the trend " + name);
        	System.out.println("url of the trend " + url);
        	System.out.println("--------------------------------------");
        }
        
        
    }
    	
    	// Instagram Trend Controller
    	
    	@Scheduled(fixedRate = 900000)
 	    public void getInstagramTrend() throws IOException, ParseException {
        	
    		
    		RestTemplate restTemplate = new RestTemplate();
        	
        	String s = restTemplate.getForObject("https://api.instagram.com/v1/media/popular?client_id=6e47d51306e34d34931e62a18244711c", String.class);
        	
        	System.out.println("Instagram Popular Photos");
        	System.out.println(s);
        	
        	JSONParser parser = new JSONParser();
            Object obj = parser.parse(s);
            
            JSONObject obj2 = (JSONObject) obj;
            
            JSONArray array1 = (JSONArray) obj2.get("data");
            
            for(int i=0; i< array1.size(); i++)
            {
            	System.out.println(i + "  Media:");
            	JSONObject obj3 = (JSONObject) array1.get(i);
            	
            	String id = (String) obj3.get("id");
            	System.out.println("ID of the image: " + obj3.get("id"));
            	
            	String unixTime = (String) obj3.get("created_time");
            	long unixSeconds = Long.parseLong(unixTime);
            	Date date = new Date(unixSeconds*1000L);
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
            	sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles")); // give a timezone reference for formating (see comment at the bottom
            	String formattedDate = sdf.format(date);
            	System.out.println("Created Time of the Instagram Trend" + formattedDate);
            	
            	String createTime = formattedDate;
            	
            	JSONObject obj4 = (JSONObject) obj3.get("images");
            	JSONObject obj5 = (JSONObject) obj4.get("standard_resolution");
            	String iUrl = (String) obj5.get("url");
            	
            	JSONObject obj6 = (JSONObject) obj3.get("likes");
            	long likesCount = (long) obj6.get("count");
            	
            	JSONObject obj7 = (JSONObject) obj3.get("comments");
            	long commentCount = (long) obj7.get("count");
            	
            	ArrayList<String> al = new ArrayList<String>();
            	String[] tags;
            	
            	if(obj3.get("tags") != null)
            	{
            	JSONArray array2 = (JSONArray) obj3.get("tags");
            	
            	if(array2.size()!=0)
            	{
            		
                for(int k =0; k< array2.size(); k++)
                {
                	String temp = (String) array2.get(k);
                	al.add(temp);
                }
                tags = al.toArray(new String[al.size()]);
                System.out.println("tags of the Instagram Trend" + tags[0]);
            	}
            	else
            	{
            		tags = new String[]{""};
            		System.out.println("No tags available for the Instagram Post");
            	}
            	
            	}
            	
            	else
            	{
            		tags = new String[]{""};
            		System.out.println("No tags available for the Instagram Post");
            	}
            	
            	
            	String caption = "";
            	if(obj3.get("caption") != null)
            	{
            	JSONObject obj8 = (JSONObject) obj3.get("caption");
            	
            	if(obj8.get("text") != null)
            	{
            		caption = (String) obj8.get("text");
            	}
            	}
            	System.out.println("Caption of the Instagram Trend" + caption);
            	
            	
            	double latitude = 0;
            	double longitude= 0;
            	boolean locationAvailable = false;
            	if(obj3.get("location") != null)
            	{
            	locationAvailable = true;
            	JSONObject obj9 = (JSONObject) obj3.get("location");
            	if(obj9.get("latitude") != null)
            	{
            	latitude = (double) obj9.get("latitude");
            	}
            	if(obj9.get("longitude") != null)
            	{
            	longitude = (double) obj9.get("longitude");
            	}
            	
            	System.out.println("Printing location of the Instagram trend");
            	}
            	
            	
            	System.out.println("URL of the media : " + iUrl);
            	
            	InstagramTrend it = new InstagramTrend();
            	
            	it.setId(id);
            	it.setCreated_time(createTime);
            	it.setUrl(iUrl);
                it.setCaption(caption);
                it.setLikesCount(likesCount);
                it.setCommentCount(commentCount);
                it.setTags(tags);
                it.setLocationAvailable(locationAvailable);
                it.setLatitude(latitude);
                it.setLongitude(longitude);
                
            	iRepo.save(it);
            	
            }
                  
        	
        }
    	
    	@Scheduled(fixedRate = 900000)
 	    public void getFourSquareTrend() throws IOException, ParseException {
        	
    		
    		RestTemplate restTemplate1 = new RestTemplate();
        	
        	String s = restTemplate1.getForObject("https://api.foursquare.com/v2/venues/trending?near=San francisco&client_id=4RINDNKSXCYBVKZCNNXMIVQSPRO2UKCJISGH4LEXNA2WLM0V&client_secret=U4X2X00LVWAD3ARBOSZQ3PZRKBUNG1OGPXYTOWV542XMFNEK&v=20150512&m=foursquare&count=10&radius=2000", String.class);
        	//SF Civic Center location ll=37.782193,-122.420262
        	System.out.println("FourSquare Popular Places");
        	System.out.println(s);
            
            
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(s);
            
            JSONObject obj2 = (JSONObject) obj;
            
            JSONObject obj3 = (JSONObject) obj2.get("response");
            
            JSONArray array1 = (JSONArray) obj3.get("venues");
            
            for(int i=0; i< array1.size(); i++)
            {
            	
                JSONObject obj4 = (JSONObject) array1.get(i);
            	
            	String name = (String) obj4.get("name");
            	System.out.println("Name of the place: " + obj4.get("name"));
            	
            	JSONObject obj5 = (JSONObject) obj4.get("location");
            	String address = (String) obj5.get("address");
            	String crossStreet = (String) obj5.get("crossStreet");
            	String city = (String) obj5.get("city");
            	String state = (String) obj5.get("state");
            	String zipCode = (String) obj5.get("postalCode");
            	JSONArray array2 = (JSONArray) obj5.get("formattedAddress");
            	String formattedAddress = (String) array2.get(0);
            	
            	JSONArray array3 = (JSONArray) obj4.get("categories");
            	JSONObject obj6 = (JSONObject) array3.get(0);
            	String category = (String) obj6.get("name");
            	
            	JSONObject obj7 = (JSONObject) obj4.get("stats");
            	long checkinsCount = (long) obj7.get("checkinsCount");
            	long usersCount = (long) obj7.get("usersCount");
            	
            	String url = (String) obj4.get("url");
            	
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
            	sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            	
            	//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            	Calendar cal = Calendar.getInstance();
            	String insertedTime = sdf.format(cal.getTime());
            	System.out.println("Inserted time of FourSquare " + insertedTime);
                
            	FourSquareTrend fs = new FourSquareTrend();
            	
            	fs.setName(name);
            	fs.setAddress(address);
            	fs.setCrossStreet(crossStreet);
            	fs.setCity(city);
            	fs.setState(state);
            	fs.setZipCode(zipCode);
            	fs.setFullAddress(formattedAddress);
            	fs.setCheckinsCounts(checkinsCount);
            	fs.setUsersCount(usersCount);
            	fs.setCategory(category);
            	fs.setUrl(url);
            	fs.setInsertedTime(insertedTime);
            	
            	fRepo.save(fs);
            	
            	System.out.println("Uploading FourSquare Data into MongoDB");
            	
            	
            }
            
    	}
    	
    	
    	@Scheduled(fixedRate = 900000)
 	    public void getTwitterAvailableTrend() throws IOException, ParseException {
    		
    	System.out.println("Twitter Available Trends");
    	System.out.println("----------------------------");
    		
    	RestTemplate restTemplate2 = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	
    	System.out.println("Bearer Token: " + TwitterAppAuth.requestBearerToken("https://api.twitter.com/oauth2/token"));
    	headers.add("Authorization", "Bearer " + TwitterAppAuth.requestBearerToken("https://api.twitter.com/oauth2/token"));
    	headers.add("Content-Type", "application/json");

    	HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        
    	String t = restTemplate2.exchange("https://api.twitter.com/1.1/trends/available.json", HttpMethod.GET, entity, String.class).getBody();
    	
    	
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(t);
        
        JSONArray array = (JSONArray) obj;
        
        for(int i = 0; i < array.size(); i++)
        {
          JSONObject obj1 = (JSONObject) array.get(i);
          String country = (String) obj1.get("country");
          String countryCode = (String) obj1.get("countryCode");
          String name = (String) obj1.get("name");
          JSONObject obj2 = (JSONObject) obj1.get("placeType");
          String placeType = (String) obj2.get("name");
          System.out.println("Name of the Twitter Avaialbe Trending Place: "+ name);
          
          
          TwitterAvailableTrend tat = new TwitterAvailableTrend();
          tat.setCountry(country);
          tat.setName(name);
          tat.setPlaceType(placeType);
          tat.setCountryCode(countryCode);
          
          taRepo.save(tat);
          System.out.println("Uploading Twitter Available Trends");
        	
        }
        
    	}
    	
    	
}
