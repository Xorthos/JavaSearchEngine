package DAL;

import BE.Document;
import GUI.MainForm;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DatabaseGateway {

    private static DatabaseGateway instance;
    private MongoClient client;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private Morphia morphia = new Morphia();

    private Datastore datastore;

    public static DatabaseGateway getInstance(){
        if(instance == null){
            instance = new DatabaseGateway();
        }
        return instance;
    }

    public DatabaseGateway(){
        morphia.mapPackage("BE");
        client = new MongoClient();
    }


    public void getDocuments(MainForm context, String[] terms){

        HashSet<String> strings;



            List<BasicDBObject> pipeline = new ArrayList<>(Arrays.asList(
                    new BasicDBObject("$match", new BasicDBObject("value", new BasicDBObject("$in", terms))),
                    new BasicDBObject("$lookup", new BasicDBObject("from", "Position")
                            .append("localField", "_id")
                            .append("foreignField", "termId")
                            .append("as", "term_positions")),
                    new BasicDBObject("$unwind", "$term_positions"),
                    new BasicDBObject("$lookup", new BasicDBObject("from", "Document")
                            .append("localField", "term_positions.docId")
                            .append("foreignField", "_id")
                            .append("as", "docs")),
                    new BasicDBObject("$unwind", "$docs"),
                    new BasicDBObject("$group",
                            new BasicDBObject("_id", "$docs.title"))));

            AggregateIterable<org.bson.Document> output = client.getDatabase("SearchEngineV2").getCollection("Term").aggregate(pipeline);
            strings = new HashSet<>();
            for (org.bson.Document document : output) {
                strings.add((String) document.get("_id"));
            }


        context.handleResult(strings);

    }
}