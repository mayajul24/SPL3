package bgu.spl.net.srv;
import java.util.LinkedList;

public class ConnectionsImpl<T> implements Connections<T>{
    private LinkedList<ConnectionHandler<T>> connectionHandlers;

    public ConnectionsImpl(){
        connectionHandlers = new LinkedList<ConnectionHandler<T>>();
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
    
}
