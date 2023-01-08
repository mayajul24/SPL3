package bgu.spl.net.srv;
import java.util.HashMap;
import java.util.LinkedList;

public class ConnectionsImpl<T> implements Connections<T>{
    private LinkedList<ConnectionHandler<T>> connectionHandlers;
    private HashMap<String,String> usersToPasscode;
    private LinkedList<String> connectedUsers;
    
    private LinkedList<Topic> topics;


    public ConnectionsImpl(){
        //TODO: maintain the users to connection handler
        connectionHandlers = new LinkedList<ConnectionHandler<T>>();
        this.connectedUsers = new LinkedList<String>();
    }

    public LinkedList<ConnectionHandler<T>> getConnectionHandlers(){
        return connectionHandlers;
    }

    boolean send(int connectionId, T msg){
        
        return false;
    }

    void send(String channel, T msg){

    }

    void disconnect(int connectionId){

    }

    public LinkedList<Topic> getTopics(){
        return topics;
    }

    public HashMap<String, String> getUsersToPasscode(){
        return usersToPasscode;
    }

    public LinkedList<String> getConnectedUsers(){
        return connectedUsers;
    }
    
}
