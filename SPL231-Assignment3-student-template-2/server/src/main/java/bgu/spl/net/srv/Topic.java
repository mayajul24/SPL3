package bgu.spl.net.srv;

import java.util.HashMap;

public class Topic {
    private String name;
    private HashMap<Integer,String> connectionIDsToSubsctiptionIDs;

    public Topic(String name, int connectionID, String subsctiptionID)
    {
        this.name = name;
        this.connectionIDsToSubsctiptionIDs = new HashMap<>();
        connectionIDsToSubsctiptionIDs.put(connectionID, subsctiptionID);

    }
    public String getName()
    {
        return name;
    }
    public HashMap<Integer,String> getConnectionIDs()
    {
        return connectionIDsToSubsctiptionIDs;
    }

    public void addSubscription(int connectionId, String subsctiptionID)
    {
        connectionIDsToSubsctiptionIDs.put(connectionId, subsctiptionID);
    }

    public void removeSubscription(int connectionID)
    {
        connectionIDsToSubsctiptionIDs.remove(connectionID);
    }
    public HashMap<Integer,String> getConnectionIDsToSubsctiptionIDs()
    {
        return connectionIDsToSubsctiptionIDs;
    }
} 
