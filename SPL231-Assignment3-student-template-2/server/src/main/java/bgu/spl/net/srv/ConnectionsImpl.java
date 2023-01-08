package bgu.spl.net.srv;
import java.util.HashMap;
import java.util.LinkedList;

public class ConnectionsImpl<T> implements Connections<T>{
    private LinkedList<ConnectionHandler<T>> connectionHandlers;
    private HashMap<String,String> usersToPasscode;
    private LinkedList<String> connectedUsers;
    private HashMap<Integer,ConnectionHandler<T>> connectionIDToConnectionHandler;
    private HashMap<String,Topic> nameToTopic;


    public ConnectionsImpl(){
        //TODO: maintain the users to connection handler
        connectionHandlers = new LinkedList<ConnectionHandler<T>>();
        this.connectedUsers = new LinkedList<String>();
    }

    public LinkedList<ConnectionHandler<T>> getConnectionHandlers(){
        return connectionHandlers;
    }

    public boolean send(int connectionId, T msg){
        connectionIDToConnectionHandler.get(connectionId).send(msg);
        return false;
    }

    public void send(String channel, T msg){
        if(nameToTopic.containsKey(channel))
        {
            Topic topic = nameToTopic.get(channel);
            LinkedList<Integer> connectionIds = topic.getConnectionIDs();
            for(int i=0;i<connectionIds.size();i++)
            {
                connectionIDToConnectionHandler.get(connectionIds.get(i)).send(msg);
            }
        }
        else{
            //TODO: 
            //nameToTopic.put(channel, new Topic(channel, null))
        }
    }

    public void disconnect(int connectionId){

    }


    public HashMap<String, String> getUsersToPasscode(){
        return usersToPasscode;
    }

    public LinkedList<String> getConnectedUsers(){
        return connectedUsers;
    }
    
}
