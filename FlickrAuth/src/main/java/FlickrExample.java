import com.mongodb.*;
import com.sun.tools.javac.util.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.model.Response;
import org.scribe.oauth.*;
import spark.*;
import spark.Request;

import static spark.Spark.*;

import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;

public class FlickrExample
{
    private static final String PROTECTED_RESOURCE_URL = "https://api.flickr.com/services/rest/";

    public static void main(String[] args)
    {

        final Map<Object, Object> tokenMap = new HashMap<Object, Object>();

        //freemarker
        final Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setClassForTemplateLoading(FlickrExample.class, "/freemarker");
        cfg.setClassForTemplateLoading(FlickrExample.class, "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

        //mongo
        MongoClientURI uri = new MongoClientURI("mongodb://root:test123@ds047911.mongolab.com:47911/cmpe273");
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(uri);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        DB mongoDB = mongoClient.getDB("cmpe273");
        final DBCollection coll = mongoDB.getCollection("flickr");
        final DBCollection insta = mongoDB.getCollection("instagramTrend");
        final DBCollection twitter = mongoDB.getCollection("twitterTrend");

        // Replace these with your own api key and secret
        String apiKey = "Your API key";
        String apiSecret = "Your secret";
        final OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(apiKey).apiSecret(apiSecret)
                                        .callback("http://localhost:4567/callback/url").build();
        final OAuthRequest authRequest = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);

        get("/getRequestToken", new Route() {
            public Object handle(Request request, spark.Response response) throws Exception {
                // Obtain the Request Token
                Token requestToken = service.getRequestToken();
                tokenMap.put("requestToken", requestToken);

                String authorizationUrl = service.getAuthorizationUrl(requestToken);
                tokenMap.put("authURL", authorizationUrl);
                String finalAuthURL = authorizationUrl + "&perms=read";
                tokenMap.put("finalAuthURL", finalAuthURL);

                response.redirect(finalAuthURL);

                return null;
            }
        });


        get("/callback/url", new Route() {
            public Object handle(Request request, spark.Response response) throws Exception {

                String oauth_token = request.queryParams("oauth_token");
                String ouauth_verifier = request.queryParams("oauth_verifier");

                Verifier verifier = new Verifier(ouauth_verifier);
                tokenMap.put("verifier", verifier);

                response.redirect("/getAccessToken");
                return null;
            }
        });


        get("/getAccessToken", new Route() {
            public Object handle(Request request, spark.Response response) throws Exception {

                // Trade the Request Token and Verfier for the Access Token
                Token token = (Token)tokenMap.get("requestToken");
                Verifier verifier = (Verifier)tokenMap.get("verifier");
                Token accessToken = service.getAccessToken(token, verifier);
                tokenMap.put("accessToken", accessToken);

                return "Here's your access token" + accessToken + " Now use it to call an awesome API!";
            }
        });

        get("/getFlickrUserName", new Route() {
            public Object handle(Request request, spark.Response response) throws Exception {

                Token accessToken = (Token)tokenMap.get("accessToken");
                authRequest.addQuerystringParameter("method", "flickr.test.login");
                service.signRequest(accessToken, authRequest);
                Response authResponse = authRequest.send();

                return authResponse.getBody();
            }
        });

        get("/getPics", new Route() {
            public Object handle(Request request, spark.Response response) throws Exception {

                Token accessToken = (Token)tokenMap.get("accessToken");
                authRequest.addQuerystringParameter("method", "flickr.photos.getRecent");
                authRequest.addQuerystringParameter("format", "json");
                authRequest.addQuerystringParameter("nojsoncallback", "1");
                service.signRequest(accessToken, authRequest);

                Response authResponse = authRequest.send();

                //mongo
                BasicDBObject query = new BasicDBObject("recentPosts", authResponse.getBody());
                WriteResult write = coll.insert(query);

                return write;
            }
        });

        get("/getInstaPics", new Route() {
            public Object handle(Request request, spark.Response response) throws Exception {

                //mongo
                BasicDBObject instaQuery = new BasicDBObject();
                BasicDBObject instaProjection = new BasicDBObject("_id", 0).append("_class", 0).append("created_time", 0);
                BasicDBObject twitQuery = new BasicDBObject();
                BasicDBObject twitProjection = new BasicDBObject("_id", 0).append("_class", 0).append("created_at", 0).append("as_of", 0);
                DBCursor twitCursor = twitter.find(twitQuery, twitProjection);
                DBCursor cursor = insta.find(instaQuery, instaProjection);

                List<String> instaUrls = new ArrayList<String>();
                while (cursor.hasNext()){
                    DBObject urlObject = cursor.next();
                    instaUrls.add((String) urlObject.get("url"));
                }

                List<String> twitUrls = new ArrayList<String>();
                List<String> twitNames = new ArrayList<String>();
                Map<String, String> twitMap = new HashMap<String, String>();
                List<Map<String, String>> twitNameUrl = new ArrayList<Map<String, String>>();
                while (twitCursor.hasNext()){
                    DBObject urlObject = twitCursor.next();
                    twitMap.put((String) urlObject.get("name"), (String) urlObject.get("url"));
                    twitUrls.add((String) urlObject.get("url"));
                    twitNames.add((String) urlObject.get("name"));
                }


                System.out.println(instaUrls);
                //System.out.println(twitUrls);
                System.out.println(twitNames);
                System.out.println(twitNameUrl);

                Map map = new HashMap();
                map.put("instaUrls", instaUrls);
                map.put("twitNames", twitNames);
                map.put("twitUrls", twitUrls);
                map.put("twitMap", twitMap);
                //map.put("twitNameUrl", twitNameUrl);


                StringWriter writer = new StringWriter();
                Template temp = null;
                try {
                    temp = cfg.getTemplate("trend.ftl");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp.process(map, writer);

                return writer;
            }
        });

    }

}

