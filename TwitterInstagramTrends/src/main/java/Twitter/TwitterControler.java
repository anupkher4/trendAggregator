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
    	
    	
}
