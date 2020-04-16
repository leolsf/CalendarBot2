package hk.hkucs.calendarbot2;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;

// Packages needed to interact with MongoDB and Stitch
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

// Necessary component for working with MongoDB Mobile
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;

import org.bson.Document;

import java.util.ArrayList;


public class MongoDBAdaptor {

    protected String DBName = "";
    protected StitchAppClient client;
    protected MongoClient mobileClient;
    protected MongoDatabase localDB;

    public MongoDBAdaptor(String name){
        this.DBName = name;
        this.client = Stitch.initializeDefaultAppClient("CalendarBot2");
        this.mobileClient = client.getServiceClient(LocalMongoDbService.clientFactory);

        this.localDB = mobileClient.getDatabase(this.DBName);
    }

    public void insertOneDocument(String collection, Document doc){
        MongoCollection<Document> localCollection = this.localDB.getCollection(collection);
        localCollection.insertOne(doc);
    }

    public void insertManyDocuments(String collection, ArrayList<Document> docList){
        for(Document d : docList){
            this.insertOneDocument(collection, d);
        }
    }

    public ArrayList<Document> selectAll(String collection){
        MongoCollection<Document> localCollection = this.localDB.getCollection(collection);
        FindIterable<Document> cursor = localCollection.find();
        ArrayList<Document> results =
                (ArrayList<Document>) cursor.into(new ArrayList<Document>());
        return results;

    }

    //public ArrayList<Document> selectByDate(String collection, String time){}



    public void dumpDB(){
        this.localDB.drop();
    }

    public void dumpCollection(String collection){
        MongoCollection<Document> localCollection = this.localDB.getCollection(collection);
        localCollection.drop();
    }

}
