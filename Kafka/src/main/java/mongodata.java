/**
 * Created by raghav on 5/12/15.
 */
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class mongodata {
    void result()
    {

        try
        {
            // To connect to mongodb server
            MongoClient mongoClient = new MongoClient("@ds047911.mongolab.com", 47911);
            // Now connect to your databases
            //DB db = mongoClient.getDB("cmpe273");
            System.out.println("Connect to database successfully");
            MongoClientURI uri  = new MongoClientURI("mongodb://root:test123@ds047911.mongolab.com:47911/cmpe273");
            MongoClient client = new MongoClient(uri);
            DB db = client.getDB(uri.getDatabase());
            DBCollection twitter = db.getCollection("twitterTrend");
            System.out.println(twitter);
            BasicDBObject findQuery = new BasicDBObject();
            findQuery.put("employeeId", "MePuedeMuchoQue");
            BasicDBObject orderBy = new BasicDBObject("decade", 1);
            DBCursor docs = twitter.find( );
            while(docs.hasNext()){
                DBObject doc = docs.next();
                List a1 = new ArrayList();
                a1.add(doc.get("url"));
                System.out.println("\t" + a1);
            }

            client.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }
}





