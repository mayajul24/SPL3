package bgu.spl.net.srv;
import java.util.HashMap;
import java.util.LinkedList;

public class ConnectionsImpl<T> implements Connections<T>{
    private HashMap<String,String> usersToPasscode;
    private LinkedList<String> connectedUsers;
    private HashMap<Integer,ConnectionHandler<T>> connectionIDToConnectionHandler;
    private HashMap<String,Topic> nameToTopic;
    private HashMap<Integer,String> connectionIdToUsername;
    private int messageID;

    public ConnectionsImpl(){
        this.usersToPasscode = new HashMap<String,String>();
        this.connectedUsers = new LinkedList<String>();
        this.connectionIDToConnectionHandler = new HashMap<Integer,ConnectionHandler<T>>();
        this.nameToTopic = new HashMap<String,Topic>();
        this.connectionIdToUsername = new HashMap<Integer,String>();
        this.messageID = 0;
    }

    public boolean send(int connectionId, T msg){
        connectionIDToConnectionHandler.get(connectionId).send(msg);
        return false;
    }
    public void send(String channel, T msg){
        Topic topic = nameToTopic.get(channel);
        HashMap<Integer,String> connectionIds = topic.getCon("ConnectionIDs");
        for(int i=0;i<connectionIds.size();i++)
        {
            send(connectionIds.get(i),msg);
        }
    }
    public void disconnect(int connectionId){
        String username = connectionIdToUsername.get(connectionId);
            connectedUsers.remove(username);
            connectionIDToConnectionHandler.remove(connectionId);
            for (HashMap.Entry<String,Topic> entry : nameToTopic.entrySet()) 
            {
                Topic currentTopic = entry.getValue();
                if(currentTopic.getConnectionIDs().contains(connectionId)){
                    currentTopic.getConnectionIDs().remove(connectionId);
                }
            }
            connectionIdToUsername.remove(connectionId);
    }
    public HashMap<String, String> getUsersToPasscode(){
        return usersToPasscode;
    }
    public LinkedList<String> getConnectedUsers(){
        return connectedUsers;
    }
    public HashMap<String,Topic> getNameToTopic(){
        return nameToTopic;
    }
    public HashMap<Integer,ConnectionHandler<T>> getConnectionIdToConnectionHandler(){
        return connectionIDToConnectionHandler;
    }
    public HashMap<Integer,String> getConnectionIdToUsername()
    {
        return connectionIdToUsername;
    }
    public int getMessageID()
    {
        return messageID++;
    }
}