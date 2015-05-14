package Twitter;

import java.io.IOException;

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
    	
    	@Scheduled(fixedRate = 900000)
 	    public void getTrend() throws IOException, ParseException {
    		
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
            	
            	String createTime = (String) obj3.get("created_time");
            	
            	JSONObject obj4 = (JSONObject) obj3.get("images");
            	JSONObject obj5 = (JSONObject) obj4.get("standard_resolution");
            	String iUrl = (String) obj5.get("url");
            	
            	System.out.println("URL of the media : " + iUrl);
            	
            	InstagramTrend it = new InstagramTrend();
            	
            	it.setId(id);
            	it.setCreated_time(createTime);
            	it.setUrl(iUrl);
            	
            	iRepo.save(it);
            	
            }
                  
        	
        }
    	
    	@Scheduled(fixedRate = 900000)
 	    public void getFourSquareTrend() throws IOException, ParseException {
        	
    		
    		RestTemplate restTemplate1 = new RestTemplate();
        	
        	String s = restTemplate1.getForObject("https://api.foursquare.com/v2/venues/trending?ll=37.782193,-122.420262&client_id=4RINDNKSXCYBVKZCNNXMIVQSPRO2UKCJISGH4LEXNA2WLM0V&client_secret=U4X2X00LVWAD3ARBOSZQ3PZRKBUNG1OGPXYTOWV542XMFNEK&v=20150512&m=foursquare&count=10&radius=2000", String.class);
        	
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
            	
            	fRepo.save(fs);
            	
            	System.out.println("Uploading FourSquare Data into MongoDB");
            	
            	
            }
            
    	}
    	
    	
}
