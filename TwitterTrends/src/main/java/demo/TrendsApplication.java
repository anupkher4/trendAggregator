package demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.RestTemplate;
import org.springframework.social.twitter.api.*;

@SpringBootApplication
public class TrendsApplication {

    public static void main(String[] args) throws IOException, HttpMessageNotReadableException, ParseException {
    	//RestTemplate restTemplate = new RestTemplate();
    	
    	
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	
    	System.out.println("Bearer Token: " + TwitterAppAuth.requestBearerToken("https://api.twitter.com/oauth2/token"));
    	headers.add("Authorization", "Bearer " + TwitterAppAuth.requestBearerToken("https://api.twitter.com/oauth2/token"));
    	//headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.add("Content-Type", "application/json");
    	//headers.add("Accept","application/json");

    	HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        
    	String t = restTemplate.exchange("https://api.twitter.com/1.1/trends/place.json?id=1", HttpMethod.GET, entity, String.class).getBody();
    	
        //Trends t = restTemplate.getForObject("https://api.twitter.com/1.1/trends/place.json?id=1", Trends.class);
        //System.out.println("Name:    " + t.get(0).toString());
    	
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(t);
        
        JSONArray array = (JSONArray) obj;
        
        System.out.println("Second element of array " + array.get(0));
        System.out.println();

        JSONObject obj2 = (JSONObject) array.get(0);
        System.out.println("Trends ");
        System.out.println(obj2.get("trends"));
        
        JSONArray array2 = (JSONArray) obj2.get("trends");
        
        for(int i=0; i<array2.size();i++)
        {
        	JSONObject obj3 = (JSONObject) array2.get(i);
        	
        	String name = (String) obj3.get("name");
        	if(name.charAt(0) == '#')
        	{
        		name = name.substring(1);
        	}
        	System.out.println("name of the trend " + name);
        	System.out.println("url of the trend " + obj3.get("url"));
        	System.out.println("--------------------------------------");
        }
    }
}
