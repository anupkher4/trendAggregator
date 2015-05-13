package demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) throws IOException, HttpMessageNotReadableException {
    	//RestTemplate restTemplate = new RestTemplate();
    	
    	
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	
    	System.out.println("Bearer Token: " + TwitterAppAuth.requestBearerToken("https://api.twitter.com/oauth2/token"));
    	headers.add("Authorization", "Bearer " + TwitterAppAuth.requestBearerToken("https://api.twitter.com/oauth2/token"));
    	//headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.add("Content-Type", "application/json");
    	//headers.add("Accept","application/json");

    	HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        
    	ArrayList<TwitterTrend> al = new ArrayList<TwitterTrend>();
    	ArrayList t = restTemplate.exchange("https://api.twitter.com/1.1/trends/place.json?id=1", HttpMethod.GET, entity, ArrayList.class).getBody();
    	
        //Trends t = restTemplate.getForObject("https://api.twitter.com/1.1/trends/place.json?id=1", Trends.class);
        System.out.println("Name:    " + t.get(0).toString());
        
    }
}
