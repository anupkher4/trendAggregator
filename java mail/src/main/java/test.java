/**
 * Created by Raghavendra on 5/13/2015.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
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

public class test
{

    void sendMail()
    {
        String from = "cmpe273trends@gmail.com";
        String to = "raghavendra.reddy@sjsu.edu";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        List a1 = new ArrayList();
        List a2 = new ArrayList();
        List a3 = new ArrayList();
        // Get the default Session object.
        //Session session = Session.getDefaultInstance(properties);
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("cmpe273trends@gmail.com", "Deepu1234");
                    }
                });


        try {

            //Connecting to the Mongolab and getting the twitter collection
            MongoClientURI uri = new MongoClientURI("mongodb://root:test123@ds047911.mongolab.com:47911/cmpe273");
            MongoClient client = new MongoClient(uri);
            DB db = client.getDB(uri.getDatabase());
            DBCollection twitter = db.getCollection("twitterTrend");
            //Extract the data  from Twitter
            DBCursor docs = twitter.find().sort(new BasicDBObject("_id", -1)).limit(5);
             while (docs.hasNext()) {
                DBObject doc = docs.next();

                 String temp = "Name of the Topic: " + doc.get("name") + " URL: " + doc.get("url") + "\n";

                a1.add(temp);

            }
            System.out.println(a1);

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Top Trending Tweets");

            // Now set the actual message
            message.setText("\n" + a1);

           // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        test a= new test();
        a.sendMail();

    }
}



